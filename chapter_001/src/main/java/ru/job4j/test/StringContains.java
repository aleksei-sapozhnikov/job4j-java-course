package ru.job4j.test;

/**
 * Check if String contains another string.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 12.01.2018
 */
public class StringContains {

    /**
     * Checks if out string contains another string.
     *
     * @param origin Original string.
     * @param sub    String to check if is in original.
     * @return true if contains, false if not.
     */
    public boolean contains(String origin, String sub) {
        boolean result = false;
        if (sub.length() <= origin.length()) {
            char[] originArray = origin.toCharArray();
            char[] subArray = sub.toCharArray();
            ready:
            for (int originIndex = 0; originIndex < originArray.length; originIndex++) {
                if (result) {
                    break;
                }
                if (originIndex + subArray.length - 1 > originArray.length) {
                    break;
                }
                int subIndex = 0;
                while (originArray[originIndex + subIndex] == subArray[subIndex]) {
                    if (subIndex == subArray.length - 1) {
                        result = true;
                        continue ready;
                    } else {
                        subIndex++;
                    }
                }
            }
        }
        return result;
    }

}