package ru.job4j.paint;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for Paint class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 25.01.2018
 */
public class PaintTest {

    /**
     * Test draw method.
     */
    @Test
    public void whenDrawSquareThenSquare() {
        //set new out
        PrintStream stdout = System.out;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        //draw
        new Paint().draw(new Square());
        //match
        assertThat(
                new String(out.toByteArray()),
                is(
                        new StringBuilder()
                                .append("*-*-*-*-*").append(System.lineSeparator())
                                .append("*-*-*-*-*").append(System.lineSeparator())
                                .append("*-*-*-*-*").append(System.lineSeparator())
                                .append("*-*-*-*-*").append(System.lineSeparator())
                                .append("*-*-*-*-*").append(System.lineSeparator())
                                .toString()
                )
        );
        // return standard out
        System.setOut(stdout);
    }

    /**
     * Test draw method.
     */
    @Test
    public void whenDrawTriangleThenTriangle() {
        //set new out
        PrintStream stdout = System.out;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        //draw
        new Paint().draw(new Triangle());
        //match
        assertThat(
                new String(out.toByteArray()),
                is(
                        new StringBuilder()
                                .append("    *    ").append(System.lineSeparator())
                                .append("   ***   ").append(System.lineSeparator())
                                .append("  *****  ").append(System.lineSeparator())
                                .append(" ******* ").append(System.lineSeparator())
                                .append("*********").append(System.lineSeparator())
                                .toString()
                )
        );
        // return standard out
        System.setOut(stdout);
    }

}