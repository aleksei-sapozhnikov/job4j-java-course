package ru.job4j.paint;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for Square class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 25.01.2018
 */
public class SquareTest {

    /**
     * Test draw method.
     */
    @Test
    public void whenDrawSquareThenSquare() {
        Square square = new Square();
        assertThat(
                square.draw(),
                is(
                        new StringBuilder()
                                .append("*-*-*-*-*\n")
                                .append("*-*-*-*-*\n")
                                .append("*-*-*-*-*\n")
                                .append("*-*-*-*-*\n")
                                .append("*-*-*-*-*")
                                .toString()
                )
        );
    }
}