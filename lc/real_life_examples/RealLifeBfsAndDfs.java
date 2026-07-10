import java.util.*;

/**
 * RealLifeBfsAndDfs.java
 *
 * A single, self-contained Java file that demonstrates Breadth-First Search (BFS)
 * and Depth-First Search (DFS) using real-life scenarios. It also contains the
 * underlying data structures (Graph, Node, Queue and Stack helpers) so that the
 * code is easy to read, run and extend.
 *
 * Real-life examples covered:
 *   1. Social network friend recommendations (BFS level-order traversal).
 *   2. File-system directory scanner (DFS pre-order traversal).
 *   3. GPS shortest path in an unweighted city map (BFS shortest path).
 *   4. Web crawler that follows links up to a depth (DFS iterative).
 *   5. Maze escape solver (BFS shortest route in a 2-D grid).
 */
public final class RealLifeBfsAndDfs {

    private RealLifeBfsAndDfs() {
        // utility class
    }

    public static void main(String[] args) {
        System.out.println("===== 1. Social Network Friend Recommendations (BFS) =====");
        runSocialNetworkDemo();

        System.out.println("\n===== 2. File System Directory Scanner (DFS) =====");
        runFileSystemDemo();

        System.out.println("\n===== 3. GPS Shortest Path in Unweighted City Map (BFS) =====");
        runGpsShortestPathDemo();

        System.out.println("\n===== 4. Web Crawler Up to Max Depth (DFS Iterative) =====");
        runWebCrawlerDemo();

        System.out.println("\n===== 5. Maze Escape Solver (BFS Shortest Route) =====");
        runMazeSolverDemo();
    }

    /* =============================================================
       1. SOCIAL NETWORK FRIEND RECOMMENDATIONS (BFS)
       ============================================================= */

    private static void runSocialNetworkDemo() {
        SocialNetworkGraph network = new SocialNetworkGraph();
        network.addFriendship("Alice", "Bob");
        network.addFriendship("Alice", "Carol");
        network.addFriendship("Bob", "David");
        network.addFriendship("Carol", "Eve");
        network.addFriendship("David", "Frank");
        network.addFriendship("Eve", "Frank");

        System.out.println("Network adjacency list:");
        network.print();

        String source = "Alice";
        int maxDepth = 2;
        System.out.println("\nPeople reachable from " + source + " within " + maxDepth + " friend hops (BFS):");
        List<String> recommendations = network.findFriendsWithinHops(source, maxDepth);
        for (String person : recommendations) {
            System.out.println("  - " + person);
        }
    }

    /**
     * Undirected graph that models friendships in a social network.
     */
    private static final class SocialNetworkGraph {
        private final Map<String, List<String>> adjacency = new HashMap<>();

        void addFriendship(String a, String b) {
            adjacency.computeIfAbsent(a, k -> new ArrayList<>()).add(b);
            adjacency.computeIfAbsent(b, k -> new ArrayList<>()).add(a);
        }

        List<String> findFriendsWithinHops(String source, int maxDepth) {
            List<String> result = new ArrayList<>();
            if (!adjacency.containsKey(source)) {
                return result;
            }

            // BFS queue stores (person, currentHop)
            ArrayDeque<Pair<String, Integer>> queue = new ArrayDeque<>();
            Set<String> visited = new HashSet<>();
            queue.offer(new Pair<>(source, 0));
            visited.add(source);

            while (!queue.isEmpty()) {
                Pair<String, Integer> current = queue.poll();
                String person = current.first;
                int hops = current.second;

                // Exclude the source person herself from recommendations.
                if (!person.equals(source)) {
                    result.add(person + " (distance " + hops + ")");
                }

                if (hops < maxDepth) {
                    for (String friend : adjacency.getOrDefault(person, Collections.emptyList())) {
                        if (!visited.contains(friend)) {
                            visited.add(friend);
                            queue.offer(new Pair<>(friend, hops + 1));
                        }
                    }
                }
            }
            return result;
        }

        void print() {
            for (Map.Entry<String, List<String>> entry : adjacency.entrySet()) {
                System.out.println("  " + entry.getKey() + " -> " + entry.getValue());
            }
        }
    }

    /* =============================================================
       2. FILE SYSTEM DIRECTORY SCANNER (DFS)
       ============================================================= */

    private static void runFileSystemDemo() {
        FileNode root = new FileNode("/home", true);
        FileNode documents = new FileNode("documents", true);
        FileNode pictures = new FileNode("pictures", true);
        FileNode cv = new FileNode("cv.pdf", false);
        FileNode notes = new FileNode("notes.txt", false);
        FileNode selfie = new FileNode("selfie.jpg", false);

        root.addChild(documents);
        root.addChild(pictures);
        documents.addChild(cv);
        documents.addChild(notes);
        pictures.addChild(selfie);

        System.out.println("DFS pre-order listing of the file system:");
        FileSystemScanner scanner = new FileSystemScanner();
        List<String> files = scanner.scanDfs(root);
        for (String path : files) {
            System.out.println("  " + path);
        }
    }

    /**
     * Tree node that represents a file or a directory.
     */
    private static final class FileNode {
        final String name;
        final boolean isDirectory;
        final List<FileNode> children = new ArrayList<>();

        FileNode(String name, boolean isDirectory) {
            this.name = name;
            this.isDirectory = isDirectory;
        }

        void addChild(FileNode child) {
            children.add(child);
        }
    }

    /**
     * Scanner that walks a directory tree using iterative DFS with an explicit
     * stack. This is the same pattern used by tools such as `find`, IDE file
     * explorers and disk-usage analyzers.
     */
    private static final class FileSystemScanner {
        List<String> scanDfs(FileNode root) {
            List<String> result = new ArrayList<>();
            if (root == null) {
                return result;
            }

            Deque<Pair<FileNode, String>> stack = new ArrayDeque<>();
            stack.push(new Pair<>(root, root.name));

            while (!stack.isEmpty()) {
                Pair<FileNode, String> pair = stack.pop();
                FileNode node = pair.first;
                String path = pair.second;

                result.add(path + (node.isDirectory ? "/" : ""));

                // Push children in reverse order so that the first child is
                // processed first (classic DFS left-to-right ordering).
                List<FileNode> kids = node.children;
                for (int i = kids.size() - 1; i >= 0; i--) {
                    FileNode child = kids.get(i);
                    stack.push(new Pair<>(child, path + "/" + child.name));
                }
            }
            return result;
        }
    }

    /* =============================================================
       3. GPS SHORTEST PATH IN UNWEIGHTED CITY MAP (BFS)
       ============================================================= */

    private static void runGpsShortestPathDemo() {
        CityMap city = new CityMap();
        city.addRoad("A", "B");
        city.addRoad("A", "C");
        city.addRoad("B", "D");
        city.addRoad("C", "D");
        city.addRoad("D", "E");
        city.addRoad("C", "E");

        String start = "A";
        String destination = "E";
        List<String> path = city.shortestPath(start, destination);

        System.out.println("Shortest route from " + start + " to " + destination + ":");
        if (path.isEmpty()) {
            System.out.println("  No route found.");
        } else {
            System.out.println("  " + String.join(" -> ", path));
        }
    }

    /**
     * Models an unweighted road network. BFS naturally discovers the shortest
     * path in terms of number of edges because it expands all nodes at distance
     * k before any node at distance k + 1.
     */
    private static final class CityMap {
        private final Map<String, List<String>> roads = new HashMap<>();

        void addRoad(String a, String b) {
            roads.computeIfAbsent(a, k -> new ArrayList<>()).add(b);
            roads.computeIfAbsent(b, k -> new ArrayList<>()).add(a);
        }

        List<String> shortestPath(String start, String destination) {
            if (!roads.containsKey(start) || !roads.containsKey(destination)) {
                return Collections.emptyList();
            }

            ArrayDeque<String> queue = new ArrayDeque<>();
            Set<String> visited = new HashSet<>();
            Map<String, String> parent = new HashMap<>();

            queue.offer(start);
            visited.add(start);

            while (!queue.isEmpty()) {
                String city = queue.poll();
                if (city.equals(destination)) {
                    return reconstructPath(parent, start, destination);
                }

                for (String neighbor : roads.getOrDefault(city, Collections.emptyList())) {
                    if (!visited.contains(neighbor)) {
                        visited.add(neighbor);
                        parent.put(neighbor, city);
                        queue.offer(neighbor);
                    }
                }
            }
            return Collections.emptyList(); // destination unreachable
        }

        private List<String> reconstructPath(Map<String, String> parent,
                                             String start, String destination) {
            LinkedList<String> path = new LinkedList<>();
            String current = destination;
            while (current != null) {
                path.addFirst(current);
                if (current.equals(start)) {
                    break;
                }
                current = parent.get(current);
            }
            return path;
        }
    }

    /* =============================================================
       4. WEB CRAWLER UP TO MAX DEPTH (DFS ITERATIVE)
       ============================================================= */

    private static void runWebCrawlerDemo() {
        // Simulated internet graph.
        WebGraph web = new WebGraph();
        web.addLink("https://example.com", "https://example.com/about");
        web.addLink("https://example.com", "https://example.com/products");
        web.addLink("https://example.com/products", "https://example.com/products/laptop");
        web.addLink("https://example.com/products", "https://example.com/products/phone");
        web.addLink("https://example.com/about", "https://example.com/contact");
        web.addLink("https://example.com/contact", "https://example.com/team");

        WebCrawler crawler = new WebCrawler();
        Set<String> visited = crawler.crawl(web, "https://example.com", 2);

        System.out.println("URLs visited by the crawler (max depth 2):");
        for (String url : visited) {
            System.out.println("  " + url);
        }
    }

    /**
     * Simulates the World Wide Web as a directed graph of pages.
     */
    private static final class WebGraph {
        private final Map<String, List<String>> links = new HashMap<>();

        void addLink(String from, String to) {
            links.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
        }

        List<String> getLinks(String page) {
            return links.getOrDefault(page, Collections.emptyList());
        }
    }

    /**
     * Iterative DFS web crawler. Each stack frame stores the URL plus the
     * current depth. Real crawlers also respect robots.txt, politeness delays
     * and content deduplication, but the traversal skeleton is identical.
     */
    private static final class WebCrawler {
        Set<String> crawl(WebGraph graph, String startUrl, int maxDepth) {
            Set<String> visited = new HashSet<>();
            Deque<Pair<String, Integer>> stack = new ArrayDeque<>();
            stack.push(new Pair<>(startUrl, 0));

            while (!stack.isEmpty()) {
                Pair<String, Integer> current = stack.pop();
                String url = current.first;
                int depth = current.second;

                if (visited.contains(url) || depth > maxDepth) {
                    continue;
                }
                visited.add(url);

                for (String nextUrl : graph.getLinks(url)) {
                    if (!visited.contains(nextUrl)) {
                        stack.push(new Pair<>(nextUrl, depth + 1));
                    }
                }
            }
            return visited;
        }
    }

    /* =============================================================
       5. MAZE ESCAPE SOLVER (BFS SHORTEST ROUTE IN 2-D GRID)
       ============================================================= */

    private static void runMazeSolverDemo() {
        // 0 = open path, 1 = wall
        int[][] maze = {
                {0, 1, 0, 0, 0},
                {0, 1, 0, 1, 0},
                {0, 0, 0, 1, 0},
                {1, 1, 0, 1, 0},
                {0, 0, 0, 0, 0}
        };

        MazeSolver solver = new MazeSolver();
        List<Cell> route = solver.shortestRoute(maze, new Cell(0, 0), new Cell(4, 4));

        System.out.println("Maze:");
        for (int[] row : maze) {
            System.out.println("  " + Arrays.toString(row));
        }
        System.out.println("\nShortest escape route (row, col):");
        if (route.isEmpty()) {
            System.out.println("  No escape route found.");
        } else {
            for (Cell cell : route) {
                System.out.println("  (" + cell.row + ", " + cell.col + ")");
            }
            System.out.println("Total steps: " + (route.size() - 1));
        }
    }

    private static final class Cell {
        final int row;
        final int col;

        Cell(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Cell)) return false;
            Cell cell = (Cell) o;
            return row == cell.row && col == cell.col;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, col);
        }

        @Override
        public String toString() {
            return "(" + row + ", " + col + ")";
        }
    }

    private static final class MazeSolver {
        private static final int[][] DIRECTIONS = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1}
        };

        List<Cell> shortestRoute(int[][] maze, Cell start, Cell end) {
            int rows = maze.length;
            int cols = maze[0].length;

            if (maze[start.row][start.col] == 1 || maze[end.row][end.col] == 1) {
                return Collections.emptyList();
            }

            ArrayDeque<Cell> queue = new ArrayDeque<>();
            Set<Cell> visited = new HashSet<>();
            Map<Cell, Cell> parent = new HashMap<>();

            queue.offer(start);
            visited.add(start);

            while (!queue.isEmpty()) {
                Cell current = queue.poll();
                if (current.equals(end)) {
                    return reconstructRoute(parent, start, end);
                }

                for (int[] dir : DIRECTIONS) {
                    int newRow = current.row + dir[0];
                    int newCol = current.col + dir[1];
                    Cell next = new Cell(newRow, newCol);

                    if (isValid(maze, newRow, newCol, rows, cols) && !visited.contains(next)) {
                        visited.add(next);
                        parent.put(next, current);
                        queue.offer(next);
                    }
                }
            }
            return Collections.emptyList();
        }

        private boolean isValid(int[][] maze, int row, int col, int rows, int cols) {
            return row >= 0 && row < rows && col >= 0 && col < cols && maze[row][col] == 0;
        }

        private List<Cell> reconstructRoute(Map<Cell, Cell> parent, Cell start, Cell end) {
            LinkedList<Cell> route = new LinkedList<>();
            Cell current = end;
            while (current != null) {
                route.addFirst(current);
                if (current.equals(start)) {
                    break;
                }
                current = parent.get(current);
            }
            return route;
        }
    }

    /* =============================================================
       GENERIC HELPER
       ============================================================= */

    private static final class Pair<F, S> {
        final F first;
        final S second;

        Pair(F first, S second) {
            this.first = first;
            this.second = second;
        }
    }
}
