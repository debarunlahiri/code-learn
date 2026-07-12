import java.util.*;

/**
 * P040. Roman To Integer. This is a easy-to-mid Java DSA coding problem
 * commonly practiced for service
 * based company coding rounds. Given the input described by the method
 * signature, implement the
 * required operation efficiently and return the expected result. Handle normal
 * edge cases such as
 * empty collections, duplicate values, boundary indexes, and null child
 * pointers when the data
 * structure allows them. Prefer the standard optimal approach used in coding
 * rounds, and keep the
 * implementation readable for revision.
 */
public final class P040RomanToInteger {

    private P040RomanToInteger() {
    }

    public int romanToInt(String s) {
        Map<Character, Integer> map = Map.of('I', 1, 'V', 5, 'X', 10, 'L', 50, 'C', 100, 'D', 500, 'M', 1000);
        int ans = 0;
        for (int i = 0; i < s.length(); i++) {
    int val = map.get(s.charAt(i));
    if (i + 1 < s.length() && val < map.get(s.charAt(i + 1)))
        ans -= val;
    else
        ans += val;
        }
        return ans;
    }

}
