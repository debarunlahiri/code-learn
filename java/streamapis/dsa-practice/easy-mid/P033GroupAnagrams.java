import java.util.*;

/**
 * P033. Group Anagrams.  Given the input described by the method
 * signature, implement the
 * required operation efficiently and return the expected result. Handle normal
 * edge cases such as
 * empty collections, duplicate values, boundary indexes, and null child
 * pointers when the data
 * structure allows them. Prefer the standard optimal approach used in coding
 * rounds, and keep the
 * implementation readable for revision.
 */
public final class P033GroupAnagrams {

    private P033GroupAnagrams() {
    }

    public List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();
        for (String s : strs) {
    char[] a = s.toCharArray();
    Arrays.sort(a);
    map.computeIfAbsent(new String(a), k -> new ArrayList<>()).add(s);
        }
        return new ArrayList<>(map.values());
    }

}
