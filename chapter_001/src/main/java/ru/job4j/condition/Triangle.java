package ru.job4j.condition;

/**
 * Two-dimensional triangle.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 07.01.2018
 */
public class Triangle {

    /**
     * Triangle vertices
     */
    private Point a;
    private Point b;
    private Point c;

    /**
     * Constructor.
     *
     * @param a First vertex.
     * @param b Second vertex.
     * @param c Third vertex.
     */
    public Triangle(Point a, Point b, Point c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    /**
     * Finds triangle semi-perimeter p.
     * Formula: p == (ab + ac + bc) / 2
     *
     * @param ab Distance between vertex a and vertex b.
     * @param ac Distance between vertex a and vertex c.
     * @param bc Distance between vertex b and vertex c.
     * @return semi-perimeter.
     */
    public double semiPerimeter(double ab, double ac, double bc) {
        return (ab + ac + bc) / 2;
    }

    /**
     * Finds triangle area.
     * Using Heron's formula:  S = sqrt(p*(p - ab)*(p - ac)*(p - bc)),
     * where ab, ac, bc are sides of the triangle.
     *
     * @return Triangle's area if such triangle can exist, or -1 if not.
     */
    public double area() {
        double rsl = -1;
        // Finding sides and semi-perimeter
        double ab = this.a.distanceTo(this.b);
        double ac = this.a.distanceTo(this.c);
        double bc = this.b.distanceTo(this.c);
        double p = this.semiPerimeter(ab, ac, bc);
        // Checking if triangle with such lines can exist
        if (this.exist(ab, ac, bc)) {
            rsl = Math.sqrt(p * (p - ab) * (p - ac) * (p - bc));
        }
        return rsl;
    }

    /**
     * Checks if a triangle with such sides can exist.
     * Sum of lengths of any two sides must be bigger than length of the third side.
     *
     * @param ab Distance between vertex a and vertex b.
     * @param ac Distance between vertex a and vertex c.
     * @param bc Distance between vertex b and vertex c
     * @return true if such triangle can exist, false otherwise.
     */
    private boolean exist(double ab, double ac, double bc) {
        boolean rsl = false;
        if (ab + ac > bc
                && ab + bc > ac
                && ac + bc > ab
                ) {
            rsl = true;
        }
        return rsl;
    }

}
