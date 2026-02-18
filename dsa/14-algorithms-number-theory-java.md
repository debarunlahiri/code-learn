# Algorithms: Number Theory and Math (Easy to Hard)

Goal: Master fundamental number theory algorithms and mathematical concepts for programming.

---

## 1. GCD and LCM

### What it does
Find the greatest common divisor and least common multiple of two numbers.

### Why it matters
- Fundamental for fraction operations
- Used in many number theory problems
- Basis for modular arithmetic
- Essential for competitive programming

### Intuition
GCD is the largest number that divides both. Euclidean algorithm repeatedly replaces larger number with remainder until remainder is 0. LCM = (a × b) / GCD. Like finding the biggest shared piece and the smallest combined size.

### When to use
- Simplifying fractions
- Finding common denominators
- Periodic problems
- Number theory calculations

### Time complexity
- GCD: `O(log min(a,b))`
- LCM: `O(1)` after GCD
- Space: `O(1)`

### Edge cases
- One or both numbers are 0
- Negative numbers
- Large numbers (use long)
- Overflow in LCM calculation

### Java code
```java
public class GcdLcm {
    static int gcd(int a, int b) {
        while (b != 0) {
            int t = a % b;
            a = b;
            b = t;
        }
        return a;
    }

    static long lcm(int a, int b) {
        return (long) a / gcd(a, b) * b;
    }
}
```

---

## 2. Sieve of Eratosthenes

### What it does
Efficiently find all prime numbers up to a given limit.

### Why it matters
- Prime number generation
- Factorization problems
- Cryptography applications
- Foundation for many number theory algorithms

### Intuition
Mark multiples of each prime starting from 2. Unmarked numbers are primes. Like crossing out multiples on a number grid - what remains uncrossed are primes.

### When to use
- Generate all primes up to n
- Prime factorization
- Euler's totient function
- Prime counting problems

### Time complexity
- Preprocessing: `O(n log log n)`
- Query: `O(1)` for primality check
- Space: `O(n)`

### Edge cases
- n < 2 (no primes)
- Large n (memory concerns)
- Multiple test cases (reuse sieve)

### Java code
```java
import java.util.ArrayList;
import java.util.List;

public class PrimeSieve {
    static List<Integer> primesUpTo(int n) {
        boolean[] isPrime = new boolean[n + 1];
        for (int i = 2; i <= n; i++) isPrime[i] = true;

        for (int p = 2; p * p <= n; p++) {
            if (isPrime[p]) {
                for (int m = p * p; m <= n; m += p) isPrime[m] = false;
            }
        }

        List<Integer> ans = new ArrayList<>();
        for (int i = 2; i <= n; i++) if (isPrime[i]) ans.add(i);
        return ans;
    }
}
```

---

## 3. Fast Modular Exponentiation

### What it does
Compute (base^exp) % mod efficiently using binary exponentiation.

### Why it matters
- Handle large powers without overflow
- Essential for modular arithmetic
- Used in cryptography (RSA)
- Foundation for many number theory algorithms

### Intuition
Use binary representation of exponent. Square base and halve exponent repeatedly. Multiply result only when exponent bit is 1. Like building the power using powers of 2.

### When to use
- Large exponent calculations
- Modular arithmetic problems
- Cryptographic computations
- Matrix exponentiation

### Time complexity
- `O(log exp)`
- Space: `O(1)`

### Edge cases
- exp = 0 (result is 1)
- mod = 1 (result is 0)
- Large mod values
- Negative exponents (need modular inverse)

### Java code
```java
public class ModPower {
    static long modPow(long base, long exp, long mod) {
        long ans = 1 % mod;
        base %= mod;
        while (exp > 0) {
            if ((exp & 1) == 1) ans = (ans * base) % mod;
            base = (base * base) % mod;
            exp >>= 1;
        }
        return ans;
    }
}
```

---

## 4. Modular Inverse (prime mod, Fermat)

### What it does
Find the modular inverse of a number under a prime modulus.

### Why it matters
- Division in modular arithmetic
- Solving modular equations
- Essential for combinatorics
- Used in cryptographic algorithms

### Intuition
For prime modulus p, a^(p-2) ≡ a^(-1) (mod p) by Fermat's Little Theorem. Like finding the number that "undoes" multiplication in modular arithmetic.

### When to use
- Division operations in modular arithmetic
- Combinatorial calculations
- Solving linear congruences
- CRT (Chinese Remainder Theorem)

### Time complexity
- `O(log p)` using fast exponentiation
- Space: `O(1)`

### Edge cases
- a = 0 (no inverse)
- Non-prime modulus (need extended Euclidean)
- Large prime values
- a and p not coprime

### Java code
```java
public class ModularInverse {
    static long modPow(long base, long exp, long mod) {
        long ans = 1 % mod;
        base %= mod;
        while (exp > 0) {
            if ((exp & 1) == 1) ans = (ans * base) % mod;
            base = (base * base) % mod;
            exp >>= 1;
        }
        return ans;
    }

    static long modInversePrime(long a, long primeMod) {
        return modPow(a, primeMod - 2, primeMod);
    }
}
```

---

## 5. nCr mod p (precompute factorial)

### What it does
Efficiently compute combinations (n choose r) modulo a prime.

### Why it matters
- Combinatorial problems
- Probability calculations
- Dynamic programming with combinations
- Essential for many counting problems

### Intuition
nCr = n! / (r! × (n-r)!) mod p. Precompute factorials and their modular inverses. Use Fermat's theorem for division. Like having lookup tables for all needed factorials.

### When to use
- Multiple combination queries
- Large n, r values
- Probability problems
- Combinatorial DP

### Time complexity
- Preprocessing: `O(n)`
- Query: `O(1)`
- Space: `O(n)`

### Edge cases
- r > n or r < 0 (result is 0)
- Large n (memory concerns)
- Prime modulus requirement
- Overflow in factorial calculations

### Java code
```java
public class CombinationMod {
    static final long MOD = 1_000_000_007L;
    static long[] fact;
    static long[] invFact;

    static long modPow(long b, long e) {
        long ans = 1;
        b %= MOD;
        while (e > 0) {
            if ((e & 1) == 1) ans = (ans * b) % MOD;
            b = (b * b) % MOD;
            e >>= 1;
        }
        return ans;
    }

    static void build(int n) {
        fact = new long[n + 1];
        invFact = new long[n + 1];
        fact[0] = 1;
        for (int i = 1; i <= n; i++) fact[i] = (fact[i - 1] * i) % MOD;
        invFact[n] = modPow(fact[n], MOD - 2);
        for (int i = n - 1; i >= 0; i--) invFact[i] = (invFact[i + 1] * (i + 1)) % MOD;
    }

    static long nCr(int n, int r) {
        if (r < 0 || r > n) return 0;
        return (((fact[n] * invFact[r]) % MOD) * invFact[n - r]) % MOD;
    }
}
```

---

## 6. Extended Euclidean Algorithm

### What it does
Find integers x, y such that ax + by = gcd(a,b). Also finds modular inverse for non-prime moduli.

### Why it matters
- Solving Diophantine equations
- Finding modular inverses for any modulus
- CRT implementation
- Cryptographic applications

### Intuition
Extended Euclidean algorithm tracks coefficients during GCD calculation. Backtrack through GCD steps to find x and y. Like keeping track of how we combined numbers to get the GCD.

### When to use
- Modular inverse for non-prime modulus
- Solving linear Diophantine equations
- CRT implementation
- Finding solutions to ax ≡ b (mod m)

### Time complexity
- `O(log min(a,b))`
- Space: `O(log min(a,b))` for recursion stack

### Edge cases
- a = b = 0
- Negative numbers
- Large values
- Non-coprime numbers for modular inverse

### Java code
```java
public class ExtendedEuclidean {
    static class Result {
        long x, y, gcd;
        Result(long x, long y, long gcd) {
            this.x = x; this.y = y; this.gcd = gcd;
        }
    }

    static Result extendedGcd(long a, long b) {
        if (b == 0) return new Result(1, 0, a);
        Result res = extendedGcd(b, a % b);
        return new Result(res.y, res.x - (a / b) * res.y, res.gcd);
    }

    static long modInverse(long a, long m) {
        Result res = extendedGcd(a, m);
        if (res.gcd != 1) return -1; // No inverse exists
        return (res.x % m + m) % m;
    }
}
```

---

## 7. Euler's Totient Function

### What it does
Count numbers less than n that are coprime with n.

### Why it matters
- Number theory problems
- RSA cryptography
- Counting coprime numbers
- Reduced residue systems

### Intuition
φ(n) = n × Π(1 - 1/p) for all prime factors p of n. Count numbers not sharing any prime factors with n. Like counting numbers that are "relatively prime" to n.

### When to use
- Counting coprime numbers
- Euler's theorem applications
- Cryptographic calculations
- Number theory problems

### Time complexity
- Single query: `O(√n)`
- Preprocessing: `O(n log log n)` with sieve
- Space: `O(n)` for preprocessing

### Edge cases
- n = 1 (φ(1) = 1)
- Prime numbers (φ(p) = p-1)
- Powers of primes
- Large n values

### Java code
```java
public class EulerTotient {
    static int phi(int n) {
        int result = n;
        for (int p = 2; p * p <= n; p++) {
            if (n % p == 0) {
                while (n % p == 0) n /= p;
                result -= result / p;
            }
        }
        if (n > 1) result -= result / n;
        return result;
    }

    // Sieve approach for multiple queries
    static int[] phiSieve(int n) {
        int[] phi = new int[n + 1];
        for (int i = 0; i <= n; i++) phi[i] = i;
        
        for (int p = 2; p <= n; p++) {
            if (phi[p] == p) { // p is prime
                for (int i = p; i <= n; i += p) {
                    phi[i] -= phi[i] / p;
                }
            }
        }
        return phi;
    }
}
```

---

## 8. Chinese Remainder Theorem

### What it does
Find number x that satisfies multiple congruences: x ≡ a₁ (mod n₁), x ≡ a₂ (mod n₂), ...

### Why it matters
- Solving systems of congruences
- Cryptography applications
- Number theory problems
- Calendar problems

### Intuition
Combine congruences pairwise using extended Euclidean algorithm. Like finding a number that leaves specific remainders when divided by different numbers.

### When to use
- Systems of congruences
- Calendar calculations
- Cryptographic protocols
- Number theory puzzles

### Time complexity
- `O(k log M)` where k is number of congruences, M is product of moduli
- Space: `O(1)`

### Edge cases
- Non-coprime moduli
- No solution exists
- Large numbers
- Overflow concerns

### Java code
```java
public class ChineseRemainder {
    static class Pair {
        long first, second;
        Pair(long first, long second) {
            this.first = first; this.second = second;
        }
    }

    static Pair extendedGcd(long a, long b) {
        if (b == 0) return new Pair(1, 0);
        Pair p = extendedGcd(b, a % b);
        return new Pair(p.second, p.first - (a / b) * p.second);
    }

    static long crt(long[] num, long[] rem) {
        int k = num.length;
        long x = rem[0];
        long currMod = num[0];

        for (int i = 1; i < k; i++) {
            Pair p = extendedGcd(currMod, num[i]);
            long gcd = p.first * currMod + p.second * num[i];
            
            if ((rem[i] - x) % gcd != 0) return -1; // No solution
            
            long lcm = currMod / gcd * num[i];
            x = (x + (rem[i] - x) / gcd * p.first % (num[i] / gcd) * currMod) % lcm;
            currMod = lcm;
        }
        return (x + currMod) % currMod;
    }
}
```

---

## Practice Problems

### Easy
1. **GCD of Array Numbers** (Basic GCD application)
2. **Prime Check** (Modified sieve)
3. **Power of Two** (Modular exponentiation)

### Medium
1. **Euler's Totient Function** (Number theory)
2. **Modular Inverse** (Extended Euclidean)
3. **Combination Calculation** (nCr mod p)

### Hard
1. **Chinese Remainder Theorem** (System of congruences)
2. **Discrete Logarithm** (Advanced number theory)
3. **Prime Factorization** (Advanced sieve techniques)

---

**Remember:** Number theory is foundational - master these concepts for advanced algorithms and cryptography!

