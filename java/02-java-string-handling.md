# Java String Handling - Complete Guide (Basics to Advanced)

**Target:** Skill enhancement for WITCH companies (Wipro, Infosys, TCS, Cognizant, HCL) and beyond.  
**Covers:** String Internals, String Pool, Immutability, StringBuilder, StringBuffer, Regex, and tricky scenarios.

---

## Table of Contents

1. [String Basics & Immutability](#string-basics-immutability)
2. [String Pool (Interning)](#string-pool-interning)
3. [String vs StringBuilder vs StringBuffer](#string-vs-stringbuilder-vs-stringbuffer)
4. [String Methods](#string-methods)
5. [String Comparison](#string-comparison)
6. [String Formatting](#string-formatting)
7. [Regular Expressions (Regex)](#regular-expressions-regex)
8. [Text Blocks (Java 13+)](#text-blocks-java-13)
9. [Common Coding Challenges](#common-coding-challenges)
10. [Complex Technical Scenarios](#complex-technical-scenarios)
11. [Key Topics & Explanations](#key-topics-explanations)

---

## 1. String Basics & Immutability

### Why is String Immutable?

1. **String Pool**: Allows sharing — if Strings were mutable, changing one would affect all references.
2. **Security**: Used in DB connections, file paths, network URLs — modification would be a security risk.
3. **Thread Safety**: Immutable objects are inherently thread-safe — no synchronization needed.
4. **Caching HashCode**: `hashCode()` is cached on first call — enables fast HashMap lookup.
5. **ClassLoader**: Class names are Strings — if mutable, someone could alter the class being loaded.

### How Immutability Works

```java
String s = "Hello";
s.concat(" World"); // Creates a NEW String — 's' is unchanged
System.out.println(s); // "Hello" — NOT "Hello World"

s = s.concat(" World"); // Now 's' points to the new String
System.out.println(s); // "Hello World"
```

### Internally

```java
// String is backed by a final char[] (Java 8) or byte[] (Java 9+)
public final class String {
    private final byte[] value; // Java 9+ (Compact Strings)
    private int hash;           // Cached hashCode
    // No setter methods — immutable
}
```

---

## 2. String Pool (Interning)

The String Pool (String Intern Pool) is a special memory area inside the **Heap** (since Java 7 — was in PermGen before).

### How it Works

```java
// 1. String Literal — goes to Pool
String s1 = "Hello";  // Creates in Pool
String s2 = "Hello";  // Reuses from Pool

System.out.println(s1 == s2);      // true (same reference)
System.out.println(s1.equals(s2)); // true (same content)

// 2. new String() — goes to Heap (NOT Pool)
String s3 = new String("Hello"); // Creates in Heap (+ Pool if "Hello" not there)
String s4 = new String("Hello"); // Creates ANOTHER object in Heap

System.out.println(s3 == s4);      // false (different references)
System.out.println(s3.equals(s4)); // true (same content)

// 3. Comparing Pool vs Heap
System.out.println(s1 == s3);      // false (Pool ref != Heap ref)

// 4. intern() — force into Pool
String s5 = s3.intern();
System.out.println(s1 == s5);      // true (both point to Pool)
```

### Tricky Scenario ⚠️

```java
// How many objects are created?
String s = new String("ABC");

// Answer: 2 objects (if "ABC" not already in pool)
// 1. "ABC" literal → String Pool
// 2. new String("ABC") → Heap

// If "ABC" already exists in Pool → only 1 object (Heap)
```

### String Concatenation Internals

```java
// Compile-time constants are resolved at compile time
String s1 = "Hello" + " World"; // Compiler optimizes to "Hello World" (Pool)
String s2 = "Hello World";
System.out.println(s1 == s2); // true (same Pool reference)

// Variables are NOT resolved at compile time
String a = "Hello";
String b = a + " World"; // Uses StringBuilder internally → Heap
System.out.println(b == s2); // false

// final variables ARE resolved at compile time
final String c = "Hello";
String d = c + " World"; // Compiler knows c is constant → "Hello World" (Pool)
System.out.println(d == s2); // true
```

---

## 3. String vs StringBuilder vs StringBuffer

| Feature | String | StringBuilder | StringBuffer |
|---------|--------|---------------|--------------|
| **Mutability** | Immutable | Mutable | Mutable |
| **Thread Safety** | ✅ (immutable) | ❌ (not synchronized) | ✅ (synchronized) |
| **Performance** | Slow for concatenation | Fastest | Slower than StringBuilder |
| **Storage** | String Pool + Heap | Heap | Heap |
| **Since** | JDK 1.0 | JDK 1.5 | JDK 1.0 |
| **Use When** | Few modifications | Single-threaded + many modifications | Multi-threaded + many modifications |

### StringBuilder Usage

```java
StringBuilder sb = new StringBuilder("Hello");

sb.append(" World");     // "Hello World"
sb.insert(5, ",");       // "Hello, World"
sb.replace(0, 5, "Hi");  // "Hi, World"
sb.delete(2, 4);         // "Hi World"
sb.reverse();            // "dlroW iH"

String result = sb.toString(); // Convert back to String

// Chaining
String name = new StringBuilder()
    .append("Rahul")
    .append(" ")
    .append("Kumar")
    .toString(); // "Rahul Kumar"
```

### Performance Comparison

```java
// BAD — String concatenation in loop (O(n²) — creates n objects)
String result = "";
for (int i = 0; i < 10000; i++) {
    result += i; // Each += creates a NEW String
}

// GOOD — StringBuilder (O(n) — single mutable object)
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 10000; i++) {
    sb.append(i);
}
String result = sb.toString();
```

---

## 4. String Methods

### Essential Methods

```java
String str = "Hello World";

// Length
str.length();                    // 11

// Character access
str.charAt(0);                   // 'H'
str.codePointAt(0);              // 72 (Unicode of 'H')

// Substring
str.substring(6);               // "World"
str.substring(0, 5);            // "Hello"

// Search
str.indexOf("World");           // 6
str.lastIndexOf('l');           // 9
str.contains("World");          // true
str.startsWith("Hello");        // true
str.endsWith("World");          // true

// Case
str.toUpperCase();              // "HELLO WORLD"
str.toLowerCase();              // "hello world"

// Trim
"  Hello  ".trim();             // "Hello" (removes leading/trailing spaces)
"  Hello  ".strip();            // "Hello" (Java 11 — Unicode-aware)
"  Hello  ".stripLeading();     // "Hello  "
"  Hello  ".stripTrailing();    // "  Hello"

// Replace
str.replace('l', 'r');          // "Herro Worrd"
str.replace("World", "Java");   // "Hello Java"
str.replaceAll("[aeiou]", "*"); // "H*ll* W*rld" (regex)
str.replaceFirst("[aeiou]", "*"); // "H*llo World"

// Split
"a,b,c,d".split(",");          // ["a", "b", "c", "d"]
"a,,b".split(",", -1);         // ["a", "", "b"] (preserve empty strings)

// Join (Java 8+)
String.join(", ", "a", "b", "c"); // "a, b, c"

// Check
str.isEmpty();                   // false ("" is empty)
str.isBlank();                   // false (Java 11 — " " is blank, not empty)

// Repeat (Java 11)
"Ha".repeat(3);                 // "HaHaHa"

// toCharArray
char[] chars = str.toCharArray(); // ['H','e','l','l','o',' ','W','o','r','l','d']

// valueOf
String.valueOf(123);            // "123"
String.valueOf(true);           // "true"
String.valueOf(new char[]{'a','b'}); // "ab"
```

---

## 5. String Comparison

```java
String s1 = "Hello";
String s2 = "Hello";
String s3 = new String("Hello");

// == compares REFERENCES
s1 == s2;           // true (same Pool reference)
s1 == s3;           // false (Pool vs Heap)

// .equals() compares CONTENT (always use this!)
s1.equals(s2);      // true
s1.equals(s3);      // true

// .equalsIgnoreCase()
"hello".equalsIgnoreCase("HELLO"); // true

// .compareTo() — lexicographic
"abc".compareTo("abd");  // -1 (abc < abd)
"abc".compareTo("abc");  //  0 (equal)
"abd".compareTo("abc");  //  1 (abd > abc)

// Null-safe comparison
Objects.equals(s1, null);        // false (no NPE)
// s1.equals(null);              // false (safe — Java spec)
// null.equals(s1);              // NullPointerException!

// Best practice: put literal first
"Hello".equals(userInput);       // ✅ Never throws NPE
// userInput.equals("Hello");    // ❌ NPE if userInput is null
```

---

## 6. String Formatting

```java
// printf style
String formatted = String.format("Name: %s, Age: %d, Score: %.2f", "Rahul", 25, 95.567);
// "Name: Rahul, Age: 25, Score: 95.57"

// Common format specifiers
// %s — String
// %d — Integer
// %f — Float/Double
// %b — Boolean
// %n — Platform-specific newline
// %t — Date/Time

// Formatted (Java 15+)
String result = "Name: %s, Age: %d".formatted("Amit", 30);
// "Name: Amit, Age: 30"
```

---

## 7. Regular Expressions (Regex)

### Pattern Matching

```java
import java.util.regex.*;

// Basic check
boolean isMatch = "hello123".matches("[a-z]+\\d+"); // true

// Pattern & Matcher
Pattern pattern = Pattern.compile("\\d+");
Matcher matcher = pattern.matcher("Order 123 has 5 items");

while (matcher.find()) {
    System.out.println(matcher.group()); // "123", then "5"
}

// Groups
Pattern p = Pattern.compile("(\\d{2})-(\\d{2})-(\\d{4})");
Matcher m = p.matcher("DOB: 15-08-1947");
if (m.find()) {
    System.out.println(m.group(0)); // "15-08-1947" (full match)
    System.out.println(m.group(1)); // "15" (day)
    System.out.println(m.group(2)); // "08" (month)
    System.out.println(m.group(3)); // "1947" (year)
}
```

### Common Patterns

```java
// Email validation
String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

// Phone number (Indian)
String phoneRegex = "^[6-9]\\d{9}$";

// Password (Min 8 chars, 1 upper, 1 lower, 1 digit, 1 special)
String passRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=]).{8,}$";

// IP Address
String ipRegex = "^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}$";
```

---

## 8. Text Blocks (Java 13+)

```java
// Before — messy string with escapes
String json = "{\n" +
              "  \"name\": \"Rahul\",\n" +
              "  \"age\": 25\n" +
              "}";

// After — clean text block
String json = """
        {
          "name": "Rahul",
          "age": 25
        }
        """;

// SQL
String sql = """
        SELECT e.name, d.dept_name
        FROM employees e
        JOIN departments d ON e.dept_id = d.id
        WHERE e.salary > 50000
        ORDER BY e.name
        """;

// HTML
String html = """
        <html>
            <body>
                <h1>Hello</h1>
            </body>
        </html>
        """;
```

---

## 9. Common Coding Challenges

### Problem 1: Reverse a String

```java
// Using StringBuilder
String reversed = new StringBuilder("Hello").reverse().toString(); // "olleH"

// Without StringBuilder
public static String reverse(String str) {
    char[] chars = str.toCharArray();
    int left = 0, right = chars.length - 1;
    while (left < right) {
        char temp = chars[left];
        chars[left] = chars[right];
        chars[right] = temp;
        left++;
        right--;
    }
    return new String(chars);
}
```

### Problem 2: Check Palindrome

```java
public static boolean isPalindrome(String s) {
    s = s.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
    return s.equals(new StringBuilder(s).reverse().toString());
}
```

### Problem 3: Count Character Occurrences

```java
// Using Streams
long count = "programming".chars()
    .filter(c -> c == 'g')
    .count(); // 2

// Using Map for all characters
Map<Character, Long> freq = "programming".chars()
    .mapToObj(c -> (char) c)
    .collect(Collectors.groupingBy(c -> c, Collectors.counting()));
// {p=1, r=2, o=1, g=2, a=1, m=2, i=1, n=1}
```

### Problem 4: Find First Non-Repeating Character

```java
public static char firstNonRepeating(String str) {
    LinkedHashMap<Character, Integer> map = new LinkedHashMap<>();
    for (char c : str.toCharArray()) {
        map.put(c, map.getOrDefault(c, 0) + 1);
    }
    for (Map.Entry<Character, Integer> entry : map.entrySet()) {
        if (entry.getValue() == 1) return entry.getKey();
    }
    return '\0';
}
// firstNonRepeating("aabbccd") → 'd'
```

### Problem 5: Check Anagram

```java
public static boolean isAnagram(String s1, String s2) {
    if (s1.length() != s2.length()) return false;
    char[] a = s1.toLowerCase().toCharArray();
    char[] b = s2.toLowerCase().toCharArray();
    Arrays.sort(a);
    Arrays.sort(b);
    return Arrays.equals(a, b);
}
// isAnagram("listen", "silent") → true
```

### Problem 6: Remove Duplicates from String

```java
public static String removeDuplicates(String str) {
    return str.chars()
        .distinct()
        .mapToObj(c -> String.valueOf((char) c))
        .collect(Collectors.joining());
}
// removeDuplicates("programming") → "progamin"
```

---

## 10. Complex Technical Scenarios

### Topic 1: String s = new String("abc") — How many objects?

**Explanation:**
- If `"abc"` is NOT in the Pool: **2 objects** (1 in Pool + 1 in Heap)
- If `"abc"` IS already in Pool: **1 object** (only Heap)

### Topic 2: Why shouldn't you use `==` for String comparison?

**Explanation:** 
`==` compares **references** (memory addresses), not content. Strings created via `new` keyword are in Heap and have different references even with same content. Always use `.equals()`.

### Topic 3: String concatenation with `+` inside loop — why bad?

**Explanation:**
Each `+` creates a new String object and copies old content + new content. In a loop of N iterations, this is **O(n²)** time and creates N temporary objects for GC. Use `StringBuilder` instead — O(n) time, 1 object.

### Topic 4: Compact Strings (Java 9+)

**Explanation:**
Before Java 9, String used `char[]` (2 bytes per character). Java 9 introduced Compact Strings using `byte[]` with an encoding flag:
- **LATIN1** (1 byte per char): For ASCII/Latin characters
- **UTF-16** (2 bytes per char): For other characters

This reduces memory usage by ~50% for Latin-based languages.

---

## 11. Key Topics & Explanations

### Topic 1: String Pool location — heap or stack?

**Explanation:**
String Pool is inside the **Heap** (since Java 7). Before Java 7, it was in PermGen (which was part of Method Area). In Java 8, PermGen was replaced by Metaspace, but String Pool stayed in Heap.

### Topic 2: Can we make String mutable using Reflection?

**Explanation:** Technically yes (by accessing the private `value` field), but it's **extremely dangerous** and breaks the contract of immutability. Java 9+ modules restrict this further with `--illegal-access` flags.

### Topic 3: What is String.intern()?

**Explanation:**
`intern()` returns the canonical representation from the String Pool. If the Pool already has an equal String, it returns that reference. If not, it adds this String to the Pool and returns its reference. Useful for saving memory when many duplicate Strings exist.
