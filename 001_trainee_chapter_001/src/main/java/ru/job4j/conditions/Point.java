package ru.job4j.conditions;

/**
 * Class operates with 2-dimensional points.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 07.01.2018
 */
public class Point {

    /**
     * Point coordinates.
     */
    private int x;
    private int y;

    /**
     * Constructor.
     *
     * @param x x-coordinate.
     * @param y y-coordinate.
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Calculates distance between our Point and another Point.
     *
     * @param that Other point.
     * @return Distance between points.
     */
    public double distanceTo(Point that) {
        return Math.sqrt(
                Math.pow(that.x - this.x, 2) + Math.pow(that.y - this.y, 2)
        );
    }

    /**
     * Main.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        Point a = new Point(1, 2);
        Point b = new Point(3, 4);

        System.out.println("x1 = " + a.x);
        System.out.println("y1 = " + a.y);
        System.out.println("x2 = " + b.x);
        System.out.println("y2 = " + b.y);

        System.out.println("Расстояние между точками А и В : " + a.distanceTo(b));
    }

}
