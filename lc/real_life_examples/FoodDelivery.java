import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Real-life food-delivery example using common data structures.
 *
 * <ul>
 *   <li>HashMap: quickly finds restaurants, drivers and orders by ID.</li>
 *   <li>Queue: processes orders in the same order in which they were placed.</li>
 *   <li>PriorityQueue: selects the closest available driver.</li>
 *   <li>ArrayList: preserves the status history of each order.</li>
 * </ul>
 *
 * <p>Driver distance uses the Euclidean formula. A real application would get
 * road distance and traffic-aware travel time from a mapping service.</p>
 */
public final class FoodDelivery {

    private FoodDelivery() {
        // utility class
    }

    public static void main(String[] args) {
        DeliverySystem system = new DeliverySystem();

        system.addRestaurant(new Restaurant("R1", "Spice Kitchen", new Location(2, 3)));
        system.addRestaurant(new Restaurant("R2", "Pizza Corner", new Location(8, 6)));

        system.addDriver(new Driver("D1", "Asha", new Location(1, 2)));
        system.addDriver(new Driver("D2", "Ravi", new Location(7, 5)));
        system.addDriver(new Driver("D3", "Mina", new Location(4, 4)));

        system.placeOrder("O101", "R1", "Anil", new Location(6, 7),
                "Paneer meal", 320.0);
        system.placeOrder("O102", "R2", "Sara", new Location(10, 9),
                "Large pizza", 540.0);
        system.placeOrder("O103", "R1", "Kabir", new Location(3, 8),
                "Biryani", 280.0);

        System.out.println("===== PROCESSING PENDING ORDERS =====");
        system.processPendingOrders();

        System.out.println("\n===== ORDER DETAILS =====");
        system.printAllOrders();

        System.out.println("\n===== COMPLETING ORDER O101 =====");
        system.markPickedUp("O101");
        system.markDelivered("O101");
        system.printOrder("O101");

        System.out.println("\n===== DRIVER AVAILABILITY =====");
        system.printDrivers();
    }

    public static final class DeliverySystem {
        private final Map<String, Restaurant> restaurants = new HashMap<>();
        private final Map<String, Driver> drivers = new LinkedHashMap<>();
        private final Map<String, Order> orders = new LinkedHashMap<>();
        private final Queue<Order> pendingOrders = new ArrayDeque<>();

        public void addRestaurant(Restaurant restaurant) {
            requireUnique(restaurants, restaurant.id, "restaurant");
            restaurants.put(restaurant.id, restaurant);
        }

        public void addDriver(Driver driver) {
            requireUnique(drivers, driver.id, "driver");
            drivers.put(driver.id, driver);
        }

        public void placeOrder(String orderId, String restaurantId, String customerName,
                               Location deliveryLocation, String item, double price) {
            requireUnique(orders, orderId, "order");
            Restaurant restaurant = restaurants.get(restaurantId);
            if (restaurant == null) {
                throw new IllegalArgumentException("Unknown restaurant: " + restaurantId);
            }
            if (price < 0) {
                throw new IllegalArgumentException("Price cannot be negative.");
            }

            Order order = new Order(orderId, restaurant, customerName,
                    deliveryLocation, item, price);
            orders.put(orderId, order);
            pendingOrders.offer(order);
            order.changeStatus(OrderStatus.PLACED,
                    "Order placed and added to the preparation queue.");
        }

        /** Processes pending orders in FIFO order and assigns nearest drivers. */
        public void processPendingOrders() {
            while (!pendingOrders.isEmpty()) {
                Order order = pendingOrders.poll();
                order.changeStatus(OrderStatus.PREPARING,
                        order.restaurant.name + " started preparing the food.");

                Driver nearest = findNearestAvailableDriver(order.restaurant.location);
                if (nearest == null) {
                    order.changeStatus(OrderStatus.WAITING_FOR_DRIVER,
                            "No driver is currently available.");
                    System.out.println(order.id + ": waiting for a driver");
                    continue;
                }

                nearest.available = false;
                order.driver = nearest;
                order.changeStatus(OrderStatus.DRIVER_ASSIGNED,
                        nearest.name + " was assigned to this order.");
                double pickupDistance = nearest.location.distanceTo(order.restaurant.location);
                System.out.printf("%s: assigned %s (%.2f units from restaurant)%n",
                        order.id, nearest.name, pickupDistance);
            }
        }

        /**
         * Builds a min-heap of available drivers. The driver with the smallest
         * restaurant distance receives the order first: O(D log D).
         */
        private Driver findNearestAvailableDriver(Location restaurantLocation) {
            PriorityQueue<DriverDistance> nearestDrivers = new PriorityQueue<>(
                    Comparator.comparingDouble(candidate -> candidate.distance));
            for (Driver driver : drivers.values()) {
                if (driver.available) {
                    nearestDrivers.offer(new DriverDistance(driver,
                            driver.location.distanceTo(restaurantLocation)));
                }
            }
            return nearestDrivers.isEmpty() ? null : nearestDrivers.poll().driver;
        }

        public void markPickedUp(String orderId) {
            Order order = requireOrder(orderId);
            requireStatus(order, OrderStatus.DRIVER_ASSIGNED);
            order.changeStatus(OrderStatus.OUT_FOR_DELIVERY,
                    order.driver.name + " picked up the food.");
        }

        public void markDelivered(String orderId) {
            Order order = requireOrder(orderId);
            requireStatus(order, OrderStatus.OUT_FOR_DELIVERY);
            order.changeStatus(OrderStatus.DELIVERED,
                    "Food delivered to " + order.customerName + ".");
            order.driver.location = order.deliveryLocation;
            order.driver.available = true;
        }

        public void printOrder(String orderId) {
            Order order = requireOrder(orderId);
            System.out.println("Order " + order.id + " | " + order.item
                    + " | Rs. " + String.format("%.2f", order.price));
            System.out.println("  Restaurant: " + order.restaurant.name);
            System.out.println("  Customer: " + order.customerName);
            System.out.println("  Driver: " + (order.driver == null ? "Not assigned" : order.driver.name));
            System.out.println("  Current status: " + order.status);
            System.out.println("  Status history:");
            for (String event : order.statusHistory) {
                System.out.println("    - " + event);
            }
        }

        public void printAllOrders() {
            for (String orderId : orders.keySet()) {
                printOrder(orderId);
                System.out.println();
            }
        }

        public void printDrivers() {
            for (Driver driver : drivers.values()) {
                System.out.println(driver.id + " | " + driver.name + " | "
                        + (driver.available ? "AVAILABLE" : "BUSY")
                        + " | location " + driver.location);
            }
        }

        private Order requireOrder(String orderId) {
            Order order = orders.get(orderId);
            if (order == null) {
                throw new IllegalArgumentException("Unknown order: " + orderId);
            }
            return order;
        }

        private void requireStatus(Order order, OrderStatus expected) {
            if (order.status != expected) {
                throw new IllegalStateException("Order " + order.id + " must be "
                        + expected + " but is " + order.status + ".");
            }
        }

        private <T> void requireUnique(Map<String, T> values, String id, String type) {
            if (id == null || id.trim().isEmpty()) {
                throw new IllegalArgumentException(type + " ID must not be blank.");
            }
            if (values.containsKey(id)) {
                throw new IllegalArgumentException("Duplicate " + type + " ID: " + id);
            }
        }
    }

    public enum OrderStatus {
        PLACED, PREPARING, WAITING_FOR_DRIVER, DRIVER_ASSIGNED,
        OUT_FOR_DELIVERY, DELIVERED
    }

    public static final class Location {
        public final double x;
        public final double y;

        public Location(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double distanceTo(Location other) {
            double xDifference = x - other.x;
            double yDifference = y - other.y;
            return Math.sqrt(xDifference * xDifference + yDifference * yDifference);
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }

    public static final class Restaurant {
        public final String id;
        public final String name;
        public final Location location;

        public Restaurant(String id, String name, Location location) {
            this.id = id;
            this.name = name;
            this.location = location;
        }
    }

    public static final class Driver {
        public final String id;
        public final String name;
        public Location location;
        public boolean available = true;

        public Driver(String id, String name, Location location) {
            this.id = id;
            this.name = name;
            this.location = location;
        }
    }

    public static final class Order {
        public final String id;
        public final Restaurant restaurant;
        public final String customerName;
        public final Location deliveryLocation;
        public final String item;
        public final double price;
        public final List<String> statusHistory = new ArrayList<>();
        public OrderStatus status;
        public Driver driver;

        private Order(String id, Restaurant restaurant, String customerName,
                      Location deliveryLocation, String item, double price) {
            this.id = id;
            this.restaurant = restaurant;
            this.customerName = customerName;
            this.deliveryLocation = deliveryLocation;
            this.item = item;
            this.price = price;
        }

        private void changeStatus(OrderStatus newStatus, String explanation) {
            status = newStatus;
            statusHistory.add(newStatus + ": " + explanation);
        }
    }

    private static final class DriverDistance {
        final Driver driver;
        final double distance;

        DriverDistance(Driver driver, double distance) {
            this.driver = driver;
            this.distance = distance;
        }
    }
}
