import java.util.*;

/**
 * P038. Reverse Words In String. Given the input described by the method
 * signature, implement
 * the required operation efficiently and return the expected result. Handle
 * normal edge cases such as
 * empty collections, duplicate values, boundary indexes, and null child
 * pointers when the data
 * structure allows them. Prefer the standard optimal approach used in coding
 * rounds, and keep the
 * implementation readable for revision.
 */
public final class P038ReverseWordsInString {

    private P038ReverseWordsInString() {
    }

    public String reverseWords(String s) {
        String[] parts = s.trim().split("\s+");
        Collections.reverse(Arrays.asList(parts));
        return String.join(" ", parts);
    }

}
