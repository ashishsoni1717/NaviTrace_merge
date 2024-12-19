
package org.navitrace.helper;

public final class StringUtil {

    private StringUtil() {
    }

    public static boolean containsHex(String value) {
        for (char c : value.toCharArray()) {
            if (c >= 'a' && c <= 'f' || c >= 'A' && c <= 'F') {
                return true;
            }
        }
        return false;
    }

}
