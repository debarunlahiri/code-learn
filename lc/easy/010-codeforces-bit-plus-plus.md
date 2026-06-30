# 010. Bit Plus Plus

Platform: Codeforces  
Difficulty: Easy  
Topic: Implementation, String

## Problem Statement

A variable `x` starts with value `0`.

You are given `n` operations. Each operation either increases or decreases `x` by `1`.

Operations may look like:

- `++X`
- `X++`
- `--X`
- `X--`

Return the final value of `x`.

## Constraints

- `1 <= n <= 150`
- Each operation is one of the four valid forms.

## Example

Input:

```text
3
++X
X++
--X
```

Output:

```text
1
```

## Brute Force Approach

Check the full operation string and update `x`.

```java
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();
        int x = 0;

        for (int i = 0; i < n; i++) {
            String operation = scanner.next();

            if (operation.equals("++X") || operation.equals("X++")) {
                x++;
            } else {
                x--;
            }
        }

        System.out.println(x);
        scanner.close();
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(1)`

## Best Approach

Every increment operation contains the `+` character, and every decrement operation contains the `-` character. Check only one character pattern.

```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(reader.readLine());
        int x = 0;

        for (int i = 0; i < n; i++) {
            String operation = reader.readLine();

            if (operation.contains("+")) {
                x++;
            } else {
                x--;
            }
        }

        System.out.println(x);
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(1)`

