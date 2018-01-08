package ru.job4j.max;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Test for Max.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 07.01.2018
 */
public class MaxTest {

    /**
     * Test max method with two values.
     */
    @Test
    public void whenFirstMoreThanSecondThenFirst() {
        Max maximum = new Max();
        assertThat(
                maximum.max(2, 1), is(2)
        );
    }

    @Test
    public void whenFirstLessThanSecondThenSecond() {
        Max maximum = new Max();
        assertThat(
                maximum.max(3, 4), is(4)
        );
    }

    @Test
    public void whenFirstEqualsSecondThenAny() {
        Max maximum = new Max();
        assertThat(
                maximum.max(3, 3), is(3)
        );
    }

    /**
     * Test max method with three values.
     */
    @Test
    public void whenFirstTheBiggestThenFirst() {
        Max maximum = new Max();
        assertThat(
                maximum.max(3, 2, 1), is(3)
        );
    }

    @Test
    public void whenSecondTheBiggestThenSecond() {
        Max maximum = new Max();
        assertThat(
                maximum.max(-3, 5, -4), is(5)
        );
    }

    @Test
    public void whenThirdTheBiggestThenThird() {
        Max maximum = new Max();
        assertThat(
                maximum.max(3, -4, 6), is(6)
        );
    }

    @Test
    public void whenAllEqualThenAny() {
        Max maximum = new Max();
        assertThat(
                maximum.max(-3, -3, -3), is(-3)
        );
    }

}
