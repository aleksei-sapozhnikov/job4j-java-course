package ru.job4j.array;

import java.util.Arrays;

/**
 * Remove duplicate elements from array.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 12.01.2018
 */
public class ArrayDuplicate {

    /**
     * Removes duplicates from given array.
     *
     * @param array Given array.
     * @return New array without duplicate elements.
     */
    public String[] remove(String[] array) {
        int unique = array.length;
        for (int outCount = 0; outCount < unique; outCount++) {
            for (int inCount = outCount + 1; inCount < unique; inCount++) {
                if (array[outCount].equals(array[inCount])) {
                    array[inCount] = array[unique - 1];
                    unique--;
                    inCount--;
                }
            }
        }
        return Arrays.copyOf(array, unique);
    }
}
