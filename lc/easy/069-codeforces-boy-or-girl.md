# 069. Boy or Girl

Platform: Codeforces  
Difficulty: Easy  
Topic: String, Set

## Problem Statement

Given a username, count how many distinct characters it contains.

If the count is odd, print `"IGNORE HIM!"`. If the count is even, print `"CHAT WITH HER!"`.

## Constraints

- `1 <= username.length <= 100`
- The username contains lowercase English letters.

## Example

Input:

```text
wjmzbmr
```

Output:

```text
CHAT WITH HER!
```

## Brute Force Approach

For each character, check whether it appeared earlier.

```java
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String username = scanner.next();
        int distinct = 0;

        for (int i = 0; i < username.length(); i++) {
            boolean seenBefore = false;

            for (int j = 0; j < i; j++) {
                if (username.charAt(i) == username.charAt(j)) {
                    seenBefore = true;
                    break;
                }
            }

            if (!seenBefore) {
                distinct++;
            }
        }

        System.out.println(distinct % 2 == 0 ? "CHAT WITH HER!" : "IGNORE HIM!");
        scanner.close();
    }
}
```

Complexity:

- Time: `O(n^2)`
- Space: `O(1)`

## Best Approach

Use a set or boolean array to count distinct letters.

```java
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String username = scanner.next();
        boolean[] seen = new boolean[26];
        int distinct = 0;

        for (char ch : username.toCharArray()) {
            int index = ch - 'a';

            if (!seen[index]) {
                seen[index] = true;
                distinct++;
            }
        }

        System.out.println(distinct % 2 == 0 ? "CHAT WITH HER!" : "IGNORE HIM!");
        scanner.close();
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(1)`

