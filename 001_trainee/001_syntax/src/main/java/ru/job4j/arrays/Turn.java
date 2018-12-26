package ru.job4j.arrays;

/**
 * Turn array backwards.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 10.01.2018
 */
public class Turn {

    /**
     * Turns array backwards.
     *
     * @param array Given array.
     * @return The same array object with elements standing in backwards order.
     */
    public int[] back(int[] array) {
        for (int i = 0; i <= array.length / 2 - 1; i++) {
            int temp = array[i];
            array[i] = array[(array.length - 1) - i];
            array[array.length - 1 - i] = temp;
        }
        return array;
    }

}
