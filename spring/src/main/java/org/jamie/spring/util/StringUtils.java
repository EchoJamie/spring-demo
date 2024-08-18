package org.jamie.spring.util;

/**
 * TODO
 *
 * @author jamie
 * @version 1.0.0
 * @since 2024/2/25 05:36
 */
public class StringUtils {

    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }

    public static boolean isNotEmpty(String s) {
        return !isEmpty(s);
    }

    public static String firstUpper(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    public static String firstLower(String s) {
        return s.substring(0, 1).toLowerCase() + s.substring(1);
    }
}
