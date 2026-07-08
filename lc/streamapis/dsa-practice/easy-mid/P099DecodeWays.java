import java.util.*;

/**
 * P099. Decode Ways. This is a easy-to-mid Java DSA coding problem commonly
 * practiced for service
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
public final class P099DecodeWays {

    private P099DecodeWays() {
    }

    public int numDecodings(String s) {
        if (s.isEmpty() || s.charAt(0) == '0')
    return 0;
        int a = 1, b = 1;
        for (int i = 1; i < s.length(); i++) {
    int cur = s.charAt(i) == '0' ? 0 : b;
    int two = Integer.parseInt(s.substring(i - 1, i + 1));
    if (two >= 10 && two <= 26)
        cur += a;
    a = b;
    b = cur;
        }
        return b;
    }

}
