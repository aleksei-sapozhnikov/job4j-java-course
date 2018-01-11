package ru.job4j.array;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Test for the Matrix class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 11.01.2018
 */
public class MatrixTest {

    /**
     * Test multiple method.
     */
    @Test
    public void whenSizeFiveThenMatrixFromZeroToFour() {
        Matrix matrix = new Matrix();
        int[][] result = matrix.multiple(5);
        int[][] expected = new int[][]{
                {0, 0, 0, 0, 0},
                {0, 1, 2, 3, 4},
                {0, 2, 4, 6, 8},
                {0, 3, 6, 9, 12},
                {0, 4, 8, 12, 16}
        };
        assertThat(result, is(expected));
    }

    @Test
    public void whenSizeZeroThenMatrixEmpty() {
        Matrix matrix = new Matrix();
        int[][] result = matrix.multiple(0);
        int[][] expected = new int[][]{};
        assertThat(result, is(expected));
    }

    @Test
    public void whenSizeOneThenMatrixWithZero() {
        Matrix matrix = new Matrix();
        int[][] result = matrix.multiple(1);
        int[][] expected = new int[][]{{0}};
        assertThat(result, is(expected));
    }

    @Test
    public void whenSizeElevenThenMatrixFromZeroToTen() {
        Matrix matrix = new Matrix();
        int[][] result = matrix.multiple(11);
        int[][] expected = new int[][]{
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10},
                {0, 2, 4, 6, 8, 10, 12, 14, 16, 18, 20},
                {0, 3, 6, 9, 12, 15, 18, 21, 24, 27, 30},
                {0, 4, 8, 12, 16, 20, 24, 28, 32, 36, 40},
                {0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50},
                {0, 6, 12, 18, 24, 30, 36, 42, 48, 54, 60},
                {0, 7, 14, 21, 28, 35, 42, 49, 56, 63, 70},
                {0, 8, 16, 24, 32, 40, 48, 56, 64, 72, 80},
                {0, 9, 18, 27, 36, 45, 54, 63, 72, 81, 90},
                {0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100}
        };
        assertThat(result, is(expected));
    }

}