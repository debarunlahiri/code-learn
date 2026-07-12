import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Real-life map and navigation system using Dijkstra's shortest-path algorithm.
 *
 * <p>The map is stored as a weighted graph:</p>
 * <ul>
 *   <li>Each location is a vertex (node).</li>
 *   <li>Each road is an edge connecting two locations.</li>
 *   <li>The edge weight is travel time in minutes.</li>
 *   <li>When traffic is enabled, weight = normal time * traffic multiplier.</li>
 * </ul>
 *
 * <p>Features demonstrated:</p>
 * <ol>
 *   <li>Find the shortest route while ignoring traffic.</li>
 *   <li>Find the quickest route using current traffic conditions.</li>
 *   <li>Find routes from one origin to several destination endpoints.</li>
 *   <li>Return a useful result when a destination cannot be reached.</li>
 *   <li>Show a road-by-road explanation of the selected route.</li>
 * </ol>
 *
 * <p>Dijkstra's algorithm is suitable because every road has a positive travel
 * time. With an adjacency list and priority queue, its time complexity is
 * O((V + E) log V), where V is locations and E is roads.</p>
 */
public final class MAP {

    private MAP() {
        // utility class
    }

    public static void main(String[] args) {
        NavigationMap map = new NavigationMap();

        // Times are normal travel times in minutes. TrafficMultiplier describes
        // how much slower that road currently is (1.0 means no delay).
        map.addRoad("Home", "Market", 8, 1.0);
        map.addRoad("Home", "School", 6, 2.5);
        map.addRoad("Market", "Office", 7, 1.2);
        map.addRoad("School", "Office", 5, 2.0);
        map.addRoad("Market", "Hospital", 4, 1.0);
        map.addRoad("Hospital", "Office", 6, 1.0);
        map.addRoad("Office", "Airport", 12, 1.5);
        map.addRoad("Hospital", "Airport", 16, 1.0);

        System.out.println("===== Shortest route without traffic =====");
        printRoute(map.shortestRoute("Home", "Office", false));

        System.out.println("\n===== Shortest route with current traffic =====");
        printRoute(map.shortestRoute("Home", "Office", true));

        System.out.println("\n===== Multiple destination endpoints (with traffic) =====");
        List<String> destinations = Arrays.asList("Office", "Hospital", "Airport");
        Map<String, RouteResult> routes = map.shortestRoutes("Home", destinations, true);
        for (Map.Entry<String, RouteResult> entry : routes.entrySet()) {
            System.out.println("Destination: " + entry.getKey());
            printRoute(entry.getValue());
        }
    }

    private static void printRoute(RouteResult result) {
        if (!result.isReachable()) {
            System.out.println("  No route found from " + result.start
                    + " to " + result.destination + ".");
            return;
        }

        System.out.println("  Route: " + String.join(" -> ", result.path));
        System.out.println("  Traffic considered: " + (result.trafficIncluded ? "Yes" : "No"));
        System.out.println("  Road-by-road details:");
        for (RouteLeg leg : result.legs) {
            System.out.printf("    %s -> %s: normal %.1f min, traffic x%.1f, used %.1f min%n",
                    leg.from, leg.to, leg.normalMinutes, leg.trafficMultiplier,
                    leg.selectedTravelMinutes);
        }
        System.out.printf("  Normal route time: %.1f minutes%n", result.normalTravelTimeMinutes);
        if (result.trafficIncluded) {
            System.out.printf("  Traffic delay: +%.1f minutes%n",
                    result.travelTimeMinutes - result.normalTravelTimeMinutes);
        }
        System.out.printf("  Final travel time: %.1f minutes%n", result.travelTimeMinutes);
    }

    /** An undirected road network. */
    public static final class NavigationMap {
        private final Map<String, List<Road>> roads = new HashMap<>();

        /**
         * Adds a two-way road.
         *
         * @param normalMinutes time needed when there is no traffic
         * @param trafficMultiplier current slowdown; 1.0 means no traffic,
         *                          2.0 means the road takes twice as long
         */
        public void addRoad(String from, String to, double normalMinutes,
                            double trafficMultiplier) {
            if (from == null || to == null || from.trim().isEmpty() || to.trim().isEmpty()) {
                throw new IllegalArgumentException("Locations must not be blank.");
            }
            if (normalMinutes <= 0 || trafficMultiplier < 1.0) {
                throw new IllegalArgumentException(
                        "Travel time must be positive and traffic multiplier must be at least 1.0.");
            }

            roads.computeIfAbsent(from, key -> new ArrayList<>())
                    .add(new Road(to, normalMinutes, trafficMultiplier));
            roads.computeIfAbsent(to, key -> new ArrayList<>())
                    .add(new Road(from, normalMinutes, trafficMultiplier));
        }

        public RouteResult shortestRoute(String start, String destination,
                                         boolean includeTraffic) {
            Map<String, RouteResult> result = shortestRoutes(
                    start, Collections.singletonList(destination), includeTraffic);
            return result.get(destination);
        }

        /**
         * Finds the shortest route from one start location to every requested
         * endpoint. Dijkstra runs once, so this is more efficient than running a
         * separate search for each destination.
         */
        public Map<String, RouteResult> shortestRoutes(String start,
                                                       List<String> destinations,
                                                       boolean includeTraffic) {
            if (start == null || destinations == null) {
                throw new IllegalArgumentException("Start and destinations must not be null.");
            }

            Map<String, Double> distances = new HashMap<>();
            Map<String, String> previous = new HashMap<>();
            PriorityQueue<LocationTime> queue = new PriorityQueue<>(
                    Comparator.comparingDouble(location -> location.time));

            distances.put(start, 0.0);
            queue.offer(new LocationTime(start, 0.0));

            while (!queue.isEmpty()) {
                LocationTime current = queue.poll();
                if (current.time > distances.getOrDefault(current.location, Double.POSITIVE_INFINITY)) {
                    continue;
                }

                for (Road road : roads.getOrDefault(current.location, Collections.emptyList())) {
                    double roadTime = includeTraffic
                            ? road.normalMinutes * road.trafficMultiplier
                            : road.normalMinutes;
                    double candidateTime = current.time + roadTime;

                    if (candidateTime < distances.getOrDefault(road.to, Double.POSITIVE_INFINITY)) {
                        distances.put(road.to, candidateTime);
                        previous.put(road.to, current.location);
                        queue.offer(new LocationTime(road.to, candidateTime));
                    }
                }
            }

            Map<String, RouteResult> results = new LinkedHashMap<>();
            for (String destination : destinations) {
                double time = distances.getOrDefault(destination, Double.POSITIVE_INFINITY);
                List<String> path = reconstructPath(previous, start, destination, time);
                List<RouteLeg> legs = buildRouteLegs(path, includeTraffic);
                double normalTime = 0.0;
                for (RouteLeg leg : legs) {
                    normalTime += leg.normalMinutes;
                }
                results.put(destination, new RouteResult(start, destination, path, legs,
                        normalTime, time, includeTraffic));
            }
            return results;
        }

        /** Converts a location path into detailed road segments for display. */
        private List<RouteLeg> buildRouteLegs(List<String> path, boolean includeTraffic) {
            List<RouteLeg> legs = new ArrayList<>();
            for (int index = 0; index + 1 < path.size(); index++) {
                String from = path.get(index);
                String to = path.get(index + 1);
                Road road = findRoad(from, to);
                double selectedTime = includeTraffic
                        ? road.normalMinutes * road.trafficMultiplier
                        : road.normalMinutes;
                legs.add(new RouteLeg(from, to, road.normalMinutes,
                        road.trafficMultiplier, selectedTime));
            }
            return legs;
        }

        private Road findRoad(String from, String to) {
            for (Road road : roads.getOrDefault(from, Collections.emptyList())) {
                if (road.to.equals(to)) {
                    return road;
                }
            }
            throw new IllegalStateException("Road disappeared while building route.");
        }

        private List<String> reconstructPath(Map<String, String> previous,
                                             String start,
                                             String destination,
                                             double travelTime) {
            if (Double.isInfinite(travelTime)) {
                return Collections.emptyList();
            }

            LinkedList<String> path = new LinkedList<>();
            String current = destination;
            while (current != null) {
                path.addFirst(current);
                if (current.equals(start)) {
                    return path;
                }
                current = previous.get(current);
            }
            return Collections.emptyList();
        }
    }

    public static final class RouteResult {
        public final String start;
        public final String destination;
        public final List<String> path;
        public final List<RouteLeg> legs;
        public final double normalTravelTimeMinutes;
        public final double travelTimeMinutes;
        public final boolean trafficIncluded;

        private RouteResult(String start, String destination, List<String> path,
                            List<RouteLeg> legs, double normalTravelTimeMinutes,
                            double travelTimeMinutes, boolean trafficIncluded) {
            this.start = start;
            this.destination = destination;
            this.path = Collections.unmodifiableList(new ArrayList<>(path));
            this.legs = Collections.unmodifiableList(new ArrayList<>(legs));
            this.normalTravelTimeMinutes = normalTravelTimeMinutes;
            this.travelTimeMinutes = travelTimeMinutes;
            this.trafficIncluded = trafficIncluded;
        }

        public boolean isReachable() {
            return !path.isEmpty();
        }
    }

    /** One road in the final route, including the traffic calculation used. */
    public static final class RouteLeg {
        public final String from;
        public final String to;
        public final double normalMinutes;
        public final double trafficMultiplier;
        public final double selectedTravelMinutes;

        private RouteLeg(String from, String to, double normalMinutes,
                         double trafficMultiplier, double selectedTravelMinutes) {
            this.from = from;
            this.to = to;
            this.normalMinutes = normalMinutes;
            this.trafficMultiplier = trafficMultiplier;
            this.selectedTravelMinutes = selectedTravelMinutes;
        }
    }

    private static final class Road {
        final String to;
        final double normalMinutes;
        final double trafficMultiplier;

        Road(String to, double normalMinutes, double trafficMultiplier) {
            this.to = to;
            this.normalMinutes = normalMinutes;
            this.trafficMultiplier = trafficMultiplier;
        }
    }

    private static final class LocationTime {
        final String location;
        final double time;

        LocationTime(String location, double time) {
            this.location = location;
            this.time = time;
        }
    }
}
