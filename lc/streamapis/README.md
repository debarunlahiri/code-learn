# Java Stream API Coding Questions

Platform: Java Practice  
Difficulty: Easy to Hard  
Topic: Java 8+, Stream API, Collections, Collectors

Use these questions to practice common Java Stream API interview patterns. Each answer
assumes these imports when needed:

Full runnable method implementations are available here:
[StreamApiInterviewSolutions.java](StreamApiInterviewSolutions.java).

```java
import java.util.*;
import java.util.function.*;
import java.util.stream.*;
```

## Easy

1. Given `List<Integer> nums`, write a Stream API solution to return only the even numbers.

```java
List<Integer> ans = nums.stream().filter(n -> n % 2 == 0).toList();
```

2. Given `List<Integer> nums`, write a Stream API solution to return only the odd numbers.

```java
List<Integer> ans = nums.stream().filter(n -> n % 2 != 0).toList();
```

3.  Given `List<Integer> nums`, write a Stream API solution to return a new list containing
    the square of each number.

```java
List<Integer> ans = nums.stream().map(n -> n * n).toList();
```

4.  Given `List<String> words`, write a Stream API solution to convert every word to
    uppercase.

```java
List<String> ans = words.stream().map(String::toUpperCase).toList();
```

5.  Given `List<String> words`, write a Stream API solution to convert every word to
    lowercase.

```java
List<String> ans = words.stream().map(String::toLowerCase).toList();
```

6.  Given `List<Integer> nums`, write a Stream API solution to count how many numbers are
    greater than `10`.

```java
long ans = nums.stream().filter(n -> n > 10).count();
```

7.  Given `List<Integer> nums`, how will you get the first element using Stream API without
    directly using index `0`?

```java
Optional<Integer> ans = nums.stream().findFirst();
```

8. Given `List<Integer> nums`, how will you get any one element using Stream API?

```java
Optional<Integer> ans = nums.stream().findAny();
```

9.  Given `List<Integer> nums`, write a Stream API solution to check whether all numbers are
    positive.

```java
boolean ans = nums.stream().allMatch(n -> n > 0);
```

10. Given `List<Integer> nums`, write a Stream API solution to check whether at least one
    number is negative.

```java
boolean ans = nums.stream().anyMatch(n -> n < 0);
```

11. Given `List<Integer> nums`, write a Stream API solution to check whether the list
    contains no zero.

```java
boolean ans = nums.stream().noneMatch(n -> n == 0);
```

12. Given `List<Integer> nums` with duplicate values, write a Stream API solution to return
    only unique numbers.

```java
List<Integer> ans = nums.stream().distinct().toList();
```

13. Given `List<Integer> nums`, write a Stream API solution to sort the numbers in
    ascending order.

```java
List<Integer> ans = nums.stream().sorted().toList();
```

14. Given `List<Integer> nums`, write a Stream API solution to sort the numbers in
    descending order.

```java
List<Integer> ans = nums.stream().sorted(Comparator.reverseOrder()).toList();
```

15. Given `List<String> words`, write a Stream API solution to sort the strings
    alphabetically.

```java
List<String> ans = words.stream().sorted().toList();
```

16. Given `List<String> words`, write a Stream API solution to sort the strings by length.

```java
List<String> ans = words.stream().sorted(Comparator.comparingInt(String::length)).toList();
```

17. Given `List<Integer> nums`, write a Stream API solution to return only the first `5`
    elements.

```java
List<Integer> ans = nums.stream().limit(5).toList();
```

18. Given `List<Integer> nums`, write a Stream API solution to skip the first `3` elements
    and return the rest.

```java
List<Integer> ans = nums.stream().skip(3).toList();
```

19. Given `List<Integer> nums`, write a Stream API solution to calculate the sum of all
    numbers.

```java
int ans = nums.stream().mapToInt(Integer::intValue).sum();
```

20. Given `List<Integer> nums`, write a Stream API solution to find the maximum number.

```java
Optional<Integer> ans = nums.stream().max(Integer::compareTo);
```

21. Given `List<Integer> nums`, write a Stream API solution to find the minimum number.

```java
Optional<Integer> ans = nums.stream().min(Integer::compareTo);
```

22. Given `List<Integer> nums`, write a Stream API solution to calculate the average value.

```java
OptionalDouble ans = nums.stream().mapToInt(Integer::intValue).average();
```

23. Given `List<String> words`, write a Stream API solution to join all words using comma
    and space as the separator.

```java
String ans = words.stream().collect(Collectors.joining(", "));
```

24. Given `List<String> words`, write a Stream API solution to convert the list into a
    `Set`.

```java
Set<String> ans = words.stream().collect(Collectors.toSet());
```

25. Given `List<String> words`, write a Stream API solution to create a map where key is
    the word and value is its length.

```java
Map<String, Integer> ans = words.stream().collect(Collectors.toMap(w -> w, String::length));
```

## Medium

26. Given `List<Integer> nums`, write a Stream API solution to find all numbers whose
    decimal representation starts with digit `1`.

```java
List<Integer> ans = nums.stream().filter(n -> String.valueOf(n).startsWith("1")).toList();
```

27. Given `List<Integer> nums`, write a Stream API solution to find all duplicate numbers.

```java
Set<Integer> seen = new HashSet<>();
List<Integer> ans = nums.stream().filter(n -> !seen.add(n)).distinct().toList();
```

28. Given `List<Integer> nums`, write a Stream API solution to find the first duplicate
    number in encounter order.

```java
Set<Integer> seen = new HashSet<>();
Optional<Integer> ans = nums.stream().filter(n -> !seen.add(n)).findFirst();
```

29. Given `List<String> words`, write a Stream API solution to count the frequency of every
    word.

```java
Map<String, Long> ans = words.stream().collect(Collectors.groupingBy(w -> w, Collectors.counting()));
```

30. Given `List<String> words`, write a Stream API solution to group words by their length.

```java
Map<Integer, List<String>> ans = words.stream().collect(Collectors.groupingBy(String::length));
```

31. Given `List<String> words`, write a Stream API solution to count how many words exist
    for each length.

```java
Map<Integer, Long> ans = words.stream().collect(Collectors.groupingBy(String::length, Collectors.counting()));
```

32. Given `List<Integer> nums`, write a Stream API solution to partition numbers into even
    and odd groups.

```java
Map<Boolean, List<Integer>> ans = nums.stream().collect(Collectors.partitioningBy(n -> n % 2 == 0));
```

33. Given `List<Integer> nums`, write a Stream API solution to find the second highest
    distinct number.

```java
Optional<Integer> ans = nums.stream().distinct().sorted(Comparator.reverseOrder()).skip(1).findFirst();
```

34. Given `List<Integer> nums`, write a Stream API solution to find the second lowest
    distinct number.

```java
Optional<Integer> ans = nums.stream().distinct().sorted().skip(1).findFirst();
```

35. Given `List<String> words`, write a Stream API solution to find the longest string.

```java
Optional<String> ans = words.stream().max(Comparator.comparingInt(String::length));
```

36. Given `List<String> words`, write a Stream API solution to find the shortest string.

```java
Optional<String> ans = words.stream().min(Comparator.comparingInt(String::length));
```

37. Given `List<String> words` that may contain `null`, write a Stream API solution to
    remove all null values.

```java
List<String> ans = words.stream().filter(Objects::nonNull).toList();
```

38. Given `List<String> words` that may contain null, empty, or blank values, write a
    Stream API solution to keep only real text values.

```java
List<String> ans = words.stream().filter(s -> s != null && !s.isBlank()).toList();
```

39. Given `List<List<Integer>> matrix`, write a Stream API solution to flatten it into a
    single list.

```java
List<Integer> ans = matrix.stream().flatMap(List::stream).toList();
```

40. Given `List<String> lines`, write a Stream API solution to split all sentences and
    collect unique words.

```java
Set<String> ans = lines.stream().flatMap(s -> Arrays.stream(s.split("\\s+"))).collect(Collectors.toSet());
```

41. Given `int[] arr`, write a Stream API solution to return only positive numbers as
    `List<Integer>`.

```java
List<Integer> ans = Arrays.stream(arr).filter(n -> n > 0).boxed().toList();
```

42. Given two lists `a` and `b`, write a Stream API solution to find common elements.

```java
Set<Integer> lookup = new HashSet<>(b);
List<Integer> ans = a.stream().filter(lookup::contains).distinct().toList();
```

43. Given two lists `a` and `b`, write a Stream API solution to find elements present in
    `a` but not in `b`.

```java
Set<Integer> lookup = new HashSet<>(b);
List<Integer> ans = a.stream().filter(n -> !lookup.contains(n)).toList();
```

44. Given `List<Employee> employees`, write a Stream API solution to sort employees by
    salary from highest to lowest.

```java
List<Employee> ans = employees.stream().sorted(Comparator.comparing(Employee::getSalary).reversed()).toList();
```

45. Given `List<Employee> employees`, write a Stream API solution to find the employee with
    the highest salary.

```java
Optional<Employee> ans = employees.stream().max(Comparator.comparing(Employee::getSalary));
```

46. Given `List<Employee> employees`, write a Stream API solution to return only employee
    names.

```java
List<String> ans = employees.stream().map(Employee::getName).toList();
```

47. Given `List<Employee> employees`, write a Stream API solution to group employees by
    department.

```java
Map<String, List<Employee>> ans = employees.stream().collect(Collectors.groupingBy(Employee::getDepartment));
```

48. Given `List<Employee> employees`, write a Stream API solution to count employees in
    each department.

```java
Map<String, Long> ans = employees.stream()
        .collect(Collectors.groupingBy(
                Employee::getDepartment,
                Collectors.counting()
        ));
```

49. Given `List<Employee> employees`, write a Stream API solution to calculate average
    salary department-wise.

```java
Map<String, Double> ans = employees.stream()
        .collect(Collectors.groupingBy(
                Employee::getDepartment,
                Collectors.averagingDouble(Employee::getSalary)
        ));
```

50. Given `List<Employee> employees`, write a Stream API solution to find the highest-paid
    employee in each department.

```java
Map<String, Optional<Employee>> ans = employees.stream()
        .collect(Collectors.groupingBy(
                Employee::getDepartment,
                Collectors.maxBy(Comparator.comparing(Employee::getSalary))
        ));
```

51. Given `List<Employee> employees`, write a Stream API solution to join employee names
    department-wise.

```java
Map<String, String> ans = employees.stream()
        .collect(Collectors.groupingBy(
                Employee::getDepartment,
                Collectors.mapping(Employee::getName, Collectors.joining(", "))
        ));
```

52. Given `List<User> users`, write a Stream API solution to convert users into a map of id
    to name and handle duplicate ids.

```java
Map<Integer, String> ans = users.stream()
        .collect(Collectors.toMap(
                User::getId,
                User::getName,
                (oldVal, newVal) -> oldVal
        ));
```

53. Given `Map<String, Integer> map`, write a Stream API solution to sort entries by value
    in ascending order.

```java
Map<String, Integer> ans = map.entrySet()
        .stream()
        .sorted(Map.Entry.comparingByValue())
        .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (a, b) -> a,
                LinkedHashMap::new
        ));
```

54. Given `Map<String, Integer> map`, write a Stream API solution to sort entries by value
    in descending order.

```java
Map<String, Integer> ans = map.entrySet()
        .stream()
        .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
        .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (a, b) -> a,
                LinkedHashMap::new
        ));
```

55. Given `List<Employee> employees`, write a Stream API solution to find the top `3`
    distinct salaries.

```java
List<Double> ans = employees.stream()
        .map(Employee::getSalary)
        .distinct()
        .sorted(Comparator.reverseOrder())
        .limit(3)
        .toList();
```

56. Given `List<Employee> employees`, write a Stream API solution to find all employees
    whose name starts with `A`.

```java
List<Employee> ans = employees.stream().filter(e -> e.getName().startsWith("A")).toList();
```

57. Given `List<Employee> employees`, write a Stream API solution to calculate total salary
    paid to all employees.

```java
double ans = employees.stream().mapToDouble(Employee::getSalary).sum();
```

58. Given `List<Employee> employees`, write a Stream API solution to produce count, min,
    max, sum, and average salary statistics.

```java
DoubleSummaryStatistics ans = employees.stream().collect(Collectors.summarizingDouble(Employee::getSalary));
```

59. Given a `String str`, write a Stream API solution to find the first non-repeated
    character.

```java
Map<Character, Long> freq = str.chars()
        .mapToObj(c -> (char) c)
        .collect(Collectors.groupingBy(
                c -> c,
                LinkedHashMap::new,
                Collectors.counting()
        ));
Optional<Character> ans = freq.entrySet().stream().filter(e -> e.getValue() == 1).map(Map.Entry::getKey).findFirst();
```

60. Given a `String str`, write a Stream API solution to find the first repeated character.

```java
Set<Character> seen = new HashSet<>();
Optional<Character> ans = str.chars().mapToObj(c -> (char) c).filter(c -> !seen.add(c)).findFirst();
```

61. Given a `String str`, write a Stream API solution to count vowels.

```java
long ans = str.toLowerCase().chars().filter(c -> "aeiou".indexOf(c) >= 0).count();
```

62. Given two strings `s1` and `s2`, write a Stream API solution to check whether they are
    anagrams.

```java
boolean ans = s1.chars().sorted().boxed().toList().equals(s2.chars().sorted().boxed().toList());
```

63. Given `List<String> words`, write a Stream API solution to return only palindrome words.

```java
List<String> ans = words.stream().filter(w -> new StringBuilder(w).reverse().toString().equals(w)).toList();
```

64. Given `List<String> words`, write a Stream API solution to remove duplicates while
    ignoring case.

```java
List<String> ans = words.stream().map(String::toLowerCase).distinct().toList();
```

65. Given `List<Integer> nums` and integer `k`, write a Stream API solution to find the kth
    largest distinct number.

```java
Optional<Integer> ans = nums.stream().distinct().sorted(Comparator.reverseOrder()).skip(k - 1L).findFirst();
```

## Hard

66. Given `List<String> words`, write a Stream API solution to find the top `3` most
    frequent words. If frequencies tie, sort alphabetically.

```java
List<String> ans = words.stream()
        .collect(Collectors.groupingBy(w -> w, Collectors.counting()))
        .entrySet()
        .stream()
        .sorted(Map.Entry.<String, Long>comparingByValue()
                .reversed()
                .thenComparing(Map.Entry.comparingByKey()))
        .limit(3)
        .map(Map.Entry::getKey)
        .toList();
```

67. Given `List<Employee> employees`, write a Stream API solution to group employees first
    by department and then by gender.

```java
Map<String, Map<String, List<Employee>>> ans = employees.stream()
        .collect(Collectors.groupingBy(
                Employee::getDepartment,
                Collectors.groupingBy(Employee::getGender)
        ));
```

68. Given `List<Employee> employees`, write a Stream API solution to find the highest-paid
    employee per department without returning `Optional` values.

```java
Map<String, Employee> ans = employees.stream()
        .collect(Collectors.toMap(
                Employee::getDepartment,
                Function.identity(),
                BinaryOperator.maxBy(Comparator.comparing(Employee::getSalary))
        ));
```

69. Given `List<Employee> employees`, write a Stream API solution to find the youngest
    employee in each department.

```java
Map<String, Optional<Employee>> ans = employees.stream()
        .collect(Collectors.groupingBy(
                Employee::getDepartment,
                Collectors.minBy(Comparator.comparing(Employee::getAge))
        ));
```

70. Given `List<String> words`, write a Stream API solution to find duplicate words while
    treating uppercase and lowercase as same.

```java
Set<String> seen = new HashSet<>();
List<String> ans = words.stream().map(String::toLowerCase).filter(w -> !seen.add(w)).distinct().toList();
```

71. Given `List<Employee> employees`, write a Stream API solution to build a nested map of
    department to employee id to employee object.

```java
Map<String, Map<Integer, Employee>> ans = employees.stream()
        .collect(Collectors.groupingBy(
                Employee::getDepartment,
                Collectors.toMap(Employee::getId, Function.identity())
        ));
```

72. Given `List<Employee> employees`, write a Stream API solution to find departments
    having more than `5` employees.

```java
List<String> ans = employees.stream()
        .collect(Collectors.groupingBy(Employee::getDepartment, Collectors.counting()))
        .entrySet()
        .stream()
        .filter(e -> e.getValue() > 5)
        .map(Map.Entry::getKey)
        .toList();
```

73. Given `List<Employee> employees`, write a Stream API solution to partition employees by
    salary greater than `100000` and collect only names.

```java
Map<Boolean, List<String>> ans = employees.stream()
        .collect(Collectors.partitioningBy(
                e -> e.getSalary() > 100000,
                Collectors.mapping(Employee::getName, Collectors.toList())
        ));
```

74. Given `List<String> lines`, write a Stream API solution to find the longest word from
    each sentence.

```java
List<String> ans = lines.stream()
        .map(s -> Arrays.stream(s.split("\\s+"))
                .max(Comparator.comparingInt(String::length))
                .orElse(""))
        .toList();
```

75. Given a `String str`, write a Stream API solution to create a character frequency map.

```java
Map<Character, Long> ans = str.chars()
        .mapToObj(c -> (char) c)
        .collect(Collectors.groupingBy(c -> c, Collectors.counting()));
```

76. Given two strings `s1` and `s2`, write a Stream API solution to find distinct common
    characters.

```java
Set<Character> set = s2.chars().mapToObj(c -> (char) c).collect(Collectors.toSet());
List<Character> ans = s1.chars().mapToObj(c -> (char) c).filter(set::contains).distinct().toList();
```

77. Given `List<Integer> nums` and `target`, write a Stream API solution to find all unique
    pairs whose sum equals target.

```java
Set<Integer> set = new HashSet<>(nums);
List<List<Integer>> ans = nums.stream()
        .filter(n -> set.contains(target - n) && n < target - n)
        .map(n -> List.of(n, target - n))
        .toList();
```

78. Given `List<Order> orders`, where each order has products, write a Stream API solution
    to flatten all orders into product names.

```java
List<String> ans = orders.stream().flatMap(o -> o.getProducts().stream()).map(Product::getName).toList();
```

79. Given `List<Order> orders`, write a Stream API solution to calculate total order amount
    by customer.

```java
Map<String, Double> ans = orders.stream()
        .collect(Collectors.groupingBy(
                Order::getCustomerName,
                Collectors.summingDouble(Order::getAmount)
        ));
```

80. Given `List<Order> orders`, write a Stream API solution to find the latest order placed
    by each customer.

```java
Map<String, Optional<Order>> ans = orders.stream()
        .collect(Collectors.groupingBy(
                Order::getCustomerName,
                Collectors.maxBy(Comparator.comparing(Order::getDate))
        ));
```

81. Given all `products` and all `orders`, write a Stream API solution to find products
    that were never ordered.

```java
Set<Integer> ordered = orders.stream()
        .flatMap(o -> o.getProducts().stream())
        .map(Product::getId)
        .collect(Collectors.toSet());
List<Product> ans = products.stream().filter(p -> !ordered.contains(p.getId())).toList();
```

82. Given two lists `a` and `b`, write a Stream API solution to merge them and remove
    duplicates.

```java
List<Integer> ans = Stream.concat(a.stream(), b.stream()).distinct().toList();
```

83. Given two lists `a` and `b`, write a Stream API solution to combine elements by index
    into pair strings.

```java
List<String> ans = IntStream.range(0, Math.min(a.size(), b.size())).mapToObj(i -> a.get(i) + ":" + b.get(i)).toList();
```

84. Given `List<Integer> nums`, write a Stream API solution to split the list into chunks
    of size `3`.

```java
List<List<Integer>> ans = IntStream.range(0, (nums.size() + 2) / 3)
        .mapToObj(i -> nums.subList(i * 3, Math.min(nums.size(), i * 3 + 3)))
        .toList();
```

85. Given `List<Integer> nums`, write a Stream API solution to calculate the sum of every
    sliding window of size `3`.

```java
List<Integer> ans = IntStream.rangeClosed(0, nums.size() - 3)
        .mapToObj(i -> nums.subList(i, i + 3)
                .stream()
                .mapToInt(Integer::intValue)
                .sum())
        .toList();
```

86. Given `List<Integer> nums` and window size `k`, write a Stream API solution to find the
    maximum sum among all windows of size `k`.

```java
OptionalInt ans = IntStream.rangeClosed(0, nums.size() - k)
        .map(i -> nums.subList(i, i + k)
                .stream()
                .mapToInt(Integer::intValue)
                .sum())
        .max();
```

87. Given CSV `lines`, where the first line is a header and each data line has `id,name`,
    write a Stream API solution to convert them into `User` objects.

```java
List<User> ans = lines.stream()
        .skip(1)
        .map(s -> s.split(","))
        .map(a -> new User(Integer.parseInt(a[0]), a[1]))
        .toList();
```

88. Given `List<String> emails`, write a Stream API solution to return invalid email
    addresses.

```java
List<String> ans = emails.stream().filter(e -> !e.matches("^[\\w.-]+@[\\w.-]+\\.\\w+$")).toList();
```

89. Given `List<String> names`, write a Stream API solution to trim, remove empty names,
    lowercase them, and sort the result.

```java
List<String> ans = names.stream()
        .map(String::trim)
        .filter(s -> !s.isEmpty())
        .map(String::toLowerCase)
        .sorted()
        .toList();
```

90. Given `List<Integer> nums`, write a Stream API solution to find the median.

```java
List<Integer> sorted = nums.stream().sorted().toList();
double ans = sorted.size() % 2 == 1
        ? sorted.get(sorted.size() / 2)
        : (sorted.get(sorted.size() / 2 - 1) + sorted.get(sorted.size() / 2)) / 2.0;
```

91. Given `List<Integer> nums`, write a Stream API solution to find the mode.

```java
Optional<Integer> ans = nums.stream()
        .collect(Collectors.groupingBy(n -> n, Collectors.counting()))
        .entrySet()
        .stream()
        .max(Map.Entry.comparingByValue())
        .map(Map.Entry::getKey);
```

92. Given `List<String> words`, write a Stream API solution to find the least frequent word.

```java
Optional<String> ans = words.stream()
        .collect(Collectors.groupingBy(w -> w, Collectors.counting()))
        .entrySet()
        .stream()
        .min(Map.Entry.comparingByValue())
        .map(Map.Entry::getKey);
```

93. Given `List<String> words`, write a Stream API solution to collect words longer than
    `3` into an immutable list.

```java
List<String> ans = words.stream().filter(w -> w.length() > 3).collect(Collectors.toUnmodifiableList());
```

94. Given `List<Integer> nums`, write a Stream API `reduce` solution to calculate the
    product of all numbers.

```java
int ans = nums.stream().reduce(1, (a, b) -> a * b);
```

95. Given `List<String> words`, write a Stream API `reduce` solution to find the longest
    string.

```java
Optional<String> ans = words.stream().reduce((a, b) -> a.length() >= b.length() ? a : b);
```

96. Given `List<String> words`, write a Stream API `reduce` solution to create a
    comma-separated string.

```java
String ans = words.stream().reduce("", (a, b) -> a.isEmpty() ? b : a + "," + b);
```

97. Given `List<String> words`, write a Stream API solution to group all anagrams together.

```java
Map<String, List<String>> ans = words.stream()
        .collect(Collectors.groupingBy(w -> w.chars()
                .sorted()
                .collect(
                        StringBuilder::new,
                        StringBuilder::appendCodePoint,
                        StringBuilder::append
                )
                .toString()));
```

98. Given `List<String> words`, write a Stream API solution to return only anagram groups
    that contain more than one word.

```java
List<List<String>> ans = words.stream()
        .collect(Collectors.groupingBy(w -> w.chars()
                .sorted()
                .collect(
                        StringBuilder::new,
                        StringBuilder::appendCodePoint,
                        StringBuilder::append
                )
                .toString()))
        .values()
        .stream()
        .filter(g -> g.size() > 1)
        .toList();
```

99. Given `List<Employee> employees`, write a Stream API solution to rank employees by
    salary in descending order.

```java
List<Double> salaries = employees.stream()
        .map(Employee::getSalary)
        .distinct()
        .sorted(Comparator.reverseOrder())
        .toList();

Map<Integer, List<Employee>> ans = employees.stream()
        .collect(Collectors.groupingBy(e -> salaries.indexOf(e.getSalary()) + 1));
```

100.  Given `List<Integer> nums`, write a Stream API solution to find the first missing
      positive integer.

```java
Set<Integer> set = nums.stream().filter(n -> n > 0).collect(Collectors.toSet());
int ans = IntStream.iterate(1, i -> i + 1).filter(i -> !set.contains(i)).findFirst().orElse(1);
```

## Employee-Focused Interview Questions

101. Given `List<Employee> employees`, write a Stream API solution to find all
     employees who belong to the `IT` department and earn more than `80000`.

```java
List<Employee> ans = employees.stream()
        .filter(e -> "IT".equals(e.getDepartment()))
        .filter(e -> e.getSalary() > 80000)
        .toList();
```

102. Given `List<Employee> employees`, write a Stream API solution to get the
     names of employees older than `30`, sorted alphabetically.

```java
List<String> ans = employees.stream()
        .filter(e -> e.getAge() > 30)
        .map(Employee::getName)
        .sorted()
        .toList();
```

103. Given `List<Employee> employees`, write a Stream API solution to find the
     average age of employees in each department.

```java
Map<String, Double> ans = employees.stream()
        .collect(Collectors.groupingBy(
                Employee::getDepartment,
                Collectors.averagingInt(Employee::getAge)
        ));
```

104. Given `List<Employee> employees`, write a Stream API solution to find the
     minimum salary in each department.

```java
Map<String, Optional<Employee>> ans = employees.stream()
        .collect(Collectors.groupingBy(
                Employee::getDepartment,
                Collectors.minBy(Comparator.comparing(Employee::getSalary))
        ));
```

105. Given `List<Employee> employees`, write a Stream API solution to find the
     second-highest salary employee in the whole company.

```java
Optional<Employee> ans = employees.stream()
        .sorted(Comparator.comparing(Employee::getSalary).reversed())
        .skip(1)
        .findFirst();
```

106. Given `List<Employee> employees`, write a Stream API solution to find the
     second-highest distinct salary in each department.

```java
Map<String, Optional<Double>> ans = employees.stream()
        .collect(Collectors.groupingBy(
                Employee::getDepartment,
                Collectors.collectingAndThen(
                        Collectors.mapping(Employee::getSalary, Collectors.toSet()),
                        salaries -> salaries.stream()
                                .sorted(Comparator.reverseOrder())
                                .skip(1)
                                .findFirst()
                )
        ));
```

107. Given `List<Employee> employees`, write a Stream API solution to group
     employees by department and collect only employee names.

```java
Map<String, List<String>> ans = employees.stream()
        .collect(Collectors.groupingBy(
                Employee::getDepartment,
                Collectors.mapping(Employee::getName, Collectors.toList())
        ));
```

108. Given `List<Employee> employees`, write a Stream API solution to find the
     highest salary amount in each department.

```java
Map<String, Optional<Double>> ans = employees.stream()
        .collect(Collectors.groupingBy(
                Employee::getDepartment,
                Collectors.mapping(
                        Employee::getSalary,
                        Collectors.maxBy(Double::compareTo)
                )
        ));
```

109. Given `List<Employee> employees`, write a Stream API solution to find the
     department with the highest total salary expense.

```java
Optional<Map.Entry<String, Double>> ans = employees.stream()
        .collect(Collectors.groupingBy(
                Employee::getDepartment,
                Collectors.summingDouble(Employee::getSalary)
        ))
        .entrySet()
        .stream()
        .max(Map.Entry.comparingByValue());
```

110. Given `List<Employee> employees`, write a Stream API solution to find the
     department with the highest number of employees.

```java
Optional<Map.Entry<String, Long>> ans = employees.stream()
        .collect(Collectors.groupingBy(
                Employee::getDepartment,
                Collectors.counting()
        ))
        .entrySet()
        .stream()
        .max(Map.Entry.comparingByValue());
```

111. Given `List<Employee> employees`, write a Stream API solution to find all
     employees who have duplicate names.

```java
Set<String> duplicateNames = employees.stream()
        .collect(Collectors.groupingBy(Employee::getName, Collectors.counting()))
        .entrySet()
        .stream()
        .filter(e -> e.getValue() > 1)
        .map(Map.Entry::getKey)
        .collect(Collectors.toSet());

List<Employee> ans = employees.stream()
        .filter(e -> duplicateNames.contains(e.getName()))
        .toList();
```

112. Given `List<Employee> employees`, write a Stream API solution to create a
     map where the key is employee id and the value is the employee object.

```java
Map<Integer, Employee> ans = employees.stream()
        .collect(Collectors.toMap(Employee::getId, Function.identity()));
```

113. Given `List<Employee> employees`, write a Stream API solution to create a
     map where duplicate employee ids keep the employee with the higher salary.

```java
Map<Integer, Employee> ans = employees.stream()
        .collect(Collectors.toMap(
                Employee::getId,
                Function.identity(),
                BinaryOperator.maxBy(Comparator.comparing(Employee::getSalary))
        ));
```

114. Given `List<Employee> employees`, write a Stream API solution to find all
     departments where every employee earns more than `50000`.

```java
List<String> ans = employees.stream()
        .collect(Collectors.groupingBy(Employee::getDepartment))
        .entrySet()
        .stream()
        .filter(e -> e.getValue().stream().allMatch(emp -> emp.getSalary() > 50000))
        .map(Map.Entry::getKey)
        .toList();
```

115. Given `List<Employee> employees`, write a Stream API solution to find all
     departments where at least one employee earns more than `150000`.

```java
List<String> ans = employees.stream()
        .collect(Collectors.groupingBy(Employee::getDepartment))
        .entrySet()
        .stream()
        .filter(e -> e.getValue().stream().anyMatch(emp -> emp.getSalary() > 150000))
        .map(Map.Entry::getKey)
        .toList();
```

116. Given `List<Employee> employees`, write a Stream API solution to partition
     employees into senior and junior employees using age `40` as the cutoff.

```java
Map<Boolean, List<Employee>> ans = employees.stream()
        .collect(Collectors.partitioningBy(e -> e.getAge() >= 40));
```

117. Given `List<Employee> employees`, write a Stream API solution to find the
     oldest employee in each department.

```java
Map<String, Optional<Employee>> ans = employees.stream()
        .collect(Collectors.groupingBy(
                Employee::getDepartment,
                Collectors.maxBy(Comparator.comparing(Employee::getAge))
        ));
```

118. Given `List<Employee> employees`, write a Stream API solution to sort
     employees first by department ascending and then by salary descending.

```java
List<Employee> ans = employees.stream()
        .sorted(Comparator.comparing(Employee::getDepartment)
                .thenComparing(Comparator.comparing(Employee::getSalary).reversed()))
        .toList();
```

119. Given `List<Employee> employees`, write a Stream API solution to find the
     salary range for each department using summary statistics.

```java
Map<String, DoubleSummaryStatistics> ans = employees.stream()
        .collect(Collectors.groupingBy(
                Employee::getDepartment,
                Collectors.summarizingDouble(Employee::getSalary)
        ));
```

120. Given `List<Employee> employees`, write a Stream API solution to return the
     top `2` highest-paid employees from each department.

```java
Map<String, List<Employee>> ans = employees.stream()
        .collect(Collectors.groupingBy(
                Employee::getDepartment,
                Collectors.collectingAndThen(
                        Collectors.toList(),
                        list -> list.stream()
                                .sorted(Comparator.comparing(Employee::getSalary).reversed())
                                .limit(2)
                                .toList()
                )
        ));
```
