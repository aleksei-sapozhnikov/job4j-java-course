package ru.job4j.paint;

/**
 * Paints in pseudo-graphics to console.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 25.01.2018
 */
public class Paint {

    /**
     * Paints given figure to console.
     *
     * @param shape Geometrical figure.
     */
    public void draw(Shape shape) {
        System.out.println(shape.draw());
    }

    /**
     * Main method.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        Shape[] shapes = {new Triangle(), new Square()};
        Paint paint = new Paint();
        for (Shape shape : shapes) {
            paint.draw(shape);
            System.out.println();
        }
    }
}
