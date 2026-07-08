import java.util.*;

/**
 * P031. Valid Anagram.  Given the input described by the method
 * signature, implement the
 * required operation efficiently and return the expected result. Handle normal
 * edge cases such as
 * empty collections, duplicate values, boundary indexes, and null child
 * pointers when the data
 * structure allows them. Prefer the standard optimal approach used in coding
 * rounds, and keep the
 * implementation readable for revision.
 */
public final class P031ValidAnagram {

    private P031ValidAnagram() {
    }

    public boolean isAnagram(String s, String t) {
        if (s.length() != t.length())
    return false;
        int[] count = new int[26];
        for (int i = 0; i < s.length(); i++) {
    count[s.charAt(i) - 'a']++;
    count[t.charAt(i) - 'a']--;
        }
        for (int c : count)
    if (c != 0)
        return false;
        return true;
    }

}
