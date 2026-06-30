# 068. Bear and Big Brother

Platform: Codeforces  
Difficulty: Easy  
Topic: Math, Loop

## Problem Statement

Limak has weight `a`, and Bob has weight `b`. Every year, Limak's weight triples and Bob's weight doubles.

Return the number of full years needed until Limak becomes strictly heavier than Bob.

## Constraints

- `1 <= a <= b <= 10`

## Example

Input:

```text
4 7
```

Output:

```text
2
```

## Brute Force Approach

Simulate year by year until Limak's weight becomes greater.

```java
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int years = 0;

        while (a <= b) {
            a *= 3;
            b *= 2;
            years++;
        }

        System.out.println(years);
        scanner.close();
    }
}
```

Complexity:

- Time: `O(years)`
- Space: `O(1)`

## Best Approach

The simulation is already optimal and easiest to understand because the values grow quickly.

```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer tokenizer = new StringTokenizer(reader.readLine());

        int limak = Integer.parseInt(tokenizer.nextToken());
        int bob = Integer.parseInt(tokenizer.nextToken());
        int years = 0;

        while (limak <= bob) {
            limak *= 3;
            bob *= 2;
            years++;
        }

        System.out.println(years);
    }
}
```

Complexity:

- Time: `O(years)`
- Space: `O(1)`

