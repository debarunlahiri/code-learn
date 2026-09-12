import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class DSAByPattern {

    // ============================================================
    // LINKED LIST NODE
    // ============================================================

    public static class ListNode {

        int value;
        ListNode next;

        public ListNode(int value) {
            this.value = value;
        }

        public ListNode(int value, ListNode next) {
            this.value = value;
            this.next = next;
        }
    }


    // ============================================================
    // PATTERN 1: COMPARE EVERY PAIR
    // Problems:
    // 1. Two Sum
    // 11. Find Duplicate
    // 14. Intersection of Two Arrays
    // 26. Contains Duplicate
    // 47. Intersection of Two Linked Lists
    // ============================================================


    // 1. TWO SUM
    public static int[] twoSum(int[] a, int target) {

        for (int i = 0; i < a.length; i++) {

            for (int j = i + 1; j < a.length; j++) {

                if (a[i] + a[j] == target) {

                    return new int[] {i, j};
                }
            }
        }

        return new int[0];
    }


    // 11. FIND DUPLICATE
    public static int findDuplicate(int[] a) {

        for (int i = 0; i < a.length; i++) {

            for (int j = i + 1; j < a.length; j++) {

                if (a[i] == a[j]) {

                    return a[i];
                }
            }
        }

        return -1;
    }


    // 14. INTERSECTION OF TWO ARRAYS
    public static int[] intersection(int[] a, int[] b) {

        int[] temp = new int[Math.min(a.length, b.length)];

        int size = 0;

        for (int i = 0; i < a.length; i++) {

            for (int j = 0; j < b.length; j++) {

                if (a[i] == b[j]) {

                    boolean alreadyAdded = false;

                    for (int k = 0; k < size; k++) {

                        if (temp[k] == a[i]) {
                            alreadyAdded = true;
                            break;
                        }
                    }

                    if (!alreadyAdded) {

                        temp[size] = a[i];
                        size++;
                    }

                    break;
                }
            }
        }

        int[] result = new int[size];

        for (int i = 0; i < size; i++) {

            result[i] = temp[i];
        }

        return result;
    }


    // 26. CONTAINS DUPLICATE
    public static boolean containsDuplicate(int[] a) {

        for (int i = 0; i < a.length; i++) {

            for (int j = i + 1; j < a.length; j++) {

                if (a[i] == a[j]) {

                    return true;
                }
            }
        }

        return false;
    }


    // 47. INTERSECTION OF TWO LINKED LISTS
    public static ListNode getIntersectionNode(
            ListNode headA,
            ListNode headB) {

        ListNode first = headA;

        while (first != null) {

            ListNode second = headB;

            while (second != null) {

                if (first == second) {

                    return first;
                }

                second = second.next;
            }

            first = first.next;
        }

        return null;
    }


    // ============================================================
    // PATTERN 2: FIND + SHIFT
    // Problems:
    // 2. Remove Duplicates
    // 3. Remove Element
    // 4. Move Zeroes
    // 10. Rotate Array
    // ============================================================


    // 2. REMOVE DUPLICATES FROM SORTED ARRAY
    public static int removeDuplicates(int[] a) {

        if (a.length == 0) {
            return 0;
        }

        int length = a.length;

        int i = 0;

        while (i < length - 1) {

            if (a[i] == a[i + 1]) {

                for (int j = i + 1; j < length - 1; j++) {

                    a[j] = a[j + 1];
                }

                length--;

            } else {

                i++;
            }
        }

        return length;
    }


    // 3. REMOVE ELEMENT
    public static int removeElement(int[] a, int value) {

        int length = a.length;

        int i = 0;

        while (i < length) {

            if (a[i] == value) {

                for (int j = i; j < length - 1; j++) {

                    a[j] = a[j + 1];
                }

                length--;

            } else {

                i++;
            }
        }

        return length;
    }


    // 4. MOVE ZEROES
    public static void moveZeroes(int[] a) {

        int length = a.length;

        for (int i = 0; i < length; i++) {

            if (a[i] == 0) {

                for (int j = i; j < length - 1; j++) {

                    a[j] = a[j + 1];
                }

                a[length - 1] = 0;

                length--;

                i--;
            }
        }
    }


    // 10. ROTATE ARRAY
    public static void rotateArray(int[] a, int k) {

        if (a.length == 0) {
            return;
        }

        k = k % a.length;

        for (int rotation = 0; rotation < k; rotation++) {

            int last = a[a.length - 1];

            for (int i = a.length - 1; i > 0; i--) {

                a[i] = a[i - 1];
            }

            a[0] = last;
        }
    }


    // ============================================================
    // PATTERN 3: COUNT / FREQUENCY
    // Problems:
    // 9. Majority Element
    // 15. Valid Anagram
    // 16. Group Anagrams
    // 27. Single Number
    // 34. Count and Say
    // ============================================================


    // 9. MAJORITY ELEMENT
    public static int majorityElement(int[] a) {

        for (int i = 0; i < a.length; i++) {

            int count = 0;

            for (int j = 0; j < a.length; j++) {

                if (a[i] == a[j]) {

                    count++;
                }
            }

            if (count > a.length / 2) {

                return a[i];
            }
        }

        return -1;
    }


    // 15. VALID ANAGRAM
    public static boolean isAnagram(String a, String b) {

        if (a.length() != b.length()) {

            return false;
        }

        boolean[] used = new boolean[b.length()];

        for (int i = 0; i < a.length(); i++) {

            boolean found = false;

            for (int j = 0; j < b.length(); j++) {

                if (!used[j]
                        && a.charAt(i) == b.charAt(j)) {

                    used[j] = true;

                    found = true;

                    break;
                }
            }

            if (!found) {

                return false;
            }
        }

        return true;
    }


    // 16. GROUP ANAGRAMS
    public static List<List<String>> groupAnagrams(
            String[] words) {

        List<List<String>> groups =
                new ArrayList<List<String>>();

        for (int i = 0; i < words.length; i++) {

            boolean added = false;

            for (int j = 0; j < groups.size(); j++) {

                String firstWord =
                        groups.get(j).get(0);

                if (areAnagrams(
                        words[i],
                        firstWord)) {

                    groups.get(j).add(words[i]);

                    added = true;

                    break;
                }
            }

            if (!added) {

                List<String> newGroup =
                        new ArrayList<String>();

                newGroup.add(words[i]);

                groups.add(newGroup);
            }
        }

        return groups;
    }


    private static boolean areAnagrams(
            String a,
            String b) {

        if (a.length() != b.length()) {

            return false;
        }

        boolean[] used =
                new boolean[b.length()];

        for (int i = 0; i < a.length(); i++) {

            boolean found = false;

            for (int j = 0; j < b.length(); j++) {

                if (!used[j]
                        && a.charAt(i) == b.charAt(j)) {

                    used[j] = true;

                    found = true;

                    break;
                }
            }

            if (!found) {

                return false;
            }
        }

        return true;
    }


    // 27. SINGLE NUMBER
    public static int singleNumber(int[] a) {

        for (int i = 0; i < a.length; i++) {

            int count = 0;

            for (int j = 0; j < a.length; j++) {

                if (a[i] == a[j]) {

                    count++;
                }
            }

            if (count == 1) {

                return a[i];
            }
        }

        return -1;
    }


    // 34. COUNT AND SAY
    public static String countAndSay(int n) {

        String current = "1";

        for (int round = 1;
             round < n;
             round++) {

            String next = "";

            int i = 0;

            while (i < current.length()) {

                char currentChar =
                        current.charAt(i);

                int count = 0;

                while (i < current.length()
                        && current.charAt(i)
                        == currentChar) {

                    count++;

                    i++;
                }

                next =
                        next
                        + count
                        + currentChar;
            }

            current = next;
        }

        return current;
    }


    // ============================================================
    // PATTERN 4: RANGE / MAXIMUM
    // Problems:
    // 6. Best Time to Buy and Sell Stock
    // 7. Maximum Subarray
    // 25. Longest Substring Without Repeating
    // ============================================================


    // 6. BEST TIME TO BUY AND SELL STOCK
    public static int maxProfit(int[] prices) {

        int maxProfit = 0;

        for (int buy = 0;
             buy < prices.length;
             buy++) {

            for (int sell = buy + 1;
                 sell < prices.length;
                 sell++) {

                int profit =
                        prices[sell]
                        - prices[buy];

                if (profit > maxProfit) {

                    maxProfit = profit;
                }
            }
        }

        return maxProfit;
    }


    // 7. MAXIMUM SUBARRAY
    public static int maxSubArray(int[] a) {

        if (a.length == 0) {

            return 0;
        }

        int maxSum = a[0];

        for (int start = 0;
             start < a.length;
             start++) {

            int sum = 0;

            for (int end = start;
                 end < a.length;
                 end++) {

                sum = sum + a[end];

                if (sum > maxSum) {

                    maxSum = sum;
                }
            }
        }

        return maxSum;
    }


    // 25. LONGEST SUBSTRING WITHOUT REPEATING
    public static int lengthOfLongestSubstring(
            String text) {

        int maxLength = 0;

        for (int start = 0;
             start < text.length();
             start++) {

            for (int end = start;
                 end < text.length();
                 end++) {

                boolean duplicate = false;

                for (int i = start;
                     i <= end;
                     i++) {

                    for (int j = i + 1;
                         j <= end;
                         j++) {

                        if (text.charAt(i)
                                == text.charAt(j)) {

                            duplicate = true;

                            break;
                        }
                    }

                    if (duplicate) {

                        break;
                    }
                }

                if (duplicate) {

                    break;
                }

                int length =
                        end - start + 1;

                if (length > maxLength) {

                    maxLength = length;
                }
            }
        }

        return maxLength;
    }


    // ============================================================
    // PATTERN 5: TWO POINTERS
    // Problems:
    // 17. Valid Palindrome
    // 18. Reverse String
    // 49. Palindrome Linked List
    // ============================================================


    // 17. VALID PALINDROME
    public static boolean validPalindrome(
            String text) {

        String cleaned = "";

        for (int i = 0;
             i < text.length();
             i++) {

            char c = text.charAt(i);

            if (Character.isLetterOrDigit(c)) {

                cleaned =
                        cleaned
                        + Character.toLowerCase(c);
            }
        }

        int left = 0;

        int right =
                cleaned.length() - 1;

        while (left < right) {

            if (cleaned.charAt(left)
                    != cleaned.charAt(right)) {

                return false;
            }

            left++;

            right--;
        }

        return true;
    }


    // 18. REVERSE STRING
    public static void reverseString(
            char[] text) {

        int left = 0;

        int right =
                text.length - 1;

        while (left < right) {

            char temp =
                    text[left];

            text[left] =
                    text[right];

            text[right] =
                    temp;

            left++;

            right--;
        }
    }


    // 49. PALINDROME LINKED LIST
    public static boolean palindromeLinkedList(
            ListNode head) {

        ArrayList<Integer> values =
                new ArrayList<Integer>();

        ListNode current = head;

        while (current != null) {

            values.add(current.value);

            current =
                    current.next;
        }

        int left = 0;

        int right =
                values.size() - 1;

        while (left < right) {

            if (!values.get(left)
                    .equals(values.get(right))) {

                return false;
            }

            left++;

            right--;
        }

        return true;
    }


    // ============================================================
    // PATTERN 6: LINEAR SEARCH
    // Problems:
    // 12 Missing Number
    // 13 First Missing Positive
    // 20 Longest Common Prefix
    // 23 Next Greater Element
    // 24 strStr
    // 31 Search Insert
    // 32 Find Minimum
    // 33 Find Peak
    // ============================================================


    // 12. MISSING NUMBER
    public static int missingNumber(int[] a) {

        for (int number = 0;
             number <= a.length;
             number++) {

            boolean found = false;

            for (int i = 0;
                 i < a.length;
                 i++) {

                if (a[i] == number) {

                    found = true;

                    break;
                }
            }

            if (!found) {

                return number;
            }
        }

        return -1;
    }


    // 13. FIRST MISSING POSITIVE
    public static int firstMissingPositive(
            int[] a) {

        int number = 1;

        while (true) {

            boolean found = false;

            for (int i = 0;
                 i < a.length;
                 i++) {

                if (a[i] == number) {

                    found = true;

                    break;
                }
            }

            if (!found) {

                return number;
            }

            number++;
        }
    }


    // 20. LONGEST COMMON PREFIX
    public static String longestCommonPrefix(
            String[] words) {

        if (words.length == 0) {

            return "";
        }

        String first = words[0];

        String result = "";

        for (int i = 0;
             i < first.length();
             i++) {

            char current =
                    first.charAt(i);

            for (int j = 1;
                 j < words.length;
                 j++) {

                if (i >= words[j].length()) {

                    return result;
                }

                if (words[j].charAt(i)
                        != current) {

                    return result;
                }
            }

            result =
                    result + current;
        }

        return result;
    }


    // 23. NEXT GREATER ELEMENT
    public static int[] nextGreaterElement(
            int[] subset,
            int[] all) {

        int[] answer =
                new int[subset.length];

        for (int i = 0;
             i < subset.length;
             i++) {

            answer[i] = -1;

            int position = -1;

            for (int j = 0;
                 j < all.length;
                 j++) {

                if (all[j] == subset[i]) {

                    position = j;

                    break;
                }
            }

            if (position != -1) {

                for (int j = position + 1;
                     j < all.length;
                     j++) {

                    if (all[j] > subset[i]) {

                        answer[i] = all[j];

                        break;
                    }
                }
            }
        }

        return answer;
    }


    // 24. IMPLEMENT strStr
    public static int strStr(
            String text,
            String pattern) {

        if (pattern.length() == 0) {

            return 0;
        }

        if (pattern.length()
                > text.length()) {

            return -1;
        }

        for (int i = 0;
             i <= text.length()
                     - pattern.length();
             i++) {

            boolean match = true;

            for (int j = 0;
                 j < pattern.length();
                 j++) {

                if (text.charAt(i + j)
                        != pattern.charAt(j)) {

                    match = false;

                    break;
                }
            }

            if (match) {

                return i;
            }
        }

        return -1;
    }


    // 31. SEARCH INSERT POSITION
    public static int searchInsert(
            int[] a,
            int target) {

        for (int i = 0;
             i < a.length;
             i++) {

            if (a[i] >= target) {

                return i;
            }
        }

        return a.length;
    }


    // 32. FIND MINIMUM
    public static int findMin(int[] a) {

        int minimum = a[0];

        for (int i = 1;
             i < a.length;
             i++) {

            if (a[i] < minimum) {

                minimum = a[i];
            }
        }

        return minimum;
    }


    // 33. FIND PEAK ELEMENT
    public static int findPeakElement(
            int[] a) {

        for (int i = 0;
             i < a.length;
             i++) {

            boolean leftOk;

            boolean rightOk;

            if (i == 0) {

                leftOk = true;

            } else {

                leftOk =
                        a[i] > a[i - 1];
            }

            if (i == a.length - 1) {

                rightOk = true;

            } else {

                rightOk =
                        a[i] > a[i + 1];
            }

            if (leftOk && rightOk) {

                return i;
            }
        }

        return -1;
    }


    // ============================================================
    // PATTERN 7: SORTING
    // ============================================================


    // 5. SORT COLORS
    public static void sortColors(int[] a) {

        for (int i = 0;
             i < a.length - 1;
             i++) {

            for (int j = 0;
                 j < a.length - 1 - i;
                 j++) {

                if (a[j] > a[j + 1]) {

                    int temp = a[j];

                    a[j] = a[j + 1];

                    a[j + 1] = temp;
                }
            }
        }
    }


    // 8. MERGE SORTED ARRAY
    public static void mergeSortedArray(
            int[] nums1,
            int m,
            int[] nums2,
            int n) {

        for (int i = 0; i < n; i++) {

            nums1[m + i] =
                    nums2[i];
        }

        int length = m + n;

        for (int i = 0;
             i < length - 1;
             i++) {

            for (int j = 0;
                 j < length - 1 - i;
                 j++) {

                if (nums1[j]
                        > nums1[j + 1]) {

                    int temp =
                            nums1[j];

                    nums1[j] =
                            nums1[j + 1];

                    nums1[j + 1] =
                            temp;
                }
            }
        }
    }


    // 36. MERGE INTERVALS
    public static int[][] mergeIntervals(
            int[][] intervals) {

        if (intervals.length == 0) {

            return new int[0][0];
        }

        boolean[] removed =
                new boolean[intervals.length];

        boolean changed = true;

        while (changed) {

            changed = false;

            for (int i = 0;
                 i < intervals.length;
                 i++) {

                if (removed[i]) {

                    continue;
                }

                for (int j = i + 1;
                     j < intervals.length;
                     j++) {

                    if (removed[j]) {

                        continue;
                    }

                    int start1 =
                            intervals[i][0];

                    int end1 =
                            intervals[i][1];

                    int start2 =
                            intervals[j][0];

                    int end2 =
                            intervals[j][1];

                    boolean overlap =
                            start1 <= end2
                            && start2 <= end1;

                    if (overlap) {

                        intervals[i][0] =
                                Math.min(
                                        start1,
                                        start2);

                        intervals[i][1] =
                                Math.max(
                                        end1,
                                        end2);

                        removed[j] = true;

                        changed = true;
                    }
                }
            }
        }

        int count = 0;

        for (int i = 0;
             i < removed.length;
             i++) {

            if (!removed[i]) {

                count++;
            }
        }

        int[][] result =
                new int[count][2];

        int index = 0;

        for (int i = 0;
             i < intervals.length;
             i++) {

            if (!removed[i]) {

                result[index][0] =
                        intervals[i][0];

                result[index][1] =
                        intervals[i][1];

                index++;
            }
        }

        return result;
    }


    // 46. MERGE TWO SORTED LINKED LISTS
    public static ListNode mergeTwoLists(
            ListNode a,
            ListNode b) {

        ArrayList<Integer> values =
                new ArrayList<Integer>();

        while (a != null) {

            values.add(a.value);

            a = a.next;
        }

        while (b != null) {

            values.add(b.value);

            b = b.next;
        }

        // Bubble sort

        for (int i = 0;
             i < values.size() - 1;
             i++) {

            for (int j = 0;
                 j < values.size() - 1 - i;
                 j++) {

                if (values.get(j)
                        > values.get(j + 1)) {

                    int temp =
                            values.get(j);

                    values.set(
                            j,
                            values.get(j + 1));

                    values.set(
                            j + 1,
                            temp);
                }
            }
        }

        ListNode head = null;

        ListNode tail = null;

        for (int value : values) {

            ListNode node =
                    new ListNode(value);

            if (head == null) {

                head = node;

                tail = node;

            } else {

                tail.next = node;

                tail = node;
            }
        }

        return head;
    }


    // 50A. BUBBLE SORT
    public static void bubbleSort(int[] a) {

        for (int i = 0;
             i < a.length - 1;
             i++) {

            for (int j = 0;
                 j < a.length - 1 - i;
                 j++) {

                if (a[j] > a[j + 1]) {

                    int temp = a[j];

                    a[j] = a[j + 1];

                    a[j + 1] = temp;
                }
            }
        }
    }


    // 50B. SELECTION SORT
    public static void selectionSort(int[] a) {

        for (int i = 0;
             i < a.length - 1;
             i++) {

            int minIndex = i;

            for (int j = i + 1;
                 j < a.length;
                 j++) {

                if (a[j]
                        < a[minIndex]) {

                    minIndex = j;
                }
            }

            int temp = a[i];

            a[i] = a[minIndex];

            a[minIndex] = temp;
        }
    }


    // 50C. INSERTION SORT
    public static void insertionSort(int[] a) {

        for (int i = 1;
             i < a.length;
             i++) {

            int current = a[i];

            int j = i - 1;

            while (j >= 0
                    && a[j] > current) {

                a[j + 1] = a[j];

                j--;
            }

            a[j + 1] = current;
        }
    }


    // ============================================================
    // PATTERN 8: CARRY
    // ============================================================


    // 28. PLUS ONE
    public static int[] plusOne(
            int[] digits) {

        int carry = 1;

        for (int i = digits.length - 1;
             i >= 0;
             i--) {

            int sum =
                    digits[i] + carry;

            digits[i] =
                    sum % 10;

            carry =
                    sum / 10;

            if (carry == 0) {

                return digits;
            }
        }

        int[] result =
                new int[digits.length + 1];

        result[0] = 1;

        return result;
    }


    // 29. ADD BINARY
    public static String addBinary(
            String a,
            String b) {

        int i = a.length() - 1;

        int j = b.length() - 1;

        int carry = 0;

        String result = "";

        while (i >= 0
                || j >= 0
                || carry > 0) {

            int first = 0;

            int second = 0;

            if (i >= 0) {

                first =
                        a.charAt(i) - '0';

                i--;
            }

            if (j >= 0) {

                second =
                        b.charAt(j) - '0';

                j--;
            }

            int sum =
                    first
                    + second
                    + carry;

            int digit =
                    sum % 2;

            carry =
                    sum / 2;

            result =
                    digit + result;
        }

        return result;
    }


    // ============================================================
    // PATTERN 9: STACK
    // ============================================================


    // 21. VALID PARENTHESES
    public static boolean validParentheses(
            String text) {

        Stack<Character> stack =
                new Stack<Character>();

        for (int i = 0;
             i < text.length();
             i++) {

            char c =
                    text.charAt(i);

            if (c == '('
                    || c == '['
                    || c == '{') {

                stack.push(c);

            } else {

                if (stack.isEmpty()) {

                    return false;
                }

                char open =
                        stack.pop();

                if (c == ')'
                        && open != '(') {

                    return false;
                }

                if (c == ']'
                        && open != '[') {

                    return false;
                }

                if (c == '}'
                        && open != '{') {

                    return false;
                }
            }
        }

        return stack.isEmpty();
    }


    // 22. MIN STACK
    public static class MinStack {

        ArrayList<Integer> values =
                new ArrayList<Integer>();

        public void push(int value) {

            values.add(value);
        }

        public void pop() {

            if (!values.isEmpty()) {

                values.remove(
                        values.size() - 1);
            }
        }

        public int top() {

            return values.get(
                    values.size() - 1);
        }

        public int getMin() {

            int minimum =
                    values.get(0);

            for (int i = 1;
                 i < values.size();
                 i++) {

                if (values.get(i)
                        < minimum) {

                    minimum =
                            values.get(i);
                }
            }

            return minimum;
        }
    }


    // 41. QUEUE USING TWO STACKS
    public static class QueueUsingStacks {

        Stack<Integer> stack1 =
                new Stack<Integer>();

        Stack<Integer> stack2 =
                new Stack<Integer>();

        public void offer(int value) {

            stack1.push(value);
        }

        public int poll() {

            while (!stack1.isEmpty()) {

                stack2.push(
                        stack1.pop());
            }

            int value =
                    stack2.pop();

            while (!stack2.isEmpty()) {

                stack1.push(
                        stack2.pop());
            }

            return value;
        }
    }


    // ============================================================
    // PATTERN 10: QUEUE
    // ============================================================


    // 42. STACK USING QUEUE
    public static class StackUsingQueue {

        Queue<Integer> queue =
                new LinkedList<Integer>();

        public void push(int value) {

            queue.offer(value);
        }

        public int pop() {

            int size =
                    queue.size();

            for (int i = 0;
                 i < size - 1;
                 i++) {

                queue.offer(
                        queue.poll());
            }

            return queue.poll();
        }

        public int top() {

            int size =
                    queue.size();

            for (int i = 0;
                 i < size - 1;
                 i++) {

                queue.offer(
                        queue.poll());
            }

            int value =
                    queue.peek();

            queue.offer(
                    queue.poll());

            return value;
        }
    }


    // ============================================================
    // PATTERN 11: MATRIX
    // ============================================================


    // 37. SPIRAL MATRIX
    public static List<Integer> spiralOrder(
            int[][] matrix) {

        List<Integer> result =
                new ArrayList<Integer>();

        if (matrix.length == 0) {

            return result;
        }

        int top = 0;

        int bottom =
                matrix.length - 1;

        int left = 0;

        int right =
                matrix[0].length - 1;

        while (top <= bottom
                && left <= right) {

            for (int col = left;
                 col <= right;
                 col++) {

                result.add(
                        matrix[top][col]);
            }

            top++;

            for (int row = top;
                 row <= bottom;
                 row++) {

                result.add(
                        matrix[row][right]);
            }

            right--;

            if (top <= bottom) {

                for (int col = right;
                     col >= left;
                     col--) {

                    result.add(
                            matrix[bottom][col]);
                }

                bottom--;
            }

            if (left <= right) {

                for (int row = bottom;
                     row >= top;
                     row--) {

                    result.add(
                            matrix[row][left]);
                }

                left++;
            }
        }

        return result;
    }


    // 38. SET MATRIX ZEROES
    public static void setZeroes(
            int[][] matrix) {

        int rows =
                matrix.length;

        int columns =
                matrix[0].length;

        int[][] copy =
                new int[rows][columns];

        for (int row = 0;
             row < rows;
             row++) {

            for (int col = 0;
                 col < columns;
                 col++) {

                copy[row][col] =
                        matrix[row][col];
            }
        }

        for (int row = 0;
             row < rows;
             row++) {

            for (int col = 0;
                 col < columns;
                 col++) {

                if (copy[row][col] == 0) {

                    for (int c = 0;
                         c < columns;
                         c++) {

                        matrix[row][c] = 0;
                    }

                    for (int r = 0;
                         r < rows;
                         r++) {

                        matrix[r][col] = 0;
                    }
                }
            }
        }
    }


    // 39. ROTATE IMAGE
    public static void rotateImage(
            int[][] matrix) {

        int n =
                matrix.length;

        int[][] rotated =
                new int[n][n];

        for (int row = 0;
             row < n;
             row++) {

            for (int col = 0;
                 col < n;
                 col++) {

                rotated[col]
                        [n - 1 - row] =
                        matrix[row][col];
            }
        }

        for (int row = 0;
             row < n;
             row++) {

            for (int col = 0;
                 col < n;
                 col++) {

                matrix[row][col] =
                        rotated[row][col];
            }
        }
    }


    // ============================================================
    // PATTERN 12: DFS / BACKTRACKING
    // ============================================================


    // 40. WORD SEARCH
    public static boolean wordSearch(
            char[][] board,
            String word) {

        for (int row = 0;
             row < board.length;
             row++) {

            for (int col = 0;
                 col < board[0].length;
                 col++) {

                boolean[][] visited =
                        new boolean
                        [board.length]
                        [board[0].length];

                if (searchWord(
                        board,
                        word,
                        row,
                        col,
                        0,
                        visited)) {

                    return true;
                }
            }
        }

        return false;
    }


    private static boolean searchWord(
            char[][] board,
            String word,
            int row,
            int col,
            int index,
            boolean[][] visited) {

        if (index == word.length()) {

            return true;
        }

        if (row < 0
                || row >= board.length
                || col < 0
                || col >= board[0].length) {

            return false;
        }

        if (visited[row][col]) {

            return false;
        }

        if (board[row][col]
                != word.charAt(index)) {

            return false;
        }

        visited[row][col] = true;

        boolean down =
                searchWord(
                        board,
                        word,
                        row + 1,
                        col,
                        index + 1,
                        visited);

        boolean up =
                searchWord(
                        board,
                        word,
                        row - 1,
                        col,
                        index + 1,
                        visited);

        boolean right =
                searchWord(
                        board,
                        word,
                        row,
                        col + 1,
                        index + 1,
                        visited);

        boolean left =
                searchWord(
                        board,
                        word,
                        row,
                        col - 1,
                        index + 1,
                        visited);

        visited[row][col] = false;

        return down
                || up
                || right
                || left;
    }


    // ============================================================
    // PATTERN 13: LINKED LIST TRAVERSAL
    // ============================================================


    // 43. LINKED LIST CYCLE
    public static boolean hasCycle(
            ListNode head) {

        ArrayList<ListNode> visited =
                new ArrayList<ListNode>();

        ListNode current = head;

        while (current != null) {

            for (int i = 0;
                 i < visited.size();
                 i++) {

                if (visited.get(i)
                        == current) {

                    return true;
                }
            }

            visited.add(current);

            current =
                    current.next;
        }

        return false;
    }


    // 44. MIDDLE OF LINKED LIST
    public static ListNode middleNode(
            ListNode head) {

        int length = 0;

        ListNode current = head;

        while (current != null) {

            length++;

            current =
                    current.next;
        }

        int middle =
                length / 2;

        current = head;

        for (int i = 0;
             i < middle;
             i++) {

            current =
                    current.next;
        }

        return current;
    }


    // 45. REVERSE LINKED LIST
    public static ListNode reverseList(
            ListNode head) {

        ListNode previous = null;

        ListNode current = head;

        while (current != null) {

            ListNode next =
                    current.next;

            current.next =
                    previous;

            previous =
                    current;

            current =
                    next;
        }

        return previous;
    }


    // 48. REMOVE NTH NODE FROM END
    public static ListNode removeNthFromEnd(
            ListNode head,
            int n) {

        int length = 0;

        ListNode current = head;

        while (current != null) {

            length++;

            current =
                    current.next;
        }

        int index =
                length - n;

        if (index == 0) {

            return head.next;
        }

        current = head;

        for (int i = 0;
             i < index - 1;
             i++) {

            current =
                    current.next;
        }

        current.next =
                current.next.next;

        return head;
    }


    // ============================================================
    // PATTERN 14: SIMULATION
    // ============================================================


    // 19. REVERSE WORDS
    public static String reverseWords(
            String text) {

        text = text.trim();

        String[] words =
                text.split("\\s+");

        String result = "";

        for (int i = words.length - 1;
             i >= 0;
             i--) {

            result =
                    result + words[i];

            if (i != 0) {

                result =
                        result + " ";
            }
        }

        return result;
    }


    // 35. PASCAL TRIANGLE
    public static List<List<Integer>>
    pascalTriangle(int rows) {

        List<List<Integer>> answer =
                new ArrayList<List<Integer>>();

        for (int i = 0;
             i < rows;
             i++) {

            List<Integer> row =
                    new ArrayList<Integer>();

            for (int j = 0;
                 j <= i;
                 j++) {

                if (j == 0
                        || j == i) {

                    row.add(1);

                } else {

                    int left =
                            answer
                            .get(i - 1)
                            .get(j - 1);

                    int right =
                            answer
                            .get(i - 1)
                            .get(j);

                    row.add(
                            left + right);
                }
            }

            answer.add(row);
        }

        return answer;
    }
}
