package ru.job4j.paint;

import org.junit.After;
import org.junit.Before;
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
     * Stores default "standard" output to console.
     */
    private final PrintStream stdout = System.out;

    /**
     * Buffer for results.
     */
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();


    /**
     * Change "standard" output to array.
     */
    @Before
    public void loadOutput() {
        System.out.println("executing @Before method");
        System.setOut(new PrintStream(this.out));
    }

    /**
     * Change output back to "standard".
     */
    @After
    public void backOutput() {
        System.setOut(this.stdout);
        System.out.println("executed @After method");
    }


    /**
     * Test draw method.
     */
    @Test
    public void whenDrawSquareThenSquare() {
        new Paint().draw(new Square());
        assertThat(
                new String(this.out.toByteArray()),
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
    }

    @Test
    public void whenDrawTriangleThenTriangle() {
        new Paint().draw(new Triangle());
        assertThat(
                new String(this.out.toByteArray()),
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
    }

}