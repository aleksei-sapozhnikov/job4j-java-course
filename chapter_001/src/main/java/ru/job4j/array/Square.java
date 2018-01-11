package ru.job4j.array;

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
     * @return Array with squares if bound > 0, empty array if value <= 0.
     */
    public int[] calculate(int bound) {
        // array size cannot be < 0
        if (bound < 0) {
            bound = 0;
        }
        int[] result = new int[bound];
        // Fill array with squares of values.
        // Value index in array == value - 1 (because starting with 0)
        for (int i = 1; i < (result.length + 1); i++) {
            result[i - 1] = i * i;
        }
        return result;
    }
}

