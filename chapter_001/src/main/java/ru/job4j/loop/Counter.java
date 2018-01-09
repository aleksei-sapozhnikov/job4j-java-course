package ru.job4j.loop;

/**
 * Counts different things using loops.
 *
 * @author Aleksei Sapozhnikov
 * @version $Id$
 * @since 09.01.2018
 */
public class Counter {

    /**
     * Finds sum of _even_ values in given range.
     *
     * @param start  Lower boundary of the range (inclusive).
     * @param finish Higher boundary of the range (inclusive).
     * @return Sum of the even values in the range.
     */
    public int add(int start, int finish) {
        // If start is not even value, we take the next one.
        if (start % 2 != 0) {
            start++;
        }
        // Then, with step == 2 we take even numbers only.
        int result = 0;
        for (int i = start; i <= finish; i = i + 2) {
            result += i;
        }
        return result;
    }

}
