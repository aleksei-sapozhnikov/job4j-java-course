package ru.job4j.test;

import java.util.Arrays;

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
     * @param firstArray  First array, sorted in ascending order.
     * @param secondArray Second array, sorted in ascending order.
     * @return new array, ascending sorted.
     */
    public int[] joinSortedAscending(int[] firstArray, int[] secondArray) {
        int[] resultArray = new int[firstArray.length + secondArray.length];
        int firstIndex = 0;
        int secondIndex = 0;
        int resultIndex = 0;
        while (firstIndex < firstArray.length && secondIndex < secondArray.length) {
            resultArray[resultIndex++] = firstArray[firstIndex] < secondArray[secondIndex] ? firstArray[firstIndex++] : secondArray[secondIndex++];
        }
        boolean copyFirst = firstIndex < firstArray.length;
        System.arraycopy(
                copyFirst ? firstArray : secondArray,
                copyFirst ? firstIndex : secondIndex,
                resultArray,
                resultIndex,
                copyFirst ? firstArray.length - firstIndex : secondArray.length - secondIndex
        );
        return resultArray;
    }
}
