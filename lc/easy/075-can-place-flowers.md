# 075. Can Place Flowers

Platform: LeetCode  
Difficulty: Easy  
Topic: Array, Greedy

## Problem Statement

Given a flowerbed array containing `0`s and `1`s, where `0` means empty and `1` means planted, determine whether `n` new flowers can be planted.

Flowers cannot be planted in adjacent plots.

## Constraints

- `1 <= flowerbed.length <= 2 * 10^4`
- `flowerbed[i]` is `0` or `1`.
- `0 <= n <= flowerbed.length`

## Example

Input:

```text
flowerbed = [1, 0, 0, 0, 1], n = 1
```

Output:

```text
true
```

## Brute Force Approach

Try planting at every empty position when both neighbors are empty or outside the array.

```java
class Solution {
    public boolean canPlaceFlowers(int[] flowerbed, int n) {
        int planted = 0;

        for (int i = 0; i < flowerbed.length; i++) {
            if (flowerbed[i] == 1) {
                continue;
            }

            boolean emptyLeft = i == 0 || flowerbed[i - 1] == 0;
            boolean emptyRight = i == flowerbed.length - 1 || flowerbed[i + 1] == 0;

            if (emptyLeft && emptyRight) {
                flowerbed[i] = 1;
                planted++;
            }
        }

        return planted >= n;
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(1)`

## Best Approach

The greedy scan is optimal because planting as early as possible never reduces future valid positions.

```java
class Solution {
    public boolean canPlaceFlowers(int[] flowerbed, int n) {
        for (int i = 0; i < flowerbed.length && n > 0; i++) {
            if (flowerbed[i] == 1) {
                continue;
            }

            boolean leftEmpty = i == 0 || flowerbed[i - 1] == 0;
            boolean rightEmpty = i == flowerbed.length - 1 || flowerbed[i + 1] == 0;

            if (leftEmpty && rightEmpty) {
                flowerbed[i] = 1;
                n--;
            }
        }

        return n == 0;
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(1)`

