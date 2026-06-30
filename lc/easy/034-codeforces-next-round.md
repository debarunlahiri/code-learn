# 034. Next Round

Platform: Codeforces  
Difficulty: Easy  
Topic: Implementation

## Problem Statement

There are `n` participants and their scores are sorted from highest to lowest.

A participant advances to the next round if:

- Their score is positive.
- Their score is at least the score of the participant in position `k`.

Count how many participants advance.

## Constraints

- `1 <= k <= n <= 50`
- `0 <= score[i] <= 100`
- Scores are sorted in non-increasing order.

## Example

Input:

```text
8 5
10 9 8 7 7 7 5 5
```

Output:

```text
6
```

## Brute Force Approach

Read all scores into an array, find the `k`-th score, and count qualifying participants.

```java
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();
        int k = scanner.nextInt();
        int[] scores = new int[n];

        for (int i = 0; i < n; i++) {
            scores[i] = scanner.nextInt();
        }

        int cutoff = scores[k - 1];
        int count = 0;

        for (int score : scores) {
            if (score > 0 && score >= cutoff) {
                count++;
            }
        }

        System.out.println(count);
        scanner.close();
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(n)`

## Best Approach

For this input size, the direct approach is already optimal. Space can be reduced by checking during a second pass only if scores are stored, or by keeping the array as shown for clarity.

```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer firstLine = new StringTokenizer(reader.readLine());
        int n = Integer.parseInt(firstLine.nextToken());
        int k = Integer.parseInt(firstLine.nextToken());

        int[] scores = new int[n];
        StringTokenizer secondLine = new StringTokenizer(reader.readLine());

        for (int i = 0; i < n; i++) {
            scores[i] = Integer.parseInt(secondLine.nextToken());
        }

        int cutoff = scores[k - 1];
        int advanced = 0;

        for (int score : scores) {
            if (score > 0 && score >= cutoff) {
                advanced++;
            }
        }

        System.out.println(advanced);
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(n)`

