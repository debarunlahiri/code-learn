# Algorithms: Bit Manipulation (Easy to Hard)

Goal: Master bit manipulation techniques for efficient problem solving and optimization.

---

## 1. Check Odd or Even

### What it does
Determine if a number is odd or even using bitwise operations.

### Why it matters
- Faster than modulo operation
- Fundamental bit manipulation concept
- Used in parity checks
- Foundation for more complex bit operations

### Intuition
LSB (Least Significant Bit) determines parity. If LSB is 1, number is odd; if 0, even. Like checking the last digit in binary - 1 means odd, 0 means even.

### When to use
- Quick parity checks
- Performance-critical code
- Bit manipulation problems
- Understanding binary representation

### Time complexity
- `O(1)`
- Space: `O(1)`

### Edge cases
- Negative numbers (works due to two's complement)
- Zero (even)
- Large integers
- Integer overflow concerns

### Java code
```java
public class OddEvenBit {
    static boolean isOdd(int x) {
        return (x & 1) == 1;
    }
}
```

---

## 2. Count Set Bits (Brian Kernighan)

### What it does
Efficiently count the number of 1s (set bits) in binary representation.

### Why it matters
- Faster than checking each bit individually
- Used in population count problems
- Foundation for many bit algorithms
- Essential for optimization problems

### Intuition
x & (x-1) clears the rightmost set bit. Each iteration removes one 1, so loop runs exactly count of set bits times. Like turning off lights one by one until all are off.

### When to use
- Count set bits in numbers
- Hamming distance calculations
- Population count problems
- Bit manipulation optimizations

### Time complexity
- `O(k)` where k = number of set bits
- Space: `O(1)`

### Edge cases
- Zero (no set bits)
- All bits set (maximum iterations)
- Negative numbers (two's complement)
- Large bit lengths

### Java code
```java
public class CountSetBits {
    static int count(int x) {
        int c = 0;
        while (x != 0) {
            x = x & (x - 1);
            c++;
        }
        return c;
    }
}
```

---

## 3. Find Single Number (others appear twice)

### What it does
Find the unique number in an array where all other numbers appear exactly twice.

### Why it matters
- Elegant solution using XOR properties
- O(n) time, O(1) space
- Common interview problem
- Demonstrates XOR properties

### Intuition
XOR of a number with itself is 0, and XOR with 0 is the number. All pairs cancel out, leaving the single number. Like pairing up identical items - they disappear, leaving the odd one out.

### When to use
- Find unique element in pairs
- Remove duplicates using XOR
- Bit manipulation puzzles
- Memory-efficient solutions

### Time complexity
- `O(n)`
- Space: `O(1)`

### Edge cases
- Empty array (return 0)
- All elements appear twice (result 0)
- Single element array
- Large arrays

### Java code
```java
public class SingleNumber {
    static int single(int[] nums) {
        int ans = 0;
        for (int x : nums) ans ^= x;
        return ans;
    }
}
```

---

## 4. Power of Two

### What it does
Check if a number is a power of two using bitwise operations.

### Why it matters
- Efficient power-of-two check
- Used in memory allocation
- Binary tree problems
- Bit manipulation fundamentals

### Intuition
Power of two has exactly one set bit. n & (n-1) clears the rightmost set bit. If result is 0, there was only one set bit. Like a power of two in binary is always 1 followed by zeros.

### When to use
- Check if number is power of two
- Memory alignment problems
- Binary tree height checks
- Bit manipulation practice

### Time complexity
- `O(1)`
- Space: `O(1)`

### Edge cases
- Zero and negative numbers
- Integer overflow
- Large powers of two
- Non-integer inputs

### Java code
```java
public class PowerOfTwo {
    static boolean isPowerOfTwo(int n) {
        return n > 0 && (n & (n - 1)) == 0;
    }
}
```

---

## 5. Subsets using Bitmask

### What it does
Generate all subsets of an array using bitmask representation.

### Why it matters
- Elegant subset generation
- O(2^n) time complexity
- Used in combinatorial problems
- Foundation for backtracking alternatives

### Intuition
Each bit in mask represents whether to include an element. 2^n possible masks = 2^n subsets. Like each element has an on/off switch - all combinations of switches give all subsets.

### When to use
- Generate all subsets
- Combinatorial problems
- Power set generation
- Alternative to backtracking

### Time complexity
- `O(n * 2^n)`
- Space: `O(2^n)` for storing all subsets

### Edge cases
- Empty array (only empty subset)
- Large arrays (exponential growth)
- Memory constraints
- Duplicate elements

### Java code
```java
import java.util.ArrayList;
import java.util.List;

public class SubsetsBitmask {
    static List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> ans = new ArrayList<>();
        int n = nums.length;
        int total = 1 << n;

        for (int mask = 0; mask < total; mask++) {
            List<Integer> cur = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) cur.add(nums[i]);
            }
            ans.add(cur);
        }
        return ans;
    }
}
```

---

## 6. Maximum XOR Pair (Trie)

### What it does
Find maximum XOR value of any pair in an array using bitwise trie.

### Why it matters
- Efficient O(n) solution
- Advanced bit manipulation
- Used in optimization problems
- Demonstrates trie applications

### Intuition
Build trie of binary representations. For each number, try to take opposite bits to maximize XOR. Like finding the most different binary representation for each number.

### When to use
- Find maximum XOR pair
- Bitwise optimization problems
- Advanced bit manipulation
- Trie practice problems

### Time complexity
- Build: `O(n * 32)`
- Query: `O(n * 32)`
- Space: `O(n * 32)`

### Edge cases
- Empty array
- Single element
- Large numbers
- Memory limits

### Java code
```java
public class MaxXorPair {
    static class Node {
        Node[] child = new Node[2];
    }

    static void insert(Node root, int num) {
        Node cur = root;
        for (int i = 31; i >= 0; i--) {
            int bit = (num >> i) & 1;
            if (cur.child[bit] == null) cur.child[bit] = new Node();
            cur = cur.child[bit];
        }
    }

    static int query(Node root, int num) {
        Node cur = root;
        int best = 0;
        for (int i = 31; i >= 0; i--) {
            int bit = (num >> i) & 1;
            int want = 1 - bit;
            if (cur.child[want] != null) {
                best |= (1 << i);
                cur = cur.child[want];
            } else {
                cur = cur.child[bit];
            }
        }
        return best;
    }
}
```

---

## 7. Find Missing Number

### What it does
Find the missing number in array containing n numbers from 0 to n.

### Why it matters
- O(n) time, O(1) space solution
- Elegant XOR application
- Common interview problem
- Demonstrates bit manipulation power

### Intuition
XOR all numbers from 0 to n and XOR with all array elements. Pairs cancel out, leaving missing number. Like everyone has a partner except the missing one.

### When to use
- Find missing element in sequence
- XOR-based problem solving
- Memory-efficient solutions
- Interview preparation

### Time complexity
- `O(n)`
- Space: `O(1)`

### Edge cases
- Empty array (missing is 0)
- No missing number
- Large n values
- Integer overflow

### Java code
```java
public class MissingNumber {
    static int missing(int[] nums) {
        int n = nums.length;
        int xor = 0;
        
        // XOR all numbers from 0 to n
        for (int i = 0; i <= n; i++) xor ^= i;
        
        // XOR with all array elements
        for (int num : nums) xor ^= num;
        
        return xor;
    }
}
```

---

## 8. Reverse Bits

### What it does
Reverse the bits of a 32-bit unsigned integer.

### Why it matters
- Bit manipulation practice
- Used in networking problems
- Binary operations understanding
- Interview preparation

### Intuition
Swap bits pairwise: first with last, second with second-last, etc. Use masks to extract and position bits. Like reversing a string but with bits.

### When to use
- Bit reversal problems
- Network byte order conversion
- Binary manipulation practice
- Algorithm optimization

### Time complexity
- `O(32)` = `O(1)`
- Space: `O(1)`

### Edge cases
- Zero (remains zero)
- All bits set
- Single bit set
- Large numbers

### Java code
```java
public class ReverseBits {
    static int reverse(int n) {
        int result = 0;
        for (int i = 0; i < 32; i++) {
            result <<= 1;
            result |= (n & 1);
            n >>= 1;
        }
        return result;
    }
}
```

---

## 9. Gray Code

### What it does
Generate Gray code sequence where adjacent numbers differ by exactly one bit.

### Why it matters
- Used in digital systems
- Minimizes errors in bit transitions
- Bit manipulation application
- Algorithmic thinking practice

### Intuition
Gray code of n bits can be generated by: reflect Gray code of n-1 bits and prefix 0 to first half, 1 to second half. Like building up from smaller patterns.

### When to use
- Generate Gray code sequence
- Digital system design
- Error minimization
- Bit manipulation practice

### Time complexity
- `O(2^n)`
- Space: `O(2^n)`

### Edge cases
- n = 0 (only [0])
- n = 1 ([0, 1])
- Large n (exponential growth)
- Memory constraints

### Java code
```java
import java.util.*;

public class GrayCode {
    static List<Integer> grayCode(int n) {
        List<Integer> result = new ArrayList<>();
        result.add(0);
        
        for (int i = 0; i < n; i++) {
            int size = result.size();
            int mask = 1 << i;
            
            for (int j = size - 1; j >= 0; j--) {
                result.add(result.get(j) | mask);
            }
        }
        return result;
    }
}
```

---

## 10. Bitwise AND of Range

### What it does
Find bitwise AND of all numbers in a range [m, n].

### Why it matters
- Efficient range operations
- Bit manipulation optimization
- Common interview problem
- Understanding bit patterns

### Intuition
Find common prefix of m and n in binary. Shift right until m == n, count shifts, then shift back. Like finding where numbers start to differ.

### When to use
- Range bitwise operations
- Bit manipulation optimization
- Interview problems
- Algorithm efficiency

### Time complexity
- `O(32)` = `O(1)`
- Space: `O(1)`

### Edge cases
- m = n (result is m)
- Large ranges
- Zero values
- Integer overflow

### Java code
```java
public class RangeBitwiseAnd {
    static int rangeBitwiseAnd(int m, int n) {
        int shift = 0;
        while (m < n) {
            m >>= 1;
            n >>= 1;
            shift++;
        }
        return m << shift;
    }
}
```

---

## Practice Problems

### Easy
1. **Number of 1 Bits** (Count set bits)
2. **Single Number** (Find unique element)
3. **Power of Two** (Check power of two)

### Medium
1. **Subsets** (Generate all subsets)
2. **Missing Number** (Find missing element)
3. **Reverse Bits** (Bit reversal)

### Hard
1. **Maximum XOR of Two Numbers** (Trie approach)
2. **Gray Code** (Generate sequence)
3. **Bitwise AND of Numbers Range** (Range operations)

---

**Remember:** Bit manipulation offers elegant O(1) solutions for many problems - master these techniques!
