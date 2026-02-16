# Algorithms: Number Theory and Math (Easy to Hard)

## 1. GCD and LCM
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

## 2. Sieve of Eratosthenes
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

## 3. Fast Modular Exponentiation
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

## 4. Modular Inverse (prime mod, Fermat)
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

## 5. nCr mod p (precompute factorial)
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

