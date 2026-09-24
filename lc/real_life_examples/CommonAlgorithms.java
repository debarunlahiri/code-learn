import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

/**
 * Beginner-friendly examples of common algorithms.
 *
 * <p>You do not need to understand this entire file at once. Pick one section,
 * run its example from {@link #main(String[])}, and then read that method.
 * Each public method is independent, so it can also be copied into a small
 * practice program.</p>
 *
 * <p>Useful words used in this file:</p>
 * <ul>
 *   <li><b>Time complexity</b>: how the running time grows as the input grows.</li>
 *   <li><b>Space complexity</b>: how much extra memory an algorithm needs.</li>
 *   <li><b>Vertex</b>: a point in a graph, such as a city or a user.</li>
 *   <li><b>Edge</b>: a connection between two vertices, such as a road.</li>
 * </ul>
 */
public final class CommonAlgorithms {

    private static final long INF = Long.MAX_VALUE / 4;

    private CommonAlgorithms() {
    }

    public static void main(String[] args) {
        System.out.println("\n--- Number theory ---");
        System.out.println("Primes up to 30: " + sieveOfEratosthenes(30));
        System.out.println("GCD of 48 and 18: " + gcd(48, 18));
        System.out.println("2^10: " + fastPower(2, 10));

        System.out.println("\n--- Searching and sorting ---");
        int[] values = {7, 2, 9, 1, 5};
        mergeSort(values);
        System.out.println("Merge sort: " + Arrays.toString(values));
        System.out.println("Binary search for 5: " + binarySearch(values, 5));

        System.out.println("\n--- Arrays and strings ---");
        int[] gains = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        System.out.println("Maximum subarray sum: " + kadane(gains));
        System.out.println("KMP matches: " + kmpSearch("ABABDABACDABABCABAB", "ABABCABAB"));

        System.out.println("\n--- Graphs ---");
        WeightedGraph graph = new WeightedGraph(5, false);
        graph.addEdge(0, 1, 4);
        graph.addEdge(0, 2, 1);
        graph.addEdge(2, 1, 2);
        graph.addEdge(1, 3, 1);
        graph.addEdge(2, 3, 5);
        graph.addEdge(3, 4, 3);
        System.out.println("BFS: " + bfs(graph, 0));
        System.out.println("Dijkstra distances: " + Arrays.toString(dijkstra(graph, 0)));
        System.out.println("Prim MST weight: " + primMstWeight(graph));

        System.out.println("\n--- Dynamic programming and backtracking ---");
        System.out.println("0/1 knapsack: "
                + zeroOneKnapsack(new int[]{2, 3, 4}, new int[]{4, 5, 7}, 5));
        System.out.println("LCS length: " + longestCommonSubsequence("AGGTAB", "GXTXAYB"));
        System.out.println("N-Queens solutions for n=4: " + solveNQueens(4).size());
    }

    // ---------------------------------------------------------------------
    // Number theory: algorithms that work with whole numbers.
    // ---------------------------------------------------------------------

    /**
     * Finds every prime number from 2 through {@code limit}.
     *
     * <p>Idea: begin with every number available. When a prime is found, mark
     * all of its multiples as composite. The unmarked numbers are prime.</p>
     * Time: O(n log log n), space: O(n).
     */
    public static List<Integer> sieveOfEratosthenes(int limit) {
        if (limit < 2) {
            return Collections.emptyList();
        }

        boolean[] composite = new boolean[limit + 1];
        for (int number = 2; number <= limit / number; number++) {
            if (!composite[number]) {
                for (int multiple = number * number; multiple <= limit; multiple += number) {
                    composite[multiple] = true;
                }
            }
        }

        List<Integer> primes = new ArrayList<>();
        for (int number = 2; number <= limit; number++) {
            if (!composite[number]) {
                primes.add(number);
            }
        }
        return primes;
    }

    /**
     * Finds the greatest common divisor using repeated division.
     * Example: gcd(48, 18) eventually becomes gcd(6, 0), so the answer is 6.
     * Time: O(log(min(a, b))).
     */
    public static long gcd(long a, long b) {
        a = Math.abs(a);
        b = Math.abs(b);
        while (b != 0) {
            long remainder = a % b;
            a = b;
            b = remainder;
        }
        return a;
    }

    /**
     * Returns {@code [gcd, x, y]} where {@code a*x + b*y = gcd(a, b)}.
     * This extended form is useful for modular arithmetic and cryptography.
     */
    public static long[] extendedGcd(long a, long b) {
        if (b == 0) {
            return new long[]{Math.abs(a), a < 0 ? -1 : 1, 0};
        }
        long[] next = extendedGcd(b, a % b);
        return new long[]{next[0], next[2], next[1] - (a / b) * next[2]};
    }

    /**
     * Calculates a power by repeatedly squaring the base.
     * This needs about log(exponent) steps instead of exponent steps.
     */
    public static long fastPower(long base, long exponent) {
        if (exponent < 0) {
            throw new IllegalArgumentException("Exponent must be non-negative");
        }
        long result = 1;
        while (exponent > 0) {
            if ((exponent & 1) == 1) {
                result *= base;
            }
            base *= base;
            exponent >>= 1;
        }
        return result;
    }

    // ---------------------------------------------------------------------
    // Searching and sorting: finding values and putting them in order.
    // ---------------------------------------------------------------------

    public static int linearSearch(int[] values, int target) {
        for (int index = 0; index < values.length; index++) {
            if (values[index] == target) {
                return index;
            }
        }
        return -1;
    }

    /**
     * Finds a value by repeatedly discarding half of a sorted array.
     * Returns its index, or -1 when it is absent. The array must be sorted.
     */
    public static int binarySearch(int[] values, int target) {
        int left = 0;
        int right = values.length - 1;
        while (left <= right) {
            int middle = left + (right - left) / 2;
            if (values[middle] == target) {
                return middle;
            }
            if (values[middle] < target) {
                left = middle + 1;
            } else {
                right = middle - 1;
            }
        }
        return -1;
    }

    public static void bubbleSort(int[] values) {
        // After each pass, the largest remaining value has "bubbled" to end.
        for (int end = values.length - 1; end > 0; end--) {
            boolean swapped = false;
            for (int index = 0; index < end; index++) {
                if (values[index] > values[index + 1]) {
                    swap(values, index, index + 1);
                    swapped = true;
                }
            }
            if (!swapped) {
                return;
            }
        }
    }

    public static void selectionSort(int[] values) {
        // Select the smallest remaining value and place it at the front.
        for (int index = 0; index < values.length - 1; index++) {
            int minimum = index;
            for (int candidate = index + 1; candidate < values.length; candidate++) {
                if (values[candidate] < values[minimum]) {
                    minimum = candidate;
                }
            }
            swap(values, index, minimum);
        }
    }

    public static void insertionSort(int[] values) {
        // Grow a sorted left side by inserting one value at a time.
        for (int index = 1; index < values.length; index++) {
            int current = values[index];
            int position = index - 1;
            while (position >= 0 && values[position] > current) {
                values[position + 1] = values[position];
                position--;
            }
            values[position + 1] = current;
        }
    }

    public static void mergeSort(int[] values) {
        int[] buffer = new int[values.length];
        mergeSort(values, buffer, 0, values.length - 1);
    }

    private static void mergeSort(int[] values, int[] buffer, int left, int right) {
        if (left >= right) {
            return;
        }
        int middle = left + (right - left) / 2;
        mergeSort(values, buffer, left, middle);
        mergeSort(values, buffer, middle + 1, right);
        int leftIndex = left;
        int rightIndex = middle + 1;
        int bufferIndex = left;

        // Merge the two sorted halves into the temporary buffer.
        while (leftIndex <= middle && rightIndex <= right) {
            if (values[leftIndex] <= values[rightIndex]) {
                buffer[bufferIndex] = values[leftIndex];
                leftIndex++;
            } else {
                buffer[bufferIndex] = values[rightIndex];
                rightIndex++;
            }
            bufferIndex++;
        }
        while (leftIndex <= middle) {
            buffer[bufferIndex++] = values[leftIndex++];
        }
        while (rightIndex <= right) {
            buffer[bufferIndex++] = values[rightIndex++];
        }
        for (int index = left; index <= right; index++) {
            values[index] = buffer[index];
        }
    }

    public static void quickSort(int[] values) {
        quickSort(values, 0, values.length - 1);
    }

    private static void quickSort(int[] values, int low, int high) {
        if (low >= high) {
            return;
        }
        int pivot = values[high];
        int boundary = low;
        for (int index = low; index < high; index++) {
            if (values[index] <= pivot) {
                swap(values, boundary++, index);
            }
        }
        swap(values, boundary, high);
        quickSort(values, low, boundary - 1);
        quickSort(values, boundary + 1, high);
    }

    public static void heapSort(int[] values) {
        for (int index = values.length / 2 - 1; index >= 0; index--) {
            heapify(values, values.length, index);
        }
        for (int end = values.length - 1; end > 0; end--) {
            swap(values, 0, end);
            heapify(values, end, 0);
        }
    }

    private static void heapify(int[] values, int size, int root) {
        int largest = root;
        int left = 2 * root + 1;
        int right = left + 1;
        if (left < size && values[left] > values[largest]) {
            largest = left;
        }
        if (right < size && values[right] > values[largest]) {
            largest = right;
        }
        if (largest != root) {
            swap(values, root, largest);
            heapify(values, size, largest);
        }
    }

    /** Counting sort supporting both negative and positive integers. */
    public static void countingSort(int[] values) {
        if (values.length < 2) {
            return;
        }
        int minimum = values[0];
        int maximum = values[0];
        for (int value : values) {
            minimum = Math.min(minimum, value);
            maximum = Math.max(maximum, value);
        }
        long range = (long) maximum - minimum + 1;
        if (range > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Value range is too large for counting sort");
        }
        int[] counts = new int[(int) range];
        for (int value : values) {
            counts[value - minimum]++;
        }
        int write = 0;
        for (int offset = 0; offset < counts.length; offset++) {
            while (counts[offset]-- > 0) {
                values[write++] = offset + minimum;
            }
        }
    }

    // ---------------------------------------------------------------------
    // Arrays: common techniques for processing a sequence of values.
    // ---------------------------------------------------------------------

    /**
     * Finds the largest sum made by consecutive values.
     * At each position, either start a new subarray or extend the old one.
     */
    public static int kadane(int[] values) {
        requireNonEmpty(values);
        int bestEndingHere = values[0];
        int best = values[0];
        for (int index = 1; index < values.length; index++) {
            bestEndingHere = Math.max(values[index], bestEndingHere + values[index]);
            best = Math.max(best, bestEndingHere);
        }
        return best;
    }

    /**
     * Sorts an array containing only 0, 1, and 2 in one pass.
     * Values before {@code low} are 0; values after {@code high} are 2.
     */
    public static void dutchNationalFlag(int[] values) {
        int low = 0;
        int middle = 0;
        int high = values.length - 1;
        while (middle <= high) {
            if (values[middle] == 0) {
                swap(values, low++, middle++);
            } else if (values[middle] == 1) {
                middle++;
            } else if (values[middle] == 2) {
                swap(values, middle, high--);
            } else {
                throw new IllegalArgumentException("Only 0, 1 and 2 are allowed");
            }
        }
    }

    /**
     * Finds two values that make {@code target} in a sorted array.
     * A small sum moves the left pointer; a large sum moves the right pointer.
     */
    public static int[] twoPointerPairSum(int[] sortedValues, int target) {
        int left = 0;
        int right = sortedValues.length - 1;
        while (left < right) {
            long sum = (long) sortedValues[left] + sortedValues[right];
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

    /**
     * Finds the largest sum of exactly {@code windowSize} consecutive values.
     * The window moves by adding one new value and removing one old value.
     */
    public static long maximumWindowSum(int[] values, int windowSize) {
        if (windowSize <= 0 || windowSize > values.length) {
            throw new IllegalArgumentException("Invalid window size");
        }
        long current = 0;
        for (int index = 0; index < windowSize; index++) {
            current += values[index];
        }
        long best = current;
        for (int index = windowSize; index < values.length; index++) {
            current += values[index] - values[index - windowSize];
            best = Math.max(best, current);
        }
        return best;
    }

    /**
     * Precalculates running totals. {@code prefix[i]} contains the sum before
     * index {@code i}, allowing later range-sum queries to take constant time.
     */
    public static long[] prefixSums(int[] values) {
        long[] prefix = new long[values.length + 1];
        for (int index = 0; index < values.length; index++) {
            prefix[index + 1] = prefix[index] + values[index];
        }
        return prefix;
    }

    public static long rangeSum(long[] prefix, int leftInclusive, int rightInclusive) {
        if (leftInclusive < 0 || rightInclusive < leftInclusive
                || rightInclusive + 1 >= prefix.length) {
            throw new IndexOutOfBoundsException("Invalid range");
        }
        return prefix[rightInclusive + 1] - prefix[leftInclusive];
    }

    // ---------------------------------------------------------------------
    // Strings: finding a smaller pattern inside a larger piece of text.
    // ---------------------------------------------------------------------

    /**
     * Finds every starting index of {@code pattern} inside {@code text}.
     * KMP remembers how much of the pattern still matches after a mismatch,
     * so it never moves backward through the text.
     */
    public static List<Integer> kmpSearch(String text, String pattern) {
        if (pattern.isEmpty()) {
            return Collections.singletonList(0);
        }
        int[] lps = buildLps(pattern);
        List<Integer> matches = new ArrayList<>();
        int textIndex = 0;
        int patternIndex = 0;
        while (textIndex < text.length()) {
            if (text.charAt(textIndex) == pattern.charAt(patternIndex)) {
                textIndex++;
                patternIndex++;
                if (patternIndex == pattern.length()) {
                    matches.add(textIndex - patternIndex);
                    patternIndex = lps[patternIndex - 1];
                }
            } else if (patternIndex > 0) {
                patternIndex = lps[patternIndex - 1];
            } else {
                textIndex++;
            }
        }
        return matches;
    }

    private static int[] buildLps(String pattern) {
        int[] lps = new int[pattern.length()];
        int prefixLength = 0;
        for (int index = 1; index < pattern.length();) {
            if (pattern.charAt(index) == pattern.charAt(prefixLength)) {
                lps[index++] = ++prefixLength;
            } else if (prefixLength > 0) {
                prefixLength = lps[prefixLength - 1];
            } else {
                index++;
            }
        }
        return lps;
    }

    /**
     * Finds a pattern by comparing numeric fingerprints called hashes.
     * The rolling hash cheaply removes the old character and adds the new one.
     */
    public static List<Integer> rabinKarp(String text, String pattern) {
        List<Integer> matches = new ArrayList<>();
        if (pattern.isEmpty()) {
            matches.add(0);
            return matches;
        }
        if (pattern.length() > text.length()) {
            return matches;
        }
        long base = 256;
        long modulus = 1_000_000_007L;
        long highestPower = 1;
        long patternHash = 0;
        long windowHash = 0;
        for (int index = 0; index < pattern.length(); index++) {
            patternHash = (patternHash * base + pattern.charAt(index)) % modulus;
            windowHash = (windowHash * base + text.charAt(index)) % modulus;
            if (index < pattern.length() - 1) {
                highestPower = highestPower * base % modulus;
            }
        }
        for (int start = 0; start <= text.length() - pattern.length(); start++) {
            if (patternHash == windowHash
                    && text.regionMatches(start, pattern, 0, pattern.length())) {
                matches.add(start);
            }
            if (start < text.length() - pattern.length()) {
                windowHash = (windowHash - text.charAt(start) * highestPower) % modulus;
                if (windowHash < 0) {
                    windowHash += modulus;
                }
                windowHash = (windowHash * base + text.charAt(start + pattern.length())) % modulus;
            }
        }
        return matches;
    }

    /**
     * For every index, records how many characters match the string's prefix.
     * For example, the Z values help find repeated prefixes and patterns.
     */
    public static int[] zAlgorithm(String value) {
        int[] z = new int[value.length()];
        int left = 0;
        int right = 0;
        for (int index = 1; index < value.length(); index++) {
            if (index <= right) {
                z[index] = Math.min(right - index + 1, z[index - left]);
            }
            while (index + z[index] < value.length()
                    && value.charAt(z[index]) == value.charAt(index + z[index])) {
                z[index]++;
            }
            if (index + z[index] - 1 > right) {
                left = index;
                right = index + z[index] - 1;
            }
        }
        return z;
    }

    // ---------------------------------------------------------------------
    // Linked list: nodes connected one after another.
    // ---------------------------------------------------------------------

    public static final class ListNode {
        public final int value;
        public ListNode next;

        public ListNode(int value) {
            this.value = value;
        }
    }

    /**
     * Detects a loop with a slow pointer and a fast pointer.
     * If a loop exists, the faster pointer eventually catches the slower one.
     */
    public static boolean hasCycle(ListNode head) {
        ListNode slow = head;
        ListNode fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) {
                return true;
            }
        }
        return false;
    }

    // ---------------------------------------------------------------------
    // Graphs: points (vertices) connected by weighted links (edges).
    // ---------------------------------------------------------------------

    public static final class Edge {
        public final int from;
        public final int to;
        public final int weight;

        public Edge(int from, int to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
    }

    public static final class WeightedGraph {
        private final List<List<Edge>> adjacency;
        private final List<Edge> edges = new ArrayList<>();
        private final boolean directed;

        public WeightedGraph(int vertices, boolean directed) {
            if (vertices < 0) {
                throw new IllegalArgumentException("Vertex count cannot be negative");
            }
            this.directed = directed;
            adjacency = new ArrayList<>(vertices);
            for (int vertex = 0; vertex < vertices; vertex++) {
                adjacency.add(new ArrayList<>());
            }
        }

        public void addEdge(int from, int to, int weight) {
            validateVertex(from);
            validateVertex(to);
            Edge edge = new Edge(from, to, weight);
            adjacency.get(from).add(edge);
            edges.add(edge);
            if (!directed) {
                adjacency.get(to).add(new Edge(to, from, weight));
            }
        }

        public int size() {
            return adjacency.size();
        }

        private void validateVertex(int vertex) {
            if (vertex < 0 || vertex >= size()) {
                throw new IndexOutOfBoundsException("Invalid vertex: " + vertex);
            }
        }
    }

    public static List<Integer> bfs(WeightedGraph graph, int start) {
        graph.validateVertex(start);
        List<Integer> order = new ArrayList<>();
        boolean[] visited = new boolean[graph.size()];
        Queue<Integer> queue = new ArrayDeque<>();
        visited[start] = true;
        queue.offer(start);
        // A queue makes us visit nearby vertices before distant vertices.
        while (!queue.isEmpty()) {
            int vertex = queue.poll();
            order.add(vertex);
            for (Edge edge : graph.adjacency.get(vertex)) {
                if (!visited[edge.to]) {
                    visited[edge.to] = true;
                    queue.offer(edge.to);
                }
            }
        }
        return order;
    }

    public static List<Integer> dfs(WeightedGraph graph, int start) {
        graph.validateVertex(start);
        List<Integer> order = new ArrayList<>();
        boolean[] visited = new boolean[graph.size()];
        dfs(graph, start, visited, order);
        return order;
    }

    private static void dfs(WeightedGraph graph, int vertex, boolean[] visited,
                            List<Integer> order) {
        // Visit this vertex, then explore each unvisited neighbour completely.
        visited[vertex] = true;
        order.add(vertex);
        for (Edge edge : graph.adjacency.get(vertex)) {
            if (!visited[edge.to]) {
                dfs(graph, edge.to, visited, order);
            }
        }
    }

    /**
     * Finds the shortest distance from one vertex to every other vertex.
     * The priority queue always gives us the closest unfinished vertex.
     * All edge weights must be non-negative.
     */
    public static long[] dijkstra(WeightedGraph graph, int source) {
        graph.validateVertex(source);
        long[] distance = new long[graph.size()];
        Arrays.fill(distance, INF);
        distance[source] = 0;
        PriorityQueue<VertexDistance> queue = new PriorityQueue<>(
                Comparator.comparingLong(item -> item.distance));
        queue.offer(new VertexDistance(source, 0));
        while (!queue.isEmpty()) {
            VertexDistance current = queue.poll();
            if (current.distance != distance[current.vertex]) {
                continue;
            }
            for (Edge edge : graph.adjacency.get(current.vertex)) {
                if (edge.weight < 0) {
                    throw new IllegalArgumentException("Dijkstra cannot use negative edges");
                }
                long candidate = current.distance + edge.weight;
                if (candidate < distance[edge.to]) {
                    distance[edge.to] = candidate;
                    queue.offer(new VertexDistance(edge.to, candidate));
                }
            }
        }
        return distance;
    }

    /**
     * Finds shortest paths even when edges have negative weights.
     * Returns {@code null} when a reachable negative cycle makes a shortest
     * distance impossible to define.
     */
    public static long[] bellmanFord(WeightedGraph graph, int source) {
        graph.validateVertex(source);
        long[] distance = new long[graph.size()];
        Arrays.fill(distance, INF);
        distance[source] = 0;
        for (int pass = 1; pass < graph.size(); pass++) {
            boolean changed = false;
            for (Edge edge : graph.edges) {
                if (distance[edge.from] != INF
                        && distance[edge.from] + edge.weight < distance[edge.to]) {
                    distance[edge.to] = distance[edge.from] + edge.weight;
                    changed = true;
                }
                if (!graph.directed && distance[edge.to] != INF
                        && distance[edge.to] + edge.weight < distance[edge.from]) {
                    distance[edge.from] = distance[edge.to] + edge.weight;
                    changed = true;
                }
            }
            if (!changed) {
                break;
            }
        }
        for (Edge edge : directedEdges(graph)) {
            if (distance[edge.from] != INF
                    && distance[edge.from] + edge.weight < distance[edge.to]) {
                return null;
            }
        }
        return distance;
    }

    public static long[][] floydWarshall(WeightedGraph graph) {
        int size = graph.size();
        long[][] distance = new long[size][size];
        for (int from = 0; from < size; from++) {
            Arrays.fill(distance[from], INF);
            distance[from][from] = 0;
            for (Edge edge : graph.adjacency.get(from)) {
                distance[from][edge.to] = Math.min(distance[from][edge.to], edge.weight);
            }
        }
        // Try allowing each vertex as an intermediate stop.
        for (int through = 0; through < size; through++) {
            for (int from = 0; from < size; from++) {
                for (int to = 0; to < size; to++) {
                    if (distance[from][through] != INF && distance[through][to] != INF) {
                        distance[from][to] = Math.min(distance[from][to],
                                distance[from][through] + distance[through][to]);
                    }
                }
            }
        }
        return distance;
    }

    public static long kruskalMstWeight(WeightedGraph graph) {
        // Take the cheapest edge that does not create a cycle.
        List<Edge> edges = new ArrayList<>(graph.edges);
        edges.sort(Comparator.comparingInt(edge -> edge.weight));
        DisjointSet disjointSet = new DisjointSet(graph.size());
        long weight = 0;
        int used = 0;
        for (Edge edge : edges) {
            if (disjointSet.union(edge.from, edge.to)) {
                weight += edge.weight;
                used++;
            }
        }
        if (graph.size() > 0 && used != graph.size() - 1) {
            throw new IllegalArgumentException("Graph is disconnected");
        }
        return weight;
    }

    public static long primMstWeight(WeightedGraph graph) {
        if (graph.size() == 0) {
            return 0;
        }
        boolean[] included = new boolean[graph.size()];
        PriorityQueue<Edge> queue = new PriorityQueue<>(Comparator.comparingInt(e -> e.weight));
        queue.offer(new Edge(-1, 0, 0));
        long weight = 0;
        int visited = 0;
        // Grow one connected tree by repeatedly taking its cheapest next edge.
        while (!queue.isEmpty() && visited < graph.size()) {
            Edge edge = queue.poll();
            if (included[edge.to]) {
                continue;
            }
            included[edge.to] = true;
            visited++;
            weight += edge.weight;
            for (Edge next : graph.adjacency.get(edge.to)) {
                if (!included[next.to]) {
                    queue.offer(next);
                }
            }
        }
        if (visited != graph.size()) {
            throw new IllegalArgumentException("Graph is disconnected");
        }
        return weight;
    }

    public static final class DisjointSet {
        private final int[] parent;
        private final int[] rank;

        public DisjointSet(int size) {
            parent = new int[size];
            rank = new int[size];
            for (int index = 0; index < size; index++) {
                parent[index] = index;
            }
        }

        public int find(int value) {
            if (parent[value] != value) {
                parent[value] = find(parent[value]);
            }
            return parent[value];
        }

        public boolean union(int first, int second) {
            int firstRoot = find(first);
            int secondRoot = find(second);
            if (firstRoot == secondRoot) {
                return false;
            }
            if (rank[firstRoot] < rank[secondRoot]) {
                parent[firstRoot] = secondRoot;
            } else if (rank[firstRoot] > rank[secondRoot]) {
                parent[secondRoot] = firstRoot;
            } else {
                parent[secondRoot] = firstRoot;
                rank[firstRoot]++;
            }
            return true;
        }
    }

    /** Kahn's algorithm. Returns an empty list when the directed graph has a cycle. */
    public static List<Integer> kahnTopologicalSort(WeightedGraph graph) {
        int[] indegree = new int[graph.size()];
        for (List<Edge> edges : graph.adjacency) {
            for (Edge edge : edges) {
                indegree[edge.to]++;
            }
        }
        Queue<Integer> queue = new ArrayDeque<>();
        for (int vertex = 0; vertex < graph.size(); vertex++) {
            if (indegree[vertex] == 0) {
                queue.offer(vertex);
            }
        }
        List<Integer> order = new ArrayList<>();
        while (!queue.isEmpty()) {
            int vertex = queue.poll();
            order.add(vertex);
            for (Edge edge : graph.adjacency.get(vertex)) {
                if (--indegree[edge.to] == 0) {
                    queue.offer(edge.to);
                }
            }
        }
        return order.size() == graph.size() ? order : Collections.emptyList();
    }

    /** DFS topological sort. Returns an empty list when a cycle exists. */
    public static List<Integer> dfsTopologicalSort(WeightedGraph graph) {
        int[] state = new int[graph.size()];
        Deque<Integer> order = new ArrayDeque<>();
        for (int vertex = 0; vertex < graph.size(); vertex++) {
            if (state[vertex] == 0 && !topologicalDfs(graph, vertex, state, order)) {
                return Collections.emptyList();
            }
        }
        return new ArrayList<>(order);
    }

    private static boolean topologicalDfs(WeightedGraph graph, int vertex, int[] state,
                                          Deque<Integer> order) {
        state[vertex] = 1;
        for (Edge edge : graph.adjacency.get(vertex)) {
            if (state[edge.to] == 1
                    || (state[edge.to] == 0 && !topologicalDfs(graph, edge.to, state, order))) {
                return false;
            }
        }
        state[vertex] = 2;
        order.addFirst(vertex);
        return true;
    }

    // ---------------------------------------------------------------------
    // Trees: hierarchical data such as folders, menus, or family relationships.
    // ---------------------------------------------------------------------

    public static final class TreeNode {
        public final int value;
        public TreeNode left;
        public TreeNode right;

        public TreeNode(int value) {
            this.value = value;
        }
    }

    public static List<Integer> levelOrder(TreeNode root) {
        List<Integer> order = new ArrayList<>();
        if (root == null) {
            return order;
        }
        Queue<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);
        // The queue visits the tree one level at a time, from top to bottom.
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            order.add(node.value);
            if (node.left != null) {
                queue.offer(node.left);
            }
            if (node.right != null) {
                queue.offer(node.right);
            }
        }
        return order;
    }

    public static List<Integer> preorder(TreeNode root) {
        List<Integer> order = new ArrayList<>();
        preorder(root, order);
        return order;
    }

    private static void preorder(TreeNode node, List<Integer> order) {
        if (node != null) {
            order.add(node.value);
            preorder(node.left, order);
            preorder(node.right, order);
        }
    }

    public static List<Integer> inorder(TreeNode root) {
        List<Integer> order = new ArrayList<>();
        inorder(root, order);
        return order;
    }

    private static void inorder(TreeNode node, List<Integer> order) {
        if (node != null) {
            inorder(node.left, order);
            order.add(node.value);
            inorder(node.right, order);
        }
    }

    public static List<Integer> postorder(TreeNode root) {
        List<Integer> order = new ArrayList<>();
        postorder(root, order);
        return order;
    }

    private static void postorder(TreeNode node, List<Integer> order) {
        if (node != null) {
            postorder(node.left, order);
            postorder(node.right, order);
            order.add(node.value);
        }
    }

    /**
     * Performs inorder traversal without recursion or an extra stack.
     * It temporarily links a node's predecessor back to that node and removes
     * the link after using it. Time: O(n), extra space: O(1).
     */
    public static List<Integer> morrisInorder(TreeNode root) {
        List<Integer> order = new ArrayList<>();
        TreeNode current = root;
        while (current != null) {
            if (current.left == null) {
                order.add(current.value);
                current = current.right;
            } else {
                TreeNode predecessor = current.left;
                while (predecessor.right != null && predecessor.right != current) {
                    predecessor = predecessor.right;
                }
                if (predecessor.right == null) {
                    predecessor.right = current;
                    current = current.left;
                } else {
                    predecessor.right = null;
                    order.add(current.value);
                    current = current.right;
                }
            }
        }
        return order;
    }

    // ---------------------------------------------------------------------
    // Dynamic programming: save smaller answers and reuse them.
    // ---------------------------------------------------------------------

    public static int zeroOneKnapsack(int[] weights, int[] values, int capacity) {
        if (weights.length != values.length || capacity < 0) {
            throw new IllegalArgumentException("Invalid knapsack input");
        }
        // best[c] is the highest value possible with capacity c.
        int[] best = new int[capacity + 1];
        for (int item = 0; item < weights.length; item++) {
            if (weights[item] <= 0) {
                throw new IllegalArgumentException("Weights must be positive");
            }
            for (int current = capacity; current >= weights[item]; current--) {
                best[current] = Math.max(best[current],
                        best[current - weights[item]] + values[item]);
            }
        }
        return best[capacity];
    }

    public static int longestCommonSubsequence(String first, String second) {
        // length[i][j] is the answer for the first i and j characters.
        int[][] length = new int[first.length() + 1][second.length() + 1];
        for (int i = 1; i <= first.length(); i++) {
            for (int j = 1; j <= second.length(); j++) {
                if (first.charAt(i - 1) == second.charAt(j - 1)) {
                    length[i][j] = length[i - 1][j - 1] + 1;
                } else {
                    length[i][j] = Math.max(length[i - 1][j], length[i][j - 1]);
                }
            }
        }
        return length[first.length()][second.length()];
    }

    /**
     * Returns the length of the longest strictly increasing subsequence.
     * {@code tails[i]} stores the smallest ending value found for a sequence
     * of length {@code i + 1}. Time: O(n log n).
     */
    public static int longestIncreasingSubsequence(int[] values) {
        int[] tails = new int[values.length];
        int size = 0;
        for (int value : values) {
            int left = 0;
            int right = size;
            while (left < right) {
                int middle = left + (right - left) / 2;
                if (tails[middle] < value) {
                    left = middle + 1;
                } else {
                    right = middle;
                }
            }
            tails[left] = value;
            if (left == size) {
                size++;
            }
        }
        return size;
    }

    public static long matrixChainMultiplication(int[] dimensions) {
        if (dimensions.length < 2) {
            return 0;
        }
        int matrices = dimensions.length - 1;
        // cost[left][right] stores the cheapest way to multiply that range.
        long[][] cost = new long[matrices][matrices];
        for (int length = 2; length <= matrices; length++) {
            for (int left = 0; left + length <= matrices; left++) {
                int right = left + length - 1;
                cost[left][right] = INF;
                for (int split = left; split < right; split++) {
                    long candidate = cost[left][split] + cost[split + 1][right]
                            + (long) dimensions[left] * dimensions[split + 1]
                            * dimensions[right + 1];
                    cost[left][right] = Math.min(cost[left][right], candidate);
                }
            }
        }
        return cost[0][matrices - 1];
    }

    // ---------------------------------------------------------------------
    // Greedy algorithms: choose the best-looking option at each step.
    // ---------------------------------------------------------------------

    public static final class Activity {
        public final String name;
        public final int start;
        public final int finish;

        public Activity(String name, int start, int finish) {
            this.name = name;
            this.start = start;
            this.finish = finish;
        }
    }

    public static List<Activity> activitySelection(List<Activity> activities) {
        // Finishing early leaves the most room for later activities.
        List<Activity> sorted = new ArrayList<>(activities);
        sorted.sort(Comparator.comparingInt(activity -> activity.finish));
        List<Activity> selected = new ArrayList<>();
        int lastFinish = Integer.MIN_VALUE;
        for (Activity activity : sorted) {
            if (activity.start >= lastFinish) {
                selected.add(activity);
                lastFinish = activity.finish;
            }
        }
        return selected;
    }

    /**
     * Builds short binary codes for frequent characters and longer codes for
     * rare characters. No result code is the prefix of another result code.
     */
    public static Map<Character, String> huffmanCodes(Map<Character, Integer> frequencies) {
        PriorityQueue<HuffmanNode> queue = new PriorityQueue<>(
                Comparator.comparingInt(node -> node.frequency));
        for (Map.Entry<Character, Integer> entry : frequencies.entrySet()) {
            if (entry.getValue() <= 0) {
                throw new IllegalArgumentException("Frequencies must be positive");
            }
            queue.offer(new HuffmanNode(entry.getKey(), entry.getValue(), null, null));
        }
        if (queue.isEmpty()) {
            return Collections.emptyMap();
        }
        while (queue.size() > 1) {
            HuffmanNode left = queue.poll();
            HuffmanNode right = queue.poll();
            queue.offer(new HuffmanNode(null, left.frequency + right.frequency, left, right));
        }
        Map<Character, String> codes = new HashMap<>();
        buildHuffmanCodes(queue.poll(), "", codes);
        return codes;
    }

    private static void buildHuffmanCodes(HuffmanNode node, String prefix,
                                          Map<Character, String> codes) {
        if (node.character != null) {
            codes.put(node.character, prefix.isEmpty() ? "0" : prefix);
            return;
        }
        buildHuffmanCodes(node.left, prefix + '0', codes);
        buildHuffmanCodes(node.right, prefix + '1', codes);
    }

    // ---------------------------------------------------------------------
    // Backtracking: try a choice, undo it if needed, and try another choice.
    // ---------------------------------------------------------------------

    /**
     * Places queens so none share a row, column, or diagonal.
     * Each solution stores one chosen column for every row.
     */
    public static List<List<Integer>> solveNQueens(int size) {
        List<List<Integer>> solutions = new ArrayList<>();
        if (size < 0) {
            return solutions;
        }
        int[] columns = new int[size];
        boolean[] usedColumns = new boolean[size];
        boolean[] descendingDiagonal = new boolean[Math.max(0, 2 * size - 1)];
        boolean[] ascendingDiagonal = new boolean[Math.max(0, 2 * size - 1)];
        solveNQueens(0, columns, usedColumns, descendingDiagonal,
                ascendingDiagonal, solutions);
        return solutions;
    }

    private static void solveNQueens(int row, int[] columns, boolean[] usedColumns,
                                     boolean[] descendingDiagonal,
                                     boolean[] ascendingDiagonal,
                                     List<List<Integer>> solutions) {
        int size = columns.length;
        if (row == size) {
            List<Integer> solution = new ArrayList<>(size);
            for (int column : columns) {
                solution.add(column);
            }
            solutions.add(solution);
            return;
        }
        for (int column = 0; column < size; column++) {
            int descending = row - column + size - 1;
            int ascending = row + column;
            if (!usedColumns[column] && !descendingDiagonal[descending]
                    && !ascendingDiagonal[ascending]) {
                // Choose this square.
                columns[row] = column;
                usedColumns[column] = true;
                descendingDiagonal[descending] = true;
                ascendingDiagonal[ascending] = true;
                solveNQueens(row + 1, columns, usedColumns, descendingDiagonal,
                        ascendingDiagonal, solutions);

                // Undo the choice before trying the next column.
                usedColumns[column] = false;
                descendingDiagonal[descending] = false;
                ascendingDiagonal[ascending] = false;
            }
        }
    }

    /**
     * Solves a 9-by-9 Sudoku board in place; zero means an empty cell.
     * For the first empty cell, try each valid number and backtrack on failure.
     */
    public static boolean solveSudoku(int[][] board) {
        validateSudokuBoard(board);
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                if (board[row][column] == 0) {
                    for (int value = 1; value <= 9; value++) {
                        if (isValidSudokuValue(board, row, column, value)) {
                            board[row][column] = value;
                            if (solveSudoku(board)) {
                                return true;
                            }
                            board[row][column] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isValidSudokuValue(int[][] board, int row, int column, int value) {
        for (int index = 0; index < 9; index++) {
            if (board[row][index] == value || board[index][column] == value) {
                return false;
            }
        }
        int boxRow = row - row % 3;
        int boxColumn = column - column % 3;
        for (int currentRow = boxRow; currentRow < boxRow + 3; currentRow++) {
            for (int currentColumn = boxColumn; currentColumn < boxColumn + 3; currentColumn++) {
                if (board[currentRow][currentColumn] == value) {
                    return false;
                }
            }
        }
        return true;
    }

    private static void validateSudokuBoard(int[][] board) {
        if (board.length != 9) {
            throw new IllegalArgumentException("Sudoku board must have 9 rows");
        }
        for (int[] row : board) {
            if (row.length != 9) {
                throw new IllegalArgumentException("Sudoku board must have 9 columns");
            }
        }
    }

    // ---------------------------------------------------------------------
    // Caches: keep recently or frequently used data ready for quick access.
    // ---------------------------------------------------------------------

    /**
     * Removes the item that has gone unused for the longest time.
     * LinkedHashMap already maintains the required access order.
     */
    public static final class LruCache<K, V> extends LinkedHashMap<K, V> {
        private final int capacity;

        public LruCache(int capacity) {
            super(validateCapacity(capacity), 0.75f, true);
            this.capacity = capacity;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return size() > capacity;
        }
    }

    /**
     * Removes the least frequently accessed item. If frequencies tie, it
     * removes the least recently used item among them. Get and put are O(1).
     */
    public static final class LfuCache<K, V> {
        private final int capacity;
        private int minimumFrequency;
        private final Map<K, LfuEntry<V>> entries = new HashMap<>();
        private final Map<Integer, LinkedHashSet<K>> keysByFrequency = new HashMap<>();

        public LfuCache(int capacity) {
            this.capacity = validateCapacity(capacity);
        }

        public V get(K key) {
            LfuEntry<V> entry = entries.get(key);
            if (entry == null) {
                return null;
            }
            increaseFrequency(key, entry);
            return entry.value;
        }

        public void put(K key, V value) {
            LfuEntry<V> existing = entries.get(key);
            if (existing != null) {
                existing.value = value;
                increaseFrequency(key, existing);
                return;
            }
            if (entries.size() == capacity) {
                LinkedHashSet<K> leastUsed = keysByFrequency.get(minimumFrequency);
                K evicted = leastUsed.iterator().next();
                leastUsed.remove(evicted);
                entries.remove(evicted);
                if (leastUsed.isEmpty()) {
                    keysByFrequency.remove(minimumFrequency);
                }
            }
            entries.put(key, new LfuEntry<>(value));
            keysByFrequency.computeIfAbsent(1, ignored -> new LinkedHashSet<>()).add(key);
            minimumFrequency = 1;
        }

        private void increaseFrequency(K key, LfuEntry<V> entry) {
            LinkedHashSet<K> currentKeys = keysByFrequency.get(entry.frequency);
            currentKeys.remove(key);
            if (currentKeys.isEmpty()) {
                keysByFrequency.remove(entry.frequency);
                if (minimumFrequency == entry.frequency) {
                    minimumFrequency++;
                }
            }
            entry.frequency++;
            keysByFrequency.computeIfAbsent(entry.frequency,
                    ignored -> new LinkedHashSet<>()).add(key);
        }
    }

    // ---------------------------------------------------------------------
    // Bit manipulation: work directly with the binary form of an integer.
    // ---------------------------------------------------------------------

    /**
     * Counts the 1s in an integer's binary representation.
     * {@code value & (value - 1)} removes the lowest remaining 1 each time.
     */
    public static int countSetBits(int value) {
        int count = 0;
        while (value != 0) {
            value &= value - 1;
            count++;
        }
        return count;
    }

    // ---------------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------------

    private static List<Edge> directedEdges(WeightedGraph graph) {
        List<Edge> edges = new ArrayList<>();
        for (List<Edge> adjacent : graph.adjacency) {
            edges.addAll(adjacent);
        }
        return edges;
    }

    private static void swap(int[] values, int first, int second) {
        int temporary = values[first];
        values[first] = values[second];
        values[second] = temporary;
    }

    private static void requireNonEmpty(int[] values) {
        if (values.length == 0) {
            throw new IllegalArgumentException("Array must not be empty");
        }
    }

    private static int validateCapacity(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        return capacity;
    }

    private static final class VertexDistance {
        private final int vertex;
        private final long distance;

        private VertexDistance(int vertex, long distance) {
            this.vertex = vertex;
            this.distance = distance;
        }
    }

    private static final class HuffmanNode {
        private final Character character;
        private final int frequency;
        private final HuffmanNode left;
        private final HuffmanNode right;

        private HuffmanNode(Character character, int frequency,
                            HuffmanNode left, HuffmanNode right) {
            this.character = character;
            this.frequency = frequency;
            this.left = left;
            this.right = right;
        }
    }

    private static final class LfuEntry<V> {
        private V value;
        private int frequency = 1;

        private LfuEntry(V value) {
            this.value = value;
        }
    }
}
