import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * A dependency-free, single-file model of a production Spring Boot order API.
 *
 * <p>The classes deliberately use plain Java so this file can be compiled and
 * run on its own. In a real Spring Boot project the matching stereotypes would
 * be:</p>
 *
 * <ul>
 *   <li>{@link OrderController}: {@code @RestController}</li>
 *   <li>{@link OrderService}: {@code @Service} with {@code @Transactional}</li>
 *   <li>{@link OrderRepository}: Spring Data {@code JpaRepository}</li>
 *   <li>{@link ApiExceptionHandler}: {@code @RestControllerAdvice}</li>
 *   <li>{@link EventPublisher}: Kafka/RabbitMQ or Spring application events</li>
 * </ul>
 *
 * <p>It demonstrates constructor injection, DTO validation, entity/DTO
 * separation, HTTP-style responses, idempotent writes, optimistic locking,
 * transaction rollback, cache-aside reads, an outbox event, authorization,
 * correlation IDs, metrics and sanitized global error handling.</p>
 */
public final class SpringBootOrderService {

    private SpringBootOrderService() {
    }

    public static void main(String[] args) {
        ApplicationContext context = ApplicationContext.start();
        OrderController api = context.orderController;
        RequestContext customer = new RequestContext(
                "corr-1001", "customer-42", List.of("CUSTOMER"));

        HttpResponse<?> created = api.createOrder(
                "request-abc-123",
                new CreateOrderRequest("customer-42", List.of(
                        new OrderItemRequest("BOOK-JAVA", 2, new BigDecimal("499.00")),
                        new OrderItemRequest("MOUSE-01", 1, new BigDecimal("799.00"))
                )), customer);
        print(created);

        // Retrying with the same idempotency key returns the original order.
        print(api.createOrder("request-abc-123",
                new CreateOrderRequest("customer-42", List.of(
                        new OrderItemRequest("IGNORED-ON-RETRY", 1,
                                new BigDecimal("1.00")))), customer));

        String orderId = ((OrderResponse) created.body).id;
        print(api.getOrder(orderId, customer));
        print(api.cancelOrder(orderId, 0, customer));

        // Shows the standard error response produced by global exception handling.
        print(api.getOrder("missing-order", customer));

        System.out.println("\nPublished events: " + context.eventPublisher.publishedEvents());
        System.out.println("Metrics: " + context.metrics.snapshot());
    }

    private static void print(HttpResponse<?> response) {
        System.out.println("HTTP " + response.status + " " + response.body);
    }

    /** Composition root; analogous to Spring's ApplicationContext and @Bean methods. */
    static final class ApplicationContext {
        final InMemoryOrderRepository repository;
        final InMemoryEventPublisher eventPublisher;
        final MetricsRegistry metrics;
        final OrderController orderController;

        private ApplicationContext(InMemoryOrderRepository repository,
                                   InMemoryEventPublisher eventPublisher,
                                   MetricsRegistry metrics,
                                   OrderController orderController) {
            this.repository = repository;
            this.eventPublisher = eventPublisher;
            this.metrics = metrics;
            this.orderController = orderController;
        }

        static ApplicationContext start() {
            InMemoryOrderRepository repository = new InMemoryOrderRepository();
            InMemoryEventPublisher publisher = new InMemoryEventPublisher();
            MetricsRegistry metrics = new MetricsRegistry();
            OrderCache cache = new OrderCache();
            OrderService service = new OrderService(repository, publisher, cache,
                    metrics, Clock.systemUTC());
            ApiExceptionHandler errors = new ApiExceptionHandler();
            return new ApplicationContext(repository, publisher, metrics,
                    new OrderController(service, errors));
        }
    }

    /** REST adapter. A real version uses @PostMapping, @GetMapping and @PatchMapping. */
    static final class OrderController {
        private final OrderService service;
        private final ApiExceptionHandler errors;

        OrderController(OrderService service, ApiExceptionHandler errors) {
            this.service = Objects.requireNonNull(service);
            this.errors = Objects.requireNonNull(errors);
        }

        HttpResponse<?> createOrder(String idempotencyKey, CreateOrderRequest request,
                                    RequestContext context) {
            return execute(context, () -> HttpResponse.created(
                    service.create(idempotencyKey, request, context)));
        }

        HttpResponse<?> getOrder(String id, RequestContext context) {
            return execute(context, () -> HttpResponse.ok(service.findById(id, context)));
        }

        HttpResponse<?> cancelOrder(String id, long expectedVersion,
                                    RequestContext context) {
            return execute(context, () -> HttpResponse.ok(
                    service.cancel(id, expectedVersion, context)));
        }

        private HttpResponse<?> execute(RequestContext context, ApiCall call) {
            try {
                requireContext(context);
                return call.invoke();
            } catch (RuntimeException exception) {
                return errors.handle(exception, context == null ? "unknown" : context.correlationId);
            }
        }
    }

    /** Business layer. Public write methods are the intended transaction boundaries. */
    static final class OrderService {
        private final InMemoryOrderRepository repository;
        private final EventPublisher events;
        private final OrderCache cache;
        private final MetricsRegistry metrics;
        private final Clock clock;

        OrderService(InMemoryOrderRepository repository, EventPublisher events,
                     OrderCache cache, MetricsRegistry metrics, Clock clock) {
            this.repository = Objects.requireNonNull(repository);
            this.events = Objects.requireNonNull(events);
            this.cache = Objects.requireNonNull(cache);
            this.metrics = Objects.requireNonNull(metrics);
            this.clock = Objects.requireNonNull(clock);
        }

        synchronized OrderResponse create(String idempotencyKey,
                                          CreateOrderRequest request,
                                          RequestContext context) {
            requireRole(context, "CUSTOMER");
            validateIdempotencyKey(idempotencyKey);
            request.validate();
            if (!context.userId.equals(request.customerId)) {
                throw new ForbiddenException("Customers may create only their own orders");
            }

            Optional<Order> previous = repository.findByIdempotencyKey(idempotencyKey);
            if (previous.isPresent()) {
                metrics.increment("orders.idempotent_replay");
                return OrderResponse.from(previous.get());
            }

            // In Spring: @Transactional makes the entity and outbox insert atomic.
            RepositorySnapshot before = repository.snapshot();
            try {
                Instant now = clock.instant();
                Order order = Order.create(request, idempotencyKey, now);
                repository.save(order, -1);
                repository.addOutboxEvent(new OrderEvent(UUID.randomUUID().toString(),
                        "OrderCreated", order.id, now));
                events.publish("OrderCreated:" + order.id);
                cache.put(order);
                metrics.increment("orders.created");
                return OrderResponse.from(order);
            } catch (RuntimeException exception) {
                repository.restore(before); // illustrates transaction rollback
                throw exception;
            }
        }

        OrderResponse findById(String id, RequestContext context) {
            Order order = cache.get(id).orElseGet(() -> {
                metrics.increment("orders.cache_miss");
                Order loaded = repository.findById(id)
                        .orElseThrow(() -> new NotFoundException("Order not found: " + id));
                cache.put(loaded);
                return loaded;
            });
            requireOwnerOrAdmin(order, context);
            return OrderResponse.from(order);
        }

        synchronized OrderResponse cancel(String id, long expectedVersion,
                                          RequestContext context) {
            Order current = repository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Order not found: " + id));
            requireOwnerOrAdmin(current, context);
            Order cancelled = current.cancel(clock.instant());
            repository.save(cancelled, expectedVersion);
            repository.addOutboxEvent(new OrderEvent(UUID.randomUUID().toString(),
                    "OrderCancelled", id, clock.instant()));
            cache.evict(id); // equivalent to @CacheEvict
            events.publish("OrderCancelled:" + id);
            metrics.increment("orders.cancelled");
            return OrderResponse.from(cancelled);
        }

        private static void requireOwnerOrAdmin(Order order, RequestContext context) {
            requireContext(context);
            if (!order.customerId.equals(context.userId) && !context.roles.contains("ADMIN")) {
                throw new ForbiddenException("Access denied");
            }
        }
    }

    /** Domain entity. In JPA this would use @Entity, @Id and @Version. */
    static final class Order {
        final String id;
        final String customerId;
        final String idempotencyKey;
        final List<OrderItem> items;
        final BigDecimal total;
        final OrderStatus status;
        final long version;
        final Instant createdAt;
        final Instant updatedAt;

        private Order(String id, String customerId, String idempotencyKey,
                      List<OrderItem> items, BigDecimal total, OrderStatus status,
                      long version, Instant createdAt, Instant updatedAt) {
            this.id = id;
            this.customerId = customerId;
            this.idempotencyKey = idempotencyKey;
            this.items = List.copyOf(items);
            this.total = total;
            this.status = status;
            this.version = version;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }

        static Order create(CreateOrderRequest request, String key, Instant now) {
            List<OrderItem> items = request.items.stream()
                    .map(i -> new OrderItem(i.sku, i.quantity, i.unitPrice))
                    .toList();
            BigDecimal total = items.stream().map(OrderItem::lineTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            return new Order("ORD-" + UUID.randomUUID(), request.customerId, key,
                    items, total, OrderStatus.CREATED, 0, now, now);
        }

        Order cancel(Instant now) {
            if (status != OrderStatus.CREATED) {
                throw new ConflictException("Only a newly created order can be cancelled");
            }
            return new Order(id, customerId, idempotencyKey, items, total,
                    OrderStatus.CANCELLED, version + 1, createdAt, now);
        }

        Order copy() {
            return new Order(id, customerId, idempotencyKey, items, total,
                    status, version, createdAt, updatedAt);
        }
    }

    static final class OrderItem {
        final String sku;
        final int quantity;
        final BigDecimal unitPrice;

        OrderItem(String sku, int quantity, BigDecimal unitPrice) {
            this.sku = sku;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
        }

        BigDecimal lineTotal() {
            return unitPrice.multiply(BigDecimal.valueOf(quantity));
        }

        @Override
        public String toString() {
            return sku + " x " + quantity + " @ " + unitPrice;
        }
    }

    /** Request DTO; validate() represents Jakarta Bean Validation annotations. */
    static final class CreateOrderRequest {
        final String customerId;
        final List<OrderItemRequest> items;

        CreateOrderRequest(String customerId, List<OrderItemRequest> items) {
            this.customerId = customerId;
            this.items = items == null ? null : List.copyOf(items);
        }

        void validate() {
            requireText(customerId, "customerId");
            if (items == null || items.isEmpty()) {
                throw new ValidationException("items must not be empty");
            }
            if (items.size() > 100) {
                throw new ValidationException("an order cannot contain more than 100 lines");
            }
            items.forEach(OrderItemRequest::validate);
        }
    }

    static final class OrderItemRequest {
        final String sku;
        final int quantity;
        final BigDecimal unitPrice;

        OrderItemRequest(String sku, int quantity, BigDecimal unitPrice) {
            this.sku = sku;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
        }

        void validate() {
            requireText(sku, "sku");
            if (quantity < 1 || quantity > 99) {
                throw new ValidationException("quantity must be between 1 and 99");
            }
            if (unitPrice == null || unitPrice.signum() <= 0) {
                throw new ValidationException("unitPrice must be positive");
            }
        }
    }

    /** Response DTO: persistence internals such as idempotency keys are not exposed. */
    static final class OrderResponse {
        final String id;
        final String customerId;
        final List<OrderItem> items;
        final BigDecimal total;
        final String status;
        final long version;

        private OrderResponse(Order order) {
            this.id = order.id;
            this.customerId = order.customerId;
            this.items = order.items;
            this.total = order.total;
            this.status = order.status.name();
            this.version = order.version;
        }

        static OrderResponse from(Order order) {
            return new OrderResponse(order);
        }

        @Override
        public String toString() {
            return "OrderResponse{id='" + id + "', customer='" + customerId
                    + "', total=" + total + ", status=" + status
                    + ", version=" + version + "}";
        }
    }

    interface OrderRepository {
        Optional<Order> findById(String id);
        Optional<Order> findByIdempotencyKey(String key);
        void save(Order order, long expectedVersion);
    }

    static final class InMemoryOrderRepository implements OrderRepository {
        private final Map<String, Order> orders = new LinkedHashMap<>();
        private final Map<String, String> idempotencyIndex = new LinkedHashMap<>();
        private final List<OrderEvent> outbox = new ArrayList<>();

        @Override
        public Optional<Order> findById(String id) {
            return Optional.ofNullable(orders.get(id)).map(Order::copy);
        }

        @Override
        public Optional<Order> findByIdempotencyKey(String key) {
            return Optional.ofNullable(idempotencyIndex.get(key)).flatMap(this::findById);
        }

        @Override
        public void save(Order order, long expectedVersion) {
            Order stored = orders.get(order.id);
            long actualVersion = stored == null ? -1 : stored.version;
            if (actualVersion != expectedVersion) {
                throw new OptimisticLockException("Order changed; expected version "
                        + expectedVersion + " but found " + actualVersion);
            }
            orders.put(order.id, order.copy());
            idempotencyIndex.put(order.idempotencyKey, order.id);
        }

        void addOutboxEvent(OrderEvent event) {
            outbox.add(event);
        }

        RepositorySnapshot snapshot() {
            return new RepositorySnapshot(new LinkedHashMap<>(orders),
                    new LinkedHashMap<>(idempotencyIndex), new ArrayList<>(outbox));
        }

        void restore(RepositorySnapshot snapshot) {
            orders.clear();
            orders.putAll(snapshot.orders);
            idempotencyIndex.clear();
            idempotencyIndex.putAll(snapshot.idempotencyIndex);
            outbox.clear();
            outbox.addAll(snapshot.outbox);
        }
    }

    static final class RepositorySnapshot {
        final Map<String, Order> orders;
        final Map<String, String> idempotencyIndex;
        final List<OrderEvent> outbox;

        RepositorySnapshot(Map<String, Order> orders, Map<String, String> idempotencyIndex,
                           List<OrderEvent> outbox) {
            this.orders = orders;
            this.idempotencyIndex = idempotencyIndex;
            this.outbox = outbox;
        }
    }

    static final class OrderCache {
        private final Map<String, Order> values = new ConcurrentHashMap<>();

        Optional<Order> get(String id) {
            return Optional.ofNullable(values.get(id)).map(Order::copy);
        }

        void put(Order order) {
            values.put(order.id, order.copy());
        }

        void evict(String id) {
            values.remove(id);
        }
    }

    interface EventPublisher {
        void publish(String event);
    }

    static final class InMemoryEventPublisher implements EventPublisher {
        private final List<String> events = new ArrayList<>();

        @Override
        public void publish(String event) {
            events.add(event);
        }

        List<String> publishedEvents() {
            return Collections.unmodifiableList(events);
        }
    }

    static final class MetricsRegistry {
        private final Map<String, AtomicLong> counters = new ConcurrentHashMap<>();

        void increment(String name) {
            counters.computeIfAbsent(name, ignored -> new AtomicLong()).incrementAndGet();
        }

        Map<String, Long> snapshot() {
            Map<String, Long> result = new LinkedHashMap<>();
            counters.forEach((name, value) -> result.put(name, value.get()));
            return result;
        }
    }

    /** Global exception-to-HTTP mapping; messages reveal no stack traces or secrets. */
    static final class ApiExceptionHandler {
        HttpResponse<ApiError> handle(RuntimeException exception, String correlationId) {
            int status;
            if (exception instanceof ValidationException) status = 400;
            else if (exception instanceof ForbiddenException) status = 403;
            else if (exception instanceof NotFoundException) status = 404;
            else if (exception instanceof ConflictException
                    || exception instanceof OptimisticLockException) status = 409;
            else status = 500;

            String message = status == 500 ? "An unexpected error occurred" : exception.getMessage();
            return new HttpResponse<>(status,
                    new ApiError(status, message, correlationId, Instant.now()));
        }
    }

    static final class HttpResponse<T> {
        final int status;
        final T body;

        HttpResponse(int status, T body) {
            this.status = status;
            this.body = body;
        }

        static <T> HttpResponse<T> ok(T body) { return new HttpResponse<>(200, body); }
        static <T> HttpResponse<T> created(T body) { return new HttpResponse<>(201, body); }
    }

    static final class ApiError {
        final int status;
        final String message;
        final String correlationId;
        final Instant timestamp;

        ApiError(int status, String message, String correlationId, Instant timestamp) {
            this.status = status;
            this.message = message;
            this.correlationId = correlationId;
            this.timestamp = timestamp;
        }

        @Override
        public String toString() {
            return "ApiError{status=" + status + ", message='" + message
                    + "', correlationId='" + correlationId + "', timestamp=" + timestamp + "}";
        }
    }

    static final class RequestContext {
        final String correlationId;
        final String userId;
        final List<String> roles;

        RequestContext(String correlationId, String userId, List<String> roles) {
            this.correlationId = correlationId;
            this.userId = userId;
            this.roles = roles == null ? List.of() : List.copyOf(roles);
        }
    }

    static final class OrderEvent {
        final String eventId;
        final String type;
        final String aggregateId;
        final Instant occurredAt;

        OrderEvent(String eventId, String type, String aggregateId, Instant occurredAt) {
            this.eventId = eventId;
            this.type = type;
            this.aggregateId = aggregateId;
            this.occurredAt = occurredAt;
        }
    }

    enum OrderStatus { CREATED, CANCELLED }

    @FunctionalInterface
    interface ApiCall { HttpResponse<?> invoke(); }

    static class ValidationException extends RuntimeException {
        ValidationException(String message) { super(message); }
    }
    static class ForbiddenException extends RuntimeException {
        ForbiddenException(String message) { super(message); }
    }
    static class NotFoundException extends RuntimeException {
        NotFoundException(String message) { super(message); }
    }
    static class ConflictException extends RuntimeException {
        ConflictException(String message) { super(message); }
    }
    static class OptimisticLockException extends RuntimeException {
        OptimisticLockException(String message) { super(message); }
    }

    private static void requireContext(RequestContext context) {
        if (context == null || context.userId == null || context.userId.isBlank()) {
            throw new ForbiddenException("Authentication is required");
        }
    }

    private static void requireRole(RequestContext context, String role) {
        requireContext(context);
        if (!context.roles.contains(role)) {
            throw new ForbiddenException("Required role: " + role);
        }
    }

    private static void validateIdempotencyKey(String key) {
        requireText(key, "Idempotency-Key");
        if (key.length() > 100) {
            throw new ValidationException("Idempotency-Key is too long");
        }
    }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new ValidationException(field + " must not be blank");
        }
        return value;
    }
}
