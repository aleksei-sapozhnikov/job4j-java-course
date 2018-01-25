package ru.job4j.paint;

import java.util.StringJoiner;

/**
 * Triangle - geometrical figure.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 25.01.2018
 */
public class Triangle implements Shape {

    /**
     * Draw this figure.
     *
     * @return String containing drawing in pseudo-graphics.
     */
    public String draw() {
        return new StringJoiner("\n")
                .add("    *    ")
                .add("   ***   ")
                .add("  *****  ")
                .add(" ******* ")
                .add("*********")
                .toString();
    }
}
