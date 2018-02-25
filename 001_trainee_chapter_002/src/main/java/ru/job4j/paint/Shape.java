package ru.job4j.paint;

/**
 * Is geometrical figure which can be drawn.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 25.01.2018
 */
public interface Shape {

    /**
     * Draw geometrical figure.
     *
     * @return String containing drawing in pseudo-graphics.
     */
    String draw();
}
