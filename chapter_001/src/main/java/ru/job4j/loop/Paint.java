package ru.job4j.loop;

/**
 * Make strings in form of geometrical figures.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 09.01.2018
 */
public class Paint {

    /**
     * Draws pyramid using '^' symbols.
     *
     * @param height Pyramid height.
     * @return String containing pyramid.
     */
    public String pyramid(int height) {
        StringBuilder screen = new StringBuilder();
        int weight = 2 * height - 1;
        for (int row = 0; row != height; row++) {
            for (int column = 0; column != weight; column++) {
                if (row >= height - column - 1 && row + height - 1 >= column) {
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
     * @param height Pyramid height (number of lines).
     * @return String with right triangle.
     */
    public String rightTrl(int height) {
        StringBuilder screen = new StringBuilder();
        // width is equal to height
        int width = height;
        // rows (lines)
        for (int row = 0; row != height; row++) {
            // columns in line (cells)
            for (int column = 0; column != width; column++) {
                // printing ^ symbol where needed
                if (row >= column) {
                    screen.append("^");
                } else {
                    screen.append(" ");
                }
            }
            // new line
            screen.append(System.lineSeparator());
        }
        // result
        return screen.toString();
    }

    /**
     * Paints right triangle.
     *
     * @param height Pyramid height (number of lines).
     * @return String with right triangle.
     */
    public String leftTrl(int height) {
        StringBuilder screen = new StringBuilder();
        // width is equal to height
        int width = height;
        // rows (lines)
        for (int row = 0; row != height; row++) {
            // columns in line (cells)
            for (int column = 0; column != width; column++) {
                // printing ^ symbol where needed
                if (row >= width - column - 1) {
                    screen.append("^");
                } else {
                    screen.append(" ");
                }
            }
            // new line
            screen.append(System.lineSeparator());
        }
        // result
        return screen.toString();
    }
}
