import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

/**
 * A detailed DSA case study for a smart-city control centre.
 *
 * <p>Instead of presenting algorithms as unrelated textbook exercises, this
 * file connects them to traffic, emergency response, energy consumption,
 * citizen search and resource scheduling. Each section states why the data
 * structure is appropriate and gives its time/space complexity.</p>
 *
 * <p>Notation used in the comments:</p>
 * <ul>
 *   <li>Big O: asymptotic upper bound (the work grows no faster than this).</li>
 *   <li>Big Omega: lower bound (the work grows at least this quickly).</li>
 *   <li>Big Theta: tight bound when upper and lower bounds match.</li>
 * </ul>
 */
public final class SmartCityDSA {

    private SmartCityDSA() {
        // utility class
    }

    public static void main(String[] args) {
        demonstrateArrayAnalytics();
        demonstrateSearchingAndSorting();
        demonstrateStringsAndTrie();
        demonstrateStackQueueAndHashing();
        demonstrateTreesAndHeap();
        demonstrateGraphRouting();
        demonstrateRecursionGreedyAndDynamicProgramming();
        demonstrateBitsMathematicsAndRangeQueries();
        demonstrateGeometryAndCache();
    }

    /* ================================================================
       1. ARRAYS AND COMMON PROBLEM-SOLVING PATTERNS
       ================================================================ */

    private static void demonstrateArrayAnalytics() {
        heading("1. Traffic sensor array analytics");
        scenario("Road sensors store the number of cars seen each hour. The control "
                + "room needs fast totals, peak-period detection and bulk lane updates.");
        int[] hourlyCars = {12, 18, 25, 20, 30, 42, 35, 28};

        // Prefix sum: O(n) preprocessing, then O(1) for every range query.
        long[] prefix = prefixSum(hourlyCars);
        System.out.println("Cars from hour 2 through 5: " + rangeSum(prefix, 2, 5));

        // Sliding window: O(n) rather than recalculating every k-hour sum O(n*k).
        System.out.println("Busiest 3-hour traffic: " + maxWindowSum(hourlyCars, 3));

        // Kadane: maximum contiguous increase/decrease period, Theta(n) time.
        int[] flowChanges = {-4, 3, 5, -2, 6, -8, 4};
        System.out.println("Largest continuous traffic gain: " + kadane(flowChanges));

        // Two pointers works because the input is sorted: O(n) time, O(1) space.
        int[] sortedLoads = {5, 8, 12, 17, 21, 29};
        System.out.println("Two sensors total 29: "
                + Arrays.toString(twoSumSorted(sortedLoads, 29)));

        // Difference array applies many range updates efficiently.
        int[] laneCapacity = applyRangeUpdates(6,
                new int[][]{{1, 3, 2}, {2, 5, 1}, {0, 1, 3}});
        System.out.println("Lane capacity after range updates: "
                + Arrays.toString(laneCapacity));
    }

    public static long[] prefixSum(int[] values) {
        long[] prefix = new long[values.length + 1];
        for (int index = 0; index < values.length; index++) {
            prefix[index + 1] = prefix[index] + values[index];
        }
        return prefix;
    }

    public static long rangeSum(long[] prefix, int left, int right) {
        if (left < 0 || right < left || right + 1 >= prefix.length) {
            throw new IllegalArgumentException("Invalid inclusive range.");
        }
        return prefix[right + 1] - prefix[left];
    }

    public static int maxWindowSum(int[] values, int windowSize) {
        if (windowSize <= 0 || windowSize > values.length) {
            throw new IllegalArgumentException("Invalid window size.");
        }
        int current = 0;
        for (int index = 0; index < windowSize; index++) {
            current += values[index];
        }
        int maximum = current;
        for (int right = windowSize; right < values.length; right++) {
            current += values[right] - values[right - windowSize];
            maximum = Math.max(maximum, current);
        }
        return maximum;
    }

    public static int kadane(int[] values) {
        if (values.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        }
        int endingHere = values[0];
        int best = values[0];
        for (int index = 1; index < values.length; index++) {
            endingHere = Math.max(values[index], endingHere + values[index]);
            best = Math.max(best, endingHere);
        }
        return best;
    }

    public static int[] twoSumSorted(int[] values, int target) {
        int left = 0;
        int right = values.length - 1;
        while (left < right) {
            int sum = values[left] + values[right];
            if (sum == target) {
                return new int[]{left, right};
            }
            if (sum < target) {
                left++;
            } else {
                right--;
            }
        }
        return new int[]{-1, -1};
    }

    public static int[] applyRangeUpdates(int size, int[][] updates) {
        int[] difference = new int[size + 1];
        for (int[] update : updates) {
            int left = update[0];
            int right = update[1];
            int change = update[2];
            difference[left] += change;
            if (right + 1 < size) {
                difference[right + 1] -= change;
            }
        }
        int[] result = new int[size];
        int running = 0;
        for (int index = 0; index < size; index++) {
            running += difference[index];
            result[index] = running;
        }
        return result;
    }

    /* ================================================================
       2. SEARCHING, SORTING AND INTERVALS
       ================================================================ */

    private static void demonstrateSearchingAndSorting() {
        heading("2. Searching, sorting and meeting intervals");
        scenario("Operators must locate an incident ID quickly, handle the most severe "
                + "incident first, and combine overlapping road-closure times.");
        int[] incidentIds = {101, 108, 115, 121, 135, 149};
        System.out.println("Incident 121 index (binary search): "
                + binarySearch(incidentIds, 121));

        List<Incident> incidents = new ArrayList<>(Arrays.asList(
                new Incident("Power failure", 4, 18),
                new Incident("Road accident", 5, 7),
                new Incident("Water leak", 2, 12)));
        incidents.sort(Comparator.comparingInt((Incident item) -> item.severity).reversed()
                .thenComparingInt(item -> item.reportedMinute));
        System.out.println("Incidents sorted by severity: " + incidents);

        int[][] closures = {{1, 4}, {3, 7}, {10, 12}, {11, 15}};
        System.out.println("Merged road-closure periods: "
                + intervalsToString(mergeIntervals(closures)));
    }

    // Every iteration halves the search space: O(log n) time, O(1) space.
    public static int binarySearch(int[] sorted, int target) {
        int left = 0;
        int right = sorted.length - 1;
        while (left <= right) {
            int middle = left + (right - left) / 2; // avoids integer overflow
            if (sorted[middle] == target) {
                return middle;
            }
            if (sorted[middle] < target) {
                left = middle + 1;
            } else {
                right = middle - 1;
            }
        }
        return -1;
    }

    // Sorting dominates: O(n log n) time. Result storage is O(n).
    public static List<int[]> mergeIntervals(int[][] intervals) {
        Arrays.sort(intervals, Comparator.comparingInt(interval -> interval[0]));
        List<int[]> merged = new ArrayList<>();
        for (int[] interval : intervals) {
            if (merged.isEmpty() || merged.get(merged.size() - 1)[1] < interval[0]) {
                merged.add(new int[]{interval[0], interval[1]});
            } else {
                int[] last = merged.get(merged.size() - 1);
                last[1] = Math.max(last[1], interval[1]);
            }
        }
        return merged;
    }

    /* ================================================================
       3. STRINGS, KMP MATCHING AND TRIE AUTOCOMPLETE
       ================================================================ */

    private static void demonstrateStringsAndTrie() {
        heading("3. Emergency-message search and location autocomplete");
        scenario("A dispatcher searches incoming reports for a location name while the "
                + "address box suggests known places as the operator types.");
        String message = "Smoke reported near Central Market";
        System.out.println("'Central' begins at: " + kmpSearch(message, "Central"));
        System.out.println("Character frequencies: " + characterFrequency("LEVEL"));
        System.out.println("LEVEL is palindrome: " + isPalindrome("LEVEL"));

        Trie locations = new Trie();
        for (String location : Arrays.asList("central market", "central park",
                "city hospital", "city hall", "airport")) {
            locations.insert(location);
        }
        System.out.println("Autocomplete 'city': " + locations.autocomplete("city"));
    }

    // KMP preprocesses the pattern, then never moves text index backward: O(n+m).
    public static int kmpSearch(String text, String pattern) {
        if (pattern.isEmpty()) {
            return 0;
        }
        int[] longestPrefixSuffix = new int[pattern.length()];
        for (int index = 1, length = 0; index < pattern.length();) {
            if (pattern.charAt(index) == pattern.charAt(length)) {
                longestPrefixSuffix[index++] = ++length;
            } else if (length > 0) {
                length = longestPrefixSuffix[length - 1];
            } else {
                index++;
            }
        }
        for (int textIndex = 0, patternIndex = 0; textIndex < text.length();) {
            if (text.charAt(textIndex) == pattern.charAt(patternIndex)) {
                textIndex++;
                patternIndex++;
                if (patternIndex == pattern.length()) {
                    return textIndex - patternIndex;
                }
            } else if (patternIndex > 0) {
                patternIndex = longestPrefixSuffix[patternIndex - 1];
            } else {
                textIndex++;
            }
        }
        return -1;
    }

    public static Map<Character, Integer> characterFrequency(String text) {
        Map<Character, Integer> frequency = new LinkedHashMap<>();
        for (char character : text.toCharArray()) {
            frequency.put(character, frequency.getOrDefault(character, 0) + 1);
        }
        return frequency;
    }

    public static boolean isPalindrome(String text) {
        for (int left = 0, right = text.length() - 1; left < right; left++, right--) {
            if (text.charAt(left) != text.charAt(right)) {
                return false;
            }
        }
        return true;
    }

    public static final class Trie {
        private final TrieNode root = new TrieNode();

        public void insert(String word) { // O(length)
            TrieNode current = root;
            for (char character : word.toCharArray()) {
                current = current.children.computeIfAbsent(character, key -> new TrieNode());
            }
            current.word = true;
        }

        public boolean contains(String word) { // O(length)
            TrieNode node = findNode(word);
            return node != null && node.word;
        }

        public List<String> autocomplete(String prefix) {
            TrieNode node = findNode(prefix);
            List<String> matches = new ArrayList<>();
            if (node != null) {
                collect(node, new StringBuilder(prefix), matches);
            }
            return matches;
        }

        private TrieNode findNode(String text) {
            TrieNode current = root;
            for (char character : text.toCharArray()) {
                current = current.children.get(character);
                if (current == null) {
                    return null;
                }
            }
            return current;
        }

        private void collect(TrieNode node, StringBuilder text, List<String> matches) {
            if (node.word) {
                matches.add(text.toString());
            }
            List<Character> letters = new ArrayList<>(node.children.keySet());
            Collections.sort(letters);
            for (char letter : letters) {
                text.append(letter);
                collect(node.children.get(letter), text, matches);
                text.deleteCharAt(text.length() - 1); // backtracking
            }
        }
    }

    private static final class TrieNode {
        final Map<Character, TrieNode> children = new HashMap<>();
        boolean word;
    }

    /* ================================================================
       4. HASHING, STACK AND QUEUE
       ================================================================ */

    private static void demonstrateStackQueueAndHashing() {
        heading("4. Duplicate reports, command validation and service queue");
        scenario("The system rejects duplicate report IDs, validates nested command "
                + "symbols and serves citizens in arrival order.");
        Set<String> reportIds = new HashSet<>();
        for (String id : Arrays.asList("F-10", "M-20", "F-10")) {
            System.out.println(id + (reportIds.add(id) ? " accepted" : " is duplicate"));
        }
        System.out.println("Command brackets valid: " + balanced("dispatch(unit[3])"));

        Queue<String> serviceQueue = new ArrayDeque<>();
        serviceQueue.offer("Citizen A");
        serviceQueue.offer("Citizen B");
        System.out.println("FIFO service order: " + serviceQueue.poll() + ", "
                + serviceQueue.poll());
    }

    public static boolean balanced(String expression) { // O(n) time, O(n) space
        Deque<Character> stack = new ArrayDeque<>();
        for (char character : expression.toCharArray()) {
            if (character == '(' || character == '[' || character == '{') {
                stack.push(character);
            } else if (character == ')' || character == ']' || character == '}') {
                if (stack.isEmpty()) {
                    return false;
                }
                char opening = stack.pop();
                if ((character == ')' && opening != '(')
                        || (character == ']' && opening != '[')
                        || (character == '}' && opening != '{')) {
                    return false;
                }
            }
        }
        return stack.isEmpty();
    }

    /* ================================================================
       5. BINARY SEARCH TREE AND HEAP
       ================================================================ */

    private static void demonstrateTreesAndHeap() {
        heading("5. Sensor BST and emergency priority heap");
        scenario("Sensor IDs need ordered lookup, but emergency calls must be removed "
                + "by severity rather than by arrival time.");
        BinarySearchTree sensors = new BinarySearchTree();
        for (int id : new int[]{50, 30, 70, 20, 40, 60, 80}) {
            sensors.insert(id);
        }
        System.out.println("Sorted sensor IDs (inorder): " + sensors.inorder());
        System.out.println("Sensor 60 exists: " + sensors.contains(60));

        PriorityQueue<Incident> emergencies = new PriorityQueue<>(
                Comparator.comparingInt((Incident item) -> item.severity).reversed());
        emergencies.offer(new Incident("Street light", 1, 3));
        emergencies.offer(new Incident("Fire", 5, 8));
        emergencies.offer(new Incident("Collision", 4, 10));
        System.out.println("Highest-priority incident: " + emergencies.poll());
    }

    public static final class BinarySearchTree {
        private TreeNode root;

        public void insert(int value) { // average O(log n), worst O(n)
            root = insert(root, value);
        }

        private TreeNode insert(TreeNode node, int value) {
            if (node == null) {
                return new TreeNode(value);
            }
            if (value < node.value) {
                node.left = insert(node.left, value);
            } else if (value > node.value) {
                node.right = insert(node.right, value);
            }
            return node;
        }

        public boolean contains(int value) {
            TreeNode current = root;
            while (current != null) {
                if (current.value == value) {
                    return true;
                }
                current = value < current.value ? current.left : current.right;
            }
            return false;
        }

        public List<Integer> inorder() {
            List<Integer> values = new ArrayList<>();
            inorder(root, values);
            return values;
        }

        private void inorder(TreeNode node, List<Integer> values) {
            if (node != null) {
                inorder(node.left, values);
                values.add(node.value);
                inorder(node.right, values);
            }
        }
    }

    private static final class TreeNode {
        final int value;
        TreeNode left;
        TreeNode right;

        TreeNode(int value) {
            this.value = value;
        }
    }

    /* ================================================================
       6. GRAPH: BFS, DFS AND DIJKSTRA
       ================================================================ */

    private static void demonstrateGraphRouting() {
        heading("6. City graph traversal and shortest emergency route");
        scenario("Locations are graph nodes and roads are weighted edges. Responders "
                + "inspect reachable areas and calculate the fastest hospital route.");
        CityGraph city = new CityGraph();
        city.addRoad("Station", "Market", 4);
        city.addRoad("Station", "School", 2);
        city.addRoad("School", "Hospital", 5);
        city.addRoad("Market", "Hospital", 2);
        city.addRoad("Hospital", "Airport", 6);
        System.out.println("BFS from Station: " + city.breadthFirst("Station"));
        System.out.println("DFS from Station: " + city.depthFirst("Station"));
        System.out.println("Fastest Station -> Airport: "
                + city.shortestPath("Station", "Airport"));
    }

    public static final class CityGraph {
        private final Map<String, List<Edge>> adjacency = new HashMap<>();

        public void addRoad(String first, String second, int minutes) {
            if (minutes <= 0) {
                throw new IllegalArgumentException("Road time must be positive.");
            }
            adjacency.computeIfAbsent(first, key -> new ArrayList<>())
                    .add(new Edge(second, minutes));
            adjacency.computeIfAbsent(second, key -> new ArrayList<>())
                    .add(new Edge(first, minutes));
        }

        public List<String> breadthFirst(String start) { // O(V+E)
            List<String> order = new ArrayList<>();
            Set<String> visited = new HashSet<>();
            Queue<String> queue = new ArrayDeque<>();
            visited.add(start);
            queue.offer(start);
            while (!queue.isEmpty()) {
                String location = queue.poll();
                order.add(location);
                for (Edge edge : adjacency.getOrDefault(location, Collections.emptyList())) {
                    if (visited.add(edge.to)) {
                        queue.offer(edge.to);
                    }
                }
            }
            return order;
        }

        public List<String> depthFirst(String start) { // O(V+E)
            List<String> order = new ArrayList<>();
            depthFirst(start, new HashSet<>(), order);
            return order;
        }

        private void depthFirst(String location, Set<String> visited, List<String> order) {
            if (!visited.add(location)) {
                return;
            }
            order.add(location);
            for (Edge edge : adjacency.getOrDefault(location, Collections.emptyList())) {
                depthFirst(edge.to, visited, order);
            }
        }

        // Dijkstra: O((V+E) log V) time and O(V) auxiliary space.
        public Path shortestPath(String start, String destination) {
            Map<String, Integer> distance = new HashMap<>();
            Map<String, String> previous = new HashMap<>();
            PriorityQueue<NodeDistance> queue = new PriorityQueue<>(
                    Comparator.comparingInt(node -> node.distance));
            distance.put(start, 0);
            queue.offer(new NodeDistance(start, 0));
            while (!queue.isEmpty()) {
                NodeDistance current = queue.poll();
                if (current.distance != distance.getOrDefault(current.location, Integer.MAX_VALUE)) {
                    continue;
                }
                for (Edge edge : adjacency.getOrDefault(current.location, Collections.emptyList())) {
                    int candidate = current.distance + edge.minutes;
                    if (candidate < distance.getOrDefault(edge.to, Integer.MAX_VALUE)) {
                        distance.put(edge.to, candidate);
                        previous.put(edge.to, current.location);
                        queue.offer(new NodeDistance(edge.to, candidate));
                    }
                }
            }
            if (!distance.containsKey(destination)) {
                return new Path(Collections.emptyList(), Integer.MAX_VALUE);
            }
            LinkedList<String> locations = new LinkedList<>();
            for (String at = destination; at != null; at = previous.get(at)) {
                locations.addFirst(at);
                if (at.equals(start)) {
                    break;
                }
            }
            return new Path(locations, distance.get(destination));
        }
    }

    /* ================================================================
       7. RECURSION, BACKTRACKING, GREEDY AND DYNAMIC PROGRAMMING
       ================================================================ */

    private static void demonstrateRecursionGreedyAndDynamicProgramming() {
        heading("7. Planning algorithms");
        scenario("The city estimates evacuation choices, forms response-team combinations, "
                + "schedules non-overlapping repairs and minimizes resource usage.");
        System.out.println("Ways to climb 8 evacuation floors: " + climbingWays(8));
        System.out.println("Possible teams from A, B, C: "
                + subsets(Arrays.asList("A", "B", "C")));

        int[][] activities = {{1, 3}, {2, 5}, {4, 7}, {6, 9}, {8, 10}};
        System.out.println("Maximum non-overlapping maintenance jobs: "
                + selectActivities(activities));

        int[] costs = {2, 5, 3, 7};
        System.out.println("Minimum cost to reach energy target 10: "
                + minimumUnboundedCost(costs, 10));
    }

    // Tabulated DP: O(n) time and O(n) space; each state reuses two earlier states.
    public static long climbingWays(int floors) {
        if (floors < 0) {
            return 0;
        }
        long[] ways = new long[Math.max(2, floors + 1)];
        ways[0] = 1;
        ways[1] = 1;
        for (int floor = 2; floor <= floors; floor++) {
            ways[floor] = ways[floor - 1] + ways[floor - 2];
        }
        return ways[floors];
    }

    // Backtracking creates all 2^n subsets; output itself requires Theta(n*2^n).
    public static List<List<String>> subsets(List<String> values) {
        List<List<String>> result = new ArrayList<>();
        buildSubsets(values, 0, new ArrayList<>(), result);
        return result;
    }

    private static void buildSubsets(List<String> values, int index,
                                     List<String> chosen, List<List<String>> result) {
        if (index == values.size()) {
            result.add(new ArrayList<>(chosen));
            return;
        }
        buildSubsets(values, index + 1, chosen, result);
        chosen.add(values.get(index));
        buildSubsets(values, index + 1, chosen, result);
        chosen.remove(chosen.size() - 1);
    }

    // Greedy earliest-finish choice: O(n log n) because of sorting.
    public static List<String> selectActivities(int[][] activities) {
        Arrays.sort(activities, Comparator.comparingInt(activity -> activity[1]));
        List<String> selected = new ArrayList<>();
        int lastEnd = Integer.MIN_VALUE;
        for (int[] activity : activities) {
            if (activity[0] >= lastEnd) {
                selected.add(activity[0] + "-" + activity[1]);
                lastEnd = activity[1];
            }
        }
        return selected;
    }

    // Unbounded coin-change style DP: O(target * choices), O(target) space.
    public static int minimumUnboundedCost(int[] choices, int target) {
        int[] count = new int[target + 1];
        Arrays.fill(count, target + 1);
        count[0] = 0;
        for (int amount = 1; amount <= target; amount++) {
            for (int choice : choices) {
                if (choice <= amount) {
                    count[amount] = Math.min(count[amount], count[amount - choice] + 1);
                }
            }
        }
        return count[target] > target ? -1 : count[target];
    }

    /* ================================================================
       8. BIT MANIPULATION, MATHEMATICS AND FENWICK TREE
       ================================================================ */

    private static void demonstrateBitsMathematicsAndRangeQueries() {
        heading("8. Permissions, mathematics and live range totals");
        scenario("Compact permission flags control operator actions, repeating signal cycles "
                + "must be synchronized, and energy readings change throughout the day.");
        int READ = 1;
        int WRITE = 1 << 1;
        int DISPATCH = 1 << 2;
        int permissions = READ | DISPATCH;
        System.out.println("Can dispatch: " + ((permissions & DISPATCH) != 0));
        permissions |= WRITE;        // set a bit
        permissions ^= READ;         // toggle a bit
        System.out.println("Permission bit count: " + Integer.bitCount(permissions));
        System.out.println("GCD of signal cycles 84 and 30: " + gcd(84, 30));
        System.out.println("2^20 mod 1,000,000,007: "
                + modularPower(2, 20, 1_000_000_007));

        FenwickTree energy = new FenwickTree(new int[]{5, 3, 7, 2, 6, 4});
        System.out.println("Energy total zones 1..4: " + energy.rangeSum(1, 4));
        energy.add(2, 5);
        System.out.println("After zone 2 update: " + energy.rangeSum(1, 4));
    }

    public static int gcd(int first, int second) { // Euclid: O(log min(a,b))
        first = Math.abs(first);
        second = Math.abs(second);
        while (second != 0) {
            int remainder = first % second;
            first = second;
            second = remainder;
        }
        return first;
    }

    // Binary exponentiation: O(log exponent), versus O(exponent) multiplication.
    public static long modularPower(long base, long exponent, long modulus) {
        if (modulus <= 0 || exponent < 0) {
            throw new IllegalArgumentException("Invalid exponent or modulus.");
        }
        long answer = 1 % modulus;
        base %= modulus;
        while (exponent > 0) {
            if ((exponent & 1) == 1) {
                answer = answer * base % modulus;
            }
            base = base * base % modulus;
            exponent >>= 1;
        }
        return answer;
    }

    public static final class FenwickTree {
        private final long[] tree;

        public FenwickTree(int[] values) {
            tree = new long[values.length + 1];
            for (int index = 0; index < values.length; index++) {
                add(index, values[index]);
            }
        }

        public void add(int zeroBasedIndex, int change) { // O(log n)
            if (zeroBasedIndex < 0 || zeroBasedIndex >= tree.length - 1) {
                throw new IndexOutOfBoundsException("Invalid Fenwick index.");
            }
            for (int index = zeroBasedIndex + 1; index < tree.length;
                 index += index & -index) {
                tree[index] += change;
            }
        }

        public long rangeSum(int left, int right) { // O(log n)
            if (left < 0 || right < left || right >= tree.length - 1) {
                throw new IllegalArgumentException("Invalid Fenwick range.");
            }
            return prefix(right) - prefix(left - 1);
        }

        private long prefix(int zeroBasedIndex) {
            long sum = 0;
            for (int index = zeroBasedIndex + 1; index > 0; index -= index & -index) {
                sum += tree[index];
            }
            return sum;
        }
    }

    /* ================================================================
       9. COMPUTATIONAL GEOMETRY AND LRU CACHE
       ================================================================ */

    private static void demonstrateGeometryAndCache() {
        heading("9. Geometry and recently-used route cache");
        scenario("Dispatch estimates straight-line distance, checks whether emergency zones "
                + "overlap and retains only the most recently requested routes.");
        Point station = new Point(1, 2);
        Point incident = new Point(7, 10);
        System.out.printf("Station-to-incident straight-line distance: %.2f%n",
                station.distanceTo(incident));
        System.out.println("Emergency zones overlap: "
                + rectanglesOverlap(new Rectangle(0, 0, 5, 5),
                new Rectangle(4, 3, 8, 7)));

        LruCache<String, String> routes = new LruCache<>(2);
        routes.put("A-B", "A -> Market -> B");
        routes.put("A-C", "A -> School -> C");
        routes.get("A-B"); // A-B becomes most recently used
        routes.put("A-D", "A -> Hospital -> D"); // evicts A-C
        System.out.println("Route cache: " + routes);
    }

    public static boolean rectanglesOverlap(Rectangle first, Rectangle second) {
        return first.left < second.right && first.right > second.left
                && first.bottom < second.top && first.top > second.bottom;
    }

    /** LinkedHashMap access order gives average O(1) get/put/eviction. */
    public static final class LruCache<K, V> extends LinkedHashMap<K, V> {
        private static final long serialVersionUID = 1L;
        private final int capacity;

        public LruCache(int capacity) {
            super(capacity, 0.75f, true);
            if (capacity <= 0) {
                throw new IllegalArgumentException("Capacity must be positive.");
            }
            this.capacity = capacity;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return size() > capacity;
        }
    }

    public static final class Point {
        final double x;
        final double y;

        Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        double distanceTo(Point other) {
            return Math.hypot(x - other.x, y - other.y);
        }
    }

    public static final class Rectangle {
        final double left;
        final double bottom;
        final double right;
        final double top;

        Rectangle(double left, double bottom, double right, double top) {
            if (left >= right || bottom >= top) {
                throw new IllegalArgumentException("Invalid rectangle coordinates.");
            }
            this.left = left;
            this.bottom = bottom;
            this.right = right;
            this.top = top;
        }
    }

    private static final class Incident {
        final String name;
        final int severity;
        final int reportedMinute;

        Incident(String name, int severity, int reportedMinute) {
            this.name = name;
            this.severity = severity;
            this.reportedMinute = reportedMinute;
        }

        @Override
        public String toString() {
            return name + "(severity=" + severity + ")";
        }
    }

    private static final class Edge {
        final String to;
        final int minutes;

        Edge(String to, int minutes) {
            this.to = to;
            this.minutes = minutes;
        }
    }

    private static final class NodeDistance {
        final String location;
        final int distance;

        NodeDistance(String location, int distance) {
            this.location = location;
            this.distance = distance;
        }
    }

    public static final class Path {
        public final List<String> locations;
        public final int totalMinutes;

        Path(List<String> locations, int totalMinutes) {
            this.locations = Collections.unmodifiableList(new ArrayList<>(locations));
            this.totalMinutes = totalMinutes;
        }

        @Override
        public String toString() {
            if (locations.isEmpty()) {
                return "unreachable";
            }
            return String.join(" -> ", locations) + " (" + totalMinutes + " min)";
        }
    }

    private static void heading(String title) {
        System.out.println("\n===== " + title + " =====");
    }

    private static void scenario(String explanation) {
        System.out.println("Real-life situation: " + explanation);
        System.out.println("What the algorithms produce:");
    }

    private static String intervalsToString(List<int[]> intervals) {
        List<String> text = new ArrayList<>();
        for (int[] interval : intervals) {
            text.add(Arrays.toString(interval));
        }
        return text.toString();
    }
}
