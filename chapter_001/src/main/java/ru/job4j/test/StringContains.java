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
            int subIndex = 0;
            for (char originChar : originArray) {
                if (result) {
                    break;
                }
                if (originChar == subArray[subIndex]) {
                    subIndex++;
                } else {
                    subIndex = 0;
                }
                if (subIndex == subArray.length) {
                    result = true;
                }
            }
        }
        return result;
    }

}