import java.time.LocalDate;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class StreamApiInterviewSolutions {

    record Employee(int id, String name, String department, String gender, int age, double salary) {}

    record User(int id, String name) {}

    record Product(int id, String name) {}

    record Order(int id, String customerName, double amount, LocalDate date, List<Product> products) {}

    public static List<Integer> q001EvenNumbers(List<Integer> nums) {
        return nums.stream().filter(n -> n % 2 == 0).toList();
    }

    public static List<Integer> q002OddNumbers(List<Integer> nums) {
        return nums.stream().filter(n -> n % 2 != 0).toList();
    }

    public static List<Integer> q003Squares(List<Integer> nums) {
        return nums.stream().map(n -> n * n).toList();
    }

    public static List<String> q004Uppercase(List<String> words) {
        return words.stream().map(String::toUpperCase).toList();
    }

    public static List<String> q005Lowercase(List<String> words) {
        return words.stream().map(String::toLowerCase).toList();
    }

    public static long q006CountGreaterThanTen(List<Integer> nums) {
        return nums.stream().filter(n -> n > 10).count();
    }

    public static Optional<Integer> q007FirstElement(List<Integer> nums) {
        return nums.stream().findFirst();
    }

    public static Optional<Integer> q008AnyElement(List<Integer> nums) {
        return nums.stream().findAny();
    }

    public static boolean q009AllPositive(List<Integer> nums) {
        return nums.stream().allMatch(n -> n > 0);
    }

    public static boolean q010AnyNegative(List<Integer> nums) {
        return nums.stream().anyMatch(n -> n < 0);
    }

    public static boolean q011NoZero(List<Integer> nums) {
        return nums.stream().noneMatch(n -> n == 0);
    }

    public static List<Integer> q012UniqueNumbers(List<Integer> nums) {
        return nums.stream().distinct().toList();
    }

    public static List<Integer> q013SortAscending(List<Integer> nums) {
        return nums.stream().sorted().toList();
    }

    public static List<Integer> q014SortDescending(List<Integer> nums) {
        return nums.stream().sorted(Comparator.reverseOrder()).toList();
    }

    public static List<String> q015SortStrings(List<String> words) {
        return words.stream().sorted().toList();
    }

    public static List<String> q016SortByLength(List<String> words) {
        return words.stream().sorted(Comparator.comparingInt(String::length)).toList();
    }

    public static List<Integer> q017FirstFive(List<Integer> nums) {
        return nums.stream().limit(5).toList();
    }

    public static List<Integer> q018SkipFirstThree(List<Integer> nums) {
        return nums.stream().skip(3).toList();
    }

    public static int q019Sum(List<Integer> nums) {
        return nums.stream().mapToInt(Integer::intValue).sum();
    }

    public static Optional<Integer> q020Max(List<Integer> nums) {
        return nums.stream().max(Integer::compareTo);
    }

    public static Optional<Integer> q021Min(List<Integer> nums) {
        return nums.stream().min(Integer::compareTo);
    }

    public static OptionalDouble q022Average(List<Integer> nums) {
        return nums.stream().mapToInt(Integer::intValue).average();
    }

    public static String q023JoinWithComma(List<String> words) {
        return words.stream().collect(Collectors.joining(", "));
    }

    public static Set<String> q024ToSet(List<String> words) {
        return words.stream().collect(Collectors.toSet());
    }

    public static Map<String, Integer> q025WordLengthMap(List<String> words) {
        return words.stream().collect(Collectors.toMap(w -> w, String::length));
    }

    public static List<Integer> q026NumbersStartingWithOne(List<Integer> nums) {
        return nums.stream().filter(n -> String.valueOf(n).startsWith("1")).toList();
    }

    public static List<Integer> q027DuplicateNumbers(List<Integer> nums) {
        Set<Integer> seen = new HashSet<>();
        return nums.stream().filter(n -> !seen.add(n)).distinct().toList();
    }

    public static Optional<Integer> q028FirstDuplicate(List<Integer> nums) {
        Set<Integer> seen = new HashSet<>();
        return nums.stream().filter(n -> !seen.add(n)).findFirst();
    }

    public static Map<String, Long> q029WordFrequency(List<String> words) {
        return words.stream().collect(Collectors.groupingBy(w -> w, Collectors.counting()));
    }

    public static Map<Integer, List<String>> q030GroupWordsByLength(List<String> words) {
        return words.stream().collect(Collectors.groupingBy(String::length));
    }

    public static Map<Integer, Long> q031CountWordsByLength(List<String> words) {
        return words.stream().collect(Collectors.groupingBy(String::length, Collectors.counting()));
    }

    public static Map<Boolean, List<Integer>> q032PartitionEvenOdd(List<Integer> nums) {
        return nums.stream().collect(Collectors.partitioningBy(n -> n % 2 == 0));
    }

    public static Optional<Integer> q033SecondHighest(List<Integer> nums) {
        return nums.stream().distinct().sorted(Comparator.reverseOrder()).skip(1).findFirst();
    }

    public static Optional<Integer> q034SecondLowest(List<Integer> nums) {
        return nums.stream().distinct().sorted().skip(1).findFirst();
    }

    public static Optional<String> q035LongestString(List<String> words) {
        return words.stream().max(Comparator.comparingInt(String::length));
    }

    public static Optional<String> q036ShortestString(List<String> words) {
        return words.stream().min(Comparator.comparingInt(String::length));
    }

    public static List<String> q037RemoveNulls(List<String> words) {
        return words.stream().filter(Objects::nonNull).toList();
    }

    public static List<String> q038RemoveBlankValues(List<String> words) {
        return words.stream().filter(s -> s != null && !s.isBlank()).toList();
    }

    public static List<Integer> q039FlattenList(List<List<Integer>> matrix) {
        return matrix.stream().flatMap(List::stream).toList();
    }

    public static Set<String> q040UniqueWordsFromLines(List<String> lines) {
        return lines.stream()
                .flatMap(s -> Arrays.stream(s.split("\\s+")))
                .collect(Collectors.toSet());
    }

    public static List<Integer> q041PositiveArrayValues(int[] arr) {
        return Arrays.stream(arr).filter(n -> n > 0).boxed().toList();
    }

    public static List<Integer> q042CommonElements(List<Integer> a, List<Integer> b) {
        Set<Integer> lookup = new HashSet<>(b);
        return a.stream().filter(lookup::contains).distinct().toList();
    }

    public static List<Integer> q043ElementsOnlyInFirst(List<Integer> a, List<Integer> b) {
        Set<Integer> lookup = new HashSet<>(b);
        return a.stream().filter(n -> !lookup.contains(n)).toList();
    }

    public static List<Employee> q044SortEmployeesBySalaryDesc(List<Employee> employees) {
        return employees.stream()
                .sorted(Comparator.comparing(Employee::salary).reversed())
                .toList();
    }

    public static Optional<Employee> q045HighestPaidEmployee(List<Employee> employees) {
        return employees.stream().max(Comparator.comparing(Employee::salary));
    }

    public static List<String> q046EmployeeNames(List<Employee> employees) {
        return employees.stream().map(Employee::name).toList();
    }

    public static Map<String, List<Employee>> q047GroupEmployeesByDepartment(List<Employee> employees) {
        return employees.stream().collect(Collectors.groupingBy(Employee::department));
    }

    public static Map<String, Long> q048CountEmployeesByDepartment(List<Employee> employees) {
        return employees.stream()
                .collect(Collectors.groupingBy(Employee::department, Collectors.counting()));
    }

    public static Map<String, Double> q049AverageSalaryByDepartment(List<Employee> employees) {
        return employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::department,
                        Collectors.averagingDouble(Employee::salary)
                ));
    }

    public static Map<String, Optional<Employee>> q050HighestPaidByDepartment(
            List<Employee> employees
    ) {
        return employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::department,
                        Collectors.maxBy(Comparator.comparing(Employee::salary))
                ));
    }

    public static Map<String, String> q051EmployeeNamesByDepartment(List<Employee> employees) {
        return employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::department,
                        Collectors.mapping(Employee::name, Collectors.joining(", "))
                ));
    }

    public static Map<Integer, String> q052UserIdNameMap(List<User> users) {
        return users.stream()
                .collect(Collectors.toMap(User::id, User::name, (oldVal, newVal) -> oldVal));
    }

    public static Map<String, Integer> q053SortMapByValueAsc(Map<String, Integer> map) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
    }

    public static Map<String, Integer> q054SortMapByValueDesc(Map<String, Integer> map) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
    }

    public static List<Double> q055TopThreeDistinctSalaries(List<Employee> employees) {
        return employees.stream()
                .map(Employee::salary)
                .distinct()
                .sorted(Comparator.reverseOrder())
                .limit(3)
                .toList();
    }

    public static List<Employee> q056NamesStartingWithA(List<Employee> employees) {
        return employees.stream().filter(e -> e.name().startsWith("A")).toList();
    }

    public static double q057TotalSalary(List<Employee> employees) {
        return employees.stream().mapToDouble(Employee::salary).sum();
    }

    public static DoubleSummaryStatistics q058SalaryStats(List<Employee> employees) {
        return employees.stream().collect(Collectors.summarizingDouble(Employee::salary));
    }

    public static Optional<Character> q059FirstNonRepeatedCharacter(String str) {
        Map<Character, Long> freq = str.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.groupingBy(
                        c -> c,
                        LinkedHashMap::new,
                        Collectors.counting()
                ));

        return freq.entrySet()
                .stream()
                .filter(e -> e.getValue() == 1)
                .map(Map.Entry::getKey)
                .findFirst();
    }

    public static Optional<Character> q060FirstRepeatedCharacter(String str) {
        Set<Character> seen = new HashSet<>();
        return str.chars().mapToObj(c -> (char) c).filter(c -> !seen.add(c)).findFirst();
    }

    public static long q061CountVowels(String str) {
        return str.toLowerCase().chars().filter(c -> "aeiou".indexOf(c) >= 0).count();
    }

    public static boolean q062AreAnagrams(String s1, String s2) {
        return s1.chars().sorted().boxed().toList().equals(s2.chars().sorted().boxed().toList());
    }

    public static List<String> q063PalindromeWords(List<String> words) {
        return words.stream()
                .filter(w -> new StringBuilder(w).reverse().toString().equals(w))
                .toList();
    }

    public static List<String> q064DistinctWordsIgnoreCase(List<String> words) {
        return words.stream().map(String::toLowerCase).distinct().toList();
    }

    public static Optional<Integer> q065KthLargestDistinct(List<Integer> nums, int k) {
        return nums.stream().distinct().sorted(Comparator.reverseOrder()).skip(k - 1L).findFirst();
    }

    public static List<String> q066TopThreeFrequentWords(List<String> words) {
        return words.stream()
                .collect(Collectors.groupingBy(w -> w, Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue()
                        .reversed()
                        .thenComparing(Map.Entry.comparingByKey()))
                .limit(3)
                .map(Map.Entry::getKey)
                .toList();
    }

    public static Map<String, Map<String, List<Employee>>> q067GroupByDepartmentAndGender(
            List<Employee> employees
    ) {
        return employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::department,
                        Collectors.groupingBy(Employee::gender)
                ));
    }

    public static Map<String, Employee> q068HighestPaidByDepartmentNoOptional(
            List<Employee> employees
    ) {
        return employees.stream()
                .collect(Collectors.toMap(
                        Employee::department,
                        Function.identity(),
                        BinaryOperator.maxBy(Comparator.comparing(Employee::salary))
                ));
    }

    public static Map<String, Optional<Employee>> q069YoungestByDepartment(
            List<Employee> employees
    ) {
        return employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::department,
                        Collectors.minBy(Comparator.comparing(Employee::age))
                ));
    }

    public static List<String> q070DuplicateWordsIgnoreCase(List<String> words) {
        Set<String> seen = new HashSet<>();
        return words.stream().map(String::toLowerCase).filter(w -> !seen.add(w)).distinct().toList();
    }

    public static Map<String, Map<Integer, Employee>> q071DepartmentEmployeeIdMap(
            List<Employee> employees
    ) {
        return employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::department,
                        Collectors.toMap(Employee::id, Function.identity())
                ));
    }

    public static List<String> q072DepartmentsWithMoreThanFiveEmployees(List<Employee> employees) {
        return employees.stream()
                .collect(Collectors.groupingBy(Employee::department, Collectors.counting()))
                .entrySet()
                .stream()
                .filter(e -> e.getValue() > 5)
                .map(Map.Entry::getKey)
                .toList();
    }

    public static Map<Boolean, List<String>> q073PartitionHighSalaryNames(
            List<Employee> employees
    ) {
        return employees.stream()
                .collect(Collectors.partitioningBy(
                        e -> e.salary() > 100000,
                        Collectors.mapping(Employee::name, Collectors.toList())
                ));
    }

    public static List<String> q074LongestWordInEachSentence(List<String> lines) {
        return lines.stream()
                .map(s -> Arrays.stream(s.split("\\s+"))
                        .max(Comparator.comparingInt(String::length))
                        .orElse(""))
                .toList();
    }

    public static Map<Character, Long> q075CharacterFrequency(String str) {
        return str.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.groupingBy(c -> c, Collectors.counting()));
    }

    public static List<Character> q076CommonCharacters(String s1, String s2) {
        Set<Character> set = s2.chars().mapToObj(c -> (char) c).collect(Collectors.toSet());
        return s1.chars().mapToObj(c -> (char) c).filter(set::contains).distinct().toList();
    }

    public static List<List<Integer>> q077PairsWithTargetSum(List<Integer> nums, int target) {
        Set<Integer> set = new HashSet<>(nums);
        return nums.stream()
                .filter(n -> set.contains(target - n) && n < target - n)
                .map(n -> List.of(n, target - n))
                .toList();
    }

    public static List<String> q078ProductNamesFromOrders(List<Order> orders) {
        return orders.stream()
                .flatMap(o -> o.products().stream())
                .map(Product::name)
                .toList();
    }

    public static Map<String, Double> q079TotalOrderAmountByCustomer(List<Order> orders) {
        return orders.stream()
                .collect(Collectors.groupingBy(
                        Order::customerName,
                        Collectors.summingDouble(Order::amount)
                ));
    }

    public static Map<String, Optional<Order>> q080LatestOrderByCustomer(List<Order> orders) {
        return orders.stream()
                .collect(Collectors.groupingBy(
                        Order::customerName,
                        Collectors.maxBy(Comparator.comparing(Order::date))
                ));
    }

    public static List<Product> q081ProductsNeverOrdered(
            List<Product> products,
            List<Order> orders
    ) {
        Set<Integer> ordered = orders.stream()
                .flatMap(o -> o.products().stream())
                .map(Product::id)
                .collect(Collectors.toSet());
        return products.stream().filter(p -> !ordered.contains(p.id())).toList();
    }

    public static List<Integer> q082MergeAndRemoveDuplicates(List<Integer> a, List<Integer> b) {
        return Stream.concat(a.stream(), b.stream()).distinct().toList();
    }

    public static List<String> q083ZipLists(List<String> a, List<String> b) {
        return IntStream.range(0, Math.min(a.size(), b.size()))
                .mapToObj(i -> a.get(i) + ":" + b.get(i))
                .toList();
    }

    public static List<List<Integer>> q084ChunksOfThree(List<Integer> nums) {
        return IntStream.range(0, (nums.size() + 2) / 3)
                .mapToObj(i -> nums.subList(i * 3, Math.min(nums.size(), i * 3 + 3)))
                .toList();
    }

    public static List<Integer> q085SlidingWindowSumThree(List<Integer> nums) {
        return IntStream.rangeClosed(0, nums.size() - 3)
                .mapToObj(i -> nums.subList(i, i + 3).stream().mapToInt(Integer::intValue).sum())
                .toList();
    }

    public static OptionalInt q086MaxWindowSum(List<Integer> nums, int k) {
        return IntStream.rangeClosed(0, nums.size() - k)
                .map(i -> nums.subList(i, i + k).stream().mapToInt(Integer::intValue).sum())
                .max();
    }

    public static List<User> q087CsvLinesToUsers(List<String> lines) {
        return lines.stream()
                .skip(1)
                .map(s -> s.split(","))
                .map(a -> new User(Integer.parseInt(a[0]), a[1]))
                .toList();
    }

    public static List<String> q088InvalidEmails(List<String> emails) {
        return emails.stream().filter(e -> !e.matches("^[\\w.-]+@[\\w.-]+\\.\\w+$")).toList();
    }

    public static List<String> q089NormalizeNames(List<String> names) {
        return names.stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(String::toLowerCase)
                .sorted()
                .toList();
    }

    public static double q090Median(List<Integer> nums) {
        List<Integer> sorted = nums.stream().sorted().toList();
        return sorted.size() % 2 == 1
                ? sorted.get(sorted.size() / 2)
                : (sorted.get(sorted.size() / 2 - 1) + sorted.get(sorted.size() / 2)) / 2.0;
    }

    public static Optional<Integer> q091Mode(List<Integer> nums) {
        return nums.stream()
                .collect(Collectors.groupingBy(n -> n, Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
    }

    public static Optional<String> q092LeastFrequentWord(List<String> words) {
        return words.stream()
                .collect(Collectors.groupingBy(w -> w, Collectors.counting()))
                .entrySet()
                .stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
    }

    public static List<String> q093ImmutableWordsLongerThanThree(List<String> words) {
        return words.stream().filter(w -> w.length() > 3).collect(Collectors.toUnmodifiableList());
    }

    public static int q094ProductWithReduce(List<Integer> nums) {
        return nums.stream().reduce(1, (a, b) -> a * b);
    }

    public static Optional<String> q095LongestStringWithReduce(List<String> words) {
        return words.stream().reduce((a, b) -> a.length() >= b.length() ? a : b);
    }

    public static String q096CommaStringWithReduce(List<String> words) {
        return words.stream().reduce("", (a, b) -> a.isEmpty() ? b : a + "," + b);
    }

    public static Map<String, List<String>> q097AnagramGroups(List<String> words) {
        return words.stream().collect(Collectors.groupingBy(StreamApiInterviewSolutions::sortedKey));
    }

    public static List<List<String>> q098AnagramGroupsWithMoreThanOneWord(List<String> words) {
        return words.stream()
                .collect(Collectors.groupingBy(StreamApiInterviewSolutions::sortedKey))
                .values()
                .stream()
                .filter(g -> g.size() > 1)
                .toList();
    }

    public static Map<Integer, List<Employee>> q099RankEmployeesBySalary(List<Employee> employees) {
        List<Double> salaries = employees.stream()
                .map(Employee::salary)
                .distinct()
                .sorted(Comparator.reverseOrder())
                .toList();

        return employees.stream()
                .collect(Collectors.groupingBy(e -> salaries.indexOf(e.salary()) + 1));
    }

    public static int q100FirstMissingPositive(List<Integer> nums) {
        Set<Integer> set = nums.stream().filter(n -> n > 0).collect(Collectors.toSet());
        return IntStream.iterate(1, i -> i + 1)
                .filter(i -> !set.contains(i))
                .findFirst()
                .orElse(1);
    }

    public static List<Employee> q101ItEmployeesAbove80000(List<Employee> employees) {
        return employees.stream()
                .filter(e -> "IT".equals(e.department()))
                .filter(e -> e.salary() > 80000)
                .toList();
    }

    public static List<String> q102EmployeeNamesOlderThan30(List<Employee> employees) {
        return employees.stream()
                .filter(e -> e.age() > 30)
                .map(Employee::name)
                .sorted()
                .toList();
    }

    public static Map<String, Double> q103AverageAgeByDepartment(List<Employee> employees) {
        return employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::department,
                        Collectors.averagingInt(Employee::age)
                ));
    }

    public static Map<String, Optional<Employee>> q104MinimumSalaryByDepartment(
            List<Employee> employees
    ) {
        return employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::department,
                        Collectors.minBy(Comparator.comparing(Employee::salary))
                ));
    }

    public static Optional<Employee> q105SecondHighestSalaryEmployee(List<Employee> employees) {
        return employees.stream()
                .sorted(Comparator.comparing(Employee::salary).reversed())
                .skip(1)
                .findFirst();
    }

    public static Map<String, Optional<Double>> q106SecondHighestDistinctSalaryByDepartment(
            List<Employee> employees
    ) {
        return employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::department,
                        Collectors.collectingAndThen(
                                Collectors.mapping(Employee::salary, Collectors.toSet()),
                                salaries -> salaries.stream()
                                        .sorted(Comparator.reverseOrder())
                                        .skip(1)
                                        .findFirst()
                        )
                ));
    }

    public static Map<String, List<String>> q107EmployeeNamesGroupedByDepartment(
            List<Employee> employees
    ) {
        return employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::department,
                        Collectors.mapping(Employee::name, Collectors.toList())
                ));
    }

    public static Map<String, Optional<Double>> q108HighestSalaryAmountByDepartment(
            List<Employee> employees
    ) {
        return employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::department,
                        Collectors.mapping(Employee::salary, Collectors.maxBy(Double::compareTo))
                ));
    }

    public static Optional<Map.Entry<String, Double>> q109DepartmentWithHighestSalaryExpense(
            List<Employee> employees
    ) {
        return employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::department,
                        Collectors.summingDouble(Employee::salary)
                ))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue());
    }

    public static Optional<Map.Entry<String, Long>> q110DepartmentWithMostEmployees(
            List<Employee> employees
    ) {
        return employees.stream()
                .collect(Collectors.groupingBy(Employee::department, Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue());
    }

    public static List<Employee> q111EmployeesWithDuplicateNames(List<Employee> employees) {
        Set<String> duplicateNames = employees.stream()
                .collect(Collectors.groupingBy(Employee::name, Collectors.counting()))
                .entrySet()
                .stream()
                .filter(e -> e.getValue() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        return employees.stream().filter(e -> duplicateNames.contains(e.name())).toList();
    }

    public static Map<Integer, Employee> q112EmployeeIdMap(List<Employee> employees) {
        return employees.stream().collect(Collectors.toMap(Employee::id, Function.identity()));
    }

    public static Map<Integer, Employee> q113EmployeeIdMapKeepHigherSalary(
            List<Employee> employees
    ) {
        return employees.stream()
                .collect(Collectors.toMap(
                        Employee::id,
                        Function.identity(),
                        BinaryOperator.maxBy(Comparator.comparing(Employee::salary))
                ));
    }

    public static List<String> q114DepartmentsWhereAllEarnAbove50000(List<Employee> employees) {
        return employees.stream()
                .collect(Collectors.groupingBy(Employee::department))
                .entrySet()
                .stream()
                .filter(e -> e.getValue().stream().allMatch(emp -> emp.salary() > 50000))
                .map(Map.Entry::getKey)
                .toList();
    }

    public static List<String> q115DepartmentsWhereAnyEarnAbove150000(List<Employee> employees) {
        return employees.stream()
                .collect(Collectors.groupingBy(Employee::department))
                .entrySet()
                .stream()
                .filter(e -> e.getValue().stream().anyMatch(emp -> emp.salary() > 150000))
                .map(Map.Entry::getKey)
                .toList();
    }

    public static Map<Boolean, List<Employee>> q116PartitionSeniorJunior(List<Employee> employees) {
        return employees.stream().collect(Collectors.partitioningBy(e -> e.age() >= 40));
    }

    public static Map<String, Optional<Employee>> q117OldestEmployeeByDepartment(
            List<Employee> employees
    ) {
        return employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::department,
                        Collectors.maxBy(Comparator.comparing(Employee::age))
                ));
    }

    public static List<Employee> q118SortByDepartmentThenSalaryDesc(List<Employee> employees) {
        return employees.stream()
                .sorted(Comparator.comparing(Employee::department)
                        .thenComparing(Comparator.comparing(Employee::salary).reversed()))
                .toList();
    }

    public static Map<String, DoubleSummaryStatistics> q119SalaryStatsByDepartment(
            List<Employee> employees
    ) {
        return employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::department,
                        Collectors.summarizingDouble(Employee::salary)
                ));
    }

    public static Map<String, List<Employee>> q120TopTwoPaidEmployeesByDepartment(
            List<Employee> employees
    ) {
        return employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::department,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> list.stream()
                                        .sorted(Comparator.comparing(Employee::salary).reversed())
                                        .limit(2)
                                        .toList()
                        )
                ));
    }

    // Q121: Find the median salary in each department.
    public static Map<String, OptionalDouble> q121MedianSalaryByDepartment(
            List<Employee> employees
    ) {
        return employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::department,
                        Collectors.collectingAndThen(
                                Collectors.mapping(Employee::salary, Collectors.toList()),
                                StreamApiInterviewSolutions::median
                        )
                ));
    }

    // Q122: Find employees earning more than their own department's average salary.
    public static List<Employee> q122EmployeesAboveDepartmentAverageSalary(
            List<Employee> employees
    ) {
        Map<String, Double> avgSalaryByDept = employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::department,
                        Collectors.averagingDouble(Employee::salary)
                ));

        return employees.stream()
                .filter(e -> e.salary() > avgSalaryByDept.get(e.department()))
                .toList();
    }

    // Q123: Return dense salary ranks inside each department.
    public static Map<String, Map<Integer, List<Employee>>> q123DenseSalaryRanksByDepartment(
            List<Employee> employees
    ) {
        return employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::department,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> {
                                    List<Double> salaries = list.stream()
                                            .map(Employee::salary)
                                            .distinct()
                                            .sorted(Comparator.reverseOrder())
                                            .toList();

                                    return list.stream()
                                            .collect(Collectors.groupingBy(
                                                    e -> salaries.indexOf(e.salary()) + 1
                                            ));
                                }
                        )
                ));
    }

    // Q124: Find the top N highest-paid employees from each department.
    public static Map<String, List<Employee>> q124TopNPaidEmployeesByDepartment(
            List<Employee> employees,
            int n
    ) {
        return employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::department,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> list.stream()
                                        .sorted(Comparator.comparing(Employee::salary).reversed())
                                        .limit(n)
                                        .toList()
                        )
                ));
    }

    // Q125: Find departments where the highest-lowest salary gap is above 100000.
    public static List<String> q125DepartmentsWithSalaryGapAbove100000(List<Employee> employees) {
        return employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::department,
                        Collectors.summarizingDouble(Employee::salary)
                ))
                .entrySet()
                .stream()
                .filter(e -> e.getValue().getMax() - e.getValue().getMin() > 100000)
                .map(Map.Entry::getKey)
                .toList();
    }

    // Q126: Find the highest-paid employee in each department without Optional values.
    public static Map<String, Employee> q126HighestPaidEmployeeByDepartmentNoOptional(
            List<Employee> employees
    ) {
        return employees.stream()
                .collect(Collectors.toMap(
                        Employee::department,
                        Function.identity(),
                        BinaryOperator.maxBy(Comparator.comparing(Employee::salary))
                ));
    }

    // Q127: Group employees by department and then salary band: LOW, MID, or HIGH.
    public static Map<String, Map<String, List<Employee>>> q127EmployeesByDepartmentAndSalaryBand(
            List<Employee> employees
    ) {
        return employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::department,
                        Collectors.groupingBy(StreamApiInterviewSolutions::salaryBand)
                ));
    }

    // Q128: Find each department's salary share percentage of total company salary.
    public static Map<String, Double> q128DepartmentSalarySharePercentage(
            List<Employee> employees
    ) {
        double totalSalary = employees.stream().mapToDouble(Employee::salary).sum();

        return employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::department,
                        Collectors.summingDouble(Employee::salary)
                ))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> totalSalary == 0 ? 0.0 : (e.getValue() * 100.0) / totalSalary
                ));
    }

    // Q129: Find employee names that appear in more than one department.
    public static List<String> q129EmployeeNamesInMoreThanOneDepartment(
            List<Employee> employees
    ) {
        return employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::name,
                        Collectors.mapping(Employee::department, Collectors.toSet())
                ))
                .entrySet()
                .stream()
                .filter(e -> e.getValue().size() > 1)
                .map(Map.Entry::getKey)
                .toList();
    }

    // Q130: Build a report of department to gender to average salary.
    public static Map<String, Map<String, Double>> q130AverageSalaryByDepartmentAndGender(
            List<Employee> employees
    ) {
        return employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::department,
                        Collectors.groupingBy(
                                Employee::gender,
                                Collectors.averagingDouble(Employee::salary)
                        )
                ));
    }

    private static String sortedKey(String word) {
        return word.chars()
                .sorted()
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    private static OptionalDouble median(List<Double> values) {
        List<Double> sorted = values.stream().sorted().toList();
        int size = sorted.size();
        if (size == 0) {
            return OptionalDouble.empty();
        }
        if (size % 2 == 1) {
            return OptionalDouble.of(sorted.get(size / 2));
        }
        return OptionalDouble.of((sorted.get(size / 2 - 1) + sorted.get(size / 2)) / 2.0);
    }

    private static String salaryBand(Employee employee) {
        if (employee.salary() < 60000) {
            return "LOW";
        }
        if (employee.salary() < 120000) {
            return "MID";
        }
        return "HIGH";
    }
}
