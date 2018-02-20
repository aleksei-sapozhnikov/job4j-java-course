package ru.job4j.loops;

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
        int result = 0;
        for (int i = start; i <= finish; i++) {
            if (i % 2 == 0) {
                result += i;
            }
        }
        return result;
    }

}
