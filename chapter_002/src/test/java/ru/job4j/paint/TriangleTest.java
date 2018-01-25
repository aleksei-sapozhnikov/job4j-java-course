package ru.job4j.paint;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for Triangle class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 25.01.2018
 */
public class TriangleTest {

    /**
     * Test draw method.
     */
    @Test
    public void whenDrawTriangleThenTriangle() {
        Triangle triangle = new Triangle();
        assertThat(
                triangle.draw(),
                is(
                        new StringBuilder()
                                .append("    *    ").append(System.lineSeparator())
                                .append("   ***   ").append(System.lineSeparator())
                                .append("  *****  ").append(System.lineSeparator())
                                .append(" ******* ").append(System.lineSeparator())
                                .append("*********")
                                .toString()
                )
        );
    }
}