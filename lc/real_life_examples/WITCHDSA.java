import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Beginner-friendly / brute-force versions of the DSA problems
 * from the original WITCHDSA list.
 *
 * Main goals:
 * 1. Keep the code simple and readable.
 * 2. Prefer basic loops over optimized tricks.
 * 3. Avoid streams, lambdas, HashMap shortcuts, XOR tricks,
 *    Floyd cycle detection, Kadane, sliding window, etc.
 * 4. Keep every numbered problem in one complete Java file.
 */
public final class WITCHDSA {

    private WITCHDSA() {
    }

    // ------------------------------------------------------------
    // 1. TWO SUM
    // Brute Force:
    // Check every possible pair.
    // Time: O(n^2)
    // Space: O(1)
    // ------------------------------------------------------------
    public static int[] twoSum(int[] a, int target) {

        for (int i = 0; i < a.length; i++) {

            for (int j = i + 1; j < a.length; j++) {

                if (a[i] + a[j] == target) {
                    return new int[] { i, j };
                }
            }
        }

        return new int[0];
    }

    // ------------------------------------------------------------
    // 2. REMOVE DUPLICATES FROM SORTED ARRAY
    // Beginner brute-force style:
    // Whenever duplicate is found, shift remaining elements left.
    // Returns number of unique elements.
    // Time: O(n^2)
    // Space: O(1)
    // ------------------------------------------------------------
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

    // ------------------------------------------------------------
    // 3. REMOVE ELEMENT
    // Brute Force:
    // Find the value and shift the remaining array left.
    // Time: O(n^2)
    // Space: O(1)
    // ------------------------------------------------------------
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

    // ------------------------------------------------------------
    // 4. MOVE ZEROES
    // Brute Force:
    // If zero is found, shift everything left and put zero at end.
    // Time: O(n^2)
    // Space: O(1)
    // ------------------------------------------------------------
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

    // ------------------------------------------------------------
    // 5. SORT COLORS
    // Brute Force:
    // Bubble sort the array.
    // Time: O(n^2)
    // Space: O(1)
    // ------------------------------------------------------------
    public static void sortColors(int[] a) {

        for (int i = 0; i < a.length - 1; i++) {

            for (int j = 0; j < a.length - 1 - i; j++) {

                if (a[j] > a[j + 1]) {

                    int temp = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = temp;
                }
            }
        }
    }

    // ------------------------------------------------------------
    // 6. BEST TIME TO BUY AND SELL STOCK
    // Brute Force:
    // Try every buy day with every later sell day.
    // Time: O(n^2)
    // Space: O(1)
    // ------------------------------------------------------------
    public static int maxProfit(int[] prices) {

        int maxProfit = 0;

        for (int buy = 0; buy < prices.length; buy++) {

            for (int sell = buy + 1; sell < prices.length; sell++) {

                int profit = prices[sell] - prices[buy];

                if (profit > maxProfit) {
                    maxProfit = profit;
                }
            }
        }

        return maxProfit;
    }

    // ------------------------------------------------------------
    // 7. MAXIMUM SUBARRAY
    // Brute Force:
    // Start at every index and calculate every subarray sum.
    // Time: O(n^2)
    // Space: O(1)
    // ------------------------------------------------------------
    public static int maxSubArray(int[] a) {

        if (a.length == 0) {
            return 0;
        }

        int maxSum = a[0];

        for (int i = 0; i < a.length; i++) {

            int sum = 0;

            for (int j = i; j < a.length; j++) {

                sum = sum + a[j];

                if (sum > maxSum) {
                    maxSum = sum;
                }
            }
        }

        return maxSum;
    }

    // ------------------------------------------------------------
    // 8. MERGE SORTED ARRAY
    // Beginner brute-force:
    // Copy nums2 into nums1 and then bubble-sort nums1.
    // Time: O((m+n)^2)
    // Space: O(1)
    // ------------------------------------------------------------
    public static void mergeSortedArray(int[] nums1, int m, int[] nums2, int n) {

        for (int i = 0; i < n; i++) {
            nums1[m + i] = nums2[i];
        }

        int totalLength = m + n;

        for (int i = 0; i < totalLength - 1; i++) {

            for (int j = 0; j < totalLength - 1 - i; j++) {

                if (nums1[j] > nums1[j + 1]) {

                    int temp = nums1[j];
                    nums1[j] = nums1[j + 1];
                    nums1[j + 1] = temp;
                }
            }
        }
    }

    // ------------------------------------------------------------
    // 9. MAJORITY ELEMENT
    // Brute Force:
    // Count each element by scanning the entire array.
    // Time: O(n^2)
    // Space: O(1)
    // ------------------------------------------------------------
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

    // ------------------------------------------------------------
    // 10. ROTATE ARRAY BY K STEPS
    // Brute Force:
    // Move last element to front one rotation at a time.
    // Time: O(k*n)
    // Space: O(1)
    // ------------------------------------------------------------
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

    // ------------------------------------------------------------
    // 11. FIND THE DUPLICATE NUMBER
    // Brute Force:
    // Compare every pair.
    // Time: O(n^2)
    // Space: O(1)
    // ------------------------------------------------------------
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

    // ------------------------------------------------------------
    // 12. MISSING NUMBER
    // Brute Force:
    // For every number from 0 to n, search the entire array.
    // Time: O(n^2)
    // Space: O(1)
    // ------------------------------------------------------------
    public static int missingNumber(int[] a) {

        for (int number = 0; number <= a.length; number++) {

            boolean found = false;

            for (int i = 0; i < a.length; i++) {

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

    // ------------------------------------------------------------
    // 13. FIRST MISSING POSITIVE
    // Brute Force:
    // Start from 1 and keep searching until a number is missing.
    // Time: O(n^2)
    // Space: O(1)
    // ------------------------------------------------------------
    public static int firstMissingPositive(int[] a) {

        int number = 1;

        while (true) {

            boolean found = false;

            for (int i = 0; i < a.length; i++) {

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

    // ------------------------------------------------------------
    // 14. INTERSECTION OF TWO ARRAYS
    // Brute Force:
    // Compare each element of first array with every element in second.
    // Keep only unique answers.
    // Time: O(n*m + result^2)
    // ------------------------------------------------------------
    public static int[] intersection(int[] a, int[] b) {

        int[] temp = new int[Math.min(a.length, b.length)];
        int resultSize = 0;

        for (int i = 0; i < a.length; i++) {

            for (int j = 0; j < b.length; j++) {

                if (a[i] == b[j]) {

                    boolean alreadyAdded = false;

                    for (int k = 0; k < resultSize; k++) {

                        if (temp[k] == a[i]) {
                            alreadyAdded = true;
                            break;
                        }
                    }

                    if (!alreadyAdded) {
                        temp[resultSize] = a[i];
                        resultSize++;
                    }

                    break;
                }
            }
        }

        int[] result = new int[resultSize];

        for (int i = 0; i < resultSize; i++) {
            result[i] = temp[i];
        }

        return result;
    }

    // ------------------------------------------------------------
    // 15. VALID ANAGRAM
    // Brute Force:
    // Match every character from first string with one unused
    // character from second string.
    // Time: O(n^2)
    // Space: O(n)
    // ------------------------------------------------------------
    public static boolean isAnagram(String a, String b) {

        if (a.length() != b.length()) {
            return false;
        }

        boolean[] used = new boolean[b.length()];

        for (int i = 0; i < a.length(); i++) {

            boolean found = false;

            for (int j = 0; j < b.length(); j++) {

                if (!used[j] && a.charAt(i) == b.charAt(j)) {
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

    // ------------------------------------------------------------
    // 16. GROUP ANAGRAMS
    // Brute Force:
    // Compare each word with already-created groups.
    // The helper uses brute-force character matching.
    // ------------------------------------------------------------
    public static List<List<String>> groupAnagrams(String[] words) {

        List<List<String>> groups = new ArrayList<List<String>>();

        for (int i = 0; i < words.length; i++) {

            boolean added = false;

            for (int g = 0; g < groups.size(); g++) {

                String firstWord = groups.get(g).get(0);

                if (areAnagramsBruteForce(words[i], firstWord)) {
                    groups.get(g).add(words[i]);
                    added = true;
                    break;
                }
            }

            if (!added) {

                List<String> newGroup = new ArrayList<String>();
                newGroup.add(words[i]);
                groups.add(newGroup);
            }
        }

        return groups;
    }

    private static boolean areAnagramsBruteForce(String a, String b) {

        if (a.length() != b.length()) {
            return false;
        }

        boolean[] used = new boolean[b.length()];

        for (int i = 0; i < a.length(); i++) {

            boolean found = false;

            for (int j = 0; j < b.length(); j++) {

                if (!used[j] && a.charAt(i) == b.charAt(j)) {
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

    // ------------------------------------------------------------
    // 17. VALID PALINDROME
    // Beginner version:
    // First create a cleaned lowercase string.
    // Then compare characters from both ends.
    // ------------------------------------------------------------
    public static boolean isPalindrome(String text) {

        String cleaned = "";

        for (int i = 0; i < text.length(); i++) {

            char c = text.charAt(i);

            if (Character.isLetterOrDigit(c)) {
                cleaned = cleaned + Character.toLowerCase(c);
            }
        }

        int left = 0;
        int right = cleaned.length() - 1;

        while (left < right) {

            if (cleaned.charAt(left) != cleaned.charAt(right)) {
                return false;
            }

            left++;
            right--;
        }

        return true;
    }

    // ------------------------------------------------------------
    // 18. REVERSE STRING
    // Beginner version:
    // Swap first with last, second with second-last, etc.
    // ------------------------------------------------------------
    public static void reverseString(char[] text) {

        int left = 0;
        int right = text.length - 1;

        while (left < right) {

            char temp = text[left];
            text[left] = text[right];
            text[right] = temp;

            left++;
            right--;
        }
    }

    // ------------------------------------------------------------
    // 19. REVERSE WORDS IN A STRING
    // Beginner version:
    // Split words and manually append from last to first.
    // ------------------------------------------------------------
    public static String reverseWords(String text) {

        text = text.trim();

        if (text.length() == 0) {
            return "";
        }

        String[] words = text.split("\\s+");
        String result = "";

        for (int i = words.length - 1; i >= 0; i--) {

            result = result + words[i];

            if (i != 0) {
                result = result + " ";
            }
        }

        return result;
    }

    // ------------------------------------------------------------
    // 20. LONGEST COMMON PREFIX
    // Brute Force:
    // Compare characters column by column.
    // ------------------------------------------------------------
    public static String longestCommonPrefix(String[] words) {

        if (words.length == 0) {
            return "";
        }

        String first = words[0];
        String result = "";

        for (int i = 0; i < first.length(); i++) {

            char current = first.charAt(i);

            for (int j = 1; j < words.length; j++) {

                if (i >= words[j].length()) {
                    return result;
                }

                if (words[j].charAt(i) != current) {
                    return result;
                }
            }

            result = result + current;
        }

        return result;
    }

    // ------------------------------------------------------------
    // 21. VALID PARENTHESES
    // Simple stack implementation using char array.
    // ------------------------------------------------------------
    public static boolean validParentheses(String text) {

        char[] stack = new char[text.length()];
        int top = -1;

        for (int i = 0; i < text.length(); i++) {

            char c = text.charAt(i);

            if (c == '(' || c == '[' || c == '{') {

                top++;
                stack[top] = c;

            } else {

                if (top == -1) {
                    return false;
                }

                char open = stack[top];
                top--;

                if (c == ')' && open != '(') {
                    return false;
                }

                if (c == ']' && open != '[') {
                    return false;
                }

                if (c == '}' && open != '{') {
                    return false;
                }
            }
        }

        return top == -1;
    }

    // ------------------------------------------------------------
    // 22. MIN STACK
    // Brute-force getMin:
    // Store values normally.
    // Whenever getMin() is called, scan entire stack.
    // ------------------------------------------------------------
    public static final class MinStack {

        private final ArrayList<Integer> values = new ArrayList<Integer>();

        public void push(int value) {
            values.add(value);
        }

        public void pop() {

            if (values.size() > 0) {
                values.remove(values.size() - 1);
            }
        }

        public int top() {

            if (values.size() == 0) {
                throw new RuntimeException("Stack is empty");
            }

            return values.get(values.size() - 1);
        }

        public int getMin() {

            if (values.size() == 0) {
                throw new RuntimeException("Stack is empty");
            }

            int minimum = values.get(0);

            for (int i = 1; i < values.size(); i++) {

                if (values.get(i) < minimum) {
                    minimum = values.get(i);
                }
            }

            return minimum;
        }
    }

    // ------------------------------------------------------------
    // 23. NEXT GREATER ELEMENT I
    // Brute Force:
    // For every subset element, locate it in all[] and search right.
    // Time: O(n*m)
    // ------------------------------------------------------------
    public static int[] nextGreaterElement(int[] subset, int[] all) {

        int[] answer = new int[subset.length];

        for (int i = 0; i < subset.length; i++) {

            answer[i] = -1;

            int position = -1;

            for (int j = 0; j < all.length; j++) {

                if (all[j] == subset[i]) {
                    position = j;
                    break;
                }
            }

            if (position != -1) {

                for (int j = position + 1; j < all.length; j++) {

                    if (all[j] > subset[i]) {
                        answer[i] = all[j];
                        break;
                    }
                }
            }
        }

        return answer;
    }

    // ------------------------------------------------------------
    // 24. IMPLEMENT strStr / indexOf
    // Brute Force:
    // Try matching pattern from every possible start position.
    // ------------------------------------------------------------
    public static int strStr(String text, String pattern) {

        if (pattern.length() == 0) {
            return 0;
        }

        if (pattern.length() > text.length()) {
            return -1;
        }

        for (int i = 0; i <= text.length() - pattern.length(); i++) {

            boolean matches = true;

            for (int j = 0; j < pattern.length(); j++) {

                if (text.charAt(i + j) != pattern.charAt(j)) {
                    matches = false;
                    break;
                }
            }

            if (matches) {
                return i;
            }
        }

        return -1;
    }

    // ------------------------------------------------------------
    // 25. LONGEST SUBSTRING WITHOUT REPEATING CHARACTERS
    // Brute Force:
    // Start from every character and expand until duplicate appears.
    // Time: O(n^3)
    // Space: O(1)
    // ------------------------------------------------------------
    public static int lengthOfLongestSubstring(String text) {

        int maxLength = 0;

        for (int start = 0; start < text.length(); start++) {

            for (int end = start; end < text.length(); end++) {

                boolean duplicateFound = false;

                for (int i = start; i <= end; i++) {

                    for (int j = i + 1; j <= end; j++) {

                        if (text.charAt(i) == text.charAt(j)) {
                            duplicateFound = true;
                            break;
                        }
                    }

                    if (duplicateFound) {
                        break;
                    }
                }

                if (duplicateFound) {
                    break;
                }

                int length = end - start + 1;

                if (length > maxLength) {
                    maxLength = length;
                }
            }
        }

        return maxLength;
    }

    // ------------------------------------------------------------
    // 26. CONTAINS DUPLICATE
    // Brute Force:
    // Compare every pair.
    // ------------------------------------------------------------
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

    // ------------------------------------------------------------
    // 27. SINGLE NUMBER
    // Brute Force:
    // Count each number. Return number that occurs once.
    // ------------------------------------------------------------
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

    // ------------------------------------------------------------
    // 28. PLUS ONE
    // Simple school addition from the last digit.
    // ------------------------------------------------------------
    public static int[] plusOne(int[] digits) {

        int carry = 1;

        for (int i = digits.length - 1; i >= 0; i--) {

            int sum = digits[i] + carry;

            digits[i] = sum % 10;
            carry = sum / 10;

            if (carry == 0) {
                return digits;
            }
        }

        if (carry == 1) {

            int[] result = new int[digits.length + 1];
            result[0] = 1;

            for (int i = 0; i < digits.length; i++) {
                result[i + 1] = digits[i];
            }

            return result;
        }

        return digits;
    }

    // ------------------------------------------------------------
    // 29. ADD BINARY
    // Simple school addition from right to left.
    // ------------------------------------------------------------
    public static String addBinary(String a, String b) {

        int i = a.length() - 1;
        int j = b.length() - 1;
        int carry = 0;

        String result = "";

        while (i >= 0 || j >= 0 || carry > 0) {

            int firstDigit = 0;
            int secondDigit = 0;

            if (i >= 0) {
                firstDigit = a.charAt(i) - '0';
                i--;
            }

            if (j >= 0) {
                secondDigit = b.charAt(j) - '0';
                j--;
            }

            int sum = firstDigit + secondDigit + carry;

            int digit = sum % 2;
            carry = sum / 2;

            result = digit + result;
        }

        return result;
    }

    // ------------------------------------------------------------
    // 30. INTEGER SQUARE ROOT
    // Brute Force:
    // Try values from 0 upward.
    // ------------------------------------------------------------
    public static int sqrt(int x) {

        if (x == 0 || x == 1) {
            return x;
        }

        int answer = 0;

        for (int i = 1; i <= x; i++) {

            long square = (long) i * i;

            if (square <= x) {
                answer = i;
            } else {
                break;
            }
        }

        return answer;
    }

    // ------------------------------------------------------------
    // 31. SEARCH INSERT POSITION
    // Brute Force:
    // Linear scan.
    // ------------------------------------------------------------
    public static int searchInsert(int[] a, int target) {

        for (int i = 0; i < a.length; i++) {

            if (a[i] >= target) {
                return i;
            }
        }

        return a.length;
    }

    // ------------------------------------------------------------
    // 32. FIND MINIMUM IN ROTATED SORTED ARRAY
    // Brute Force:
    // Scan all values.
    // ------------------------------------------------------------
    public static int findMin(int[] a) {

        if (a.length == 0) {
            throw new RuntimeException("Array is empty");
        }

        int minimum = a[0];

        for (int i = 1; i < a.length; i++) {

            if (a[i] < minimum) {
                minimum = a[i];
            }
        }

        return minimum;
    }

    // ------------------------------------------------------------
    // 33. FIND PEAK ELEMENT
    // Brute Force:
    // Check every element against left and right neighbor.
    // ------------------------------------------------------------
    public static int findPeakElement(int[] a) {

        if (a.length == 0) {
            return -1;
        }

        for (int i = 0; i < a.length; i++) {

            boolean greaterThanLeft;
            boolean greaterThanRight;

            if (i == 0) {
                greaterThanLeft = true;
            } else {
                greaterThanLeft = a[i] > a[i - 1];
            }

            if (i == a.length - 1) {
                greaterThanRight = true;
            } else {
                greaterThanRight = a[i] > a[i + 1];
            }

            if (greaterThanLeft && greaterThanRight) {
                return i;
            }
        }

        return -1;
    }

    // ------------------------------------------------------------
    // 34. COUNT AND SAY
    // Straightforward simulation.
    // ------------------------------------------------------------
    public static String countAndSay(int n) {

        String current = "1";

        for (int round = 1; round < n; round++) {

            String next = "";
            int i = 0;

            while (i < current.length()) {

                char currentChar = current.charAt(i);
                int count = 0;

                while (i < current.length()
                        && current.charAt(i) == currentChar) {

                    count++;
                    i++;
                }

                next = next + count + currentChar;
            }

            current = next;
        }

        return current;
    }

    // ------------------------------------------------------------
    // 35. PASCAL'S TRIANGLE
    // Straightforward row-by-row construction.
    // ------------------------------------------------------------
    public static List<List<Integer>> pascalTriangle(int rows) {

        List<List<Integer>> answer = new ArrayList<List<Integer>>();

        for (int i = 0; i < rows; i++) {

            List<Integer> row = new ArrayList<Integer>();

            for (int j = 0; j <= i; j++) {

                if (j == 0 || j == i) {

                    row.add(1);

                } else {

                    int leftValue = answer.get(i - 1).get(j - 1);
                    int rightValue = answer.get(i - 1).get(j);

                    row.add(leftValue + rightValue);
                }
            }

            answer.add(row);
        }

        return answer;
    }

    // ------------------------------------------------------------
    // 36. MERGE INTERVALS
    // Beginner / brute-force style:
    // 1. Repeatedly scan interval pairs.
    // 2. If two intervals overlap, merge them.
    // 3. Mark the second interval as removed.
    // 4. Repeat until no more merging is possible.
    // ------------------------------------------------------------
    public static int[][] mergeIntervals(int[][] intervals) {

        if (intervals.length == 0) {
            return new int[0][0];
        }

        boolean[] removed = new boolean[intervals.length];

        boolean changed = true;

        while (changed) {

            changed = false;

            for (int i = 0; i < intervals.length; i++) {

                if (removed[i]) {
                    continue;
                }

                for (int j = i + 1; j < intervals.length; j++) {

                    if (removed[j]) {
                        continue;
                    }

                    int start1 = intervals[i][0];
                    int end1 = intervals[i][1];

                    int start2 = intervals[j][0];
                    int end2 = intervals[j][1];

                    boolean overlap =
                            start1 <= end2 && start2 <= end1;

                    if (overlap) {

                        if (start2 < intervals[i][0]) {
                            intervals[i][0] = start2;
                        }

                        if (end2 > intervals[i][1]) {
                            intervals[i][1] = end2;
                        }

                        removed[j] = true;
                        changed = true;
                    }
                }
            }
        }

        int count = 0;

        for (int i = 0; i < removed.length; i++) {

            if (!removed[i]) {
                count++;
            }
        }

        int[][] result = new int[count][2];
        int index = 0;

        for (int i = 0; i < intervals.length; i++) {

            if (!removed[i]) {

                result[index][0] = intervals[i][0];
                result[index][1] = intervals[i][1];

                index++;
            }
        }

        // Bubble sort final intervals by start value,
        // only to make output easier to read.
        for (int i = 0; i < result.length - 1; i++) {

            for (int j = 0; j < result.length - 1 - i; j++) {

                if (result[j][0] > result[j + 1][0]) {

                    int[] temp = result[j];
                    result[j] = result[j + 1];
                    result[j + 1] = temp;
                }
            }
        }

        return result;
    }

    // ------------------------------------------------------------
    // 37. SPIRAL MATRIX
    // Straightforward boundary traversal.
    // ------------------------------------------------------------
    public static List<Integer> spiralOrder(int[][] matrix) {

        List<Integer> answer = new ArrayList<Integer>();

        if (matrix.length == 0) {
            return answer;
        }

        int top = 0;
        int bottom = matrix.length - 1;
        int left = 0;
        int right = matrix[0].length - 1;

        while (top <= bottom && left <= right) {

            for (int column = left; column <= right; column++) {
                answer.add(matrix[top][column]);
            }

            top++;

            for (int row = top; row <= bottom; row++) {
                answer.add(matrix[row][right]);
            }

            right--;

            if (top <= bottom) {

                for (int column = right; column >= left; column--) {
                    answer.add(matrix[bottom][column]);
                }

                bottom--;
            }

            if (left <= right) {

                for (int row = bottom; row >= top; row--) {
                    answer.add(matrix[row][left]);
                }

                left++;
            }
        }

        return answer;
    }

    // ------------------------------------------------------------
    // 38. SET MATRIX ZEROES
    // Brute Force using a copy:
    // Read original copy and modify matrix.
    // ------------------------------------------------------------
    public static void setZeroes(int[][] matrix) {

        if (matrix.length == 0) {
            return;
        }

        int rows = matrix.length;
        int columns = matrix[0].length;

        int[][] copy = new int[rows][columns];

        for (int row = 0; row < rows; row++) {

            for (int column = 0; column < columns; column++) {
                copy[row][column] = matrix[row][column];
            }
        }

        for (int row = 0; row < rows; row++) {

            for (int column = 0; column < columns; column++) {

                if (copy[row][column] == 0) {

                    for (int c = 0; c < columns; c++) {
                        matrix[row][c] = 0;
                    }

                    for (int r = 0; r < rows; r++) {
                        matrix[r][column] = 0;
                    }
                }
            }
        }
    }

    // ------------------------------------------------------------
    // 39. ROTATE IMAGE 90 DEGREES CLOCKWISE
    // Brute Force:
    // Use another matrix and copy values back.
    // Space: O(n^2)
    // ------------------------------------------------------------
    public static void rotateImage(int[][] matrix) {

        int n = matrix.length;

        int[][] rotated = new int[n][n];

        for (int row = 0; row < n; row++) {

            for (int column = 0; column < n; column++) {

                rotated[column][n - 1 - row] = matrix[row][column];
            }
        }

        for (int row = 0; row < n; row++) {

            for (int column = 0; column < n; column++) {

                matrix[row][column] = rotated[row][column];
            }
        }
    }

    // ------------------------------------------------------------
    // 40. WORD SEARCH
    // Exhaustive backtracking:
    // Try starting from every board cell.
    // From each cell try up, down, left and right.
    // ------------------------------------------------------------
    public static boolean wordSearch(char[][] board, String word) {

        if (word.length() == 0) {
            return true;
        }

        if (board.length == 0) {
            return false;
        }

        for (int row = 0; row < board.length; row++) {

            for (int column = 0; column < board[0].length; column++) {

                boolean[][] visited =
                        new boolean[board.length][board[0].length];

                if (searchWordBruteForce(
                        board,
                        word,
                        row,
                        column,
                        0,
                        visited)) {

                    return true;
                }
            }
        }

        return false;
    }

    private static boolean searchWordBruteForce(
            char[][] board,
            String word,
            int row,
            int column,
            int index,
            boolean[][] visited) {

        if (index == word.length()) {
            return true;
        }

        if (row < 0
                || row >= board.length
                || column < 0
                || column >= board[0].length) {

            return false;
        }

        if (visited[row][column]) {
            return false;
        }

        if (board[row][column] != word.charAt(index)) {
            return false;
        }

        visited[row][column] = true;

        boolean down = searchWordBruteForce(
                board,
                word,
                row + 1,
                column,
                index + 1,
                visited);

        boolean up = searchWordBruteForce(
                board,
                word,
                row - 1,
                column,
                index + 1,
                visited);

        boolean right = searchWordBruteForce(
                board,
                word,
                row,
                column + 1,
                index + 1,
                visited);

        boolean left = searchWordBruteForce(
                board,
                word,
                row,
                column - 1,
                index + 1,
                visited);

        visited[row][column] = false;

        return down || up || right || left;
    }

    // ------------------------------------------------------------
    // 41. QUEUE USING TWO STACKS
    // Simple implementation:
    // During poll/peek, move all items to second stack.
    // ------------------------------------------------------------
    public static final class QueueUsingStacks {

        private final Stack<Integer> stack1 = new Stack<Integer>();
        private final Stack<Integer> stack2 = new Stack<Integer>();

        public void offer(int value) {
            stack1.push(value);
        }

        public int poll() {

            if (stack1.isEmpty()) {
                throw new RuntimeException("Queue is empty");
            }

            while (!stack1.isEmpty()) {
                stack2.push(stack1.pop());
            }

            int value = stack2.pop();

            while (!stack2.isEmpty()) {
                stack1.push(stack2.pop());
            }

            return value;
        }

        public int peek() {

            if (stack1.isEmpty()) {
                throw new RuntimeException("Queue is empty");
            }

            while (!stack1.isEmpty()) {
                stack2.push(stack1.pop());
            }

            int value = stack2.peek();

            while (!stack2.isEmpty()) {
                stack1.push(stack2.pop());
            }

            return value;
        }

        public boolean isEmpty() {
            return stack1.isEmpty();
        }
    }

    // ------------------------------------------------------------
    // 42. STACK USING ONE QUEUE
    // Simple implementation:
    // push adds normally.
    // pop rotates queue until last inserted item reaches front.
    // ------------------------------------------------------------
    public static final class StackUsingQueues {

        private final Queue<Integer> queue = new LinkedList<Integer>();

        public void push(int value) {
            queue.offer(value);
        }

        public int pop() {

            if (queue.isEmpty()) {
                throw new RuntimeException("Stack is empty");
            }

            int size = queue.size();

            for (int i = 0; i < size - 1; i++) {
                queue.offer(queue.poll());
            }

            return queue.poll();
        }

        public int top() {

            if (queue.isEmpty()) {
                throw new RuntimeException("Stack is empty");
            }

            int size = queue.size();

            for (int i = 0; i < size - 1; i++) {
                queue.offer(queue.poll());
            }

            int value = queue.peek();

            queue.offer(queue.poll());

            return value;
        }

        public boolean isEmpty() {
            return queue.isEmpty();
        }
    }

    // ------------------------------------------------------------
    // LINKED LIST NODE
    // ------------------------------------------------------------
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

    // ------------------------------------------------------------
    // 43. LINKED LIST CYCLE
    // Brute Force:
    // Save every visited node in a list.
    // Before adding current node, scan list to see if it was seen.
    // Time: O(n^2)
    // Space: O(n)
    // ------------------------------------------------------------
    public static boolean hasCycle(ListNode head) {

        ArrayList<ListNode> visited =
                new ArrayList<ListNode>();

        ListNode current = head;

        while (current != null) {

            for (int i = 0; i < visited.size(); i++) {

                if (visited.get(i) == current) {
                    return true;
                }
            }

            visited.add(current);

            current = current.next;
        }

        return false;
    }

    // ------------------------------------------------------------
    // 44. MIDDLE OF LINKED LIST
    // Brute Force:
    // First count nodes.
    // Then move length/2 steps.
    // ------------------------------------------------------------
    public static ListNode middleNode(ListNode head) {

        int length = 0;

        ListNode current = head;

        while (current != null) {
            length++;
            current = current.next;
        }

        int middleIndex = length / 2;

        current = head;

        for (int i = 0; i < middleIndex; i++) {
            current = current.next;
        }

        return current;
    }

    // ------------------------------------------------------------
    // 45. REVERSE LINKED LIST
    // Straightforward pointer reversal.
    // This is already the simplest standard method.
    // ------------------------------------------------------------
    public static ListNode reverseList(ListNode head) {

        ListNode previous = null;
        ListNode current = head;

        while (current != null) {

            ListNode nextNode = current.next;

            current.next = previous;

            previous = current;
            current = nextNode;
        }

        return previous;
    }

    // ------------------------------------------------------------
    // 46. MERGE TWO SORTED LISTS
    // Beginner brute-force style:
    // 1. Copy all values into ArrayList.
    // 2. Bubble sort them.
    // 3. Build a new linked list.
    // ------------------------------------------------------------
    public static ListNode mergeTwoLists(ListNode a, ListNode b) {

        ArrayList<Integer> values =
                new ArrayList<Integer>();

        ListNode current = a;

        while (current != null) {
            values.add(current.value);
            current = current.next;
        }

        current = b;

        while (current != null) {
            values.add(current.value);
            current = current.next;
        }

        for (int i = 0; i < values.size() - 1; i++) {

            for (int j = 0; j < values.size() - 1 - i; j++) {

                if (values.get(j) > values.get(j + 1)) {

                    int temp = values.get(j);

                    values.set(j, values.get(j + 1));
                    values.set(j + 1, temp);
                }
            }
        }

        ListNode newHead = null;
        ListNode tail = null;

        for (int i = 0; i < values.size(); i++) {

            ListNode newNode =
                    new ListNode(values.get(i));

            if (newHead == null) {

                newHead = newNode;
                tail = newNode;

            } else {

                tail.next = newNode;
                tail = newNode;
            }
        }

        return newHead;
    }

    // ------------------------------------------------------------
    // 47. INTERSECTION OF TWO LINKED LISTS
    // Brute Force:
    // For each node in first list, compare with every node in second.
    // Time: O(m*n)
    // Space: O(1)
    // ------------------------------------------------------------
    public static ListNode getIntersectionNode(ListNode a, ListNode b) {

        ListNode first = a;

        while (first != null) {

            ListNode second = b;

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

    // ------------------------------------------------------------
    // 48. REMOVE NTH NODE FROM END
    // Brute Force:
    // First count length.
    // Convert "nth from end" to index from beginning.
    // ------------------------------------------------------------
    public static ListNode removeNthFromEnd(ListNode head, int n) {

        int length = 0;

        ListNode current = head;

        while (current != null) {
            length++;
            current = current.next;
        }

        int indexFromStart = length - n;

        if (indexFromStart < 0 || indexFromStart >= length) {
            return head;
        }

        if (indexFromStart == 0) {

            if (head == null) {
                return null;
            }

            return head.next;
        }

        current = head;

        for (int i = 0; i < indexFromStart - 1; i++) {
            current = current.next;
        }

        if (current != null && current.next != null) {
            current.next = current.next.next;
        }

        return head;
    }

    // ------------------------------------------------------------
    // 49. PALINDROME LINKED LIST
    // Beginner brute-force style:
    // Copy values to ArrayList and compare from both ends.
    // ------------------------------------------------------------
    public static boolean isPalindrome(ListNode head) {

        ArrayList<Integer> values =
                new ArrayList<Integer>();

        ListNode current = head;

        while (current != null) {
            values.add(current.value);
            current = current.next;
        }

        int left = 0;
        int right = values.size() - 1;

        while (left < right) {

            if (!values.get(left).equals(values.get(right))) {
                return false;
            }

            left++;
            right--;
        }

        return true;
    }

    // ------------------------------------------------------------
    // 50a. BUBBLE SORT
    // ------------------------------------------------------------
    public static void bubbleSort(int[] a) {

        for (int i = 0; i < a.length - 1; i++) {

            for (int j = 0; j < a.length - 1 - i; j++) {

                if (a[j] > a[j + 1]) {

                    int temp = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = temp;
                }
            }
        }
    }

    // ------------------------------------------------------------
    // 50b. SELECTION SORT
    // ------------------------------------------------------------
    public static void selectionSort(int[] a) {

        for (int i = 0; i < a.length - 1; i++) {

            int minimumIndex = i;

            for (int j = i + 1; j < a.length; j++) {

                if (a[j] < a[minimumIndex]) {
                    minimumIndex = j;
                }
            }

            int temp = a[i];
            a[i] = a[minimumIndex];
            a[minimumIndex] = temp;
        }
    }

    // ------------------------------------------------------------
    // 50c. INSERTION SORT
    // ------------------------------------------------------------
    public static void insertionSort(int[] a) {

        for (int i = 1; i < a.length; i++) {

            int currentValue = a[i];
            int j = i - 1;

            while (j >= 0 && a[j] > currentValue) {

                a[j + 1] = a[j];
                j--;
            }

            a[j + 1] = currentValue;
        }
    }

    // ------------------------------------------------------------
    // HELPER: PRINT ARRAY
    // ------------------------------------------------------------
    private static void printArray(int[] a) {

        System.out.print("[");

        for (int i = 0; i < a.length; i++) {

            System.out.print(a[i]);

            if (i != a.length - 1) {
                System.out.print(", ");
            }
        }

        System.out.println("]");
    }

    // ------------------------------------------------------------
    // HELPER: PRINT FIRST 'length' VALUES OF ARRAY
    // ------------------------------------------------------------
    private static void printArray(int[] a, int length) {

        System.out.print("[");

        for (int i = 0; i < length; i++) {

            System.out.print(a[i]);

            if (i != length - 1) {
                System.out.print(", ");
            }
        }

        System.out.println("]");
    }

    // ------------------------------------------------------------
    // HELPER: PRINT MATRIX
    // ------------------------------------------------------------
    private static void printMatrix(int[][] matrix) {

        for (int row = 0; row < matrix.length; row++) {

            for (int column = 0;
                    column < matrix[row].length;
                    column++) {

                System.out.print(matrix[row][column]);

                if (column != matrix[row].length - 1) {
                    System.out.print(" ");
                }
            }

            System.out.println();
        }
    }

    // ------------------------------------------------------------
    // HELPER: PRINT LINKED LIST
    // ------------------------------------------------------------
    private static void printLinkedList(ListNode head) {

        ListNode current = head;

        while (current != null) {

            System.out.print(current.value);

            if (current.next != null) {
                System.out.print(" -> ");
            }

            current = current.next;
        }

        System.out.println();
    }

    // ------------------------------------------------------------
    // MAIN METHOD
    // Contains simple examples for all problems.
    // ------------------------------------------------------------
    public static void main(String[] args) {

        System.out.println("1. Two Sum");
        int[] twoSumInput = { 2, 7, 11, 15 };
        int[] twoSumResult = twoSum(twoSumInput, 9);
        printArray(twoSumResult);

        System.out.println();

        System.out.println("2. Remove Duplicates");
        int[] duplicateArray = { 1, 1, 2, 2, 3 };
        int uniqueLength = removeDuplicates(duplicateArray);
        printArray(duplicateArray, uniqueLength);

        System.out.println();

        System.out.println("3. Remove Element");
        int[] removeArray = { 3, 2, 2, 3 };
        int newLength = removeElement(removeArray, 3);
        printArray(removeArray, newLength);

        System.out.println();

        System.out.println("4. Move Zeroes");
        int[] zeroArray = { 0, 1, 0, 3, 12 };
        moveZeroes(zeroArray);
        printArray(zeroArray);

        System.out.println();

        System.out.println("5. Sort Colors");
        int[] colors = { 2, 0, 2, 1, 1, 0 };
        sortColors(colors);
        printArray(colors);

        System.out.println();

        System.out.println("6. Best Time to Buy and Sell Stock");
        int[] prices = { 7, 1, 5, 3, 6, 4 };
        System.out.println(maxProfit(prices));

        System.out.println();

        System.out.println("7. Maximum Subarray");
        int[] maxSubArrayInput =
                { -2, 1, -3, 4, -1, 2, 1, -5, 4 };
        System.out.println(maxSubArray(maxSubArrayInput));

        System.out.println();

        System.out.println("8. Merge Sorted Array");
        int[] nums1 = { 1, 2, 3, 0, 0, 0 };
        int[] nums2 = { 2, 5, 6 };
        mergeSortedArray(nums1, 3, nums2, 3);
        printArray(nums1);

        System.out.println();

        System.out.println("9. Majority Element");
        int[] majorityInput = { 2, 2, 1, 1, 1, 2, 2 };
        System.out.println(majorityElement(majorityInput));

        System.out.println();

        System.out.println("10. Rotate Array");
        int[] rotateInput = { 1, 2, 3, 4, 5, 6, 7 };
        rotateArray(rotateInput, 3);
        printArray(rotateInput);

        System.out.println();

        System.out.println("11. Find Duplicate");
        int[] findDuplicateInput = { 1, 3, 4, 2, 2 };
        System.out.println(findDuplicate(findDuplicateInput));

        System.out.println();

        System.out.println("12. Missing Number");
        int[] missingInput = { 3, 0, 1 };
        System.out.println(missingNumber(missingInput));

        System.out.println();

        System.out.println("13. First Missing Positive");
        int[] firstMissingInput = { 3, 4, -1, 1 };
        System.out.println(firstMissingPositive(firstMissingInput));

        System.out.println();

        System.out.println("14. Intersection of Two Arrays");
        int[] intersectionA = { 1, 2, 2, 1 };
        int[] intersectionB = { 2, 2 };
        printArray(intersection(intersectionA, intersectionB));

        System.out.println();

        System.out.println("15. Valid Anagram");
        System.out.println(isAnagram("anagram", "nagaram"));

        System.out.println();

        System.out.println("16. Group Anagrams");
        String[] words =
                { "eat", "tea", "tan", "ate", "nat", "bat" };
        System.out.println(groupAnagrams(words));

        System.out.println();

        System.out.println("17. Valid Palindrome");
        System.out.println(
                isPalindrome("A man, a plan, a canal: Panama"));

        System.out.println();

        System.out.println("18. Reverse String");
        char[] reverseText =
                { 'h', 'e', 'l', 'l', 'o' };
        reverseString(reverseText);
        System.out.println(new String(reverseText));

        System.out.println();

        System.out.println("19. Reverse Words");
        System.out.println(reverseWords("the sky is blue"));

        System.out.println();

        System.out.println("20. Longest Common Prefix");
        String[] prefixWords =
                { "flower", "flow", "flight" };
        System.out.println(longestCommonPrefix(prefixWords));

        System.out.println();

        System.out.println("21. Valid Parentheses");
        System.out.println(validParentheses("{[()]}"));

        System.out.println();

        System.out.println("22. Min Stack");
        MinStack minStack = new MinStack();
        minStack.push(5);
        minStack.push(2);
        minStack.push(8);
        System.out.println("Minimum = " + minStack.getMin());
        minStack.pop();
        System.out.println("Top = " + minStack.top());

        System.out.println();

        System.out.println("23. Next Greater Element");
        int[] subset = { 4, 1, 2 };
        int[] all = { 1, 3, 4, 2 };
        printArray(nextGreaterElement(subset, all));

        System.out.println();

        System.out.println("24. strStr");
        System.out.println(strStr("hello", "ll"));

        System.out.println();

        System.out.println("25. Longest Substring Without Repeating");
        System.out.println(
                lengthOfLongestSubstring("abcabcbb"));

        System.out.println();

        System.out.println("26. Contains Duplicate");
        int[] containsDuplicateInput = { 1, 2, 3, 1 };
        System.out.println(
                containsDuplicate(containsDuplicateInput));

        System.out.println();

        System.out.println("27. Single Number");
        int[] singleNumberInput = { 4, 1, 2, 1, 2 };
        System.out.println(singleNumber(singleNumberInput));

        System.out.println();

        System.out.println("28. Plus One");
        int[] digits = { 9, 9, 9 };
        printArray(plusOne(digits));

        System.out.println();

        System.out.println("29. Add Binary");
        System.out.println(addBinary("1010", "1011"));

        System.out.println();

        System.out.println("30. Integer Square Root");
        System.out.println(sqrt(8));

        System.out.println();

        System.out.println("31. Search Insert Position");
        int[] searchInsertInput = { 1, 3, 5, 6 };
        System.out.println(
                searchInsert(searchInsertInput, 5));

        System.out.println();

        System.out.println("32. Find Minimum in Rotated Array");
        int[] rotatedArray = { 3, 4, 5, 1, 2 };
        System.out.println(findMin(rotatedArray));

        System.out.println();

        System.out.println("33. Find Peak Element");
        int[] peakArray = { 1, 2, 3, 1 };
        System.out.println(findPeakElement(peakArray));

        System.out.println();

        System.out.println("34. Count and Say");
        System.out.println(countAndSay(5));

        System.out.println();

        System.out.println("35. Pascal Triangle");
        System.out.println(pascalTriangle(5));

        System.out.println();

        System.out.println("36. Merge Intervals");
        int[][] intervals = {
                { 1, 3 },
                { 2, 6 },
                { 8, 10 },
                { 15, 18 }
        };
        int[][] merged = mergeIntervals(intervals);
        printMatrix(merged);

        System.out.println();

        System.out.println("37. Spiral Matrix");
        int[][] spiralMatrix = {
                { 1, 2, 3 },
                { 4, 5, 6 },
                { 7, 8, 9 }
        };
        System.out.println(spiralOrder(spiralMatrix));

        System.out.println();

        System.out.println("38. Set Matrix Zeroes");
        int[][] zeroMatrix = {
                { 1, 1, 1 },
                { 1, 0, 1 },
                { 1, 1, 1 }
        };
        setZeroes(zeroMatrix);
        printMatrix(zeroMatrix);

        System.out.println();

        System.out.println("39. Rotate Image");
        int[][] image = {
                { 1, 2, 3 },
                { 4, 5, 6 },
                { 7, 8, 9 }
        };
        rotateImage(image);
        printMatrix(image);

        System.out.println();

        System.out.println("40. Word Search");
        char[][] board = {
                { 'A', 'B', 'C', 'E' },
                { 'S', 'F', 'C', 'S' },
                { 'A', 'D', 'E', 'E' }
        };
        System.out.println(wordSearch(board, "ABCCED"));

        System.out.println();

        System.out.println("41. Queue Using Two Stacks");
        QueueUsingStacks queue =
                new QueueUsingStacks();
        queue.offer(10);
        queue.offer(20);
        queue.offer(30);
        System.out.println("Peek = " + queue.peek());
        System.out.println("Poll = " + queue.poll());
        System.out.println("Poll = " + queue.poll());

        System.out.println();

        System.out.println("42. Stack Using One Queue");
        StackUsingQueues stack =
                new StackUsingQueues();
        stack.push(10);
        stack.push(20);
        stack.push(30);
        System.out.println("Top = " + stack.top());
        System.out.println("Pop = " + stack.pop());
        System.out.println("Pop = " + stack.pop());

        System.out.println();

        System.out.println("43. Linked List Cycle");
        ListNode cycle1 = new ListNode(3);
        ListNode cycle2 = new ListNode(2);
        ListNode cycle3 = new ListNode(0);
        ListNode cycle4 = new ListNode(-4);

        cycle1.next = cycle2;
        cycle2.next = cycle3;
        cycle3.next = cycle4;
        cycle4.next = cycle2;

        System.out.println(hasCycle(cycle1));

        System.out.println();

        System.out.println("44. Middle of Linked List");
        ListNode middleList =
                new ListNode(
                        1,
                        new ListNode(
                                2,
                                new ListNode(
                                        3,
                                        new ListNode(
                                                4,
                                                new ListNode(5)))));
        System.out.println(middleNode(middleList).value);

        System.out.println();

        System.out.println("45. Reverse Linked List");
        ListNode reverseListInput =
                new ListNode(
                        1,
                        new ListNode(
                                2,
                                new ListNode(
                                        3,
                                        new ListNode(
                                                4,
                                                new ListNode(5)))));
        ListNode reversedList =
                reverseList(reverseListInput);
        printLinkedList(reversedList);

        System.out.println();

        System.out.println("46. Merge Two Sorted Lists");
        ListNode listA =
                new ListNode(
                        1,
                        new ListNode(
                                2,
                                new ListNode(4)));

        ListNode listB =
                new ListNode(
                        1,
                        new ListNode(
                                3,
                                new ListNode(4)));

        ListNode mergedList =
                mergeTwoLists(listA, listB);

        printLinkedList(mergedList);

        System.out.println();

        System.out.println("47. Intersection of Two Linked Lists");

        ListNode common =
                new ListNode(
                        8,
                        new ListNode(
                                4,
                                new ListNode(5)));

        ListNode firstList =
                new ListNode(
                        4,
                        new ListNode(
                                1,
                                common));

        ListNode secondList =
                new ListNode(
                        5,
                        new ListNode(
                                6,
                                new ListNode(
                                        1,
                                        common)));

        ListNode intersectionNode =
                getIntersectionNode(
                        firstList,
                        secondList);

        if (intersectionNode != null) {
            System.out.println(
                    "Intersection = "
                            + intersectionNode.value);
        } else {
            System.out.println("No intersection");
        }

        System.out.println();

        System.out.println("48. Remove Nth Node From End");
        ListNode removeNthInput =
                new ListNode(
                        1,
                        new ListNode(
                                2,
                                new ListNode(
                                        3,
                                        new ListNode(
                                                4,
                                                new ListNode(5)))));

        ListNode afterRemoval =
                removeNthFromEnd(removeNthInput, 2);

        printLinkedList(afterRemoval);

        System.out.println();

        System.out.println("49. Palindrome Linked List");
        ListNode palindromeList =
                new ListNode(
                        1,
                        new ListNode(
                                2,
                                new ListNode(
                                        2,
                                        new ListNode(1))));

        System.out.println(
                isPalindrome(palindromeList));

        System.out.println();

        System.out.println("50a. Bubble Sort");
        int[] bubbleInput = { 5, 1, 4, 2, 8 };
        bubbleSort(bubbleInput);
        printArray(bubbleInput);

        System.out.println();

        System.out.println("50b. Selection Sort");
        int[] selectionInput = { 64, 25, 12, 22, 11 };
        selectionSort(selectionInput);
        printArray(selectionInput);

        System.out.println();

        System.out.println("50c. Insertion Sort");
        int[] insertionInput = { 5, 1, 4, 2, 8 };
        insertionSort(insertionInput);
        printArray(insertionInput);
    }
}
