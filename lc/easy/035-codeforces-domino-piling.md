# 035. Domino Piling

Platform: Codeforces  
Difficulty: Easy  
Topic: Math

## Problem Statement

You have a board with size `m x n`. Each domino covers exactly two adjacent cells.

Find the maximum number of dominoes that can be placed on the board without overlapping and without going outside the board.

## Constraints

- `1 <= m <= 16`
- `1 <= n <= 16`

## Example

Input:

```text
2 4
```

Output:

```text
4
```

## Brute Force Approach

Think of the board as `m * n` cells. Since every domino uses `2` cells, the maximum count cannot be more than half the cells.

```java
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int m = scanner.nextInt();
        int n = scanner.nextInt();

        int totalCells = m * n;
        int dominoes = totalCells / 2;

        System.out.println(dominoes);
        scanner.close();
    }
}
```

Complexity:

- Time: `O(1)`
- Space: `O(1)`

## Best Approach

The formula `(m * n) / 2` is already optimal.

```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer tokenizer = new StringTokenizer(reader.readLine());

        int m = Integer.parseInt(tokenizer.nextToken());
        int n = Integer.parseInt(tokenizer.nextToken());

        System.out.println((m * n) / 2);
    }
}
```

Complexity:

- Time: `O(1)`
- Space: `O(1)`

