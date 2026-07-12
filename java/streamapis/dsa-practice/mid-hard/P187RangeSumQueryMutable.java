import java.util.*;

/**
 * P187. Range Sum Query Mutable. This is a mid-to-hard Java DSA coding problem
 * commonly seen in
 * service based company technical rounds. Read the full input from the method
 * parameters, choose the
 * expected optimal data structure or algorithm, handle edge cases such as empty
 * inputs and duplicates,
 * and return the exact platform-style output.
 */
public final class P187RangeSumQueryMutable {

    private P187RangeSumQueryMutable() {
    }

    private int[] bit;
    private int[] nums;

    public void init(int[] nums) {
        this.nums = nums.clone();
        bit = new int[nums.length + 1];
        for (int i = 0; i < nums.length; i++)
            add(i + 1, nums[i]);
    }

    public void update(int index, int val) {
        int diff = val - nums[index];
        nums[index] = val;
        add(index + 1, diff);
    }

    public int sumRange(int left, int right) {
        return sum(right + 1) - sum(left);
    }

    private void add(int i, int delta) {
        for (; i < bit.length; i += i & -i)
            bit[i] += delta;
    }

    private int sum(int i) {
        int s = 0;
        for (; i > 0; i -= i & -i)
            s += bit[i];
        return s;
    }
}
