package ru.job4j.arrays;

/**
 * Fill array with squares of values.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 09.01.2018
 */
public class Square {

    /**
     * Creates an array filled with squares of values from 1 to given value.
     *
     * @param bound Last value of the range (inclusive).
     * @return Array with squares if bound is more than 0, empty array if value is less or equal to 0.
     */
    public int[] calculate(int bound) {
        if (bound < 0) {
            bound = 0;
        }
        int[] result = new int[bound];
        for (int i = 1; i < (result.length + 1); i++) {
            result[i - 1] = i * i;
        }
        return result;
    }
}

