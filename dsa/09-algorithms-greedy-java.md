# Algorithms: Greedy (Easy to Hard)

## 1. Activity Selection (Maximum Non-overlapping Intervals)
```java
import java.util.Arrays;

public class ActivitySelection {
    static int maxActivities(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> a[1] - b[1]);
        int count = 0;
        int end = Integer.MIN_VALUE;

        for (int[] in : intervals) {
            if (in[0] >= end) {
                count++;
                end = in[1];
            }
        }
        return count;
    }
}
```

## 2. Jump Game
```java
public class JumpGame {
    static boolean canJump(int[] nums) {
        int farthest = 0;
        for (int i = 0; i < nums.length; i++) {
            if (i > farthest) return false;
            farthest = Math.max(farthest, i + nums[i]);
        }
        return true;
    }
}
```

## 3. Minimum Platforms (Train/Meeting Rooms style)
```java
import java.util.Arrays;

public class MinimumPlatforms {
    static int minPlatforms(int[] arrival, int[] departure) {
        Arrays.sort(arrival);
        Arrays.sort(departure);

        int i = 0, j = 0, curr = 0, best = 0;
        while (i < arrival.length && j < departure.length) {
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
}
```

## 4. Huffman Merge Cost (connect ropes)
```java
import java.util.PriorityQueue;

public class ConnectRopes {
    static int minCost(int[] ropes) {
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

