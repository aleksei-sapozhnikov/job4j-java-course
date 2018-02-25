package ru.job4j.test;

/**
 * Join arrays.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 14.01.2018
 */
public class ArrayJoin {

    /**
     * Joins two ascending sorted arrays.
     *
     * @param first  First array, sorted in ascending order.
     * @param second Second array, sorted in ascending order.
     * @return new array, ascending sorted.
     */
    public int[] joinSortedAscending(int[] first, int[] second) {
        int[] result = new int[first.length + second.length];
        int iFirst = 0;
        int iSecond = 0;
        int iResult = 0;
        while (iFirst < first.length && iSecond < second.length) {
            result[iResult++] = first[iFirst] < second[iSecond] ? first[iFirst++] : second[iSecond++];
        }
        boolean copyFirst = iFirst < first.length;
        System.arraycopy(
                copyFirst ? first : second,
                copyFirst ? iFirst : iSecond,
                result,
                iResult,
                copyFirst ? first.length - iFirst : second.length - iSecond
        );
        return result;
    }
}
