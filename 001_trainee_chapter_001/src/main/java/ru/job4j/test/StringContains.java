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
            char[] orig = origin.toCharArray();
            char[] subs = sub.toCharArray();
            ready:
            for (int iOrig = 0; iOrig < orig.length; iOrig++) {
                if (result) {
                    break;
                }
                if (iOrig + subs.length - 1 > orig.length) {
                    break;
                }
                int iSubs = 0;
                while (orig[iOrig + iSubs] == subs[iSubs]) {
                    if (iSubs == subs.length - 1) {
                        result = true;
                        continue ready;
                    } else {
                        iSubs++;
                    }
                }
            }
        }
        return result;
    }

}