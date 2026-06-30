# 040. Count Primes

Platform: LeetCode  
Difficulty: Easy  
Topic: Math, Sieve

## Problem Statement

Given an integer `n`, return the number of prime numbers strictly less than `n`.

## Constraints

- `0 <= n <= 5 * 10^6`

## Example

Input:

```text
n = 10
```

Output:

```text
4
```

Explanation: The primes less than `10` are `2`, `3`, `5`, and `7`.

## Brute Force Approach

Check every number from `2` to `n - 1` and test whether it is prime.

```java
class Solution {
    public int countPrimes(int n) {
        int count = 0;

        for (int number = 2; number < n; number++) {
            if (isPrime(number)) {
                count++;
            }
        }

        return count;
    }

    private boolean isPrime(int number) {
        for (int divisor = 2; divisor * divisor <= number; divisor++) {
            if (number % divisor == 0) {
                return false;
            }
        }

        return true;
    }
}
```

Complexity:

- Time: `O(n * sqrt(n))`
- Space: `O(1)`

## Best Approach

Use the Sieve of Eratosthenes. Mark multiples of each prime as not prime.

```java
class Solution {
    public int countPrimes(int n) {
        if (n <= 2) {
            return 0;
        }

        boolean[] isPrime = new boolean[n];

        for (int i = 2; i < n; i++) {
            isPrime[i] = true;
        }

        for (int number = 2; number * number < n; number++) {
            if (isPrime[number]) {
                for (int multiple = number * number; multiple < n; multiple += number) {
                    isPrime[multiple] = false;
                }
            }
        }

        int count = 0;

        for (int i = 2; i < n; i++) {
            if (isPrime[i]) {
                count++;
            }
        }

        return count;
    }
}
```

Complexity:

- Time: `O(n log log n)`
- Space: `O(n)`

