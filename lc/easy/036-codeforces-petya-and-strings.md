# 036. Petya and Strings

Platform: Codeforces  
Difficulty: Easy  
Topic: String

## Problem Statement

Given two strings of the same length, compare them lexicographically after converting both to lowercase.

Print:

- `-1` if the first string is smaller.
- `1` if the first string is greater.
- `0` if they are equal.

## Constraints

- `1 <= string.length <= 100`
- Strings contain uppercase and lowercase English letters.

## Example

Input:

```text
aaaa
aaaA
```

Output:

```text
0
```

## Brute Force Approach

Convert both strings to lowercase and use Java's `compareTo`.

```java
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String first = scanner.next().toLowerCase();
        String second = scanner.next().toLowerCase();

        int comparison = first.compareTo(second);

        if (comparison < 0) {
            System.out.println(-1);
        } else if (comparison > 0) {
            System.out.println(1);
        } else {
            System.out.println(0);
        }

        scanner.close();
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(n)`

## Best Approach

Compare characters directly after converting to lowercase. This avoids creating extra lowercase strings.

```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String first = reader.readLine();
        String second = reader.readLine();

        for (int i = 0; i < first.length(); i++) {
            char a = Character.toLowerCase(first.charAt(i));
            char b = Character.toLowerCase(second.charAt(i));

            if (a < b) {
                System.out.println(-1);
                return;
            }

            if (a > b) {
                System.out.println(1);
                return;
            }
        }

        System.out.println(0);
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(1)`

