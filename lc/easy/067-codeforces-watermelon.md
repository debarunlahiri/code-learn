# 067. Watermelon

Platform: Codeforces  
Difficulty: Easy  
Topic: Math

## Problem Statement

Given a watermelon weight `w`, determine whether it can be split into two positive even parts.

Print `"YES"` if possible, otherwise print `"NO"`.

## Constraints

- `1 <= w <= 100`

## Example

Input:

```text
8
```

Output:

```text
YES
```

## Brute Force Approach

Try every possible first part and check whether both parts are even and positive.

```java
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int w = scanner.nextInt();

        for (int first = 1; first < w; first++) {
            int second = w - first;

            if (first % 2 == 0 && second % 2 == 0) {
                System.out.println("YES");
                scanner.close();
                return;
            }
        }

        System.out.println("NO");
        scanner.close();
    }
}
```

Complexity:

- Time: `O(w)`
- Space: `O(1)`

## Best Approach

The split is possible for even weights greater than `2`.

```java
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int w = scanner.nextInt();

        if (w > 2 && w % 2 == 0) {
            System.out.println("YES");
        } else {
            System.out.println("NO");
        }

        scanner.close();
    }
}
```

Complexity:

- Time: `O(1)`
- Space: `O(1)`

