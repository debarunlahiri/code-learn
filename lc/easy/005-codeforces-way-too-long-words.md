# 005. Way Too Long Words

Platform: Codeforces  
Difficulty: Easy  
Topic: String

## Problem Statement

For each given word, shorten it if its length is greater than `10`.

The shortened form should contain:

1. The first character.
2. The number of characters removed from the middle.
3. The last character.

If the word length is `10` or less, print the word unchanged.

## Constraints

- `1 <= n <= 100`
- `1 <= word.length <= 100`
- Words contain lowercase English letters.

## Example

Input:

```text
4
word
localization
internationalization
pneumonoultramicroscopicsilicovolcanoconiosis
```

Output:

```text
word
l10n
i18n
p43s
```

## Brute Force Approach

Build the answer using simple string operations for each word.

```java
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();

        for (int i = 0; i < n; i++) {
            String word = scanner.next();

            if (word.length() > 10) {
                char first = word.charAt(0);
                char last = word.charAt(word.length() - 1);
                int middleCount = word.length() - 2;

                System.out.println("" + first + middleCount + last);
            } else {
                System.out.println(word);
            }
        }

        scanner.close();
    }
}
```

Complexity:

- Time: `O(total characters)`
- Space: `O(1)` apart from input storage.

## Best Approach

The brute-force solution is already optimal because each word must be read at least once. For faster input in competitive programming, use `BufferedReader`.

```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(reader.readLine());
        StringBuilder output = new StringBuilder();

        for (int i = 0; i < n; i++) {
            String word = reader.readLine();

            if (word.length() > 10) {
                output.append(word.charAt(0));
                output.append(word.length() - 2);
                output.append(word.charAt(word.length() - 1));
            } else {
                output.append(word);
            }

            output.append('\n');
        }

        System.out.print(output.toString());
    }
}
```

Complexity:

- Time: `O(total characters)`
- Space: `O(total output characters)`

