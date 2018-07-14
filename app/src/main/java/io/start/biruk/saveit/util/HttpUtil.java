package io.start.biruk.saveit.util;

/**
 * Created by biruk on 14/07/18.
 */

public class HttpUtil {
    public static boolean isValid(String text) {
        if (text.isEmpty()) {
            return false;
        }
        return text.startsWith("http") || text.startsWith("https");
    }
}
