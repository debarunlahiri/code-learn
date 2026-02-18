# System Design Fundamentals for Interviews

Goal: Master system design concepts and patterns for technical interviews.

---

## 1. Scalability Basics

### What it is
Ability of a system to handle growing load by adding resources.

### Why it matters
- Essential for modern applications
- Interview requirement
- Real-world system design
- Performance optimization

### Key Concepts

#### Vertical Scaling (Scale Up)
- Increase resources of single server
- CPU, RAM, storage upgrades
- Limited by hardware constraints
- Simpler implementation

#### Horizontal Scaling (Scale Out)
- Add more servers to distribute load
- Better fault tolerance
- More complex coordination
- Virtually unlimited scaling

### When to use
- Growing user base
- Performance requirements
- High availability needs
- Cost optimization

### Java Example (Load Balancer)
```java
import java.util.*;
import java.util.concurrent.*;

public class LoadBalancer {
    private final List<String> servers;
    private final AtomicInteger currentIndex;
    private final Map<String, AtomicInteger> serverLoads;
    
    public LoadBalancer(List<String> servers) {
        this.servers = new ArrayList<>(servers);
        this.currentIndex = new AtomicInteger(0);
        this.serverLoads = new ConcurrentHashMap<>();
        servers.forEach(server -> serverLoads.put(server, new AtomicInteger(0)));
    }
    
    // Round Robin
    public String getNextServerRoundRobin() {
        int index = currentIndex.getAndIncrement() % servers.size();
        return servers.get(index);
    }
    
    // Least Connections
    public String getNextServerLeastConnections() {
        return serverLoads.entrySet().stream()
            .min(Map.Entry.comparingByValue(Comparator.comparingInt(AtomicInteger::get)))
            .map(Map.Entry::getKey)
            .orElse(servers.get(0));
    }
    
    public void incrementLoad(String server) {
        serverLoads.get(server).incrementAndGet();
    }
    
    public void decrementLoad(String server) {
        serverLoads.get(server).decrementAndGet();
    }
}
```

---

## 2. Caching Strategies

### What it is
Storing frequently accessed data in fast storage to reduce latency.

### Why it matters
- Performance optimization
- Reduced database load
- Better user experience
- Cost efficiency

### Cache Types

#### Client-Side Caching
- Browser cache
- Application cache
- Local storage

#### Server-Side Caching
- In-memory cache
- Distributed cache
- Database cache

### Cache Patterns

#### Cache-Aside
```java
public class CacheAsidePattern<K, V> {
    private final Map<K, V> cache;
    private final DataSource<K, V> dataSource;
    
    public CacheAsidePattern(DataSource<K, V> dataSource) {
        this.cache = new ConcurrentHashMap<>();
        this.dataSource = dataSource;
    }
    
    public V get(K key) {
        V value = cache.get(key);
        if (value == null) {
            value = dataSource.load(key);
            if (value != null) {
                cache.put(key, value);
            }
        }
        return value;
    }
    
    public void put(K key, V value) {
        cache.put(key, value);
        dataSource.save(key, value);
    }
    
    public void invalidate(K key) {
        cache.remove(key);
    }
}
```

#### Write-Through
```java
public class WriteThroughCache<K, V> {
    private final Map<K, V> cache;
    private final DataSource<K, V> dataSource;
    
    public WriteThroughCache(DataSource<K, V> dataSource) {
        this.cache = new ConcurrentHashMap<>();
        this.dataSource = dataSource;
    }
    
    public V get(K key) {
        return cache.get(key);
    }
    
    public void put(K key, V value) {
        cache.put(key, value);
        dataSource.save(key, value); // Synchronous write
    }
}
```

#### Write-Behind (Write-Back)
```java
public class WriteBehindCache<K, V> {
    private final Map<K, V> cache;
    private final DataSource<K, V> dataSource;
    private final BlockingQueue<WriteOperation<K, V>> writeQueue;
    
    public WriteBehindCache(DataSource<K, V> dataSource) {
        this.cache = new ConcurrentHashMap<>();
        this.dataSource = dataSource;
        this.writeQueue = new LinkedBlockingQueue<>();
        startWriterThread();
    }
    
    public V get(K key) {
        return cache.get(key);
    }
    
    public void put(K key, V value) {
        cache.put(key, value);
        writeQueue.offer(new WriteOperation<>(key, value));
    }
    
    private void startWriterThread() {
        Thread writer = new Thread(() -> {
            while (true) {
                try {
                    WriteOperation<K, V> op = writeQueue.take();
                    dataSource.save(op.key, op.value);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        writer.start();
    }
    
    private static class WriteOperation<K, V> {
        final K key;
        final V value;
        WriteOperation(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
```

---

## 3. Database Design

### What it is
Designing efficient database schemas for performance and scalability.

### Why it matters
- Data integrity
- Query performance
- Scalability
- Maintenance

### Key Concepts

#### Normalization vs Denormalization
- Normalization: Reduce redundancy, improve consistency
- Denormalization: Improve read performance, simpler queries

#### Indexing
```java
public class IndexManager {
    private final Map<String, Map<Object, Set<String>>> indexes;
    private final Map<String, Object> dataStore;
    
    public IndexManager() {
        this.indexes = new ConcurrentHashMap<>();
        this.dataStore = new ConcurrentHashMap<>();
    }
    
    public void createIndex(String fieldName) {
        indexes.put(fieldName, new ConcurrentHashMap<>());
    }
    
    public void insert(String id, Map<String, Object> document) {
        dataStore.put(id, document);
        
        // Update indexes
        for (String fieldName : indexes.keySet()) {
            Object value = document.get(fieldName);
            if (value != null) {
                indexes.get(fieldName).computeIfAbsent(value, k -> new HashSet<>()).add(id);
            }
        }
    }
    
    public List<String> findByField(String fieldName, Object value) {
        Map<Object, Set<String>> index = indexes.get(fieldName);
        if (index == null) {
            return Collections.emptyList();
        }
        Set<String> ids = index.get(value);
        return ids != null ? new ArrayList<>(ids) : Collections.emptyList();
    }
}
```

#### Connection Pooling
```java
public class ConnectionPool {
    private final BlockingQueue<Connection> availableConnections;
    private final Set<Connection> usedConnections;
    private final String url;
    private final String username;
    private final String password;
    private final int maxPoolSize;
    
    public ConnectionPool(String url, String username, String password, int maxPoolSize) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.maxPoolSize = maxPoolSize;
        this.availableConnections = new LinkedBlockingQueue<>(maxPoolSize);
        this.usedConnections = ConcurrentHashMap.newKeySet();
        initializePool();
    }
    
    private void initializePool() {
        for (int i = 0; i < maxPoolSize; i++) {
            availableConnections.offer(createConnection());
        }
    }
    
    public Connection getConnection() throws InterruptedException {
        Connection connection = availableConnections.poll();
        if (connection == null) {
            // Wait for available connection
            connection = availableConnections.take();
        }
        usedConnections.add(connection);
        return connection;
    }
    
    public void releaseConnection(Connection connection) {
        if (usedConnections.remove(connection)) {
            availableConnections.offer(connection);
        }
    }
    
    private Connection createConnection() {
        // Implementation would create actual DB connection
        return new MockConnection();
    }
}
```

---

## 4. Message Queues

### What it is
Asynchronous communication system for decoupling services.

### Why it matters
- Service decoupling
- Load balancing
- Fault tolerance
- Scalability

### Simple Message Queue Implementation
```java
import java.util.concurrent.*;
import java.util.function.*;

public class MessageQueue<T> {
    private final BlockingQueue<Message<T>> queue;
    private final List<Consumer<Message<T>>> consumers;
    private final ExecutorService executor;
    
    public MessageQueue(int capacity) {
        this.queue = new LinkedBlockingQueue<>(capacity);
        this.consumers = new ArrayList<>();
        this.executor = Executors.newCachedThreadPool();
    }
    
    public void publish(T message) {
        queue.offer(new Message<>(message));
    }
    
    public void subscribe(Consumer<Message<T>> consumer) {
        consumers.add(consumer);
        startConsumer(consumer);
    }
    
    private void startConsumer(Consumer<Message<T>> consumer) {
        executor.submit(() -> {
            while (true) {
                try {
                    Message<T> message = queue.take();
                    consumer.accept(message);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }
    
    public static class Message<T> {
        private final T payload;
        private final long timestamp;
        
        public Message(T payload) {
            this.payload = payload;
            this.timestamp = System.currentTimeMillis();
        }
        
        public T getPayload() { return payload; }
        public long getTimestamp() { return timestamp; }
    }
}
```

---

## 5. Rate Limiting

### What it is
Controlling the rate of requests to prevent abuse and ensure fair usage.

### Why it matters
- Prevent abuse
- Ensure stability
- Fair resource allocation
- Cost control

### Token Bucket Algorithm
```java
public class TokenBucketRateLimiter {
    private final int capacity;
    private final double refillRate;
    private double tokens;
    private long lastRefillTimestamp;
    
    public TokenBucketRateLimiter(int capacity, double refillRate) {
        this.capacity = capacity;
        this.refillRate = refillRate;
        this.tokens = capacity;
        this.lastRefillTimestamp = System.currentTimeMillis();
    }
    
    public synchronized boolean allowRequest() {
        refillTokens();
        
        if (tokens >= 1) {
            tokens--;
            return true;
        }
        return false;
    }
    
    private void refillTokens() {
        long now = System.currentTimeMillis();
        double timePassed = (now - lastRefillTimestamp) / 1000.0;
        double tokensToAdd = timePassed * refillRate;
        
        tokens = Math.min(capacity, tokens + tokensToAdd);
        lastRefillTimestamp = now;
    }
}
```

### Sliding Window Algorithm
```java
public class SlidingWindowRateLimiter {
    private final int windowSize;
    private final int maxRequests;
    private final Queue<Long> requestTimestamps;
    
    public SlidingWindowRateLimiter(int windowSizeSeconds, int maxRequests) {
        this.windowSize = windowSizeSeconds * 1000; // Convert to milliseconds
        this.maxRequests = maxRequests;
        this.requestTimestamps = new LinkedList<>();
    }
    
    public synchronized boolean allowRequest() {
        long now = System.currentTimeMillis();
        
        // Remove old requests outside the window
        while (!requestTimestamps.isEmpty() && 
               now - requestTimestamps.peek() > windowSize) {
            requestTimestamps.poll();
        }
        
        if (requestTimestamps.size() < maxRequests) {
            requestTimestamps.offer(now);
            return true;
        }
        
        return false;
    }
}
```

---

## 6. Design Patterns

### Singleton Pattern
```java
public class DatabaseConnectionPool {
    private static volatile DatabaseConnectionPool instance;
    private final Map<String, Connection> connections;
    
    private DatabaseConnectionPool() {
        this.connections = new ConcurrentHashMap<>();
    }
    
    public static DatabaseConnectionPool getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnectionPool.class) {
                if (instance == null) {
                    instance = new DatabaseConnectionPool();
                }
            }
        }
        return instance;
    }
    
    public Connection getConnection(String databaseUrl) {
        return connections.computeIfAbsent(databaseUrl, this::createConnection);
    }
    
    private Connection createConnection(String url) {
        // Create actual database connection
        return new MockConnection();
    }
}
```

### Factory Pattern
```java
public interface Cache {
    void put(String key, Object value);
    Object get(String key);
    void remove(String key);
}

public class CacheFactory {
    public static Cache createCache(CacheType type) {
        switch (type) {
            case IN_MEMORY:
                return new InMemoryCache();
            case REDIS:
                return new RedisCache();
            case MEMCACHED:
                return new MemcachedCache();
            default:
                throw new IllegalArgumentException("Unknown cache type: " + type);
        }
    }
}

enum CacheType {
    IN_MEMORY, REDIS, MEMCACHED
}
```

### Observer Pattern
```java
public class EventManager {
    private final Map<String, List<Consumer<Event>>> listeners;
    
    public EventManager() {
        this.listeners = new ConcurrentHashMap<>();
    }
    
    public void subscribe(String eventType, Consumer<Event> listener) {
        listeners.computeIfAbsent(eventType, k -> new ArrayList<>()).add(listener);
    }
    
    public void unsubscribe(String eventType, Consumer<Event> listener) {
        List<Consumer<Event>> eventListeners = listeners.get(eventType);
        if (eventListeners != null) {
            eventListeners.remove(listener);
        }
    }
    
    public void publish(Event event) {
        List<Consumer<Event>> eventListeners = listeners.get(event.getType());
        if (eventListeners != null) {
            eventListeners.forEach(listener -> listener.accept(event));
        }
    }
    
    public static class Event {
        private final String type;
        private final Object data;
        
        public Event(String type, Object data) {
            this.type = type;
            this.data = data;
        }
        
        public String getType() { return type; }
        public Object getData() { return data; }
    }
}
```

---

## 7. Common System Design Questions

### Design a URL Shortener
```java
public class URLShortener {
    private final Map<String, String> shortToLong;
    private final Map<String, String> longToShort;
    private final AtomicInteger counter;
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    
    public URLShortener() {
        this.shortToLong = new ConcurrentHashMap<>();
        this.longToShort = new ConcurrentHashMap<>();
        this.counter = new AtomicInteger(1);
    }
    
    public String shortenURL(String longURL) {
        // Check if already shortened
        String existing = longToShort.get(longURL);
        if (existing != null) {
            return existing;
        }
        
        // Generate new short URL
        int id = counter.getAndIncrement();
        String shortCode = encode(id);
        String shortURL = "https://short.ly/" + shortCode;
        
        // Store mappings
        shortToLong.put(shortCode, longURL);
        longToShort.put(longURL, shortURL);
        
        return shortURL;
    }
    
    public String expandURL(String shortURL) {
        String shortCode = shortURL.substring(shortURL.lastIndexOf('/') + 1);
        return shortToLong.get(shortCode);
    }
    
    private String encode(int num) {
        StringBuilder sb = new StringBuilder();
        while (num > 0) {
            sb.append(ALPHABET.charAt(num % ALPHABET.length()));
            num /= ALPHABET.length();
        }
        return sb.reverse().toString();
    }
}
```

### Design a Simple Social Media Feed
```java
public class SocialMediaFeed {
    private final Map<String, User> users;
    private final TimelineManager timelineManager;
    
    public SocialMediaFeed() {
        this.users = new ConcurrentHashMap<>();
        this.timelineManager = new TimelineManager();
    }
    
    public void post(String userId, String content) {
        Post post = new Post(userId, content, System.currentTimeMillis());
        User user = users.get(userId);
        if (user != null) {
            user.addPost(post);
            timelineManager.addToFollowersTimelines(user, post);
        }
    }
    
    public List<Post> getFeed(String userId, int limit) {
        User user = users.get(userId);
        if (user == null) return Collections.emptyList();
        
        return timelineManager.getTimeline(userId, limit);
    }
    
    public void follow(String followerId, String followeeId) {
        User follower = users.get(followerId);
        User followee = users.get(followeeId);
        
        if (follower != null && followee != null) {
            follower.follow(followeeId);
            timelineManager.addExistingPostsToTimeline(followerId, followee.getPosts());
        }
    }
    
    private static class User {
        private final String id;
        private final List<Post> posts;
        private final Set<String> following;
        private final Set<String> followers;
        
        public User(String id) {
            this.id = id;
            this.posts = new ArrayList<>();
            this.following = new HashSet<>();
            this.followers = new HashSet<>();
        }
        
        public void addPost(Post post) {
            posts.add(post);
        }
        
        public void follow(String userId) {
            following.add(userId);
        }
        
        public List<Post> getPosts() {
            return new ArrayList<>(posts);
        }
    }
    
    private static class Post {
        private final String userId;
        private final String content;
        private final long timestamp;
        
        public Post(String userId, String content, long timestamp) {
            this.userId = userId;
            this.content = content;
            this.timestamp = timestamp;
        }
    }
}
```

---

## Practice Questions

### Easy
1. **Design a HashMap** (Hash table implementation)
2. **Design a Stack** (LIFO data structure)
3. **Design a URL Shortener** (Hash encoding)

### Medium
1. **Design Twitter Feed** (Timeline management)
2. **Design a Web Crawler** (Graph traversal)
3. **Design a Cache System** (Caching strategies)

### Hard
1. **Design a Distributed System** (Consensus algorithms)
2. **Design a Database** (Storage and indexing)
3. **Design a CDN** (Content delivery network)

---

**Remember:** System design is about trade-offs - there's no perfect solution!
