import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

/**
 * SocialNetwork.java
 *
 * A single, self-contained Java file that models a social network platform and
 * demonstrates how classic computer-science algorithms apply to real-life
 * social-media features. Each algorithm is implemented from scratch or using
 * Java collections, with comments explaining the real-world use case.
 *
 * Algorithms covered (all applied to a social network):
 *   1. Sorting algorithm      - Sort users by popularity (friend count).
 *   2. Searching algorithm    - Binary search for a user by username.
 *   3. Shortest path algorithm- Degrees of separation between two users (BFS).
 *   4. Recommendation algorithm- Friend recommendations via mutual friends.
 *   5. Hashing algorithm      - Secure password storage and fast user lookup.
 *   6. Queue algorithm        - Friend-request processing queue (FIFO).
 *   7. Stack algorithm        - Undo recent actions and DFS post traversal.
 *   8. Graph algorithm        - Connected communities / friend circles.
 *   9. Dynamic programming    - Maximum engagement timeline scheduling.
 *  10. Pattern matching       - Hashtag search in posts (KMP algorithm).
 */
public final class SocialNetwork {

    private final Map<String, User> usersByUsername = new HashMap<>();
    private final Map<String, Set<String>> friendships = new HashMap<>();

    public static void main(String[] args) throws NoSuchAlgorithmException {
        SocialNetwork network = new SocialNetwork();
        seedData(network);

        System.out.println("===== 1. Sorting Algorithm: Sort Users by Popularity =====");
        network.demoSorting();

        System.out.println("\n===== 2. Searching Algorithm: Binary Search User by Username =====");
        network.demoSearching();

        System.out.println("\n===== 3. Shortest Path Algorithm: Degrees of Separation =====");
        network.demoShortestPath();

        System.out.println("\n===== 4. Recommendation Algorithm: Friend Suggestions =====");
        network.demoRecommendations();

        System.out.println("\n===== 5. Hashing Algorithm: Password Storage =====");
        network.demoHashing();

        System.out.println("\n===== 6. Queue Algorithm: Friend Request Processing =====");
        network.demoQueue();

        System.out.println("\n===== 7. Stack Algorithm: Undo Actions & DFS Post Traversal =====");
        network.demoStack();

        System.out.println("\n===== 8. Graph Algorithm: Find Friend Communities =====");
        network.demoGraphAlgorithm();

        System.out.println("\n===== 9. Dynamic Programming: Best Engagement Schedule =====");
        network.demoDynamicProgramming();

        System.out.println("\n===== 10. Pattern Matching: Hashtag Search in Posts =====");
        network.demoPatternMatching();
    }

    /* =============================================================
       DATA MODEL
       ============================================================= */

    private static final class User {
        final String username;
        final String email;
        String salt;
        String passwordHash;
        final List<String> posts = new ArrayList<>();
        int engagementScore;

        User(String username, String email) {
            this.username = username;
            this.email = email;
        }
    }

    private User addUser(String username, String email) {
        User user = new User(username, email);
        usersByUsername.put(username, user);
        friendships.put(username, new HashSet<>());
        return user;
    }

    private void addFriendship(String a, String b) {
        friendships.computeIfAbsent(a, k -> new HashSet<>()).add(b);
        friendships.computeIfAbsent(b, k -> new HashSet<>()).add(a);
    }

    private static void seedData(SocialNetwork network) throws NoSuchAlgorithmException {
        // Create users
        User alice = network.addUser("alice", "alice@example.com");
        User bob = network.addUser("bob", "bob@example.com");
        User carol = network.addUser("carol", "carol@example.com");
        User david = network.addUser("david", "david@example.com");
        User eve = network.addUser("eve", "eve@example.com");
        User frank = network.addUser("frank", "frank@example.com");
        User grace = network.addUser("grace", "grace@example.com");
        User henry = network.addUser("henry", "henry@example.com");

        // Set passwords using hashing
        PasswordHasher hasher = new PasswordHasher();
        for (User user : network.usersByUsername.values()) {
            String[] hashed = hasher.hash(user.username + "Pass123!");
            user.salt = hashed[0];
            user.passwordHash = hashed[1];
        }

        // Create friendships (graph edges)
        network.addFriendship("alice", "bob");
        network.addFriendship("alice", "carol");
        network.addFriendship("alice", "david");
        network.addFriendship("bob", "eve");
        network.addFriendship("carol", "eve");
        network.addFriendship("carol", "frank");
        network.addFriendship("david", "frank");
        network.addFriendship("eve", "grace");
        network.addFriendship("frank", "grace");

        // Henry is isolated (used to show disconnected community)

        // Add posts with hashtags for pattern matching demo
        alice.posts.add("Loving the new #Java course! #coding");
        alice.posts.add("Weekend hike with #friends #nature");
        bob.posts.add("Just solved a hard #leetcode problem #coding");
        carol.posts.add("#travel diaries: Paris was amazing! #friends");
        david.posts.add("Working on #springboot project #coding");
        eve.posts.add("Healthy breakfast ideas #food #health");

        // Engagement scores for DP scheduling demo
        alice.engagementScore = 50;
        bob.engagementScore = 30;
        carol.engagementScore = 70;
        david.engagementScore = 20;
        eve.engagementScore = 60;
        frank.engagementScore = 40;
        grace.engagementScore = 35;
    }

    /* =============================================================
       1. SORTING ALGORITHM: SORT USERS BY POPULARITY
       ============================================================= */

    private void demoSorting() {
        List<User> users = new ArrayList<>(usersByUsername.values());

        System.out.println("Before sorting (by friend count):");
        for (User user : users) {
            System.out.println("  " + user.username + ": " + friendships.get(user.username).size() + " friends");
        }

        // Merge sort by friend count descending.
        List<User> sorted = mergeSortByPopularity(users);

        System.out.println("\nAfter sorting (most popular first):");
        for (User user : sorted) {
            System.out.println("  " + user.username + ": " + friendships.get(user.username).size() + " friends");
        }
    }

    /**
     * Merge sort is used here to rank users by popularity. Social platforms
     * sort feeds, followers lists and trending topics using similar
     * divide-and-conquer sorting strategies.
     */
    private List<User> mergeSortByPopularity(List<User> list) {
        if (list.size() <= 1) {
            return list;
        }

        int mid = list.size() / 2;
        List<User> left = mergeSortByPopularity(list.subList(0, mid));
        List<User> right = mergeSortByPopularity(list.subList(mid, list.size()));
        return merge(left, right);
    }

    private List<User> merge(List<User> left, List<User> right) {
        List<User> result = new ArrayList<>();
        int i = 0, j = 0;
        while (i < left.size() && j < right.size()) {
            int leftCount = friendships.get(left.get(i).username).size();
            int rightCount = friendships.get(right.get(j).username).size();
            if (leftCount >= rightCount) {
                result.add(left.get(i++));
            } else {
                result.add(right.get(j++));
            }
        }
        while (i < left.size()) {
            result.add(left.get(i++));
        }
        while (j < right.size()) {
            result.add(right.get(j++));
        }
        return result;
    }

    /* =============================================================
       2. SEARCHING ALGORITHM: BINARY SEARCH USER BY USERNAME
       ============================================================= */

    private void demoSearching() {
        List<User> sortedUsers = new ArrayList<>(usersByUsername.values());
        sortedUsers.sort(Comparator.comparing(u -> u.username));

        String[] targets = {"carol", "henry", "zoe"};
        for (String target : targets) {
            int index = binarySearch(sortedUsers, target);
            if (index >= 0) {
                System.out.println("Found user '" + target + "' at index " + index);
            } else {
                System.out.println("User '" + target + "' not found (insertion point: " + (-index - 1) + ")");
            }
        }
    }

    /**
     * Binary search locates a username in O(log n) time. Search bars in social
     * apps use indexed binary/B-tree searches over sorted user lists.
     */
    private int binarySearch(List<User> users, String target) {
        int left = 0;
        int right = users.size() - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            int comparison = users.get(mid).username.compareTo(target);
            if (comparison == 0) {
                return mid;
            } else if (comparison < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -(left + 1); // insertion point
    }

    /* =============================================================
       3. SHORTEST PATH ALGORITHM: DEGREES OF SEPARATION
       ============================================================= */

    private void demoShortestPath() {
        String[][] pairs = {
                {"alice", "grace"},
                {"alice", "henry"},
                {"bob", "frank"},
                {"david", "grace"}
        };

        for (String[] pair : pairs) {
            List<String> path = bfsShortestPath(pair[0], pair[1]);
            if (path.isEmpty()) {
                System.out.println(pair[0] + " and " + pair[1] + " are not connected.");
            } else {
                int degrees = path.size() - 1;
                System.out.println(pair[0] + " -> " + pair[1] +
                        " has " + degrees + " degree(s) of separation: " +
                        String.join(" -> ", path));
            }
        }
    }

    /**
     * BFS finds the shortest path in an unweighted friendship graph. This is
     * exactly how LinkedIn shows "2nd degree" or "3rd degree" connections.
     */
    private List<String> bfsShortestPath(String source, String target) {
        if (!friendships.containsKey(source) || !friendships.containsKey(target)) {
            return Collections.emptyList();
        }

        ArrayDeque<String> queue = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();
        Map<String, String> parent = new HashMap<>();

        queue.offer(source);
        visited.add(source);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            if (current.equals(target)) {
                return reconstructPath(parent, source, target);
            }

            for (String friend : friendships.get(current)) {
                if (!visited.contains(friend)) {
                    visited.add(friend);
                    parent.put(friend, current);
                    queue.offer(friend);
                }
            }
        }
        return Collections.emptyList();
    }

    private List<String> reconstructPath(Map<String, String> parent, String source, String target) {
        LinkedList<String> path = new LinkedList<>();
        String current = target;
        while (current != null) {
            path.addFirst(current);
            if (current.equals(source)) {
                break;
            }
            current = parent.get(current);
        }
        return path;
    }

    /* =============================================================
       4. RECOMMENDATION ALGORITHM: FRIEND SUGGESTIONS
       ============================================================= */

    private void demoRecommendations() {
        String user = "alice";
        System.out.println("Friend recommendations for " + user + ":");

        Map<String, Integer> mutualCount = new HashMap<>();
        Set<String> directFriends = friendships.get(user);

        for (String friend : directFriends) {
            for (String friendOfFriend : friendships.get(friend)) {
                if (!friendOfFriend.equals(user) && !directFriends.contains(friendOfFriend)) {
                    mutualCount.merge(friendOfFriend, 1, Integer::sum);
                }
            }
        }

        // Sort recommendations by number of mutual friends descending.
        List<Map.Entry<String, Integer>> recommendations = new ArrayList<>(mutualCount.entrySet());
        recommendations.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        for (Map.Entry<String, Integer> entry : recommendations) {
            System.out.println("  " + entry.getKey() + " (" + entry.getValue() + " mutual friends)");
        }
    }

    /* =============================================================
       5. HASHING ALGORITHM: PASSWORD STORAGE
       ============================================================= */

    private void demoHashing() throws NoSuchAlgorithmException {
        PasswordHasher hasher = new PasswordHasher();
        User user = usersByUsername.get("alice");
        String password = "alicePass123!";

        System.out.println("User: " + user.username);
        System.out.println("Stored salt:  " + user.salt);
        System.out.println("Stored hash:  " + user.passwordHash);
        System.out.println("Login with correct password? " + hasher.verify(password, user.salt, user.passwordHash));
        System.out.println("Login with wrong password?   " + hasher.verify("wrong", user.salt, user.passwordHash));

        // Demonstrate hash-based fast lookup.
        System.out.println("\nFast lookup by username hash bucket: " +
                (usersByUsername.get("alice") != null ? "found alice" : "not found"));
    }

    /**
     * Hashes passwords with a random salt so that the database never stores
     * plaintext credentials. The same salt+hash pattern protects millions of
     * social-media accounts.
     */
    private static final class PasswordHasher {
        private final MessageDigest digest;
        private final SecureRandom random = new SecureRandom();

        PasswordHasher() throws NoSuchAlgorithmException {
            this.digest = MessageDigest.getInstance("SHA-256");
        }

        String[] hash(String password) {
            byte[] salt = new byte[16];
            random.nextBytes(salt);
            byte[] hash = hashWithSalt(password, salt);
            return new String[] { toHex(salt), toHex(hash) };
        }

        boolean verify(String password, String saltHex, String hashHex) {
            byte[] salt = fromHex(saltHex);
            byte[] expected = hashWithSalt(password, salt);
            return hashHex.equalsIgnoreCase(toHex(expected));
        }

        private byte[] hashWithSalt(String password, byte[] salt) {
            digest.reset();
            digest.update(salt);
            return digest.digest(password.getBytes(StandardCharsets.UTF_8));
        }

        private String toHex(byte[] bytes) {
            StringBuilder sb = new StringBuilder();
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
       6. QUEUE ALGORITHM: FRIEND REQUEST PROCESSING
       ============================================================= */

    private void demoQueue() {
        FriendRequestQueue queue = new FriendRequestQueue();
        queue.sendRequest("alice", "henry");
        queue.sendRequest("bob", "henry");
        queue.sendRequest("carol", "henry");

        System.out.println("Pending friend requests for henry: " + queue.pendingCount());
        while (queue.hasPending()) {
            FriendRequest request = queue.processNext();
            System.out.println("  Processed: " + request.from + " wants to be friends with " + request.to);
        }
    }

    /**
     * A FIFO queue processes friend requests in the order they arrive, exactly
     * like message queues, notification pipelines and task schedulers in
     * production social platforms.
     */
    private static final class FriendRequestQueue {
        private final ArrayDeque<FriendRequest> requests = new ArrayDeque<>();

        void sendRequest(String from, String to) {
            requests.offer(new FriendRequest(from, to));
        }

        FriendRequest processNext() {
            return requests.poll();
        }

        boolean hasPending() {
            return !requests.isEmpty();
        }

        int pendingCount() {
            return requests.size();
        }
    }

    private static final class FriendRequest {
        final String from;
        final String to;

        FriendRequest(String from, String to) {
            this.from = from;
            this.to = to;
        }
    }

    /* =============================================================
       7. STACK ALGORITHM: UNDO ACTIONS & DFS POST TRAVERSAL
       ============================================================= */

    private void demoStack() {
        // Undo stack example
        ActionHistory history = new ActionHistory();
        history.performAction("alice posted a photo");
        history.performAction("alice liked bob's post");
        history.performAction("alice commented on carol's post");

        System.out.println("Recent actions:");
        history.printActions();

        System.out.println("\nUndo last action:");
        history.undo();
        history.printActions();

        // DFS post-order traversal to find "deep" friend circles.
        System.out.println("\nDFS traversal starting from alice:");
        Set<String> visited = new HashSet<>();
        List<String> traversal = new ArrayList<>();
        dfs("alice", visited, traversal);
        System.out.println("  " + String.join(" -> ", traversal));
    }

    /**
     * Undo/redo features, browser back buttons and expression evaluation all
     * rely on the LIFO property of stacks.
     */
    private static final class ActionHistory {
        private final ArrayDeque<String> stack = new ArrayDeque<>();

        void performAction(String action) {
            stack.push(action);
        }

        void undo() {
            if (!stack.isEmpty()) {
                System.out.println("  Undid: " + stack.pop());
            }
        }

        void printActions() {
            for (String action : stack) {
                System.out.println("  - " + action);
            }
        }
    }

    private void dfs(String user, Set<String> visited, List<String> result) {
        visited.add(user);
        for (String friend : friendships.getOrDefault(user, Collections.emptySet())) {
            if (!visited.contains(friend)) {
                dfs(friend, visited, result);
            }
        }
        result.add(user); // post-order
    }

    /* =============================================================
       8. GRAPH ALGORITHM: FIND FRIEND COMMUNITIES
       ============================================================= */

    private void demoGraphAlgorithm() {
        List<Set<String>> communities = findConnectedComponents();
        System.out.println("Detected " + communities.size() + " friend community/communities:");
        for (int i = 0; i < communities.size(); i++) {
            System.out.println("  Community " + (i + 1) + ": " + communities.get(i));
        }
    }

    /**
     * Connected-component detection identifies isolated social circles. This
     * is useful for community recommendations, targeted ads and fraud detection.
     */
    private List<Set<String>> findConnectedComponents() {
        List<Set<String>> components = new ArrayList<>();
        Set<String> visited = new HashSet<>();

        for (String user : friendships.keySet()) {
            if (!visited.contains(user)) {
                Set<String> component = new HashSet<>();
                bfsComponent(user, visited, component);
                components.add(component);
            }
        }
        return components;
    }

    private void bfsComponent(String start, Set<String> visited, Set<String> component) {
        ArrayDeque<String> queue = new ArrayDeque<>();
        queue.offer(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            component.add(current);
            for (String friend : friendships.get(current)) {
                if (!visited.contains(friend)) {
                    visited.add(friend);
                    queue.offer(friend);
                }
            }
        }
    }

    /* =============================================================
       9. DYNAMIC PROGRAMMING: BEST ENGAGEMENT SCHEDULE
       ============================================================= */

    private void demoDynamicProgramming() {
        // Suppose each user can host one live event per day, but no two events
        // can be on consecutive days. Pick non-consecutive users to maximize
        // total engagement.
        List<User> users = new ArrayList<>(usersByUsername.values());
        int[] scores = users.stream().mapToInt(u -> u.engagementScore).toArray();

        int maxEngagement = maxNonConsecutiveSum(scores);
        System.out.println("User engagement scores: " + Arrays.toString(scores));
        System.out.println("Maximum engagement without consecutive events: " + maxEngagement);
    }

    /**
     * Classic "house robber" DP pattern. Social platforms use DP for ad
     * scheduling, feed ranking, A/B test planning and resource allocation.
     */
    private int maxNonConsecutiveSum(int[] arr) {
        if (arr.length == 0) return 0;
        if (arr.length == 1) return arr[0];

        int prev2 = arr[0];
        int prev1 = Math.max(arr[0], arr[1]);

        for (int i = 2; i < arr.length; i++) {
            int current = Math.max(prev1, prev2 + arr[i]);
            prev2 = prev1;
            prev1 = current;
        }
        return prev1;
    }

    /* =============================================================
       10. PATTERN MATCHING: HASHTAG SEARCH IN POSTS
       ============================================================= */

    private void demoPatternMatching() {
        String hashtag = "#coding";
        System.out.println("Searching for hashtag '" + hashtag + "' in all posts:");

        for (User user : usersByUsername.values()) {
            for (String post : user.posts) {
                if (kmpSearch(post, hashtag) >= 0) {
                    System.out.println("  [" + user.username + "] " + post);
                }
            }
        }
    }

    /**
     * KMP (Knuth-Morris-Pratt) searches for a pattern in text in O(n + m)
     * time. Social platforms use pattern matching for hashtags, mentions,
     * content moderation and search highlighting.
     */
    private int kmpSearch(String text, String pattern) {
        if (pattern.isEmpty()) return 0;
        int[] lps = buildLps(pattern);
        int i = 0, j = 0;
        while (i < text.length()) {
            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
                if (j == pattern.length()) {
                    return i - j; // match found
                }
            } else if (j > 0) {
                j = lps[j - 1];
            } else {
                i++;
            }
        }
        return -1;
    }

    private int[] buildLps(String pattern) {
        int[] lps = new int[pattern.length()];
        int length = 0;
        int i = 1;
        while (i < pattern.length()) {
            if (pattern.charAt(i) == pattern.charAt(length)) {
                length++;
                lps[i] = length;
                i++;
            } else if (length > 0) {
                length = lps[length - 1];
            } else {
                lps[i] = 0;
                i++;
            }
        }
        return lps;
    }
}
