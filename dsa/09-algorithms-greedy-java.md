# Algorithms: Greedy (Easy to Hard)

Goal: Understand greedy algorithms - making locally optimal choices to achieve global optimum.

---

## 1. Activity Selection (Maximum Non-overlapping Intervals)

### What it does
Select maximum number of non-overlapping activities from given intervals.

### Why it matters
- Classic greedy problem
- Interval scheduling fundamentals
- Resource allocation problems
- Tests sorting and greedy choice proof

### Intuition
Always pick the activity that finishes earliest. This leaves maximum time for remaining activities. Like booking the shortest meeting first to fit more meetings.

### When to use
- Meeting room scheduling
- Resource allocation
- Task scheduling with time constraints
- Maximum compatible intervals

### Time complexity
- Time: `O(n log n)` (due to sorting)
- Space: `O(1)` (excluding input storage)

### Edge cases
- Empty input
- All activities overlap
- Activities with same end time
- Single activity

### Java code
```java
import java.util.Arrays;

public class ActivitySelection {
    static int maxActivities(int[][] intervals) {
        if (intervals == null || intervals.length == 0) return 0;

        // Sort by end time (greedy choice)
        Arrays.sort(intervals, (a, b) -> a[1] - b[1]);
        int count = 0;
        int end = Integer.MIN_VALUE;

        for (int[] interval : intervals) {
            if (interval[0] >= end) {
                count++;
                end = interval[1];
            }
        }
        return count;
    }

    // Return selected activities
    static int[][] selectedActivities(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> a[1] - b[1]);
        java.util.List<int[]> selected = new java.util.ArrayList<>();
        int end = Integer.MIN_VALUE;

        for (int[] interval : intervals) {
            if (interval[0] >= end) {
                selected.add(interval);
                end = interval[1];
            }
        }
        return selected.toArray(new int[0][]);
    }
}
```

---

## 2. Jump Game

### What it does
Determine if you can reach the last index starting from first index, where each element represents maximum jump length.

### Why it matters
- Greedy reachability problem
- Tests understanding of range expansion
- Used in pathfinding, game development
- Foundation for Jump Game II (minimum jumps)

### Intuition
Keep track of the farthest position you can reach. If at any point current position exceeds farthest reachable, you're stuck.

### When to use
- Pathfinding with jump constraints
- Game mechanics
- Network reachability
- Array traversal problems

### Time complexity
- Time: `O(n)`
- Space: `O(1)`

### Edge cases
- Empty array
- Single element (already at end)
- Zero at start (cannot move)
- Large jump values

### Java code
```java
public class JumpGame {
    static boolean canJump(int[] nums) {
        if (nums == null || nums.length <= 1) return true;

        int farthest = 0;
        for (int i = 0; i < nums.length; i++) {
            if (i > farthest) return false; // Stuck
            farthest = Math.max(farthest, i + nums[i]);
            if (farthest >= nums.length - 1) return true; // Can reach end
        }
        return false;
    }

    // Jump Game II: Minimum jumps to reach end
    static int minJumps(int[] nums) {
        if (nums.length <= 1) return 0;
        if (nums[0] == 0) return -1;

        int jumps = 0, currentEnd = 0, farthest = 0;
        for (int i = 0; i < nums.length - 1; i++) {
            farthest = Math.max(farthest, i + nums[i]);
            if (i == currentEnd) {
                jumps++;
                currentEnd = farthest;
                if (currentEnd >= nums.length - 1) break;
            }
        }
        return jumps;
    }
}
```

---

## 3. Minimum Platforms (Train/Meeting Rooms style)

### What it does
Find minimum number of platforms needed for trains given arrival and departure times.

### Why it matters
- Resource optimization
- Interval overlap counting
- Used in scheduling systems
- Tests two-pointer technique with sorting

### Intuition
Sort arrivals and departures separately. Walk through timeline, counting how many trains are present at each point. Maximum count is the answer.

### When to use
- Meeting room allocation
- CPU scheduling
- Resource capacity planning
- Traffic management

### Time complexity
- Time: `O(n log n)` (sorting)
- Space: `O(1)` (excluding input storage)

### Edge cases
- Empty arrays
- Single train
- All trains at same time
- Arrival equals departure (can reuse platform)

### Java code
```java
import java.util.Arrays;

public class MinimumPlatforms {
    static int minPlatforms(int[] arrival, int[] departure) {
        if (arrival == null || departure == null || arrival.length != departure.length) {
            return 0;
        }

        Arrays.sort(arrival);
        Arrays.sort(departure);

        int i = 0, j = 0, curr = 0, best = 0;
        int n = arrival.length;

        while (i < n && j < n) {
            if (arrival[i] <= departure[j]) {
                curr++;
                best = Math.max(best, curr);
                i++;
            } else {
                curr--;
                j++;
            }
        }
        return best;
    }

    // Alternative using events
    static int minPlatformsEvents(int[] arrival, int[] departure) {
        int n = arrival.length;
        int[][] events = new int[2 * n][2];
        for (int i = 0; i < n; i++) {
            events[2 * i] = new int[]{arrival[i], 1};     // Arrival event
            events[2 * i + 1] = new int[]{departure[i] + 1, -1}; // Departure event
        }

        Arrays.sort(events, (a, b) -> a[0] != b[0] ? a[0] - b[0] : a[1] - b[1]);

        int curr = 0, best = 0;
        for (int[] event : events) {
            curr += event[1];
            best = Math.max(best, curr);
        }
        return best;
    }
}
```

---

## 4. Huffman Merge Cost (connect ropes)

### What it does
Find minimum cost to connect ropes where cost of connecting two ropes is sum of their lengths.

### Why it matters
- Classic greedy with min-heap
- File merging optimization
- Data compression basics
- Tests priority queue usage

### Intuition
Always connect the two shortest ropes first. This ensures smaller ropes are added multiple times, minimizing total cost.

### When to use
- File merging
- Huffman coding
- Optimal merging problems
- Cost minimization with pairwise operations

### Time complexity
- Time: `O(n log n)` (heap operations)
- Space: `O(n)`

### Edge cases
- Empty array
- Single rope (no cost)
- All ropes same length
- Very large values (use long)

### Java code
```java
import java.util.PriorityQueue;

public class ConnectRopes {
    static int minCost(int[] ropes) {
        if (ropes == null || ropes.length <= 1) return 0;

        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for (int r : ropes) pq.offer(r);

        int cost = 0;
        while (pq.size() > 1) {
            int a = pq.poll();
            int b = pq.poll();
            int sum = a + b;
            cost += sum;
            pq.offer(sum);
        }
        return cost;
    }

    // For very large values
    static long minCostLong(int[] ropes) {
        if (ropes == null || ropes.length <= 1) return 0L;

        PriorityQueue<Long> pq = new PriorityQueue<>();
        for (int r : ropes) pq.offer((long) r);

        long cost = 0L;
        while (pq.size() > 1) {
            long a = pq.poll();
            long b = pq.poll();
            long sum = a + b;
            cost += sum;
            pq.offer(sum);
        }
        return cost;
    }
}
```

---

## 5. Fractional Knapsack

### What it does
Maximize value in knapsack when fractions of items are allowed.

### Why it matters
- Greedy algorithm with ratio-based selection
- Resource allocation problems
- Different from 0/1 knapsack
- Tests sorting and greedy proof

### Intuition
Take items with highest value-to-weight ratio first. Take as much as possible of each item.

### When to use
- Resource allocation with divisibility
- Investment portfolio selection
- Cargo loading with divisible items
- Budget allocation problems

### Time complexity
- Time: `O(n log n)` (sorting)
- Space: `O(1)`

### Edge cases
- Empty items array
- Zero capacity
- Items with zero weight
- All items don't fit

### Java code
```java
import java.util.Arrays;
import java.util.Comparator;

public class FractionalKnapsack {
    static class Item {
        int value, weight;
        Item(int v, int w) { value = v; weight = w; }
    }

    static double maxValue(Item[] items, int capacity) {
        if (items == null || capacity <= 0) return 0.0;

        // Sort by value/weight ratio in descending order
        Arrays.sort(items, Comparator.comparingDouble(
            item -> -(double) item.value / item.weight
        ));

        double totalValue = 0.0;
        int remaining = capacity;

        for (Item item : items) {
            if (remaining == 0) break;
            if (item.weight <= remaining) {
                totalValue += item.value;
                remaining -= item.weight;
            } else {
                // Take fraction of item
                totalValue += item.value * ((double) remaining / item.weight);
                remaining = 0;
            }
        }
        return totalValue;
    }
}
```
    }
}
```

## 5. Fractional Knapsack
```java
import java.util.Arrays;

public class FractionalKnapsack {
    static class Item {
        int value, weight;
        Item(int value, int weight) {
            this.value = value;
            this.weight = weight;
        }
    }

    static double maxValue(Item[] items, int capacity) {
        Arrays.sort(items, (a, b) ->
            Double.compare((double)b.value / b.weight, (double)a.value / a.weight));

        double total = 0.0;
        for (Item item : items) {
            if (capacity == 0) break;
            if (item.weight <= capacity) {
                total += item.value;
                capacity -= item.weight;
            } else {
                total += (double) item.value * capacity / item.weight;
                capacity = 0;
            }
        }
        return total;
    }
}
```

