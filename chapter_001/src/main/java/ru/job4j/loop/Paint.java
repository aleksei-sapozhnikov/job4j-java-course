package ru.job4j.loop;

import java.util.function.BiPredicate;

/**
 * Make strings in form of geometrical figures.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 11.01.2018
 */
public class Paint {

    /**
     * Returns Strings painted using ^ symbols, depending on given predicate.
     *
     * @param height    Height of the drawing (lines).
     * @param width     Width of the drawing (columns).
     * @param predicate Predicate.
     * @return String with the drawing.
     */
    private String loopBy(int height, int width, BiPredicate<Integer, Integer> predicate) {
        StringBuilder screen = new StringBuilder();
        for (int row = 0; row != height; row++) {
            for (int column = 0; column != width; column++) {
                if (predicate.test(row, column)) {
                    screen.append("^");
                } else {
                    screen.append(" ");
                }
            }
            screen.append(System.lineSeparator());
        }
        return screen.toString();
    }

    /**
     * Paints right triangle.
     *
     * @param height Height (number of lines).
     * @return String with right triangle.
     */
    public String rightTrl(int height) {
        return this.loopBy(
                height,
                height,
                (row, column) -> row >= column
        );
    }

    /**
     * Paints left triangle.
     *
     * @param height Height (number of lines).
     * @return String with right triangle.
     */
    public String leftTrl(int height) {
        return this.loopBy(
                height,
                height,
                (row, column) -> row >= height - column - 1
        );
    }

    /**
     * Draws pyramid using '^' symbols.
     *
     * @param height Pyramid height.
     * @return String containing pyramid.
     */
    public String pyramid(int height) {
        return this.loopBy(
                height,
                2 * height - 1,
                (row, column) -> row >= height - column - 1 && row + height - 1 >= column
        );
    }

}
