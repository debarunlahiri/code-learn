# 009. Team

Platform: Codeforces  
Difficulty: Easy  
Topic: Implementation

## Problem Statement

Three friends are deciding whether to solve each problem in a contest.

For every problem, each friend gives either:

- `1` if they are sure about the solution.
- `0` if they are not sure.

The team will solve a problem only if at least two friends are sure. Given all decisions, count how many problems the team will solve.

## Constraints

- `1 <= n <= 1000`
- Each decision value is either `0` or `1`.

## Example

Input:

```text
3
1 1 0
1 1 1
1 0 0
```

Output:

```text
2
```

## Brute Force Approach

For each problem, add the three values and check whether the sum is at least `2`.

```java
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();
        int solved = 0;

        for (int i = 0; i < n; i++) {
            int first = scanner.nextInt();
            int second = scanner.nextInt();
            int third = scanner.nextInt();

            int sureCount = first + second + third;

            if (sureCount >= 2) {
                solved++;
            }
        }

        System.out.println(solved);
        scanner.close();
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(1)`

## Best Approach

The direct counting approach is already optimal. Use `BufferedReader` for faster input.

```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(reader.readLine());
        int solved = 0;

        for (int i = 0; i < n; i++) {
            StringTokenizer tokenizer = new StringTokenizer(reader.readLine());

            int first = Integer.parseInt(tokenizer.nextToken());
            int second = Integer.parseInt(tokenizer.nextToken());
            int third = Integer.parseInt(tokenizer.nextToken());

            if (first + second + third >= 2) {
                solved++;
            }
        }

        System.out.println(solved);
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(1)`

