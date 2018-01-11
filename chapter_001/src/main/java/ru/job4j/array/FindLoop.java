package ru.job4j.array;

/**
 * Simple array search element-by-element.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 10.01.2018
 */
public class FindLoop {

    /**
     * Finds index of given value in array.
     *
     * @param data    Array containing given value.
     * @param element Given value.
     * @return Index of element or -1 if element not found.
     */
    public int indexOf(int[] data, int element) {
        int result = -1;
        // Searching
        for (int i = 0; i < data.length; i++) {
            if (data[i] == element) {
                result = i;
                break;
            }
        }
        return result;
    }

}
