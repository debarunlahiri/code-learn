# 066. Relative Ranks

Platform: LeetCode  
Difficulty: Easy  
Topic: Array, Sorting, Hash Map

## Problem Statement

Given an array `score`, return each athlete's rank.

The highest score gets `"Gold Medal"`, second gets `"Silver Medal"`, third gets `"Bronze Medal"`, and the rest get their numeric rank as a string.

## Constraints

- `1 <= score.length <= 10^4`
- `0 <= score[i] <= 10^6`
- All scores are unique.

## Example

Input:

```text
score = [5, 4, 3, 2, 1]
```

Output:

```text
["Gold Medal", "Silver Medal", "Bronze Medal", "4", "5"]
```

## Brute Force Approach

For each score, count how many scores are greater.

```java
class Solution {
    public String[] findRelativeRanks(int[] score) {
        String[] answer = new String[score.length];

        for (int i = 0; i < score.length; i++) {
            int rank = 1;

            for (int other : score) {
                if (other > score[i]) {
                    rank++;
                }
            }

            answer[i] = rankText(rank);
        }

        return answer;
    }

    private String rankText(int rank) {
        if (rank == 1) {
            return "Gold Medal";
        }
        if (rank == 2) {
            return "Silver Medal";
        }
        if (rank == 3) {
            return "Bronze Medal";
        }

        return String.valueOf(rank);
    }
}
```

Complexity:

- Time: `O(n^2)`
- Space: `O(1)` apart from output.

## Best Approach

Sort a copy of the scores in descending order and map each score to its rank text.

```java
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class Solution {
    public String[] findRelativeRanks(int[] score) {
        int[] sorted = score.clone();
        Arrays.sort(sorted);

        Map<Integer, String> scoreToRank = new HashMap<>();
        int rank = 1;

        for (int i = sorted.length - 1; i >= 0; i--) {
            scoreToRank.put(sorted[i], rankText(rank));
            rank++;
        }

        String[] answer = new String[score.length];

        for (int i = 0; i < score.length; i++) {
            answer[i] = scoreToRank.get(score[i]);
        }

        return answer;
    }

    private String rankText(int rank) {
        if (rank == 1) {
            return "Gold Medal";
        }
        if (rank == 2) {
            return "Silver Medal";
        }
        if (rank == 3) {
            return "Bronze Medal";
        }

        return String.valueOf(rank);
    }
}
```

Complexity:

- Time: `O(n log n)`
- Space: `O(n)`

