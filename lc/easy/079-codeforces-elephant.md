# 079. Elephant

Platform: Codeforces  
Difficulty: Easy  
Topic: Math

## Problem Statement

An elephant wants to reach position `x` on a number line, starting from position `0`.

In one move, it can move `1`, `2`, `3`, `4`, or `5` steps forward. Find the minimum number of moves needed.

## Constraints

- `1 <= x <= 10^6`

## Example

Input:

```text
12
```

Output:

```text
3
```

## Brute Force Approach

Keep taking `5` steps until the remaining distance is less than or equal to `5`.

```java
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int x = scanner.nextInt();
        int moves = 0;

        while (x > 0) {
            x -= 5;
            moves++;
        }

        System.out.println(moves);
        scanner.close();
    }
}
```

Complexity:

- Time: `O(x / 5)`
- Space: `O(1)`

## Best Approach

Use ceiling division by `5`.

```java
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int x = scanner.nextInt();

        int moves = (x + 4) / 5;

        System.out.println(moves);
        scanner.close();
    }
}
```

Complexity:

- Time: `O(1)`
- Space: `O(1)`

