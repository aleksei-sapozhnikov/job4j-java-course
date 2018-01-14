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
            if (firstArray[firstIndex] < secondArray[secondIndex]) {
                resultArray[resultIndex] = firstArray[firstIndex];
                firstIndex++;
                resultIndex++;
            } else {
                resultArray[resultIndex] = secondArray[secondIndex];
                secondIndex++;
                resultIndex++;
            }
        }
        for (; firstIndex < firstArray.length; firstIndex++) {
            resultArray[resultIndex] = firstArray[firstIndex];
            resultIndex++;
        }
        for (; secondIndex < secondArray.length; secondIndex++) {
            resultArray[resultIndex] = secondArray[secondIndex];
            resultIndex++;
        }
        return resultArray;
    }
}
