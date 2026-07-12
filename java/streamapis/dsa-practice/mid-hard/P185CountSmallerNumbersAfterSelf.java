import java.util.*;

/**
 * P185. Count Smaller Numbers After Self. This is a mid-to-hard Java DSA coding
 * problem commonly seen
 * in service based company technical rounds. Read the full input from the
 * method parameters, choose
 * the expected optimal data structure or algorithm, handle edge cases such as
 * empty inputs and
 * duplicates, and return the exact platform-style output.
 */
public final class P185CountSmallerNumbersAfterSelf {

    private P185CountSmallerNumbersAfterSelf() {
    }

    public List<Integer> countSmaller(int[] nums) {
        Integer[] ans = new Integer[nums.length];
        List<Integer> sorted = new ArrayList<>();
        for (int i = nums.length - 1; i >= 0; i--) {
            int pos = Collections.binarySearch(sorted, nums[i]);
            if (pos < 0)
                pos = -(pos + 1);
            while (pos > 0 && sorted.get(pos - 1).equals(nums[i]))
                pos--;
            ans[i] = pos;
            sorted.add(pos, nums[i]);
        }
        return Arrays.asList(ans);
    }
}
