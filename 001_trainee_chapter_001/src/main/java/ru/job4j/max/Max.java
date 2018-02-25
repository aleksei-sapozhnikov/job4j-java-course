package ru.job4j.max;

/**
 * To find maximum value.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 07.01.2018
 */
public class Max {

    /**
     * Returns max of two given values.
     *
     * @param first  First value.
     * @param second Second value.
     * @return Max of two values.
     */
    public int max(int first, int second) {
        return (first > second) ? first : second;
    }

    /**
     * Returns max of three given values.
     *
     * @param first  First value.
     * @param second Second value.
     * @param third  Third value.
     * @return Max of three values.
     */
    public int max(int first, int second, int third) {
        int result = this.max(first, second);
        result = this.max(result, third);
        return result;
    }

}
