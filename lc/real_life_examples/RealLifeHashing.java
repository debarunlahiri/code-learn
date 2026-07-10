import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

/**
 * RealLifeHashing.java
 *
 * A single, self-contained Java file that demonstrates hashing algorithms and
 * hash-based data structures through real-world scenarios. It includes a
 * hand-rolled hash table, Java's built-in HashMap/HashSet usage, and practical
 * security and systems examples.
 *
 * Real-life examples covered:
 *   1. Hand-rolled hash table with separate chaining.
 *   2. Password storage with salt + SHA-256 hashing.
 *   3. URL shortener (bijective hash-like service).
 *   4. In-memory cache / memoization using HashMap.
 *   5. Voting / frequency counter using HashMap.
 *   6. File integrity verification using a simple checksum.
 *   7. Two Sum using a HashMap (classic LeetCode pattern).
 */
public final class RealLifeHashing {

    private RealLifeHashing() {
        // utility class
    }

    public static void main(String[] args) throws Exception {
        System.out.println("===== 1. Hand-Rolled Hash Table (Separate Chaining) =====");
        runCustomHashTableDemo();

        System.out.println("\n===== 2. Password Storage with Salt + SHA-256 =====");
        runPasswordStorageDemo();

        System.out.println("\n===== 3. URL Shortener Service =====");
        runUrlShortenerDemo();

        System.out.println("\n===== 4. In-Memory Cache / Memoization (HashMap) =====");
        runCacheDemo();

        System.out.println("\n===== 5. Voting / Frequency Counter (HashMap) =====");
        runVotingDemo();

        System.out.println("\n===== 6. File Integrity Checksum =====");
        runChecksumDemo();

        System.out.println("\n===== 7. Two Sum using HashMap =====");
        runTwoSumDemo();
    }

    /* =============================================================
       1. HAND-ROLLED HASH TABLE (SEPARATE CHAINING)
       ============================================================= */

    private static void runCustomHashTableDemo() {
        SimpleHashTable<String, Integer> scores = new SimpleHashTable<>();
        scores.put("Alice", 95);
        scores.put("Bob", 82);
        scores.put("Carol", 78);
        scores.put("David", 91);
        scores.put("Eve", 88);

        System.out.println("Alice's score: " + scores.get("Alice"));
        System.out.println("Bob's score: " + scores.get("Bob"));
        System.out.println("Contains 'Frank'? " + scores.containsKey("Frank"));

        scores.put("Alice", 100); // update existing key
        System.out.println("Alice's updated score: " + scores.get("Alice"));

        scores.remove("Bob");
        System.out.println("After removing Bob, contains 'Bob'? " + scores.containsKey("Bob"));
        System.out.println("Current size: " + scores.size());
    }

    /**
     * Minimal hash table implementation using separate chaining. This is the
     * same collision-resolution strategy used by Java's HashMap (before Java 8
     * it was purely linked lists; after Java 8 it converts long buckets to
     * balanced trees).
     */
    private static final class SimpleHashTable<K, V> {
        private static final int INITIAL_CAPACITY = 8;
        private static final double LOAD_FACTOR = 0.75;

        private Node<K, V>[] buckets;
        private int size;

        @SuppressWarnings("unchecked")
        SimpleHashTable() {
            this.buckets = new Node[INITIAL_CAPACITY];
            this.size = 0;
        }

        void put(K key, V value) {
            int index = hashIndex(key);
            Node<K, V> head = buckets[index];

            // Update existing key if found.
            Node<K, V> current = head;
            while (current != null) {
                if (Objects.equals(current.key, key)) {
                    current.value = value;
                    return;
                }
                current = current.next;
            }

            // Insert new node at the head of the chain.
            Node<K, V> newNode = new Node<>(key, value, head);
            buckets[index] = newNode;
            size++;

            if ((double) size / buckets.length > LOAD_FACTOR) {
                resize();
            }
        }

        V get(K key) {
            int index = hashIndex(key);
            Node<K, V> current = buckets[index];
            while (current != null) {
                if (Objects.equals(current.key, key)) {
                    return current.value;
                }
                current = current.next;
            }
            return null;
        }

        boolean containsKey(K key) {
            return get(key) != null;
        }

        boolean remove(K key) {
            int index = hashIndex(key);
            Node<K, V> current = buckets[index];
            Node<K, V> previous = null;

            while (current != null) {
                if (Objects.equals(current.key, key)) {
                    if (previous == null) {
                        buckets[index] = current.next;
                    } else {
                        previous.next = current.next;
                    }
                    size--;
                    return true;
                }
                previous = current;
                current = current.next;
            }
            return false;
        }

        int size() {
            return size;
        }

        private int hashIndex(K key) {
            return (key == null ? 0 : Math.abs(key.hashCode())) % buckets.length;
        }

        @SuppressWarnings("unchecked")
        private void resize() {
            Node<K, V>[] oldBuckets = buckets;
            buckets = new Node[oldBuckets.length * 2];
            size = 0;

            for (Node<K, V> head : oldBuckets) {
                Node<K, V> current = head;
                while (current != null) {
                    put(current.key, current.value);
                    current = current.next;
                }
            }
        }

        private static final class Node<K, V> {
            final K key;
            V value;
            Node<K, V> next;

            Node(K key, V value, Node<K, V> next) {
                this.key = key;
                this.value = value;
                this.next = next;
            }
        }
    }

    /* =============================================================
       2. PASSWORD STORAGE WITH SALT + SHA-256
       ============================================================= */

    private static void runPasswordStorageDemo() throws NoSuchAlgorithmException {
        PasswordHasher hasher = new PasswordHasher();

        String password = "mySecret123!";
        String[] stored = hasher.hash(password); // [salt, hash]
        String salt = stored[0];
        String hash = stored[1];

        System.out.println("Original password: " + password);
        System.out.println("Salt (hex): " + salt);
        System.out.println("Hash (hex): " + hash);

        boolean matches = hasher.verify(password, salt, hash);
        boolean wrongMatches = hasher.verify("wrongPassword", salt, hash);
        System.out.println("Correct password verifies? " + matches);
        System.out.println("Wrong password verifies? " + wrongMatches);
    }

    /**
     * Demonstrates how real systems avoid storing plaintext passwords. A random
     * salt is generated for each user so that identical passwords produce
     * different hashes, and rainbow-table attacks become impractical.
     */
    private static final class PasswordHasher {
        private final MessageDigest digest;
        private final SecureRandom random;

        PasswordHasher() throws NoSuchAlgorithmException {
            this.digest = MessageDigest.getInstance("SHA-256");
            this.random = new SecureRandom();
        }

        String[] hash(String password) {
            byte[] salt = new byte[16];
            random.nextBytes(salt);
            byte[] hash = hashWithSalt(password, salt);
            return new String[] { toHex(salt), toHex(hash) };
        }

        boolean verify(String password, String saltHex, String hashHex) {
            byte[] salt = fromHex(saltHex);
            byte[] expectedHash = hashWithSalt(password, salt);
            return hashHex.equalsIgnoreCase(toHex(expectedHash));
        }

        private byte[] hashWithSalt(String password, byte[] salt) {
            digest.reset();
            digest.update(salt);
            return digest.digest(password.getBytes(StandardCharsets.UTF_8));
        }

        private String toHex(byte[] bytes) {
            StringBuilder sb = new StringBuilder(bytes.length * 2);
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        }

        private byte[] fromHex(String hex) {
            byte[] bytes = new byte[hex.length() / 2];
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = (byte) Integer.parseInt(hex.substring(i * 2, i * 2 + 2), 16);
            }
            return bytes;
        }
    }

    /* =============================================================
       3. URL SHORTENER SERVICE
       ============================================================= */

    private static void runUrlShortenerDemo() {
        UrlShortener shortener = new UrlShortener();

        String longUrl1 = "https://www.example.com/articles/how-to-learn-java-in-30-days";
        String longUrl2 = "https://www.example.com/products/laptop?ref=homepage&campaign=spring";

        String short1 = shortener.shorten(longUrl1);
        String short2 = shortener.shorten(longUrl2);

        System.out.println("Long URL 1: " + longUrl1);
        System.out.println("Short URL 1: " + short1);
        System.out.println("Expanded:   " + shortener.expand(short1));

        System.out.println("Long URL 2: " + longUrl2);
        System.out.println("Short URL 2: " + short2);
        System.out.println("Expanded:   " + shortener.expand(short2));
    }

    /**
     * Tiny URL shortener. In production, services like bit.ly use distributed
     * key-generation and base-62 encoding, but the core lookup layer is always a
     * hash map from short key -> long URL.
     */
    private static final class UrlShortener {
        private static final String BASE_URL = "https://short.ly/";
        private final Map<String, String> shortToLong = new HashMap<>();
        private final Map<String, String> longToShort = new HashMap<>();
        private int counter = 1;

        String shorten(String longUrl) {
            if (longToShort.containsKey(longUrl)) {
                return longToShort.get(longUrl);
            }
            String key = encode(counter++);
            String shortUrl = BASE_URL + key;
            shortToLong.put(shortUrl, longUrl);
            longToShort.put(longUrl, shortUrl);
            return shortUrl;
        }

        String expand(String shortUrl) {
            return shortToLong.getOrDefault(shortUrl, "URL not found");
        }

        private String encode(int number) {
            String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
            StringBuilder sb = new StringBuilder();
            while (number > 0) {
                sb.append(chars.charAt(number % chars.length()));
                number /= chars.length();
            }
            return sb.reverse().toString();
        }
    }

    /* =============================================================
       4. IN-MEMORY CACHE / MEMOIZATION (HASHMAP)
       ============================================================= */

    private static void runCacheDemo() {
        FibonacciCache cache = new FibonacciCache();

        System.out.println("Fibonacci(10) = " + cache.fibonacci(10));
        System.out.println("Fibonacci(20) = " + cache.fibonacci(20));
        System.out.println("Fibonacci(30) = " + cache.fibonacci(30));
        System.out.println("Total cache hits during demo: " + cache.getHits());
    }

    /**
     * Classic memoization example. Without caching, fibonacci(n) has
     * exponential time complexity O(2^n). With a HashMap it becomes O(n) and
     * the same pattern powers HTTP caches, database query caches and CPU
     * memoization in compilers.
     */
    private static final class FibonacciCache {
        private final Map<Integer, Long> cache = new HashMap<>();
        private long hits = 0;

        long fibonacci(int n) {
            if (n <= 1) {
                return n;
            }
            if (cache.containsKey(n)) {
                hits++;
                return cache.get(n);
            }
            long result = fibonacci(n - 1) + fibonacci(n - 2);
            cache.put(n, result);
            return result;
        }

        long getHits() {
            return hits;
        }
    }

    /* =============================================================
       5. VOTING / FREQUENCY COUNTER (HASHMAP)
       ============================================================= */

    private static void runVotingDemo() {
        String[] votes = {
                "Alice", "Bob", "Alice", "Carol", "Bob", "Alice",
                "Carol", "Alice", "Bob", "Carol", "Carol", "Alice"
        };

        Election election = new Election();
        for (String candidate : votes) {
            election.vote(candidate);
        }

        System.out.println("Vote counts:");
        election.printResults();
        System.out.println("Winner: " + election.getWinner());
    }

    /**
     * A simple vote tallying system. HashMap gives O(1) increments and lookups,
     * which is why counting problems are one of the most common interview
     * patterns.
     */
    private static final class Election {
        private final Map<String, Integer> voteCount = new HashMap<>();

        void vote(String candidate) {
            voteCount.merge(candidate, 1, Integer::sum);
        }

        int getVotes(String candidate) {
            return voteCount.getOrDefault(candidate, 0);
        }

        String getWinner() {
            String winner = null;
            int maxVotes = -1;
            for (Map.Entry<String, Integer> entry : voteCount.entrySet()) {
                if (entry.getValue() > maxVotes) {
                    maxVotes = entry.getValue();
                    winner = entry.getKey();
                }
            }
            return winner + " with " + maxVotes + " votes";
        }

        void printResults() {
            for (Map.Entry<String, Integer> entry : voteCount.entrySet()) {
                System.out.println("  " + entry.getKey() + ": " + entry.getValue());
            }
        }
    }

    /* =============================================================
       6. FILE INTEGRITY CHECKSUM
       ============================================================= */

    private static void runChecksumDemo() throws NoSuchAlgorithmException {
        String document = "The quick brown fox jumps over the lazy dog.";
        String corruptedDocument = "The quick brown fox jumps over the lazy dog!";

        Checksum checksum = new Checksum();
        String hash1 = checksum.compute(document);
        String hash2 = checksum.compute(document);
        String hash3 = checksum.compute(corruptedDocument);

        System.out.println("Original document hash: " + hash1);
        System.out.println("Same document hash:     " + hash2);
        System.out.println("Corrupted document hash:" + hash3);
        System.out.println("Integrity check (original vs same): " + hash1.equals(hash2));
        System.out.println("Integrity check (original vs corrupted): " + hash1.equals(hash3));
    }

    /**
     * A tiny checksum service. Version-control systems, package managers and
     * file-transfer tools use stronger cryptographic hashes (SHA-256, BLAKE3)
     * to verify that a file has not been tampered with or corrupted.
     */
    private static final class Checksum {
        private final MessageDigest digest;

        Checksum() throws NoSuchAlgorithmException {
            this.digest = MessageDigest.getInstance("SHA-256");
        }

        String compute(String content) {
            byte[] hash = digest.digest(content.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        }
    }

    /* =============================================================
       7. TWO SUM USING HASHMAP
       ============================================================= */

    private static void runTwoSumDemo() {
        int[] numbers = { 2, 7, 11, 15 };
        int target = 9;

        TwoSum solver = new TwoSum();
        int[] result = solver.findPair(numbers, target);

        System.out.println("Array: " + Arrays.toString(numbers));
        System.out.println("Target: " + target);
        if (result.length == 2) {
            System.out.println("Indices: [" + result[0] + ", " + result[1] + "]");
            System.out.println("Values:  [" + numbers[result[0]] + ", " + numbers[result[1]] + "]");
        } else {
            System.out.println("No pair found.");
        }
    }

    /**
     * The canonical HashMap interview problem. By storing each value's index
     * as we iterate, we reduce the search from O(n^2) to O(n) time with O(n)
     * extra space.
     */
    private static final class TwoSum {
        int[] findPair(int[] nums, int target) {
            Map<Integer, Integer> indexMap = new HashMap<>();
            for (int i = 0; i < nums.length; i++) {
                int complement = target - nums[i];
                if (indexMap.containsKey(complement)) {
                    return new int[] { indexMap.get(complement), i };
                }
                indexMap.put(nums[i], i);
            }
            return new int[0];
        }
    }
}
