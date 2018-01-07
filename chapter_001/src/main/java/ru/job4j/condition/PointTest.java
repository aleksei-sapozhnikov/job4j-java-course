package ru.job4j.condition;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Test for the Point.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 07.01.2018
 */
public class PointTest {

    /**
     * Test add
     */
    @Test
    public void whenDistanceToPointOneTwoAndPointThreeFourThenTwoPointEightyTwo() {
        Point a = new Point(1, 2);
        Point b = new Point(3, 4);
        double result = a.distanceTo(b);
        double expected = 2.8284271247461903D;
        assertThat(result, is(expected));
    }

}
