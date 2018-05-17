package ru.job4j.switcher;

public class StringHolder {
    /**
     * String holding inside.
     */
    private StringBuilder holding = new StringBuilder();

    /**
     * Appends positive integer value as a string to current holding string.
     * e.g:
     * append(123) -> holder = "123";
     * append(456) -> holder = "123456";
     * etc...
     *
     * @param value integer value to append to string.
     */
    public void append(int value) {
        this.holding.append(value);
    }

    /**
     * Returns string which is currently hold inside.
     *
     * @return string hold inside.
     */
    @Override
    public String toString() {
        return this.holding.toString();
    }

}
