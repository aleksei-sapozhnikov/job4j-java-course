package ru.job4j.array;

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
        int lastIndexOfCycle;
        // 2 cases: even and odd number of elements
        if (array.length % 2 == 0) {
            lastIndexOfCycle = (array.length / 2) - 1;
        } else {
            // we don't have to swap central element
            lastIndexOfCycle = ((array.length - 1) / 2) - 1;
        }
        // swap
        for (int i = 0; i <= lastIndexOfCycle; i++) {
            int temp = array[i];
            array[i] = array[(array.length - 1) - i];
            array[array.length - 1 - i] = temp;
        }
        return array;
    }

}
