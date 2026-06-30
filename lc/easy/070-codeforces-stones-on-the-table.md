# 070. Stones on the Table

Platform: Codeforces  
Difficulty: Easy  
Topic: String

## Problem Statement

There are `n` stones in a row. Each stone has color `R`, `G`, or `B`.

You need to remove the minimum number of stones so that no two neighboring stones have the same color.

## Constraints

- `1 <= n <= 50`
- The color string contains only `R`, `G`, and `B`.

## Example

Input:

```text
3
RRG
```

Output:

```text
1
```

## Brute Force Approach

Check every adjacent pair. If two neighboring stones are equal, one of them must be removed.

```java
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();
        String colors = scanner.next();
        int removals = 0;

        for (int i = 1; i < n; i++) {
            if (colors.charAt(i) == colors.charAt(i - 1)) {
                removals++;
            }
        }

        System.out.println(removals);
        scanner.close();
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(1)`

## Best Approach

The adjacent-pair counting approach is already optimal.

```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(reader.readLine());
        String colors = reader.readLine();
        int answer = 0;

        for (int i = 1; i < n; i++) {
            if (colors.charAt(i) == colors.charAt(i - 1)) {
                answer++;
            }
        }

        System.out.println(answer);
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(1)`

