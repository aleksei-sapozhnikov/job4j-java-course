package ru.job4j.arrays;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Test for the Square class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 09.01.2018
 */
public class SquareTest {

    /**
     * Test calculate method.
     */
    @Test
    public void whenBoundThreeThenArrayToNine() {
        Square square = new Square();
        int[] result = square.calculate(3);
        int[] expected = new int[]{1, 4, 9};
        assertThat(result, is(expected));
    }

    @Test
    public void whenBoundFiveThenArrayToTwentyFive() {
        Square square = new Square();
        int[] result = square.calculate(5);
        int[] expected = new int[]{1, 4, 9, 16, 25};
        assertThat(result, is(expected));
    }

    @Test
    public void whenBoundZeroThenEmptyArray() {
        Square square = new Square();
        int[] result = square.calculate(0);
        int[] expected = new int[]{};
        assertThat(result, is(expected));
    }

    @Test
    public void whenBoundOneThenArrayToOne() {
        Square square = new Square();
        int[] result = square.calculate(1);
        int[] expected = new int[]{1};
        assertThat(result, is(expected));
    }

    @Test
    public void whenBoundNegativeThenEmptyArray() {
        Square square = new Square();
        int[] result = square.calculate(-5);
        int[] expected = new int[]{};
        assertThat(result, is(expected));
    }

}