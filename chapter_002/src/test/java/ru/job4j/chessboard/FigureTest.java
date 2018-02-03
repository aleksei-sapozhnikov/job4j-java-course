package ru.job4j.chessboard;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for the Figure class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 04.02.2018
 */
public class FigureTest {

    /**
     * Test isInPosition() method.
     */
    @Test
    public void whenArrayOfFiguresInPositionThenArrayTrue() {
        Figure[] figures = new Figure[]{
                new Bishop(new Cell(1, 2))
        };
        //result
        boolean[] result = new boolean[figures.length];
        for (int i = 0; i < figures.length; i++) {
            result[i] = figures[i].isInPosition(new Cell(1, 2));
        }
        //expected
        boolean[] expected = new boolean[]{
                true
        };
        //check
        assertThat(result, is(expected));
    }

    @Test
    public void whenArrayOfFiguresNotInPositionThenArrayFalse() {
        Figure[] figures = new Figure[]{
                new Bishop(new Cell(1, 2))
        };
        //result
        boolean[] result = new boolean[figures.length];
        for (int i = 0; i < figures.length; i++) {
            result[i] = figures[i].isInPosition(new Cell(3, 4));
        }
        //expected
        boolean[] expected = new boolean[]{
                false
        };
        //check
        assertThat(result, is(expected));
    }
}