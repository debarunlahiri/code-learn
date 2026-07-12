import java.util.*;

/**
 * Frequently asked DSA problems in WITCH/service-based company interviews.
 * WITCH commonly refers to Wipro, Infosys, TCS, Cognizant and HCL.
 *
 * Each numbered method matches the interview list and favors a clear,
 * interview-friendly solution. Run main() for a small smoke test.
 */
public final class WITCHDSA {
    private WITCHDSA() {
    }

    // 1. Two Sum - O(n) time, O(n) space.
    public static int[] twoSum(int[] a, int target) {
        Map<Integer, Integer> seen = new HashMap<>();
        for (int i = 0; i < a.length; i++) {
            if (seen.containsKey(target - a[i]))
                return new int[] { seen.get(target - a[i]), i };
            seen.put(a[i], i);
        }
        return new int[0];
    }

    // 2. Remove Duplicates from Sorted Array.
    public static int removeDuplicates(int[] a) {
        if (a.length == 0)
            return 0;
        int write = 1;
        for (int read = 1; read < a.length; read++)
            if (a[read] != a[read - 1])
                a[write++] = a[read];
        return write;
    }

    // 3. Remove Element.
    public static int removeElement(int[] a, int value) {
        int write = 0;
        for (int number : a)
            if (number != value)
                a[write++] = number;
        return write;
    }

    // 4. Move Zeroes.
    public static void moveZeroes(int[] a) {
        int write = 0;
        for (int number : a)
            if (number != 0)
                a[write++] = number;
        while (write < a.length)
            a[write++] = 0;
    }

    // 5. Sort Colors (Dutch National Flag).
    public static void sortColors(int[] a) {
        int low = 0, mid = 0, high = a.length - 1;
        while (mid <= high) {
            if (a[mid] == 0)
                swap(a, low++, mid++);
            else if (a[mid] == 1)
                mid++;
            else
                swap(a, mid, high--);
        }
    }

    // 6. Best Time to Buy and Sell Stock.
    public static int maxProfit(int[] prices) {
        int min = Integer.MAX_VALUE, answer = 0;
        for (int price : prices) {
            min = Math.min(min, price);
            answer = Math.max(answer, price - min);
        }
        return answer;
    }

    // 7. Maximum Subarray (Kadane's algorithm).
    public static int maxSubArray(int[] a) {
        int current = a[0], best = a[0];
        for (int i = 1; i < a.length; i++) {
            current = Math.max(a[i], current + a[i]);
            best = Math.max(best, current);
        }
        return best;
    }

    // 8. Merge Sorted Array into nums1.
    public static void mergeSortedArray(int[] nums1, int m, int[] nums2, int n) {
        int i = m - 1, j = n - 1, write = m + n - 1;
        while (j >= 0)
            nums1[write--] = i >= 0 && nums1[i] > nums2[j] ? nums1[i--] : nums2[j--];
    }

    // 9. Majority Element - easy HashMap solution.
    public static int majorityElement(int[] a) {
        Map<Integer, Integer> counts = new HashMap<>();
        for (int number : a) {
            int count = counts.getOrDefault(number, 0) + 1;
            counts.put(number, count);
            if (count > a.length / 2)
                return number;
        }
        return -1;
    }

    // 10. Rotate Array by k steps.
    public static void rotateArray(int[] a, int k) {
        if (a.length == 0)
            return;
        k %= a.length;
        reverse(a, 0, a.length - 1);
        reverse(a, 0, k - 1);
        reverse(a, k, a.length - 1);
    }

    // 11. Find the Duplicate Number - easy HashSet solution.
    public static int findDuplicate(int[] a) {
        Set<Integer> seen = new HashSet<>();
        for (int number : a) {
            if (!seen.add(number))
                return number;
        }
        return -1;
    }

    // 12. Missing Number.
    public static int missingNumber(int[] a) {
        int answer = a.length;
        for (int i = 0; i < a.length; i++)
            answer ^= i ^ a[i];
        return answer;
    }

    // 13. First Missing Positive - easy HashSet solution.
    public static int firstMissingPositive(int[] a) {
        Set<Integer> values = new HashSet<>();
        for (int number : a)
            values.add(number);
        int missing = 1;
        while (values.contains(missing))
            missing++;
        return missing;
    }

    // 14. Intersection of Two Arrays (unique values).
    public static int[] intersection(int[] a, int[] b) {
        Set<Integer> values = new HashSet<>(), result = new HashSet<>();
        for (int number : a)
            values.add(number);
        for (int number : b)
            if (values.contains(number))
                result.add(number);
        return result.stream().mapToInt(Integer::intValue).toArray();
    }

    // 15. Valid Anagram.
    public static boolean isAnagram(String a, String b) {
        if (a.length() != b.length())
            return false;
        Map<Character, Integer> counts = new HashMap<>();
        for (char c : a.toCharArray())
            counts.merge(c, 1, Integer::sum);
        for (char c : b.toCharArray())
            if (counts.merge(c, -1, Integer::sum) < 0)
                return false;
        return true;
    }

    // 16. Group Anagrams.
    public static List<List<String>> groupAnagrams(String[] words) {
        Map<String, List<String>> groups = new HashMap<>();
        for (String word : words) {
            char[] key = word.toCharArray();
            Arrays.sort(key);
            groups.computeIfAbsent(new String(key), x -> new ArrayList<>()).add(word);
        }
        return new ArrayList<>(groups.values());
    }

    // 17. Valid Palindrome.
    public static boolean isPalindrome(String text) {
        int left = 0, right = text.length() - 1;
        while (left < right) {
            while (left < right && !Character.isLetterOrDigit(text.charAt(left)))
                left++;
            while (left < right && !Character.isLetterOrDigit(text.charAt(right)))
                right--;
            if (Character.toLowerCase(text.charAt(left++)) != Character.toLowerCase(text.charAt(right--)))
                return false;
        }
        return true;
    }

    // 18. Reverse String in-place.
    public static void reverseString(char[] text) {
        for (int left = 0, right = text.length - 1; left < right; left++, right--) {
            char t = text[left];
            text[left] = text[right];
            text[right] = t;
        }
    }

    // 19. Reverse Words in a String.
    public static String reverseWords(String text) {
        String trimmed = text.trim();
        if (trimmed.isEmpty())
            return "";
        List<String> words = Arrays.asList(trimmed.split("\\s+"));
        Collections.reverse(words);
        return String.join(" ", words);
    }

    // 20. Longest Common Prefix.
    public static String longestCommonPrefix(String[] words) {
        if (words.length == 0)
            return "";
        String prefix = words[0];
        for (int i = 1; i < words.length; i++)
            while (!words[i].startsWith(prefix))
                prefix = prefix.substring(0, prefix.length() - 1);
        return prefix;
    }

    // 21. Valid Parentheses.
    public static boolean validParentheses(String text) {
        Deque<Character> stack = new ArrayDeque<>();
        for (char c : text.toCharArray()) {
            if (c == '(')
                stack.push(')');
            else if (c == '[')
                stack.push(']');
            else if (c == '{')
                stack.push('}');
            else if (stack.isEmpty() || stack.pop() != c)
                return false;
        }
        return stack.isEmpty();
    }

    // 22. Min Stack with O(1) getMin.
    public static final class MinStack {
        private final Deque<Integer> values = new ArrayDeque<>(), minimums = new ArrayDeque<>();

        public void push(int value) {
            values.push(value);
            if (minimums.isEmpty() || value <= minimums.peek())
                minimums.push(value);
        }

        public void pop() {
            if (values.pop().equals(minimums.peek()))
                minimums.pop();
        }

        public int top() {
            return values.peek();
        }

        public int getMin() {
            return minimums.peek();
        }
    }

    // 23. Next Greater Element I.
    public static int[] nextGreaterElement(int[] subset, int[] all) {
        Map<Integer, Integer> next = new HashMap<>();
        Deque<Integer> stack = new ArrayDeque<>();
        for (int value : all) {
            while (!stack.isEmpty() && stack.peek() < value)
                next.put(stack.pop(), value);
            stack.push(value);
        }
        int[] answer = new int[subset.length];
        for (int i = 0; i < subset.length; i++)
            answer[i] = next.getOrDefault(subset[i], -1);
        return answer;
    }

    // 24. Implement strStr/indexOf.
    public static int strStr(String text, String pattern) {
        if (pattern.isEmpty())
            return 0;
        for (int i = 0; i + pattern.length() <= text.length(); i++)
            if (text.regionMatches(i, pattern, 0, pattern.length()))
                return i;
        return -1;
    }

    // 25. Longest Substring Without Repeating Characters.
    public static int lengthOfLongestSubstring(String text) {
        Map<Character, Integer> last = new HashMap<>();
        int left = 0, best = 0;
        for (int right = 0; right < text.length(); right++) {
            left = Math.max(left, last.getOrDefault(text.charAt(right), -1) + 1);
            last.put(text.charAt(right), right);
            best = Math.max(best, right - left + 1);
        }
        return best;
    }

    // 26. Contains Duplicate.
    public static boolean containsDuplicate(int[] a) {
        Set<Integer> seen = new HashSet<>();
        for (int number : a)
            if (!seen.add(number))
                return true;
        return false;
    }

    // 27. Single Number.
    public static int singleNumber(int[] a) {
        int answer = 0;
        for (int number : a)
            answer ^= number;
        return answer;
    }

    // 28. Plus One.
    public static int[] plusOne(int[] digits) {
        for (int i = digits.length - 1; i >= 0; i--) {
            if (digits[i] < 9) {
                digits[i]++;
                return digits;
            }
            digits[i] = 0;
        }
        int[] answer = new int[digits.length + 1];
        answer[0] = 1;
        return answer;
    }

    // 29. Add Binary.
    public static String addBinary(String a, String b) {
        StringBuilder result = new StringBuilder();
        int i = a.length() - 1, j = b.length() - 1, carry = 0;
        while (i >= 0 || j >= 0 || carry != 0) {
            int sum = carry + (i >= 0 ? a.charAt(i--) - '0' : 0) + (j >= 0 ? b.charAt(j--) - '0' : 0);
            result.append(sum % 2);
            carry = sum / 2;
        }
        return result.reverse().toString();
    }

    // 30. Integer square root.
    public static int sqrt(int x) {
        int low = 0, high = x, answer = 0;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if ((long) mid * mid <= x) {
                answer = mid;
                low = mid + 1;
            } else
                high = mid - 1;
        }
        return answer;
    }

    // 31. Search Insert Position.
    public static int searchInsert(int[] a, int target) {
        int low = 0, high = a.length;
        while (low < high) {
            int mid = low + (high - low) / 2;
            if (a[mid] < target)
                low = mid + 1;
            else
                high = mid;
        }
        return low;
    }

    // 32. Find Minimum in Rotated Sorted Array - easy linear scan.
    public static int findMin(int[] a) {
        int minimum = a[0];
        for (int number : a)
            minimum = Math.min(minimum, number);
        return minimum;
    }

    // 33. Find Peak Element - easy linear scan.
    public static int findPeakElement(int[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            if (a[i] > a[i + 1])
                return i;
        }
        return a.length - 1;
    }

    // 34. Count and Say.
    public static String countAndSay(int n) {
        String current = "1";
        for (int round = 1; round < n; round++) {
            StringBuilder next = new StringBuilder();
            for (int i = 0; i < current.length();) {
                int j = i;
                while (j < current.length() && current.charAt(j) == current.charAt(i))
                    j++;
                next.append(j - i).append(current.charAt(i));
                i = j;
            }
            current = next.toString();
        }
        return current;
    }

    // 35. Pascal's Triangle.
    public static List<List<Integer>> pascalTriangle(int rows) {
        List<List<Integer>> answer = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j <= i; j++)
                row.add(j == 0 || j == i ? 1 : answer.get(i - 1).get(j - 1) + answer.get(i - 1).get(j));
            answer.add(row);
        }
        return answer;
    }

    // 36. Merge Intervals.
    public static int[][] mergeIntervals(int[][] intervals) {
        if (intervals.length == 0)
            return intervals;
        Arrays.sort(intervals, Comparator.comparingInt(x -> x[0]));
        List<int[]> result = new ArrayList<>();
        int[] current = intervals[0];
        result.add(current);
        for (int[] interval : intervals)
            if (interval[0] <= current[1])
                current[1] = Math.max(current[1], interval[1]);
            else {
                current = interval;
                result.add(current);
            }
        return result.toArray(new int[0][]);
    }

    // 37. Spiral Matrix.
    public static List<Integer> spiralOrder(int[][] matrix) {
        List<Integer> answer = new ArrayList<>();
        if (matrix.length == 0)
            return answer;
        int top = 0, bottom = matrix.length - 1, left = 0, right = matrix[0].length - 1;
        while (top <= bottom && left <= right) {
            for (int c = left; c <= right; c++)
                answer.add(matrix[top][c]);
            top++;
            for (int r = top; r <= bottom; r++)
                answer.add(matrix[r][right]);
            right--;
            if (top <= bottom)
                for (int c = right; c >= left; c--)
                    answer.add(matrix[bottom][c]);
            bottom--;
            if (left <= right)
                for (int r = bottom; r >= top; r--)
                    answer.add(matrix[r][left]);
            left++;
        }
        return answer;
    }

    // 38. Set Matrix Zeroes - easy solution using row and column sets.
    public static void setZeroes(int[][] matrix) {
        Set<Integer> zeroRows = new HashSet<>();
        Set<Integer> zeroColumns = new HashSet<>();
        for (int row = 0; row < matrix.length; row++) {
            for (int column = 0; column < matrix[0].length; column++) {
                if (matrix[row][column] == 0) {
                    zeroRows.add(row);
                    zeroColumns.add(column);
                }
            }
        }
        for (int row = 0; row < matrix.length; row++) {
            for (int column = 0; column < matrix[0].length; column++) {
                if (zeroRows.contains(row) || zeroColumns.contains(column))
                    matrix[row][column] = 0;
            }
        }
    }

    // 39. Rotate Image 90 degrees clockwise.
    public static void rotateImage(int[][] matrix) {
        for (int r = 0; r < matrix.length; r++)
            for (int c = r + 1; c < matrix.length; c++) {
                int t = matrix[r][c];
                matrix[r][c] = matrix[c][r];
                matrix[c][r] = t;
            }
        for (int[] row : matrix)
            reverse(row, 0, row.length - 1);
    }

    // 40. Word Search.
    public static boolean wordSearch(char[][] board, String word) {
        for (int r = 0; r < board.length; r++)
            for (int c = 0; c < board[0].length; c++)
                if (search(board, word, r, c, 0))
                    return true;
        return false;
    }

    private static boolean search(char[][] b, String w, int r, int c, int index) {
        if (index == w.length())
            return true;
        if (r < 0 || r == b.length || c < 0 || c == b[0].length || b[r][c] != w.charAt(index))
            return false;
        char saved = b[r][c];
        b[r][c] = '#';
        boolean found = search(b, w, r + 1, c, index + 1) || search(b, w, r - 1, c, index + 1)
                || search(b, w, r, c + 1, index + 1) || search(b, w, r, c - 1, index + 1);
        b[r][c] = saved;
        return found;
    }

    // 41. Queue using two stacks (amortized O(1)).
    public static final class QueueUsingStacks {
        private final Deque<Integer> input = new ArrayDeque<>(), output = new ArrayDeque<>();

        public void offer(int value) {
            input.push(value);
        }

        public int poll() {
            move();
            return output.pop();
        }

        public int peek() {
            move();
            return output.peek();
        }

        public boolean isEmpty() {
            return input.isEmpty() && output.isEmpty();
        }

        private void move() {
            if (output.isEmpty())
                while (!input.isEmpty())
                    output.push(input.pop());
        }
    }

    // 42. Stack using one queue.
    public static final class StackUsingQueues {
        private final Queue<Integer> queue = new ArrayDeque<>();

        public void push(int value) {
            queue.offer(value);
            for (int i = 1; i < queue.size(); i++)
                queue.offer(queue.poll());
        }

        public int pop() {
            return queue.remove();
        }

        public int top() {
            return queue.element();
        }

        public boolean isEmpty() {
            return queue.isEmpty();
        }
    }

    public static final class ListNode {
        public int value;
        public ListNode next;

        public ListNode(int value) {
            this.value = value;
        }

        public ListNode(int value, ListNode next) {
            this.value = value;
            this.next = next;
        }
    }

    // 43. Linked List Cycle.
    public static boolean hasCycle(ListNode head) {
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast)
                return true;
        }
        return false;
    }

    // 44. Middle of the Linked List.
    public static ListNode middleNode(ListNode head) {
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

    // 45. Reverse Linked List.
    public static ListNode reverseList(ListNode head) {
        ListNode previous = null;
        while (head != null) {
            ListNode next = head.next;
            head.next = previous;
            previous = head;
            head = next;
        }
        return previous;
    }

    // 46. Merge Two Sorted Lists.
    public static ListNode mergeTwoLists(ListNode a, ListNode b) {
        ListNode dummy = new ListNode(0), tail = dummy;
        while (a != null && b != null) {
            if (a.value <= b.value) {
                tail.next = a;
                a = a.next;
            } else {
                tail.next = b;
                b = b.next;
            }
            tail = tail.next;
        }
        tail.next = a != null ? a : b;
        return dummy.next;
    }

    // 47. Intersection of Two Linked Lists - easy HashSet solution.
    public static ListNode getIntersectionNode(ListNode a, ListNode b) {
        Set<ListNode> nodes = new HashSet<>();
        while (a != null) {
            nodes.add(a);
            a = a.next;
        }
        while (b != null) {
            if (nodes.contains(b))
                return b;
            b = b.next;
        }
        return null;
    }

    // 48. Remove Nth Node From End.
    public static ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode dummy = new ListNode(0, head), fast = dummy, slow = dummy;
        for (int i = 0; i <= n; i++)
            fast = fast.next;
        while (fast != null) {
            fast = fast.next;
            slow = slow.next;
        }
        slow.next = slow.next.next;
        return dummy.next;
    }

    // 49. Palindrome Linked List - easy ArrayList solution.
    public static boolean isPalindrome(ListNode head) {
        List<Integer> values = new ArrayList<>();
        while (head != null) {
            values.add(head.value);
            head = head.next;
        }
        int left = 0;
        int right = values.size() - 1;
        while (left < right) {
            if (!values.get(left).equals(values.get(right)))
                return false;
            left++;
            right--;
        }
        return true;
    }

    // 50a. Bubble Sort.
    public static void bubbleSort(int[] a) {
        for (int end = a.length - 1; end > 0; end--) {
            boolean changed = false;
            for (int i = 0; i < end; i++)
                if (a[i] > a[i + 1]) {
                    swap(a, i, i + 1);
                    changed = true;
                }
            if (!changed)
                return;
        }
    }

    // 50b. Selection Sort.
    public static void selectionSort(int[] a) {
        for (int i = 0; i < a.length; i++) {
            int min = i;
            for (int j = i + 1; j < a.length; j++)
                if (a[j] < a[min])
                    min = j;
            swap(a, i, min);
        }
    }

    // 50c. Insertion Sort.
    public static void insertionSort(int[] a) {
        for (int i = 1; i < a.length; i++) {
            int value = a[i], j = i - 1;
            while (j >= 0 && a[j] > value) {
                a[j + 1] = a[j];
                j--;
            }
            a[j + 1] = value;
        }
    }

    private static void swap(int[] a, int i, int j) {
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    private static void reverse(int[] a, int left, int right) {
        while (left < right)
            swap(a, left++, right--);
    }

    public static void main(String[] args) {
        System.out.println("Two Sum: " + Arrays.toString(twoSum(new int[] { 2, 7, 11, 15 }, 9)));
        System.out.println("Maximum Subarray: " + maxSubArray(new int[] { -2, 1, -3, 4, -1, 2, 1, -5, 4 }));
        System.out.println("Valid Parentheses: " + validParentheses("{[()]}"));
        System.out.println("Longest Unique Substring: " + lengthOfLongestSubstring("abcabcbb"));
        int[] values = { 5, 1, 4, 2, 8 };
        insertionSort(values);
        System.out.println("Insertion Sort: " + Arrays.toString(values));
    }
}
