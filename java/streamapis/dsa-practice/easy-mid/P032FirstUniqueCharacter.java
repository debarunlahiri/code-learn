import java.util.*;

/**
 * P032. First Unique Character. Given the input described by the method
 * signature, implement
 * the required operation efficiently and return the expected result. Handle
 * normal edge cases such as
 * empty collections, duplicate values, boundary indexes, and null child
 * pointers when the data
 * structure allows them. Prefer the standard optimal approach used in coding
 * rounds, and keep the
 * implementation readable for revision.
 */
public final class P032FirstUniqueCharacter {

    private P032FirstUniqueCharacter() {
    }

    public int firstUniqChar(String s) {
        int[] count = new int[26];
        for (char c : s.toCharArray())
    count[c - 'a']++;
        for (int i = 0; i < s.length(); i++)
    if (count[s.charAt(i) - 'a'] == 1)
        return i;
        return -1;
    }

}
