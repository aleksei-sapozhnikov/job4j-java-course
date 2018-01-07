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
     * Test max method.
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
}
