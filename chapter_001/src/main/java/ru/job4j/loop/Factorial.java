package ru.job4j.loop;

/**
 * Class to calculate factorial.
 *
 * @author Aleksei Sapozhnikov
 * @version $Id$
 * @since 09.01.2018
 */
public class Factorial {

    /**
     * Finds factorial (n!) for given positive value.
     * 0! == 1
     *
     * @param n Given value.
     * @return Factorial.
     */
    public int calc(int n) {
        int result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }
}
