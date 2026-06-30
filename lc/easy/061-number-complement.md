# 061. Number Complement

Platform: LeetCode  
Difficulty: Easy  
Topic: Bit Manipulation

## Problem Statement

Given a positive integer `num`, return its complement.

The complement is created by flipping every bit in the binary representation of `num`.

## Constraints

- `1 <= num < 2^31`

## Example

Input:

```text
num = 5
```

Output:

```text
2
```

Explanation: `5` is `101`, and its complement is `010`.

## Brute Force Approach

Convert the number to a binary string, flip the characters, then convert back to an integer.

```java
class Solution {
    public int findComplement(int num) {
        String binary = Integer.toBinaryString(num);
        StringBuilder flipped = new StringBuilder();

        for (char ch : binary.toCharArray()) {
            flipped.append(ch == '0' ? '1' : '0');
        }

        return Integer.parseInt(flipped.toString(), 2);
    }
}
```

Complexity:

- Time: `O(log num)`
- Space: `O(log num)`

## Best Approach

Create a mask with the same number of bits as `num`, then XOR with the mask.

```java
class Solution {
    public int findComplement(int num) {
        int mask = 0;
        int temp = num;

        while (temp > 0) {
            mask = (mask << 1) | 1;
            temp = temp >> 1;
        }

        return num ^ mask;
    }
}
```

Complexity:

- Time: `O(log num)`
- Space: `O(1)`

