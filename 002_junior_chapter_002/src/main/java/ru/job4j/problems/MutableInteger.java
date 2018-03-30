package ru.job4j.problems;

/**
 * Mutable object with value of type double.
 * To show problems of mutable objects during Race condition.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 30.03.2018
 */

class MutableInteger {
    /**
     * Value stored inside the object.
     */
    private int value;

    /**
     * Constructs a new object.
     *
     * @param value initial value.
     */
    MutableInteger(int value) {
        this.value = value;
    }

    /**
     * Returns object's value.
     *
     * @return value stored in the object.
     */
    int getValue() {
        return this.value;
    }

    /**
     * Sets new value.
     *
     * @param value new value.
     */
    void setValue(int value) {
        this.value = value;
    }

}
