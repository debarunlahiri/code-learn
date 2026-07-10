import java.util.*;

/**
 * RealLifeGraphAlgorithms.java
 *
 * A single, self-contained Java file that demonstrates graph algorithms
 * through real-world scenarios. It includes an adjacency-list graph, weighted
 * and unweighted traversals, shortest-path algorithms, cycle detection and
 * topological sorting.
 *
 * Real-life examples covered:
 *   1. Social media friend recommendations and mutual friends (BFS / DFS).
 *   2. Road networks and GPS shortest path (Dijkstra's algorithm).
 *   3. Network routing and minimum hop count (BFS shortest path).
 *   4. Dependency management and build order (Topological sort / Kahn's algo).
 */
public final class RealLifeGraphAlgorithms {

    private RealLifeGraphAlgorithms() {
        // utility class
    }

    public static void main(String[] args) {
        System.out.println("===== 1. Social Media Friends (BFS Recommendations & Mutuals) =====");
        runSocialMediaDemo();

        System.out.println("\n===== 2. Road Networks (Dijkstra's Shortest Path) =====");
        runRoadNetworkDemo();

        System.out.println("\n===== 3. Network Routing (Minimum Hop Path) =====");
        runNetworkRoutingDemo();

        System.out.println("\n===== 4. Dependency Management (Topological Build Order) =====");
        runDependencyManagementDemo();
    }

    /* =============================================================
       1. SOCIAL MEDIA FRIENDS
       ============================================================= */

    private static void runSocialMediaDemo() {
        SocialNetwork network = new SocialNetwork();
        network.addFriendship("Alice", "Bob");
        network.addFriendship("Alice", "Carol");
        network.addFriendship("Alice", "David");
        network.addFriendship("Bob", "Eve");
        network.addFriendship("Carol", "Eve");
        network.addFriendship("Carol", "Frank");
        network.addFriendship("David", "Frank");
        network.addFriendship("Eve", "Grace");

        System.out.println("Network adjacency list:");
        network.print();

        String user = "Alice";
        System.out.println("\nFriend recommendations for " + user + " (friends of friends, BFS):");
        List<String> recommendations = network.recommendFriends(user);
        for (String rec : recommendations) {
            System.out.println("  - " + rec);
        }

        String personA = "Alice";
        String personB = "Eve";
        System.out.println("\nMutual friends of " + personA + " and " + personB + ":");
        Set<String> mutuals = network.mutualFriends(personA, personB);
        if (mutuals.isEmpty()) {
            System.out.println("  No mutual friends.");
        } else {
            for (String mutual : mutuals) {
                System.out.println("  - " + mutual);
            }
        }
    }

    /**
     * Undirected graph representing friendships in a social network.
     */
    private static final class SocialNetwork {
        private final Map<String, Set<String>> adjacency = new HashMap<>();

        void addFriendship(String a, String b) {
            adjacency.computeIfAbsent(a, k -> new HashSet<>()).add(b);
            adjacency.computeIfAbsent(b, k -> new HashSet<>()).add(a);
        }

        List<String> recommendFriends(String user) {
            List<String> result = new ArrayList<>();
            if (!adjacency.containsKey(user)) {
                return result;
            }

            Set<String> directFriends = adjacency.get(user);
            Set<String> visited = new HashSet<>();
            visited.add(user);

            // Use BFS to discover people at distance 2 who are not already friends.
            ArrayDeque<Pair<String, Integer>> queue = new ArrayDeque<>();
            queue.offer(new Pair<>(user, 0));

            while (!queue.isEmpty()) {
                Pair<String, Integer> current = queue.poll();
                String person = current.first;
                int distance = current.second;

                if (distance == 2 && !directFriends.contains(person) && !person.equals(user)) {
                    result.add(person);
                }

                if (distance < 2) {
                    for (String friend : adjacency.getOrDefault(person, Collections.emptySet())) {
                        if (!visited.contains(friend)) {
                            visited.add(friend);
                            queue.offer(new Pair<>(friend, distance + 1));
                        }
                    }
                }
            }
            return result;
        }

        Set<String> mutualFriends(String a, String b) {
            Set<String> friendsA = adjacency.getOrDefault(a, Collections.emptySet());
            Set<String> friendsB = adjacency.getOrDefault(b, Collections.emptySet());
            Set<String> mutual = new HashSet<>(friendsA);
            mutual.retainAll(friendsB);
            return mutual;
        }

        void print() {
            for (Map.Entry<String, Set<String>> entry : adjacency.entrySet()) {
                System.out.println("  " + entry.getKey() + " -> " + entry.getValue());
            }
        }
    }

    /* =============================================================
       2. ROAD NETWORKS (DIJKSTRA'S SHORTEST PATH)
       ============================================================= */

    private static void runRoadNetworkDemo() {
        RoadNetwork roads = new RoadNetwork();
        roads.addRoad("Home", "A", 4);
        roads.addRoad("Home", "B", 2);
        roads.addRoad("A", "C", 5);
        roads.addRoad("B", "A", 1);
        roads.addRoad("B", "C", 8);
        roads.addRoad("B", "D", 10);
        roads.addRoad("C", "D", 2);
        roads.addRoad("D", "Mall", 6);
        roads.addRoad("C", "Mall", 4);

        String start = "Home";
        String destination = "Mall";
        RoadNetwork.PathResult result = roads.shortestPath(start, destination);

        System.out.println("Shortest drive from " + start + " to " + destination + ":");
        if (result.distance == Integer.MAX_VALUE) {
            System.out.println("  No route exists.");
        } else {
            System.out.println("  Route: " + String.join(" -> ", result.path));
            System.out.println("  Total distance: " + result.distance + " km");
        }
    }

    /**
     * Weighted directed graph that models roads with distances. Dijkstra's
     * algorithm finds the shortest travel time/distance for GPS navigation.
     */
    private static final class RoadNetwork {
        private final Map<String, List<Edge>> graph = new HashMap<>();

        void addRoad(String from, String to, int distance) {
            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(new Edge(to, distance));
        }

        PathResult shortestPath(String start, String destination) {
            Map<String, Integer> distances = new HashMap<>();
            Map<String, String> parent = new HashMap<>();
            PriorityQueue<NodeDistance> minHeap = new PriorityQueue<>(
                    Comparator.comparingInt(nd -> nd.distance));

            for (String city : graph.keySet()) {
                distances.put(city, Integer.MAX_VALUE);
            }
            distances.put(start, 0);
            minHeap.offer(new NodeDistance(start, 0));

            while (!minHeap.isEmpty()) {
                NodeDistance current = minHeap.poll();
                String city = current.city;
                int dist = current.distance;

                if (dist > distances.get(city)) {
                    continue; // stale entry
                }

                if (city.equals(destination)) {
                    break; // destination reached with optimal distance
                }

                for (Edge edge : graph.getOrDefault(city, Collections.emptyList())) {
                    int newDist = dist + edge.weight;
                    if (newDist < distances.getOrDefault(edge.to, Integer.MAX_VALUE)) {
                        distances.put(edge.to, newDist);
                        parent.put(edge.to, city);
                        minHeap.offer(new NodeDistance(edge.to, newDist));
                    }
                }
            }

            List<String> path = reconstructPath(parent, start, destination);
            int totalDistance = distances.getOrDefault(destination, Integer.MAX_VALUE);
            return new PathResult(path, totalDistance);
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

        private static final class Edge {
            final String to;
            final int weight;

            Edge(String to, int weight) {
                this.to = to;
                this.weight = weight;
            }
        }

        private static final class NodeDistance {
            final String city;
            final int distance;

            NodeDistance(String city, int distance) {
                this.city = city;
                this.distance = distance;
            }
        }

        private static final class PathResult {
            final List<String> path;
            final int distance;

            PathResult(List<String> path, int distance) {
                this.path = path;
                this.distance = distance;
            }
        }
    }

    /* =============================================================
       3. NETWORK ROUTING (MINIMUM HOP PATH)
       ============================================================= */

    private static void runNetworkRoutingDemo() {
        ComputerNetwork network = new ComputerNetwork();
        network.addLink("RouterA", "RouterB");
        network.addLink("RouterA", "RouterC");
        network.addLink("RouterB", "RouterD");
        network.addLink("RouterC", "RouterD");
        network.addLink("RouterD", "RouterE");
        network.addLink("RouterC", "RouterE");

        String source = "RouterA";
        String target = "RouterE";
        List<String> route = network.minimumHopRoute(source, target);

        System.out.println("Network topology:");
        network.print();
        System.out.println("\nMinimum-hop route from " + source + " to " + target + ":");
        if (route.isEmpty()) {
            System.out.println("  No route found.");
        } else {
            System.out.println("  " + String.join(" -> ", route));
            System.out.println("  Hops: " + (route.size() - 1));
        }
    }

    /**
     * Models a computer network as an unweighted graph. Routers use BFS to
     * discover the path with the fewest hops, similar to how RIP (Routing
     * Information Protocol) works at a conceptual level.
     */
    private static final class ComputerNetwork {
        private final Map<String, List<String>> links = new HashMap<>();

        void addLink(String a, String b) {
            links.computeIfAbsent(a, k -> new ArrayList<>()).add(b);
            links.computeIfAbsent(b, k -> new ArrayList<>()).add(a);
        }

        List<String> minimumHopRoute(String source, String target) {
            if (!links.containsKey(source) || !links.containsKey(target)) {
                return Collections.emptyList();
            }

            ArrayDeque<String> queue = new ArrayDeque<>();
            Set<String> visited = new HashSet<>();
            Map<String, String> parent = new HashMap<>();

            queue.offer(source);
            visited.add(source);

            while (!queue.isEmpty()) {
                String router = queue.poll();
                if (router.equals(target)) {
                    return reconstructRoute(parent, source, target);
                }

                for (String neighbor : links.getOrDefault(router, Collections.emptyList())) {
                    if (!visited.contains(neighbor)) {
                        visited.add(neighbor);
                        parent.put(neighbor, router);
                        queue.offer(neighbor);
                    }
                }
            }
            return Collections.emptyList();
        }

        private List<String> reconstructRoute(Map<String, String> parent,
                                              String source, String target) {
            LinkedList<String> route = new LinkedList<>();
            String current = target;
            while (current != null) {
                route.addFirst(current);
                if (current.equals(source)) {
                    break;
                }
                current = parent.get(current);
            }
            return route;
        }

        void print() {
            for (Map.Entry<String, List<String>> entry : links.entrySet()) {
                System.out.println("  " + entry.getKey() + " -> " + entry.getValue());
            }
        }
    }

    /* =============================================================
       4. DEPENDENCY MANAGEMENT (TOPOLOGICAL SORT / KAHN'S ALGORITHM)
       ============================================================= */

    private static void runDependencyManagementDemo() {
        ProjectBuild build = new ProjectBuild();
        build.addModule("utils");
        build.addModule("logging");
        build.addModule("database");
        build.addModule("auth");
        build.addModule("api");
        build.addModule("frontend");

        build.addDependency("database", "logging");
        build.addDependency("auth", "database");
        build.addDependency("auth", "utils");
        build.addDependency("api", "auth");
        build.addDependency("api", "utils");
        build.addDependency("frontend", "api");
        build.addDependency("frontend", "logging");

        System.out.println("Module dependencies:");
        build.printDependencies();

        List<String> buildOrder = build.getBuildOrder();
        System.out.println("\nValid build order (topological sort):");
        if (buildOrder.isEmpty()) {
            System.out.println("  Cycle detected! Cannot determine build order.");
        } else {
            System.out.println("  " + String.join(" -> ", buildOrder));
        }
    }

    /**
     * Directed acyclic graph (DAG) of build modules. Kahn's algorithm performs
     * a topological sort by repeatedly removing nodes with zero in-degree. This
     * is the same logic used by Maven, Gradle, npm and package managers.
     */
    private static final class ProjectBuild {
        private final Map<String, List<String>> adjacency = new HashMap<>();
        private final Map<String, Integer> inDegree = new HashMap<>();

        void addModule(String module) {
            adjacency.putIfAbsent(module, new ArrayList<>());
            inDegree.putIfAbsent(module, 0);
        }

        void addDependency(String module, String dependency) {
            addModule(module);
            addModule(dependency);
            adjacency.get(dependency).add(module); // dependency -> module
            inDegree.put(module, inDegree.get(module) + 1);
        }

        List<String> getBuildOrder() {
            ArrayDeque<String> queue = new ArrayDeque<>();
            Map<String, Integer> tempInDegree = new HashMap<>(inDegree);

            for (Map.Entry<String, Integer> entry : tempInDegree.entrySet()) {
                if (entry.getValue() == 0) {
                    queue.offer(entry.getKey());
                }
            }

            List<String> order = new ArrayList<>();
            while (!queue.isEmpty()) {
                String module = queue.poll();
                order.add(module);

                for (String dependent : adjacency.get(module)) {
                    int newDegree = tempInDegree.get(dependent) - 1;
                    tempInDegree.put(dependent, newDegree);
                    if (newDegree == 0) {
                        queue.offer(dependent);
                    }
                }
            }

            // If not all modules were processed, a cycle exists.
            if (order.size() != adjacency.size()) {
                return Collections.emptyList();
            }
            return order;
        }

        void printDependencies() {
            for (String module : adjacency.keySet()) {
                List<String> dependents = adjacency.get(module);
                if (dependents.isEmpty()) {
                    System.out.println("  " + module + " has no dependents");
                } else {
                    System.out.println("  " + module + " is required by: " + dependents);
                }
            }
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
