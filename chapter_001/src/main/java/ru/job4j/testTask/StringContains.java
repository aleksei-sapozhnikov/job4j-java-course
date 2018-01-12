package ru.job4j.testTask;

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
        char[] originArr = origin.toCharArray();
        char[] subArr = sub.toCharArray();
        boolean result = false;
        for (int i = 0; i < originArr.length; i++) {
            int j = 0;
            while (originArr[i] == subArr[j]) {
                if (j < subArr.length - 1) {
                    i++;
                    j++;
                } else {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

}