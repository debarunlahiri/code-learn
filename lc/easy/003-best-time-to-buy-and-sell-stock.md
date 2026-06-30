# 003. Best Time to Buy and Sell Stock

Platform: LeetCode  
Difficulty: Easy  
Topic: Array

## Problem Statement

Given an array `prices`, where `prices[i]` is the price of a stock on day `i`, choose one day to buy and a later day to sell.

Return the maximum profit possible. If no profit is possible, return `0`.

## Constraints

- `1 <= prices.length <= 10^5`
- `0 <= prices[i] <= 10^4`

## Example

Input:

```text
prices = [7, 1, 5, 3, 6, 4]
```

Output:

```text
5
```

Explanation: Buy at price `1` and sell at price `6`.

## Brute Force Approach

Try every possible buy day and every possible sell day after it.

```java
class Solution {
    public int maxProfit(int[] prices) {
        int maxProfit = 0;

        for (int buy = 0; buy < prices.length; buy++) {
            for (int sell = buy + 1; sell < prices.length; sell++) {
                int profit = prices[sell] - prices[buy];
                maxProfit = Math.max(maxProfit, profit);
            }
        }

        return maxProfit;
    }
}
```

Complexity:

- Time: `O(n^2)`
- Space: `O(1)`

## Best Approach

Track the minimum price seen so far. For each day, calculate the profit if we sell on that day.

```java
class Solution {
    public int maxProfit(int[] prices) {
        int minPrice = Integer.MAX_VALUE;
        int maxProfit = 0;

        for (int price : prices) {
            if (price < minPrice) {
                minPrice = price;
            }

            int profit = price - minPrice;
            maxProfit = Math.max(maxProfit, profit);
        }

        return maxProfit;
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(1)`

