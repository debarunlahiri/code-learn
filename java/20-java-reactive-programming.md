# Reactive Programming in Java - Complete Guide

Reactive programming is a programming paradigm focused on data streams and the propagation of change. It enables developers to build asynchronous, event-driven applications that can handle real-time data efficiently.

---

## 1. What is Reactive Programming?

### Core Concepts
- **Streams**: Sequences of ongoing events ordered over time
- **Observables**: Data sources that emit streams of events
- **Observers/Subscribers**: Consumers that react to emitted events
- **Operators**: Functions that transform, filter, or combine streams
- **Schedulers**: Control the threading context of stream operations
- **Backpressure**: Mechanism to handle fast producers vs slow consumers

### Benefits
- **Asynchronous**: Non-blocking operations
- **Responsive**: Better user experience
- **Resilient**: Better error handling and recovery
- **Scalable**: Efficient resource utilization
- **Composable**: Easy to combine multiple streams

---

## 2. Reactive Streams Specification

### 2.1 Core Interfaces

```java
// Publisher interface
public interface Publisher<T> {
    void subscribe(Subscriber<? super T> subscriber);
}

// Subscriber interface
public interface Subscriber<T> {
    void onSubscribe(Subscription subscription);
    void onNext(T item);
    void onError(Throwable throwable);
    void onComplete();
}

// Subscription interface
public interface Subscription {
    void request(long n);
    void cancel();
}

// Processor interface (Publisher + Subscriber)
public interface Processor<T, R> extends Subscriber<T>, Publisher<R> {}
```

### 2.2 Simple Implementation

```java
public class SimplePublisher implements Publisher<String> {
    private final List<String> items;
    private final Executor executor;
    
    public SimplePublisher(List<String> items) {
        this.items = new ArrayList<>(items);
        this.executor = Executors.newSingleThreadExecutor();
    }
    
    @Override
    public void subscribe(Subscriber<? super String> subscriber) {
        executor.execute(() -> {
            Subscription subscription = new Subscription() {
                private volatile boolean cancelled = false;
                private volatile long requested = 0;
                private int index = 0;
                
                @Override
                public void request(long n) {
                    requested += n;
                    emit();
                }
                
                @Override
                public void cancel() {
                    cancelled = true;
                }
                
                private void emit() {
                    while (!cancelled && index < items.size() && requested > 0) {
                        subscriber.onNext(items.get(index++));
                        requested--;
                    }
                    
                    if (index >= items.size() && !cancelled) {
                        subscriber.onComplete();
                    }
                }
            };
            
            subscriber.onSubscribe(subscription);
        });
    }
}

// Usage
public class ReactiveExample {
    public static void main(String[] args) {
        List<String> data = Arrays.asList("Hello", "World", "Reactive", "Programming");
        
        SimplePublisher publisher = new SimplePublisher(data);
        
        publisher.subscribe(new Subscriber<String>() {
            private Subscription subscription;
            
            @Override
            public void onSubscribe(Subscription subscription) {
                this.subscription = subscription;
                subscription.request(1); // Request first item
            }
            
            @Override
            public void onNext(String item) {
                System.out.println("Received: " + item);
                subscription.request(1); // Request next item
            }
            
            @Override
            public void onError(Throwable throwable) {
                System.err.println("Error: " + throwable.getMessage());
            }
            
            @Override
            public void onComplete() {
                System.out.println("Stream completed");
            }
        });
    }
}
```

---

## 3. Project Reactor

### 3.1 Core Types

```java
import reactor.core.publisher.*;
import reactor.core.scheduler.Schedulers;
import java.time.Duration;

public class ReactorBasics {
    
    // Mono - 0..1 items
    public Mono<String> getMonoExample() {
        return Mono.just("Hello World")
            .map(String::toUpperCase)
            .filter(s -> s.startsWith("H"));
    }
    
    // Flux - 0..N items
    public Flux<Integer> getFluxExample() {
        return Flux.range(1, 10)
            .filter(n -> n % 2 == 0)
            .map(n -> n * 2);
    }
    
    // Creating Mono
    public Mono<String> createMono() {
        return Mono.fromCallable(() -> {
            // Expensive operation
            Thread.sleep(1000);
            return "Result";
        });
        
        // Or from other sources
        Mono<String> fromOptional = Mono.justOrEmpty(Optional.of("Value"));
        Mono<String> fromFuture = Mono.fromFuture(CompletableFuture.supplyAsync(() -> "Async"));
        Mono<String> fromSupplier = Mono.fromSupplier(() -> "Supplied");
    }
    
    // Creating Flux
    public Flux<String> createFlux() {
        return Flux.just("A", "B", "C", "D");
        
        // Or from other sources
        Flux<Integer> fromIterable = Flux.fromIterable(Arrays.asList(1, 2, 3));
        Flux<Integer> fromArray = Flux.fromArray(new Integer[]{1, 2, 3});
        Flux<Long> fromInterval = Flux.interval(Duration.ofSeconds(1));
        Flux<String> fromStream = Flux.fromStream(Stream.of("X", "Y", "Z"));
    }
}
```

### 3.2 Operators

```java
public class ReactorOperators {
    
    // Transformation operators
    public Flux<String> transformOperators() {
        return Flux.just("apple", "banana", "cherry")
            .map(String::toUpperCase)                    // Transform each item
            .flatMap(s -> Flux.fromArray(s.split("")))   // Split into characters
            .distinct()                                  // Remove duplicates
            .sort();                                     // Sort
    }
    
    // Filtering operators
    public Flux<Integer> filterOperators() {
        return Flux.range(1, 20)
            .filter(n -> n % 2 == 0)                     // Even numbers only
            .take(5)                                     // Take first 5
            .skipLast(2);                                // Skip last 2
    }
    
    // Combining operators
    public Flux<String> combineOperators() {
        Flux<String> flux1 = Flux.just("A", "B", "C");
        Flux<String> flux2 = Flux.just("X", "Y", "Z");
        
        return Flux.concat(flux1, flux2)                 // Concatenate
            .mergeWith(Flux.just("M", "N"))              // Merge
            .zipWith(Flux.just(1, 2, 3, 4, 5, 6, 7),    // Zip with numbers
                (letter, number) -> letter + number);
    }
    
    // Error handling
    public Mono<String> errorHandling() {
        return Mono.error(new RuntimeException("Something went wrong"))
            .onErrorReturn("Default value")             // Return default on error
            .onErrorResume(e -> Mono.just("Recovered: " + e.getMessage()))
            .retry(3)                                    // Retry 3 times
            .timeout(Duration.ofSeconds(5));            // Timeout after 5 seconds
    }
    
    // Conditional operators
    public Flux<String> conditionalOperators() {
        return Flux.just("A", "B", "C", "D")
            .takeUntil(s -> s.equals("C"))              // Take until condition
            .defaultIfEmpty("Default")                   // Default if empty
            .repeat(3);                                  // Repeat 3 times
    }
    
    // Mathematical operators
    public Mono<Integer> mathematicalOperators() {
        return Flux.range(1, 10)
            .buffer(3)                                  // Buffer into groups of 3
            .map(list -> list.stream().mapToInt(Integer::intValue).sum())
            .reduce(Integer::sum);                      // Sum all buffer sums
    }
}
```

### 3.3 Scheduling and Threading

```java
public class ReactorScheduling {
    
    public void schedulingExample() {
        Flux.just("A", "B", "C", "D")
            .subscribeOn(Schedulers.boundedElastic())    // Subscribe on elastic thread pool
            .publishOn(Schedulers.parallel())             // Publish on parallel thread pool
            .map(this::expensiveOperation)
            .doOnNext(item -> System.out.println("Thread: " + Thread.currentThread().getName()))
            .subscribe();
    }
    
    public void parallelProcessing() {
        Flux.range(1, 100)
            .parallel()                                 // Enable parallel processing
            .runOn(Schedulers.parallel())                // Run on parallel scheduler
            .map(this::expensiveOperation)
            .sequential()                               // Switch back to sequential
            .collectList()
            .subscribe();
    }
    
    private String expensiveOperation(String item) {
        try {
            Thread.sleep(100); // Simulate expensive operation
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return item + "_processed";
    }
    
    private int expensiveOperation(int number) {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return number * 2;
    }
}
```

---

## 4. RxJava 3

### 4.1 Core Types

```java
import io.reactivex.rxjava3.core.*;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import java.util.concurrent.TimeUnit;

public class RxJavaBasics {
    
    // Observable - 0..N items
    public Observable<String> getObservableExample() {
        return Observable.just("Hello", "World", "RxJava")
            .map(String::toUpperCase)
            .filter(s -> s.startsWith("H"));
    }
    
    // Single - Exactly 1 item or error
    public Single<String> getSingleExample() {
        return Single.just("Single Value")
            .map(String::toLowerCase);
    }
    
    // Maybe - 0 or 1 item
    public Maybe<String> getMaybeExample() {
        return Maybe.just("Maybe Value")
            .filter(s -> s.length() > 5);
    }
    
    // Completable - No items, only completion/error
    public Completable getCompletableExample() {
        return Completable.fromAction(() -> {
            // Side effect
            System.out.println("Operation completed");
        });
    }
    
    // Creating Observables
    public Observable<Integer> createObservable() {
        return Observable.create(emitter -> {
            for (int i = 0; i < 10; i++) {
                if (!emitter.isDisposed()) {
                    emitter.onNext(i);
                }
            }
            if (!emitter.isDisposed()) {
                emitter.onComplete();
            }
        });
        
        // Other creation methods
        Observable<Integer> fromArray = Observable.fromArray(1, 2, 3, 4, 5);
        Observable<Long> interval = Observable.interval(1, TimeUnit.SECONDS);
        Observable<String> fromCallable = Observable.fromCallable(() -> "Callable Result");
    }
}
```

### 4.2 Operators and Transformations

```java
public class RxJavaOperators {
    
    // Transformation operators
    public Observable<String> transformOperations() {
        return Observable.just("apple", "banana", "cherry")
            .map(String::toUpperCase)
            .flatMap(s -> Observable.fromArray(s.split("")))
            .distinct()
            .sorted();
    }
    
    // Filtering operators
    public Observable<Integer> filterOperations() {
        return Observable.range(1, 20)
            .filter(n -> n % 2 == 0)
            .take(5)
            .skipLast(2);
    }
    
    // Combining operators
    public Observable<String> combineOperations() {
        Observable<String> obs1 = Observable.just("A", "B", "C");
        Observable<String> obs2 = Observable.just("X", "Y", "Z");
        
        return Observable.concat(obs1, obs2)
            .mergeWith(Observable.just("M", "N"))
            .zipWith(Observable.range(1, 10), (letter, number) -> letter + number);
    }
    
    // Error handling
    public Single<String> errorHandling() {
        return Single.error(new RuntimeException("Error occurred"))
            .onErrorReturnItem("Default value")
            .onErrorResumeNext(Single.just("Recovered"))
            .retry(3)
            .timeout(5, TimeUnit.SECONDS);
    }
    
    // Backpressure handling
    public Flowable<Integer> backpressureExample() {
        return Flowable.create(emitter -> {
            for (int i = 0; i < 1000; i++) {
                if (!emitter.isCancelled()) {
                    emitter.onNext(i);
                }
            }
            emitter.onComplete();
        }, BackpressureStrategy.BUFFER); // or DROP, LATEST, MISSING
    }
}
```

### 4.3 Schedulers and Threading

```java
public class RxJavaScheduling {
    
    public void schedulingExample() {
        Observable.just("A", "B", "C", "D")
            .subscribeOn(Schedulers.io())               // Subscribe on IO thread
            .observeOn(Schedulers.computation())         // Observe on computation thread
            .map(this::expensiveOperation)
            .doOnNext(item -> System.out.println("Thread: " + Thread.currentThread().getName()))
            .observeOn(Schedulers.single())              // Switch to single thread for UI
            .subscribe(item -> System.out.println("UI Thread: " + Thread.currentThread().getName()));
    }
    
    public void parallelProcessing() {
        Observable.range(1, 100)
            .flatMap(item -> 
                Observable.just(item)
                    .subscribeOn(Schedulers.computation())
                    .map(this::expensiveOperation)
            )
            .toList()
            .subscribe();
    }
    
    private String expensiveOperation(String item) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return item + "_processed";
    }
    
    private int expensiveOperation(int number) {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return number * 2;
    }
}
```

---

## 5. Spring WebFlux - Reactive Web Applications

### 5.1 Reactive Controllers

```java
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.*;

@RestController
@RequestMapping("/api/users")
public class ReactiveUserController {
    
    private final UserService userService;
    
    public ReactiveUserController(UserService userService) {
        this.userService = userService;
    }
    
    @GetMapping("/{id}")
    public Mono<User> getUser(@PathVariable String id) {
        return userService.findById(id);
    }
    
    @GetMapping
    public Flux<User> getAllUsers() {
        return userService.findAll();
    }
    
    @PostMapping
    public Mono<User> createUser(@RequestBody User user) {
        return userService.save(user);
    }
    
    @PutMapping("/{id}")
    public Mono<User> updateUser(@PathVariable String id, @RequestBody User user) {
        return userService.update(id, user);
    }
    
    @DeleteMapping("/{id}")
    public Mono<Void> deleteUser(@PathVariable String id) {
        return userService.deleteById(id);
    }
    
    @GetMapping("/search")
    public Flux<User> searchUsers(@RequestParam String name) {
        return userService.findByNameContaining(name);
    }
    
    // Server-Sent Events
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<UserEvent> streamUserEvents() {
        return userService.getUserEvents();
    }
}
```

### 5.2 Reactive Services

```java
import org.springframework.stereotype.Service;
import reactor.core.publisher.*;

@Service
public class UserService {
    
    private final ReactiveUserRepository userRepository;
    
    public UserService(ReactiveUserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public Mono<User> findById(String id) {
        return userRepository.findById(id)
            .switchIfEmpty(Mono.error(new UserNotFoundException("User not found: " + id)));
    }
    
    public Flux<User> findAll() {
        return userRepository.findAll();
    }
    
    public Mono<User> save(User user) {
        return userRepository.save(user)
            .doOnSuccess(savedUser -> System.out.println("User saved: " + savedUser.getId()));
    }
    
    public Mono<User> update(String id, User user) {
        return userRepository.findById(id)
            .switchIfEmpty(Mono.error(new UserNotFoundException("User not found: " + id)))
            .flatMap(existingUser -> {
                existingUser.setName(user.getName());
                existingUser.setEmail(user.getEmail());
                return userRepository.save(existingUser);
            });
    }
    
    public Mono<Void> deleteById(String id) {
        return userRepository.deleteById(id);
    }
    
    public Flux<User> findByNameContaining(String name) {
        return userRepository.findByNameContaining(name);
    }
    
    public Flux<UserEvent> getUserEvents() {
        return Flux.interval(Duration.ofSeconds(1))
            .map(sequence -> new UserEvent("event-" + sequence, System.currentTimeMillis()));
    }
}
```

### 5.3 Reactive Repository

```java
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.*;

@Repository
public interface ReactiveUserRepository extends ReactiveCrudRepository<User, String> {
    
    Flux<User> findByNameContaining(String name);
    
    @Query("{ 'name': ?0 }")
    Flux<User> findByName(String name);
    
    @Query("{ 'email': ?0 }")
    Mono<User> findByEmail(String email);
    
    Flux<User> findByAgeGreaterThan(int age);
    
    Flux<User> findByActiveTrue();
}
```

---

## 6. Reactive Database Access

### 6.1 R2DBC - Reactive Database Connectivity

```java
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Query;
import reactor.core.publisher.*;

@Repository
public class ReactiveUserRepository {
    
    private final R2dbcEntityTemplate template;
    
    public ReactiveUserRepository(R2dbcEntityTemplate template) {
        this.template = template;
    }
    
    public Mono<User> findById(String id) {
        return template.selectOne(
            Query.query(Criteria.where("id").is(id)),
            User.class
        );
    }
    
    public Flux<User> findAll() {
        return template.select(User.class).all();
    }
    
    public Mono<User> save(User user) {
        return template.insert(user);
    }
    
    public Mono<User> update(User user) {
        return template.update(user);
    }
    
    public Mono<Void> deleteById(String id) {
        return template.delete(User.class)
            .matching(Query.query(Criteria.where("id").is(id)))
            .all()
            .then();
    }
    
    public Flux<User> findByAgeGreaterThan(int age) {
        return template.select(User.class)
            .matching(Query.query(Criteria.where("age").greaterThan(age)))
            .all();
    }
}
```

### 6.2 Reactive MongoDB

```java
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.*;

@Repository
public class ReactiveMongoUserRepository {
    
    private final ReactiveMongoTemplate template;
    
    public ReactiveMongoUserRepository(ReactiveMongoTemplate template) {
        this.template = template;
    }
    
    public Mono<User> findById(String id) {
        return template.findById(id, User.class);
    }
    
    public Flux<User> findAll() {
        return template.findAll(User.class);
    }
    
    public Mono<User> save(User user) {
        return template.save(user);
    }
    
    public Flux<User> findByNameContaining(String name) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").regex(name));
        return template.find(query, User.class);
    }
    
    public Flux<User> findByAgeGreaterThan(int age) {
        Query query = new Query();
        query.addCriteria(Criteria.where("age").gt(age));
        return template.find(query, User.class);
    }
    
    public Mono<Void> deleteById(String id) {
        return template.remove(Query.query(Criteria.where("_id").is(id)), User.class)
            .then();
    }
}
```

---

## 7. Reactive Testing

### 7.1 Testing with StepVerifier

```java
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import reactor.test.publisher.TestPublisher;

public class ReactiveTesting {
    
    @Test
    public void testFluxOperations() {
        Flux<String> flux = Flux.just("A", "B", "C")
            .map(String::toUpperCase)
            .filter(s -> s.startsWith("A"));
        
        StepVerifier.create(flux)
            .expectNext("A")
            .expectComplete()
            .verify();
    }
    
    @Test
    public void testMonoOperations() {
        Mono<String> mono = Mono.just("Hello")
            .map(String::toUpperCase)
            .filter(s -> s.startsWith("H"));
        
        StepVerifier.create(mono)
            .expectNext("HELLO")
            .expectComplete()
            .verify();
    }
    
    @Test
    public void testErrorHandling() {
        Mono<String> mono = Mono.error(new RuntimeException("Test error"))
            .onErrorReturn("Default");
        
        StepVerifier.create(mono)
            .expectNext("Default")
            .expectComplete()
            .verify();
    }
    
    @Test
    public void testTimeOperations() {
        Flux<Long> flux = Flux.interval(Duration.ofMillis(100))
            .take(3);
        
        StepVerifier.create(flux)
            .expectNext(0L, 1L, 2L)
            .expectComplete()
            .verify(Duration.ofSeconds(1));
    }
    
    @Test
    public void testWithTestPublisher() {
        TestPublisher<String> publisher = TestPublisher.create();
        
        Flux<String> flux = publisher.flux()
            .map(String::toUpperCase);
        
        StepVerifier.create(flux)
            .then(() -> publisher.emit("a", "b", "c"))
            .expectNext("A", "B", "C")
            .expectComplete()
            .verify();
    }
}
```

### 7.2 Testing WebFlux Controllers

```java
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.*;

import static org.mockito.Mockito.*;

@WebFluxTest(ReactiveUserController.class)
public class ReactiveUserControllerTest {
    
    @Autowired
    private WebTestClient webTestClient;
    
    @MockBean
    private UserService userService;
    
    @Test
    public void testGetUser() {
        User user = new User("1", "John Doe", "john@example.com");
        
        when(userService.findById("1")).thenReturn(Mono.just(user));
        
        webTestClient.get().uri("/api/users/1")
            .exchange()
            .expectStatus().isOk()
            .expectBody(User.class)
            .isEqualTo(user);
    }
    
    @Test
    public void testGetAllUsers() {
        User user1 = new User("1", "John Doe", "john@example.com");
        User user2 = new User("2", "Jane Doe", "jane@example.com");
        
        when(userService.findAll()).thenReturn(Flux.just(user1, user2));
        
        webTestClient.get().uri("/api/users")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(User.class)
            .hasSize(2)
            .contains(user1, user2);
    }
    
    @Test
    public void testCreateUser() {
        User user = new User("1", "John Doe", "john@example.com");
        
        when(userService.save(any(User.class))).thenReturn(Mono.just(user));
        
        webTestClient.post().uri("/api/users")
            .bodyValue(user)
            .exchange()
            .expectStatus().isOk()
            .expectBody(User.class)
            .isEqualTo(user);
    }
    
    @Test
    public void testUserNotFound() {
        when(userService.findById("999")).thenReturn(Mono.empty());
        
        webTestClient.get().uri("/api/users/999")
            .exchange()
            .expectStatus().isNotFound();
    }
}
```

---

## 8. Advanced Reactive Patterns

### 8.1 Circuit Breaker Pattern

```java
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.reactor.CircuitBreakerOperator;

public class ReactiveCircuitBreaker {
    
    private final CircuitBreaker circuitBreaker;
    private final ExternalService externalService;
    
    public ReactiveCircuitBreaker(ExternalService externalService) {
        this.circuitBreaker = CircuitBreaker.ofDefaults("externalService");
        this.externalService = externalService;
    }
    
    public Mono<String> callExternalService(String request) {
        return externalService.process(request)
            .transform(CircuitBreakerOperator.of(circuitBreaker))
            .onErrorResume(throwable -> {
                if (throwable instanceof CallNotPermittedException) {
                    return Mono.just("Circuit breaker is open");
                }
                return Mono.just("Fallback response");
            });
    }
    
    public Flux<String> callExternalServiceStream(Flux<String> requests) {
        return requests
            .flatMap(this::callExternalService)
            .onErrorResume(throwable -> Flux.just("Error in stream"));
    }
}
```

### 8.2 Retry and Timeout Patterns

```java
public class ReactiveRetryPatterns {
    
    public Mono<String> retryWithBackoff(Mono<String> operation) {
        return operation
            .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
                .maxBackoff(Duration.ofSeconds(10))
                .jitter(0.5)
                .doBeforeRetry(retrySignal -> 
                    System.out.println("Retrying attempt: " + retrySignal.totalRetries())
                )
            )
            .timeout(Duration.ofSeconds(30))
            .onErrorResume(throwable -> Mono.just("Operation failed after retries"));
    }
    
    public Flux<String> retryStream(Flux<String> stream) {
        return stream
            .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1)))
            .onErrorResume(throwable -> Flux.just("Stream error"));
    }
    
    public Mono<String> conditionalRetry(Mono<String> operation) {
        return operation
            .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
                .filter(throwable -> throwable instanceof NetworkException)
                .doBeforeRetry(retrySignal -> 
                    System.out.println("Retrying due to network error")
                )
            );
    }
}
```

### 8.3 Rate Limiting

```java
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class RateLimiter {
    
    private final ConcurrentHashMap<String, AtomicInteger> requestCounts = new ConcurrentHashMap<>();
    private final int maxRequests;
    private final Duration window;
    
    public RateLimiter(int maxRequests, Duration window) {
        this.maxRequests = maxRequests;
        this.window = window;
    }
    
    public Mono<Boolean> allowRequest(String clientId) {
        return Mono.fromCallable(() -> {
            AtomicInteger count = requestCounts.computeIfAbsent(clientId, k -> new AtomicInteger(0));
            
            if (count.incrementAndGet() <= maxRequests) {
                // Reset count after window
                Mono.delay(window)
                    .subscribe(v -> count.set(0));
                return true;
            }
            
            return false;
        });
    }
    
    public <T> Mono<T> rateLimit(String clientId, Mono<T> operation) {
        return allowRequest(clientId)
            .flatMap(allowed -> {
                if (allowed) {
                    return operation;
                } else {
                    return Mono.error(new RateLimitExceededException("Rate limit exceeded"));
                }
            });
    }
    
    public <T> Flux<T> rateLimitStream(String clientId, Flux<T> stream) {
        return stream
            .concatMap(item -> rateLimit(clientId, Mono.just(item)));
    }
}
```

---

## 9. Performance and Best Practices

### 9.1 Memory Management

```java
public class ReactiveMemoryManagement {
    
    // Avoid memory leaks with proper disposal
    public Disposable subscribeWithDisposal() {
        return Flux.interval(Duration.ofSeconds(1))
            .doOnNext(tick -> System.out.println("Tick: " + tick))
            .subscribe();
    }
    
    // Use backpressure to handle fast producers
    public Flux<Integer> handleBackpressure() {
        return Flux.create(emitter -> {
            for (int i = 0; i < 1000; i++) {
                if (!emitter.isCancelled()) {
                    emitter.onNext(i);
                }
            }
            emitter.onComplete();
        }, BackpressureStrategy.BUFFER);
    }
    
    // Use appropriate schedulers
    public Flux<String> optimizedScheduling() {
        return Flux.just("A", "B", "C", "D")
            .subscribeOn(Schedulers.boundedElastic())  // For blocking operations
            .publishOn(Schedulers.parallel())         // For CPU-intensive operations
            .map(this::expensiveOperation)
            .observeOn(Schedulers.single());           // For sequential operations
    }
    
    // Avoid blocking calls in reactive streams
    public Mono<String> nonBlockingOperation() {
        return Mono.fromCallable(() -> {
            // Instead of blocking, use non-blocking alternatives
            return blockingOperation();
        }).subscribeOn(Schedulers.boundedElastic());
    }
    
    private String blockingOperation() {
        // Simulate blocking operation
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "Result";
    }
    
    private String expensiveOperation(String item) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return item + "_processed";
    }
}
```

### 9.2 Error Handling Best Practices

```java
public class ReactiveErrorHandling {
    
    // Centralized error handling
    public Mono<String> centralizedErrorHandling(Mono<String> operation) {
        return operation
            .onErrorResume(NetworkException.class, e -> 
                Mono.just("Network error: " + e.getMessage()))
            .onErrorResume(BusinessException.class, e ->
                Mono.just("Business error: " + e.getMessage()))
            .onErrorReturn("Unknown error occurred");
    }
    
    // Retry with exponential backoff
    public Mono<String> retryWithBackoff(Mono<String> operation) {
        return operation
            .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
                .maxBackoff(Duration.ofSeconds(10))
                .jitter(0.5)
                .filter(this::isRetryableException)
            );
    }
    
    private boolean isRetryableException(Throwable throwable) {
        return throwable instanceof NetworkException || 
               throwable instanceof TimeoutException;
    }
    
    // Timeout handling
    public Mono<String> timeoutHandling(Mono<String> operation) {
        return operation
            .timeout(Duration.ofSeconds(5))
            .onErrorResume(TimeoutException.class, e ->
                Mono.just("Operation timed out"));
    }
    
    // Circuit breaker integration
    public Mono<String> circuitBreakerPattern(Mono<String> operation) {
        return operation
            .transform(CircuitBreakerOperator.of(
                CircuitBreaker.ofDefaults("operation")
            ))
            .onErrorResume(CallNotPermittedException.class, e ->
                Mono.just("Circuit breaker is open"));
    }
}
```

---

## 10. Real-World Examples

### 10.1 Real-time Data Processing Pipeline

```java
@Service
public class DataProcessingPipeline {
    
    private final Flux<DataEvent> eventStream;
    private final DataProcessor processor;
    private final DataRepository repository;
    
    public DataProcessingPipeline(DataProcessor processor, DataRepository repository) {
        this.processor = processor;
        this.repository = repository;
        this.eventStream = createEventStream();
    }
    
    private Flux<DataEvent> createEventStream() {
        return Flux.create(emitter -> {
            // Simulate real-time events
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            scheduler.scheduleAtFixedRate(() -> {
                if (!emitter.isCancelled()) {
                    emitter.next(new DataEvent("event-" + System.currentTimeMillis()));
                }
            }, 0, 100, TimeUnit.MILLISECONDS);
            
            // Cleanup on cancellation
            emitter.onCancellation(() -> scheduler.shutdown());
        });
    }
    
    public void startProcessing() {
        eventStream
            .flatMap(this::processEvent)
            .buffer(100)                           // Batch processing
            .flatMap(this::saveBatch)
            .doOnError(error -> logError(error))
            .retry()
            .subscribe();
    }
    
    private Mono<ProcessedData> processEvent(DataEvent event) {
        return Mono.fromCallable(() -> processor.process(event))
            .subscribeOn(Schedulers.parallel());
    }
    
    private Mono<Void> saveBatch(List<ProcessedData> batch) {
        return repository.saveAll(batch)
            .then()
            .subscribeOn(Schedulers.boundedElastic());
    }
    
    private void logError(Throwable error) {
        System.err.println("Processing error: " + error.getMessage());
    }
}
```

### 10.2 Reactive WebSocket Handler

```java
@Component
public class ReactiveWebSocketHandler {
    
    private final Flux<String> eventStream;
    
    public ReactiveWebSocketHandler() {
        this.eventStream = Flux.interval(Duration.ofSeconds(1))
            .map(sequence -> "Event " + sequence);
    }
    
    public Mono<Void> handle(WebSocketSession session) {
        Mono<Void> input = session.receive()
            .map(WebSocketMessage::getPayloadAsText)
            .doOnNext(message -> System.out.println("Received: " + message))
            .then();
        
        Mono<Void> output = session.send(
            eventStream
                .map(session::textMessage)
        );
        
        return Mono.zip(input, output).then();
    }
}
```

### 10.3 Reactive File Processing

```java
@Service
public class ReactiveFileProcessor {
    
    public Flux<String> processFile(Path filePath) {
        return Flux.using(
            () -> Files.lines(filePath),
            Flux::fromStream,
            stream -> stream.close()
        );
    }
    
    public Mono<Void> processAndSaveFiles(List<Path> files) {
        return Flux.fromIterable(files)
            .flatMap(this::processFile)
            .flatMap(this::saveLine)
            .then();
    }
    
    private Mono<Void> saveLine(String line) {
        return Mono.fromRunnable(() -> {
            // Save line to database or file
            System.out.println("Saving: " + line);
        }).subscribeOn(Schedulers.boundedElastic());
    }
    
    public Flux<FileProcessingResult> processFilesWithProgress(List<Path> files) {
        AtomicInteger processed = new AtomicInteger(0);
        int total = files.size();
        
        return Flux.fromIterable(files)
            .flatMap(file -> 
                processFile(file)
                    .collectList()
                    .map(lines -> new FileProcessingResult(
                        file.toString(), 
                        lines.size(), 
                        processed.incrementAndGet(),
                        total
                    ))
            );
    }
}
```

---

## 11. Summary and Key Takeaways

### Core Concepts to Master
1. **Streams**: Sequences of events over time
2. **Backpressure**: Handling fast producers vs slow consumers
3. **Schedulers**: Controlling threading context
4. **Operators**: Transforming and combining streams
5. **Error Handling**: Robust error management strategies

### Best Practices
1. Use non-blocking operations in reactive streams
2. Implement proper backpressure handling
3. Use appropriate schedulers for different operations
4. Handle errors gracefully with retries and circuit breakers
5. Test reactive code thoroughly with StepVerifier

### When to Use Reactive Programming
- Real-time data processing
- High-concurrency applications
- Streaming data applications
- Microservices with high throughput
- Applications requiring responsive UI

### Common Pitfalls to Avoid
1. Blocking calls in reactive streams
2. Memory leaks from undisposed subscriptions
3. Ignoring backpressure
4. Poor error handling
5. Mixing blocking and non-blocking code

Reactive programming in Java provides powerful tools for building scalable, responsive applications. Master these concepts to create modern, efficient systems!
