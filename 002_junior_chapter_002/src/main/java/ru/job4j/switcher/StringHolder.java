package ru.job4j.switcher;

/**
 * Holds some string value and appends integer values as strings to it.
 * Value held inside can returned as string using toString() method.
 * <p>
 * For example:
 * append(123) -> holder.toString() equals "123".
 * append(456) -> holder.toString() equals "123456".
 * etc...
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 17.05.2018
 */
public class StringHolder {
    /**
     * String holding inside.
     */
    private StringBuilder holding = new StringBuilder();

    /**
     * Appends positive integer value as a string to current holding string.
     * For example, integer value 456 appends as "456"
     *
     * @param value integer value to append to string.
     */
    public void append(int value) {
        this.holding.append(value);
    }

    /**
     * Returns string which is currently held inside.
     *
     * @return string held inside.
     */
    @Override
    public String toString() {
        return this.holding.toString();
    }

}
