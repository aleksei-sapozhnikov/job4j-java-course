package ru.job4j.conditions;

import org.junit.Test;

import static org.hamcrest.number.IsCloseTo.closeTo;
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
     * Test method distanceTo finding distance to other point.
     */
    @Test
    public void whenDistanceToPointOneTwoAndPointThreeFourThenTwoPointEightyTwo() {
        Point a = new Point(1, 2);
        Point b = new Point(3, 4);
        double result = a.distanceTo(b);
        double expected = 2.8284271247461903D;
        assertThat(result, closeTo(expected, 0.0001));
    }

    @Test
    public void whenDistanceToPointZeroZeroAndPointZeroFiveThenFive() {
        Point a = new Point(0, 0);
        Point b = new Point(0, 5);
        double result = a.distanceTo(b);
        double expected = 5D;
        assertThat(result, closeTo(expected, 0.0001));
    }

    @Test
    public void whenDistanceToSwappedPointsThenRemainsTheSame() {
        Point a = new Point(3, 7);
        Point b = new Point(6, 8);
        double resultOne = a.distanceTo(b);
        double resultTwo = b.distanceTo(a);
        assertThat(resultOne, closeTo(resultTwo, 0.0001));
    }

    @Test
    public void whenDistanceToOneAndOnlyPointThenZero() {
        Point a = new Point(2, 5);
        Point b = new Point(2, 5);
        double result = a.distanceTo(b);
        double expected = 0D;
        assertThat(result, closeTo(expected, 0.0001));
    }

}
