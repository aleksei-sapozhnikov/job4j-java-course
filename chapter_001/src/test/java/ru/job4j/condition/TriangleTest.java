package ru.job4j.condition;

import org.junit.Test;

import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.*;

/**
 * Test for class Triangle.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 07.01.2018
 */
public class TriangleTest {

    /**
     * Test semiPerimeter method.
     */
    @Test
    public void whenSemiPerimeterSetThreePointsThenSemiPerimeter() {
        // Making new Triangle.
        Point a = new Point(0, 0);
        Point b = new Point(0, 1);
        Point c = new Point(2, 0);
        Triangle triangle = new Triangle(a, b, c);
        // Finding lengths of triangle sides and semi-perimeter.
        double ab = a.distanceTo(b);
        double ac = a.distanceTo(c);
        double bc = b.distanceTo(c);
        double result = triangle.semiPerimeter(ab, ac, bc);
        double expected = 2.61803398875D;
        // Compare.
        assertThat(result, closeTo(expected, 0.1));
    }

    /**
     * Test area finding method.
     */
    @Test
    public void whenAreaSetThreePointsThenTriangleArea() {
        // Giving vertices.
        Point a = new Point(0, 0);
        Point b = new Point(0, 2);
        Point c = new Point(2, 0);
        // Making triangle.
        Triangle triangle = new Triangle(a, b, c);
        // Вычисляем площадь.
        double result = triangle.area();
        // Result we expect.
        double expected = 2D;
        // Comparing expected and real results.
        assertThat(result, closeTo(expected, 0.1));
    }
}