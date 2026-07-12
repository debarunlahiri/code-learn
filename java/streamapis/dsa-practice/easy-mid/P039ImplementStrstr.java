import java.util.*;

/**
 * P039. Implement Strstr. This is a easy-to-mid Java DSA coding problem
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
public final class P039ImplementStrstr {

    private P039ImplementStrstr() {
    }

    public int strStr(String haystack, String needle) {
        if (needle.isEmpty())
    return 0;
        for (int i = 0; i <= haystack.length() - needle.length(); i++)
    if (haystack.startsWith(needle, i))
        return i;
        return -1;
    }

}
