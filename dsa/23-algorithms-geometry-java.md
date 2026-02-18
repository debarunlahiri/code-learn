# Algorithms: Computational Geometry (Easy to Hard)

Goal: Master geometric algorithms and spatial problem-solving.

---

## 1. Point and Line Basics

### What it does
Represent and manipulate basic geometric entities: points, lines, and segments.

### Why it matters
- Foundation for all geometric algorithms
- Computer graphics applications
- Collision detection
- Spatial computations

### Intuition
Points are coordinates in space, lines are infinite extensions through two points. Like dots and connections on graph paper.

### When to use
- Geometric computations
- Graphics programming
- Collision detection
- Spatial analysis

### Time complexity
- Point operations: `O(1)`
- Line operations: `O(1)`
- Distance calculations: `O(1)`

### Edge cases
- Vertical lines (infinite slope)
- Coincident points
- Floating point precision
- Large coordinate values

### Java code
```java
public class GeometryBasics {
    static class Point {
        double x, y;
        
        Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
        
        double distanceTo(Point other) {
            return Math.sqrt(Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2));
        }
        
        @Override
        public String toString() {
            return String.format("(%.2f, %.2f)", x, y);
        }
    }

    static class Line {
        Point p1, p2;
        
        Line(Point p1, Point p2) {
            this.p1 = p1;
            this.p2 = p2;
        }
        
        // Get slope (handle vertical lines)
        Double getSlope() {
            if (Math.abs(p2.x - p1.x) < 0.0001) return null; // Vertical line
            return (p2.y - p1.y) / (p2.x - p1.x);
        }
        
        // Get y-intercept
        Double getYIntercept() {
            Double slope = getSlope();
            if (slope == null) return null; // Vertical line
            return p1.y - slope * p1.x;
        }
        
        // Check if point is on line
        boolean contains(Point p) {
            Double slope = getSlope();
            if (slope == null) {
                return Math.abs(p.x - p1.x) < 0.0001;
            }
            return Math.abs(p.y - (slope * p.x + getYIntercept())) < 0.0001;
        }
        
        // Distance from point to line
        double distanceFromPoint(Point p) {
            double A = p2.y - p1.y;
            double B = p1.x - p2.x;
            double C = p2.x * p1.y - p1.x * p2.y;
            
            return Math.abs(A * p.x + B * p.y + C) / Math.sqrt(A * A + B * B);
        }
    }
}
```

---

## 2. Line Intersection

### What it does
Find intersection point of two lines or determine if they're parallel.

### Why it matters
- Collision detection
- Geometric computations
- Computer graphics
- Spatial algorithms

### Intuition
Solve system of linear equations representing two lines. Like finding where two paths cross.

### When to use
- Collision detection
- Path planning
- Geometric analysis
- Graphics rendering

### Time complexity
- `O(1)`
- Space: `O(1)`

### Edge cases
- Parallel lines (no intersection)
- Coincident lines (infinite intersections)
- Nearly parallel lines (precision issues)
- Vertical/horizontal lines

### Java code
```java
public class LineIntersection {
    static class Point {
        double x, y;
        Point(double x, double y) { this.x = x; this.y = y; }
    }

    static class Line {
        Point p1, p2;
        Line(Point p1, Point p2) { this.p1 = p1; this.p2 = p2; }
    }

    static class IntersectionResult {
        Point point;
        boolean exists;
        boolean infinite; // Lines coincide
        
        IntersectionResult(Point point, boolean exists, boolean infinite) {
            this.point = point;
            this.exists = exists;
            this.infinite = infinite;
        }
    }

    static IntersectionResult findIntersection(Line l1, Line l2) {
        // Line 1: a1x + b1y = c1
        double a1 = l1.p2.y - l1.p1.y;
        double b1 = l1.p1.x - l1.p2.x;
        double c1 = a1 * l1.p1.x + b1 * l1.p1.y;
        
        // Line 2: a2x + b2y = c2
        double a2 = l2.p2.y - l2.p1.y;
        double b2 = l2.p1.x - l2.p2.x;
        double c2 = a2 * l2.p1.x + b2 * l2.p1.y;
        
        double determinant = a1 * b2 - a2 * b1;
        
        // Lines are parallel
        if (Math.abs(determinant) < 0.0001) {
            // Check if lines coincide
            if (Math.abs(a1 * c2 - a2 * c1) < 0.0001 && 
                Math.abs(b1 * c2 - b2 * c1) < 0.0001) {
                return new IntersectionResult(null, false, true);
            }
            return new IntersectionResult(null, false, false);
        }
        
        // Find intersection point
        double x = (b2 * c1 - b1 * c2) / determinant;
        double y = (a1 * c2 - a2 * c1) / determinant;
        
        return new IntersectionResult(new Point(x, y), true, false);
    }

    // Check if line segments intersect
    static boolean segmentsIntersect(Point p1, Point q1, Point p2, Point q2) {
        // Find orientations
        int o1 = orientation(p1, q1, p2);
        int o2 = orientation(p1, q1, q2);
        int o3 = orientation(p2, q2, p1);
        int o4 = orientation(p2, q2, q1);
        
        // General case
        if (o1 != o2 && o3 != o4) return true;
        
        // Special cases
        if (o1 == 0 && onSegment(p1, p2, q1)) return true;
        if (o2 == 0 && onSegment(p1, q2, q1)) return true;
        if (o3 == 0 && onSegment(p2, p1, q2)) return true;
        if (o4 == 0 && onSegment(p2, q1, q2)) return true;
        
        return false;
    }

    static int orientation(Point p, Point q, Point r) {
        double val = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y);
        if (Math.abs(val) < 0.0001) return 0; // Collinear
        return (val > 0) ? 1 : 2; // Clockwise or counterclockwise
    }

    static boolean onSegment(Point p, Point q, Point r) {
        return q.x <= Math.max(p.x, r.x) && q.x >= Math.min(p.x, r.x) &&
               q.y <= Math.max(p.y, r.y) && q.y >= Math.min(p.y, r.y);
    }
}
```

---

## 3. Convex Hull

### What it does
Find the smallest convex polygon that contains all given points.

### Why it matters
- Computational geometry foundation
- Shape analysis
- Collision detection optimization
- Geographic information systems

### Intuition
Imagine wrapping a rubber band around all points - the band forms the convex hull. Like finding the outer boundary.

### When to use
- Shape analysis
- Collision detection
- Geographic applications
- Pattern recognition

### Time complexity
- Graham Scan: `O(n log n)`
- Jarvis March: `O(nh)` where h = hull size
- Space: `O(n)`

### Edge cases
- All points collinear
- Duplicate points
- Single point
- All points same

### Java code
```java
import java.util.*;

public class ConvexHull {
    static class Point {
        int x, y;
        Point(int x, int y) { this.x = x; this.y = y; }
        
        // For sorting
        static int compare(Point p1, Point p2) {
            if (p1.x != p2.x) return Integer.compare(p1.x, p2.x);
            return Integer.compare(p1.y, p2.y);
        }
    }

    // Graham Scan algorithm
    static List<Point> convexHull(List<Point> points) {
        if (points.size() <= 1) return new ArrayList<>(points);
        
        // Sort points by x-coordinate (and y if tie)
        points.sort((p1, p2) -> Point.compare(p1, p2));
        
        List<Point> hull = new ArrayList<>();
        
        // Build lower hull
        for (Point p : points) {
            while (hull.size() >= 2) {
                Point q = hull.get(hull.size() - 1);
                Point r = hull.get(hull.size() - 2);
                if (crossProduct(r, q, p) <= 0) {
                    hull.remove(hull.size() - 1);
                } else {
                    break;
                }
            }
            hull.add(p);
        }
        
        // Build upper hull
        int t = hull.size() + 1;
        for (int i = points.size() - 2; i >= 0; i--) {
            Point p = points.get(i);
            while (hull.size() >= t) {
                Point q = hull.get(hull.size() - 1);
                Point r = hull.get(hull.size() - 2);
                if (crossProduct(r, q, p) <= 0) {
                    hull.remove(hull.size() - 1);
                } else {
                    break;
                }
            }
            hull.add(p);
        }
        
        // Remove last point (it's the first point)
        hull.remove(hull.size() - 1);
        
        return hull;
    }

    static int crossProduct(Point a, Point b, Point c) {
        return (b.x - a.x) * (c.y - a.y) - (b.y - a.y) * (c.x - a.x);
    }

    // Jarvis March algorithm
    static List<Point> convexHullJarvis(List<Point> points) {
        if (points.size() <= 1) return new ArrayList<>(points);
        
        List<Point> hull = new ArrayList<>();
        
        // Find leftmost point
        int leftmost = 0;
        for (int i = 1; i < points.size(); i++) {
            if (points.get(i).x < points.get(leftmost).x) {
                leftmost = i;
            }
        }
        
        int p = leftmost;
        do {
            hull.add(points.get(p));
            int q = (p + 1) % points.size();
            
            for (int i = 0; i < points.size(); i++) {
                if (orientation(points.get(p), points.get(i), points.get(q)) == 2) {
                    q = i;
                }
            }
            
            p = q;
        } while (p != leftmost);
        
        return hull;
    }

    static int orientation(Point p, Point q, Point r) {
        int val = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y);
        if (val == 0) return 0; // Collinear
        return (val > 0) ? 1 : 2; // Clockwise or counterclockwise
    }
}
```

---

## 4. Closest Pair of Points

### What it does
Find the pair of points with minimum distance among all points.

### Why it matters
- Computational geometry
- Clustering algorithms
- Spatial analysis
- Pattern recognition

### Intuition
Divide points into halves, find closest in each half, then check across the divide. Like divide and conquer for spatial problems.

### When to use
- Clustering
- Nearest neighbor problems
- Spatial analysis
- Pattern matching

### Time complexity
- Brute force: `O(n²)`
- Divide and conquer: `O(n log n)`
- Space: `O(n)`

### Edge cases
- Duplicate points (distance = 0)
- Single point
- All points same
- Large coordinate values

### Java code
```java
import java.util.*;

public class ClosestPair {
    static class Point {
        double x, y;
        Point(double x, double y) { this.x = x; this.y = y; }
        
        double distanceTo(Point other) {
            return Math.sqrt(Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2));
        }
    }

    static class Pair {
        Point p1, p2;
        double distance;
        
        Pair(Point p1, Point p2) {
            this.p1 = p1;
            this.p2 = p2;
            this.distance = p1.distanceTo(p2);
        }
    }

    // Brute force approach
    static Pair closestPairBruteForce(List<Point> points) {
        Pair minPair = null;
        double minDistance = Double.MAX_VALUE;
        
        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                double dist = points.get(i).distanceTo(points.get(j));
                if (dist < minDistance) {
                    minDistance = dist;
                    minPair = new Pair(points.get(i), points.get(j));
                }
            }
        }
        return minPair;
    }

    // Divide and conquer approach
    static Pair closestPair(List<Point> points) {
        List<Point> pointsSortedByX = new ArrayList<>(points);
        pointsSortedByX.sort((p1, p2) -> Double.compare(p1.x, p2.x));
        
        List<Point> pointsSortedByY = new ArrayList<>(points);
        pointsSortedByY.sort((p1, p2) -> Double.compare(p1.y, p2.y));
        
        return closestPairRecursive(pointsSortedByX, pointsSortedByY);
    }

    static Pair closestPairRecursive(List<Point> pointsX, List<Point> pointsY) {
        int n = pointsX.size();
        if (n <= 3) {
            return closestPairBruteForce(pointsX);
        }
        
        int mid = n / 2;
        Point midPoint = pointsX.get(mid);
        
        // Divide points by x-coordinate
        List<Point> leftX = new ArrayList<>(pointsX.subList(0, mid));
        List<Point> rightX = new ArrayList<>(pointsX.subList(mid, n));
        
        // Separate y-sorted points
        List<Point> leftY = new ArrayList<>();
        List<Point> rightY = new ArrayList<>();
        for (Point p : pointsY) {
            if (p.x <= midPoint.x) {
                leftY.add(p);
            } else {
                rightY.add(p);
            }
        }
        
        // Recursively find closest pairs
        Pair leftResult = closestPairRecursive(leftX, leftY);
        Pair rightResult = closestPairRecursive(rightX, rightY);
        
        // Find minimum distance
        Pair minPair = leftResult.distance < rightResult.distance ? leftResult : rightResult;
        double minDistance = minPair.distance;
        
        // Check for closer pairs across the divide
        List<Point> strip = new ArrayList<>();
        for (Point p : pointsY) {
            if (Math.abs(p.x - midPoint.x) < minDistance) {
                strip.add(p);
            }
        }
        
        // Check strip for closer pairs
        for (int i = 0; i < strip.size(); i++) {
            for (int j = i + 1; j < strip.size() && (strip.get(j).y - strip.get(i).y) < minDistance; j++) {
                double dist = strip.get(i).distanceTo(strip.get(j));
                if (dist < minDistance) {
                    minDistance = dist;
                    minPair = new Pair(strip.get(i), strip.get(j));
                }
            }
        }
        
        return minPair;
    }
}
```

---

## 5. Polygon Area

### What it does
Calculate area of a simple polygon using the shoelace formula.

### Why it matters
- Geometric computations
- Area calculations
- Computer graphics
- Geographic applications

### Intuition
Break polygon into triangles and sum their areas using cross products. Like calculating area by tracing the boundary.

### When to use
- Area calculations
- Geographic measurements
- Graphics applications
- Spatial analysis

### Time complexity
- `O(n)` where n = number of vertices
- Space: `O(1)`

### Edge cases
- Self-intersecting polygons
- Collinear consecutive points
- Clockwise vs counter-clockwise order
- Very small polygons

### Java code
```java
import java.util.List;

public class PolygonArea {
    static class Point {
        double x, y;
        Point(double x, double y) { this.x = x; this.y = y; }
    }

    // Calculate polygon area using shoelace formula
    static double calculateArea(List<Point> vertices) {
        if (vertices.size() < 3) return 0; // Not a polygon
        
        double area = 0;
        int n = vertices.size();
        
        for (int i = 0; i < n; i++) {
            Point current = vertices.get(i);
            Point next = vertices.get((i + 1) % n);
            
            area += current.x * next.y;
            area -= current.y * next.x;
        }
        
        return Math.abs(area) / 2;
    }

    // Check if polygon is convex
    static boolean isConvex(List<Point> vertices) {
        if (vertices.size() < 3) return false;
        
        int n = vertices.size();
        boolean gotNegative = false;
        boolean gotPositive = false;
        
        for (int i = 0; i < n; i++) {
            Point a = vertices.get(i);
            Point b = vertices.get((i + 1) % n);
            Point c = vertices.get((i + 2) % n);
            
            double crossProduct = (b.x - a.x) * (c.y - b.y) - (b.y - a.y) * (c.x - b.x);
            
            if (crossProduct < 0) gotNegative = true;
            if (crossProduct > 0) gotPositive = true;
            
            if (gotNegative && gotPositive) return false;
        }
        
        return true;
    }

    // Check if point is inside polygon (ray casting)
    static boolean pointInPolygon(Point point, List<Point> vertices) {
        int n = vertices.size();
        boolean inside = false;
        
        for (int i = 0, j = n - 1; i < n; j = i++) {
            Point vi = vertices.get(i);
            Point vj = vertices.get(j);
            
            if (((vi.y > point.y) != (vj.y > point.y)) &&
                (point.x < (vj.x - vi.x) * (point.y - vi.y) / (vj.y - vi.y) + vi.x)) {
                inside = !inside;
            }
        }
        
        return inside;
    }

    // Calculate centroid of polygon
    static Point calculateCentroid(List<Point> vertices) {
        if (vertices.size() < 3) return null;
        
        double area = 0;
        double cx = 0, cy = 0;
        int n = vertices.size();
        
        for (int i = 0; i < n; i++) {
            Point current = vertices.get(i);
            Point next = vertices.get((i + 1) % n);
            
            double cross = current.x * next.y - current.y * next.x;
            area += cross;
            cx += (current.x + next.x) * cross;
            cy += (current.y + next.y) * cross;
        }
        
        area /= 2;
        cx /= (6 * area);
        cy /= (6 * area);
        
        return new Point(Math.abs(cx), Math.abs(cy));
    }
}
```

---

## Practice Problems

### Easy
1. **Point Distance** (Basic geometry)
2. **Line Intersection** (Linear equations)
3. **Polygon Area** (Shoelace formula)

### Medium
1. **Convex Hull** (Graham scan)
2. **Closest Pair** (Divide and conquer)
3. **Point in Polygon** (Ray casting)

### Hard
1. **Line Segment Intersection** (Computational geometry)
2. **Rotating Calipers** (Diameter of convex polygon)
3. **Voronoi Diagrams** (Advanced spatial partitioning)

---

**Remember:** Geometry requires careful handling of floating-point precision and edge cases!
