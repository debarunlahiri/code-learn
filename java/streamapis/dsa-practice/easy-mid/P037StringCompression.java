import java.util.*;

/**
 * P037. String Compression. Given the input described by the method signature,
 * implement
 * the required operation efficiently and return the expected result. Handle
 * normal edge cases such as
 * empty collections, duplicate values, boundary indexes, and null child
 * pointers when the data
 * structure allows them. Prefer the standard optimal approach used in coding
 * rounds, and keep the
 * implementation readable for revision.
 */
public final class P037StringCompression {

    private P037StringCompression() {
    }

    public int compress(char[] chars) {
        int write = 0, read = 0;
        while (read < chars.length) {
    char c = chars[read];
    int start = read;
    while (read < chars.length && chars[read] == c)
        read++;
    chars[write++] = c;
    if (read - start > 1)
        for (char d : String.valueOf(read - start).toCharArray())
            chars[write++] = d;
        }
        return write;
    }

}
