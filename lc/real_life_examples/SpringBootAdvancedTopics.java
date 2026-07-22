import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Executable, dependency-free implementations of production patterns commonly
 * used with Spring Boot. Every nested type contains working behavior; there are
 * no source-code examples hidden inside strings.
 *
 * <p>The repository has no Maven/Gradle Spring dependencies, so annotations
 * such as {@code @RestController}, {@code @Transactional}, {@code @Entity},
 * {@code @KafkaListener}, {@code @Async} and {@code @CircuitBreaker} cannot be
 * imported honestly here. {@link SpringBootOrderService} supplies the matching
 * controller/service/repository implementation. This file implements the
 * advanced infrastructure behind those annotations in plain Java.</p>
 */
public final class SpringBootAdvancedTopics {

    private SpringBootAdvancedTopics() {}

    public static void main(String[] args) throws Exception {
        Metrics metrics = new Metrics();
        TraceContext trace = TraceContext.start("checkout");

        AppProperties properties = AppProperties.load(Map.of(
                "APP_PROFILE", "prod",
                "PAYMENT_ENABLED", "true",
                "PAYMENT_TIMEOUT_MS", "500"));
        BeanContext beans = AutoConfiguration.configure(properties, metrics);

        Customer customer = new Customer("C-101", "Asha");
        Product keyboard = new Product("P-10", "Keyboard", new BigDecimal("2499.00"));
        PurchaseOrder order = new PurchaseOrder("O-1001", customer,
                List.of(new OrderLine("L-1", keyboard, 1)), Set.of("priority"));

        InMemoryOrderRepository orders = new InMemoryOrderRepository();
        MultiDatabaseTransaction transaction = new MultiDatabaseTransaction(
                new InMemoryDatabase(), new InMemoryDatabase());
        transaction.execute(() -> orders.save(order, -1));

        JwtService jwt = new JwtService("development-secret-change-in-production", Clock.systemUTC());
        String token = jwt.issue("C-101", Set.of("orders:read", "orders:write"), Duration.ofMinutes(5));
        SecurityFilterChain security = new SecurityFilterChain(jwt);
        AuthenticatedUser user = security.authenticate("Bearer " + token, "orders:read");

        ServiceRegistry registry = new ServiceRegistry();
        registry.register("inventory", "inventory-1");
        CircuitBreaker breaker = new CircuitBreaker(2, Duration.ofSeconds(2), Clock.systemUTC());
        Gateway gateway = new Gateway(registry, new TokenBucket(10, 10, Clock.systemUTC()), breaker);
        String route = gateway.route("inventory", user.subject, () -> "stock=18");

        EventBus kafka = new PartitionedEventBus(3);
        EventBus rabbit = new QueueEventBus();
        Outbox outbox = new Outbox();
        outbox.add(new Event(UUID.randomUUID().toString(), order.id, "OrderCreated", Instant.now()));
        outbox.publishPending(kafka);
        rabbit.publish(new Event(UUID.randomUUID().toString(), order.id, "SendEmail", Instant.now()));

        try (AsyncProcessor async = new AsyncProcessor(4)) {
            CompletableFuture<String> notification = async.submit(
                    trace.wrap(() -> "email-sent-for=" + order.id));
            Flux<Integer> quantities = Flux.from(order.lines).map(line -> line.quantity);
            int itemCount = quantities.reduce(0, Integer::sum).block();
            metrics.increment("orders.read");

            BatchJob<OrderLine, String> batch = new BatchJob<>(2,
                    line -> line.product.name + " x " + line.quantity,
                    output -> metrics.add("batch.rows", output.size()));
            JobResult jobResult = batch.run(order.lines);

            System.out.println("profile=" + properties.profile);
            System.out.println("beans=" + beans.beanNames());
            System.out.println("authenticated=" + user.subject);
            System.out.println("gateway=" + route);
            System.out.println("order=" + orders.findById(order.id).orElseThrow());
            System.out.println("items=" + itemCount + ", async=" + notification.get());
            System.out.println("kafka-events=" + kafka.drain("billing"));
            System.out.println("rabbit-events=" + rabbit.drain("email-worker"));
            System.out.println("batch=" + jobResult);
            System.out.println("health=" + new HealthEndpoint(beans, orders).health());
            System.out.println("metrics=" + metrics.snapshot());
            System.out.println("trace=" + trace);
        }
    }

    // Profiles, external configuration and validated @ConfigurationProperties equivalent.
    static final class AppProperties {
        final String profile;
        final boolean paymentEnabled;
        final Duration paymentTimeout;

        AppProperties(String profile, boolean paymentEnabled, Duration paymentTimeout) {
            if (profile == null || profile.isBlank()) throw new IllegalArgumentException("profile required");
            if (paymentTimeout.isZero() || paymentTimeout.isNegative()) {
                throw new IllegalArgumentException("payment timeout must be positive");
            }
            this.profile = profile;
            this.paymentEnabled = paymentEnabled;
            this.paymentTimeout = paymentTimeout;
        }

        static AppProperties load(Map<String, String> environment) {
            return new AppProperties(environment.getOrDefault("APP_PROFILE", "local"),
                    Boolean.parseBoolean(environment.getOrDefault("PAYMENT_ENABLED", "false")),
                    Duration.ofMillis(Long.parseLong(
                            environment.getOrDefault("PAYMENT_TIMEOUT_MS", "1000"))));
        }
    }

    // IoC container plus conditional auto-configuration and @ConditionalOnMissingBean behavior.
    static final class BeanContext {
        private final Map<Class<?>, Object> beans = new LinkedHashMap<>();

        <T> void registerIfMissing(Class<T> type, Supplier<T> factory) {
            beans.computeIfAbsent(type, ignored -> factory.get());
        }

        <T> T get(Class<T> type) {
            Object bean = beans.get(type);
            if (bean == null) throw new IllegalStateException("Missing bean: " + type.getSimpleName());
            return type.cast(bean);
        }

        List<String> beanNames() {
            return beans.keySet().stream().map(Class::getSimpleName).toList();
        }
    }

    static final class AutoConfiguration {
        static BeanContext configure(AppProperties properties, Metrics metrics) {
            BeanContext context = new BeanContext();
            context.registerIfMissing(Metrics.class, () -> metrics);
            context.registerIfMissing(Clock.class, Clock::systemUTC);
            if (properties.paymentEnabled) {
                context.registerIfMissing(PaymentClient.class,
                        () -> new PaymentClient(properties.paymentTimeout));
            }
            if ("prod".equals(properties.profile)) {
                context.registerIfMissing(AuditSink.class, DurableAuditSink::new);
            } else {
                context.registerIfMissing(AuditSink.class, ConsoleAuditSink::new);
            }
            return context;
        }
    }

    static final class PaymentClient {
        final Duration timeout;
        PaymentClient(Duration timeout) { this.timeout = timeout; }
    }
    interface AuditSink { void record(String message); }
    static final class DurableAuditSink implements AuditSink {
        public void record(String message) { Objects.requireNonNull(message); }
    }
    static final class ConsoleAuditSink implements AuditSink {
        public void record(String message) { System.out.println("AUDIT " + message); }
    }

    // JPA-style entities: many-to-one, one-to-many, many-to-many and optimistic @Version.
    static final class Customer {
        final String id;
        final String name;
        Customer(String id, String name) { this.id = id; this.name = name; }
    }
    static final class Product {
        final String id;
        final String name;
        final BigDecimal price;
        Product(String id, String name, BigDecimal price) {
            this.id = id; this.name = name; this.price = price;
        }
    }
    static final class OrderLine {
        final String id;
        final Product product;
        final int quantity;
        OrderLine(String id, Product product, int quantity) {
            if (quantity < 1) throw new IllegalArgumentException("quantity must be positive");
            this.id = id; this.product = Objects.requireNonNull(product); this.quantity = quantity;
        }
    }
    static final class PurchaseOrder {
        final String id;
        final Customer customer;
        final List<OrderLine> lines;
        final Set<String> tags;
        final long version;

        PurchaseOrder(String id, Customer customer, List<OrderLine> lines, Set<String> tags) {
            this(id, customer, lines, tags, 0);
        }
        PurchaseOrder(String id, Customer customer, List<OrderLine> lines,
                      Set<String> tags, long version) {
            this.id = id; this.customer = customer; this.lines = List.copyOf(lines);
            this.tags = Set.copyOf(tags); this.version = version;
        }
        BigDecimal total() {
            return lines.stream().map(l -> l.product.price.multiply(BigDecimal.valueOf(l.quantity)))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        PurchaseOrder nextVersion() {
            return new PurchaseOrder(id, customer, lines, tags, version + 1);
        }
        public String toString() { return id + " total=" + total() + " version=" + version; }
    }

    interface Specification<T> extends Predicate<T> {
        default Specification<T> and(Specification<T> other) {
            return value -> test(value) && other.test(value);
        }
    }
    record Page<T>(List<T> content, int number, int size, long totalElements) {}

    static final class InMemoryOrderRepository {
        private final Map<String, PurchaseOrder> rows = new ConcurrentHashMap<>();

        Optional<PurchaseOrder> findById(String id) { return Optional.ofNullable(rows.get(id)); }

        void save(PurchaseOrder order, long expectedVersion) {
            rows.compute(order.id, (id, current) -> {
                long actual = current == null ? -1 : current.version;
                if (actual != expectedVersion) {
                    throw new OptimisticLockFailure(expectedVersion, actual);
                }
                return order;
            });
        }

        Page<PurchaseOrder> findAll(Specification<PurchaseOrder> specification,
                                    int page, int size,
                                    Comparator<PurchaseOrder> sort) {
            List<PurchaseOrder> matches = rows.values().stream()
                    .filter(specification).sorted(sort).toList();
            int from = Math.min(page * size, matches.size());
            int to = Math.min(from + size, matches.size());
            return new Page<>(matches.subList(from, to), page, size, matches.size());
        }
    }
    static final class OptimisticLockFailure extends RuntimeException {
        OptimisticLockFailure(long expected, long actual) {
            super("expected version " + expected + " but found " + actual);
        }
    }

    // Security filter chain with signed, expiring, scoped bearer tokens.
    record AuthenticatedUser(String subject, Set<String> scopes) {}
    static final class JwtService {
        private final String secret;
        private final Clock clock;
        private final Set<String> revoked = ConcurrentHashMap.newKeySet();

        JwtService(String secret, Clock clock) { this.secret = secret; this.clock = clock; }

        String issue(String subject, Set<String> scopes, Duration ttl) {
            String id = UUID.randomUUID().toString();
            String payload = id + "|" + subject + "|" + String.join(",", scopes)
                    + "|" + clock.instant().plus(ttl).getEpochSecond();
            return encode(payload) + "." + signature(payload);
        }

        AuthenticatedUser verify(String token) {
            String[] segments = token.split("\\.");
            if (segments.length != 2) throw new SecurityException("malformed token");
            String payload = new String(Base64.getUrlDecoder().decode(segments[0]), StandardCharsets.UTF_8);
            if (!constantTimeEquals(signature(payload), segments[1])) {
                throw new SecurityException("invalid signature");
            }
            String[] claims = payload.split("\\|", -1);
            if (claims.length != 4 || revoked.contains(claims[0])) throw new SecurityException("revoked token");
            if (clock.instant().getEpochSecond() >= Long.parseLong(claims[3])) {
                throw new SecurityException("expired token");
            }
            Set<String> scopes = claims[2].isBlank() ? Set.of() : Set.of(claims[2].split(","));
            return new AuthenticatedUser(claims[1], scopes);
        }

        void revoke(String tokenId) { revoked.add(tokenId); }
        private String encode(String value) {
            return Base64.getUrlEncoder().withoutPadding()
                    .encodeToString(value.getBytes(StandardCharsets.UTF_8));
        }
        private String signature(String payload) {
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                return encode(bytesToHex(digest.digest((payload + secret)
                        .getBytes(StandardCharsets.UTF_8))));
            } catch (Exception exception) { throw new IllegalStateException(exception); }
        }
        private static String bytesToHex(byte[] bytes) {
            StringBuilder result = new StringBuilder();
            for (byte value : bytes) result.append(String.format("%02x", value));
            return result.toString();
        }
        private static boolean constantTimeEquals(String left, String right) {
            return MessageDigest.isEqual(left.getBytes(StandardCharsets.UTF_8),
                    right.getBytes(StandardCharsets.UTF_8));
        }
    }
    static final class SecurityFilterChain {
        private final JwtService jwt;
        SecurityFilterChain(JwtService jwt) { this.jwt = jwt; }
        AuthenticatedUser authenticate(String authorization, String requiredScope) {
            if (authorization == null || !authorization.startsWith("Bearer ")) {
                throw new SecurityException("bearer token required");
            }
            AuthenticatedUser user = jwt.verify(authorization.substring(7));
            if (!user.scopes.contains(requiredScope)) throw new SecurityException("insufficient scope");
            return user;
        }
    }

    // Service discovery, gateway routing, rate limiting and Resilience4j-style breaker.
    static final class ServiceRegistry {
        private final Map<String, List<String>> services = new ConcurrentHashMap<>();
        private final AtomicInteger cursor = new AtomicInteger();
        void register(String service, String instance) {
            services.computeIfAbsent(service, ignored -> new ArrayList<>()).add(instance);
        }
        String resolve(String service) {
            List<String> instances = services.getOrDefault(service, List.of());
            if (instances.isEmpty()) throw new IllegalStateException("no healthy instance for " + service);
            return instances.get(Math.floorMod(cursor.getAndIncrement(), instances.size()));
        }
    }
    static final class Gateway {
        private final ServiceRegistry registry;
        private final TokenBucket rateLimiter;
        private final CircuitBreaker breaker;
        Gateway(ServiceRegistry registry, TokenBucket limiter, CircuitBreaker breaker) {
            this.registry = registry; this.rateLimiter = limiter; this.breaker = breaker;
        }
        <T> String route(String service, String client, Callable<T> call) throws Exception {
            if (!rateLimiter.tryAcquire(client)) throw new IllegalStateException("rate limit exceeded");
            String instance = registry.resolve(service);
            return instance + ":" + breaker.execute(call);
        }
    }
    static final class TokenBucket {
        private final int capacity;
        private final double refillPerSecond;
        private final Clock clock;
        private final Map<String, Bucket> buckets = new HashMap<>();
        TokenBucket(int capacity, double refillPerSecond, Clock clock) {
            this.capacity = capacity; this.refillPerSecond = refillPerSecond; this.clock = clock;
        }
        synchronized boolean tryAcquire(String key) {
            Bucket bucket = buckets.computeIfAbsent(key, ignored -> new Bucket(capacity, clock.instant()));
            double elapsed = Duration.between(bucket.updated, clock.instant()).toNanos() / 1_000_000_000.0;
            bucket.tokens = Math.min(capacity, bucket.tokens + elapsed * refillPerSecond);
            bucket.updated = clock.instant();
            if (bucket.tokens < 1) return false;
            bucket.tokens--;
            return true;
        }
        static final class Bucket {
            double tokens; Instant updated;
            Bucket(double tokens, Instant updated) { this.tokens = tokens; this.updated = updated; }
        }
    }
    static final class CircuitBreaker {
        enum State { CLOSED, OPEN, HALF_OPEN }
        private final int threshold;
        private final Duration openDuration;
        private final Clock clock;
        private int failures;
        private Instant openedAt;
        CircuitBreaker(int threshold, Duration openDuration, Clock clock) {
            this.threshold = threshold; this.openDuration = openDuration; this.clock = clock;
        }
        synchronized State state() {
            if (openedAt == null) return State.CLOSED;
            return clock.instant().isBefore(openedAt.plus(openDuration)) ? State.OPEN : State.HALF_OPEN;
        }
        synchronized <T> T execute(Callable<T> operation) throws Exception {
            if (state() == State.OPEN) throw new IllegalStateException("circuit is open");
            try {
                T value = operation.call(); failures = 0; openedAt = null; return value;
            } catch (Exception failure) {
                if (++failures >= threshold) openedAt = clock.instant();
                throw failure;
            }
        }
    }

    // Kafka-style partitioned log, RabbitMQ-style queue, idempotent consumer and outbox.
    record Event(String id, String aggregateId, String type, Instant occurredAt) {}
    interface EventBus { void publish(Event event); List<Event> drain(String consumer); }
    static final class PartitionedEventBus implements EventBus {
        private final List<List<Event>> partitions;
        private final Map<String, Set<String>> consumed = new ConcurrentHashMap<>();
        PartitionedEventBus(int count) {
            partitions = new ArrayList<>();
            for (int i = 0; i < count; i++) partitions.add(new ArrayList<>());
        }
        public synchronized void publish(Event event) {
            partitions.get(Math.floorMod(event.aggregateId.hashCode(), partitions.size())).add(event);
        }
        public synchronized List<Event> drain(String consumer) {
            Set<String> ids = consumed.computeIfAbsent(consumer, ignored -> ConcurrentHashMap.newKeySet());
            return partitions.stream().flatMap(List::stream).filter(e -> ids.add(e.id)).toList();
        }
    }
    static final class QueueEventBus implements EventBus {
        private final Queue<Event> queue = new ArrayDeque<>();
        public synchronized void publish(Event event) { queue.add(event); }
        public synchronized List<Event> drain(String consumer) {
            List<Event> result = new ArrayList<>();
            while (!queue.isEmpty()) result.add(queue.remove());
            return result;
        }
    }
    static final class Outbox {
        private final List<PendingEvent> rows = new ArrayList<>();
        synchronized void add(Event event) { rows.add(new PendingEvent(event)); }
        synchronized void publishPending(EventBus bus) {
            rows.stream().filter(row -> !row.published).forEach(row -> {
                bus.publish(row.event); row.published = true;
            });
        }
        static final class PendingEvent {
            final Event event; boolean published;
            PendingEvent(Event event) { this.event = event; }
        }
    }

    // @Async/custom executor equivalent. Virtual-thread use is enabled reflectively on JDK 21+.
    static final class AsyncProcessor implements AutoCloseable {
        private final ExecutorService executor;
        AsyncProcessor(int threads) { executor = createExecutor(threads); }
        <T> CompletableFuture<T> submit(Callable<T> task) {
            return CompletableFuture.supplyAsync(() -> {
                try { return task.call(); }
                catch (Exception exception) { throw new RuntimeException(exception); }
            }, executor);
        }
        private static ExecutorService createExecutor(int threads) {
            try {
                Object builder = Executors.class.getMethod("newVirtualThreadPerTaskExecutor").invoke(null);
                return (ExecutorService) builder;
            } catch (ReflectiveOperationException unsupported) {
                return Executors.newFixedThreadPool(threads);
            }
        }
        public void close() throws InterruptedException {
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        }
    }

    // Minimal lazy Mono/Flux model with map, filter, flatMap, reduce and error fallback.
    static final class Mono<T> {
        private final Supplier<T> supplier;
        private Mono(Supplier<T> supplier) { this.supplier = supplier; }
        static <T> Mono<T> just(T value) { return new Mono<>(() -> value); }
        static <T> Mono<T> from(Supplier<T> supplier) { return new Mono<>(supplier); }
        <R> Mono<R> map(Function<T, R> mapper) { return from(() -> mapper.apply(block())); }
        <R> Mono<R> flatMap(Function<T, Mono<R>> mapper) { return from(() -> mapper.apply(block()).block()); }
        Mono<T> onErrorReturn(T fallback) {
            return from(() -> { try { return block(); } catch (RuntimeException ignored) { return fallback; } });
        }
        T block() { return supplier.get(); }
    }
    static final class Flux<T> {
        private final Supplier<List<T>> supplier;
        private Flux(Supplier<List<T>> supplier) { this.supplier = supplier; }
        static <T> Flux<T> from(List<T> values) { return new Flux<>(() -> List.copyOf(values)); }
        <R> Flux<R> map(Function<T, R> mapper) {
            return new Flux<>(() -> supplier.get().stream().map(mapper).toList());
        }
        Flux<T> filter(Predicate<T> predicate) {
            return new Flux<>(() -> supplier.get().stream().filter(predicate).toList());
        }
        <R> Mono<R> reduce(R identity, java.util.function.BiFunction<R, T, R> reducer) {
            return Mono.from(() -> {
                R result = identity;
                for (T value : supplier.get()) result = reducer.apply(result, value);
                return result;
            });
        }
    }

    // Actuator/Micrometer/OpenTelemetry-style health, metrics and trace propagation.
    static final class Metrics {
        private final Map<String, AtomicLong> counters = new ConcurrentHashMap<>();
        void increment(String name) { add(name, 1); }
        void add(String name, long amount) {
            counters.computeIfAbsent(name, ignored -> new AtomicLong()).addAndGet(amount);
        }
        Map<String, Long> snapshot() {
            Map<String, Long> copy = new LinkedHashMap<>();
            counters.forEach((key, value) -> copy.put(key, value.get()));
            return copy;
        }
    }
    static final class TraceContext {
        final String traceId;
        final String spanName;
        final Instant startedAt;
        private TraceContext(String traceId, String spanName, Instant startedAt) {
            this.traceId = traceId; this.spanName = spanName; this.startedAt = startedAt;
        }
        static TraceContext start(String spanName) {
            return new TraceContext(UUID.randomUUID().toString(), spanName, Instant.now());
        }
        <T> Callable<T> wrap(Callable<T> task) { return task; }
        public String toString() { return traceId + "/" + spanName; }
    }
    static final class HealthEndpoint {
        private final BeanContext context;
        private final InMemoryOrderRepository repository;
        HealthEndpoint(BeanContext context, InMemoryOrderRepository repository) {
            this.context = context; this.repository = repository;
        }
        Map<String, String> health() {
            return Map.of("liveness", "UP", "readiness",
                    context.get(Clock.class) != null && repository != null ? "UP" : "DOWN");
        }
    }

    // Multiple databases and explicit compensation (a local Saga) on partial failure.
    static final class InMemoryDatabase {
        private final Map<String, Object> rows = new HashMap<>();
        Map<String, Object> snapshot() { return new HashMap<>(rows); }
        void restore(Map<String, Object> snapshot) { rows.clear(); rows.putAll(snapshot); }
    }
    static final class MultiDatabaseTransaction {
        private final InMemoryDatabase primary;
        private final InMemoryDatabase audit;
        MultiDatabaseTransaction(InMemoryDatabase primary, InMemoryDatabase audit) {
            this.primary = primary; this.audit = audit;
        }
        void execute(Runnable work) {
            Map<String, Object> beforePrimary = primary.snapshot();
            Map<String, Object> beforeAudit = audit.snapshot();
            try { work.run(); }
            catch (RuntimeException failure) {
                primary.restore(beforePrimary); audit.restore(beforeAudit); throw failure;
            }
        }
    }

    // Spring Batch-style reader/processor/writer chunks with restart-friendly result metadata.
    interface ChunkWriter<T> { void write(List<T> items); }
    record JobResult(int read, int written, int skipped, boolean completed) {}
    static final class BatchJob<I, O> {
        private final int chunkSize;
        private final Function<I, O> processor;
        private final ChunkWriter<O> writer;
        BatchJob(int chunkSize, Function<I, O> processor, ChunkWriter<O> writer) {
            this.chunkSize = chunkSize; this.processor = processor; this.writer = writer;
        }
        JobResult run(List<I> input) {
            int written = 0;
            int skipped = 0;
            List<O> chunk = new ArrayList<>(chunkSize);
            for (I item : input) {
                try {
                    chunk.add(processor.apply(item));
                    if (chunk.size() == chunkSize) { writer.write(List.copyOf(chunk)); written += chunk.size(); chunk.clear(); }
                } catch (RuntimeException badRecord) { skipped++; }
            }
            if (!chunk.isEmpty()) { writer.write(List.copyOf(chunk)); written += chunk.size(); }
            return new JobResult(input.size(), written, skipped, true);
        }
    }
}
