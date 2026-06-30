# 080. Word

Platform: Codeforces  
Difficulty: Easy  
Topic: String

## Problem Statement

Given a word, convert it to either all lowercase or all uppercase.

If the word has more uppercase letters than lowercase letters, convert the whole word to uppercase. Otherwise, convert it to lowercase.

## Constraints

- `1 <= word.length <= 100`
- The word contains lowercase and uppercase English letters.

## Example

Input:

```text
HoUse
```

Output:

```text
house
```

## Brute Force Approach

Count uppercase and lowercase letters, then use built-in conversion.

```java
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String word = scanner.next();

        int uppercase = 0;
        int lowercase = 0;

        for (char ch : word.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                uppercase++;
            } else {
                lowercase++;
            }
        }

        if (uppercase > lowercase) {
            System.out.println(word.toUpperCase());
        } else {
            System.out.println(word.toLowerCase());
        }

        scanner.close();
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(n)`

## Best Approach

The same count-and-convert approach is optimal for this problem.

```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String word = reader.readLine();

        int uppercase = 0;

        for (char ch : word.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                uppercase++;
            }
        }

        if (uppercase > word.length() - uppercase) {
            System.out.println(word.toUpperCase());
        } else {
            System.out.println(word.toLowerCase());
        }
    }
}
```

Complexity:

- Time: `O(n)`
- Space: `O(n)`

