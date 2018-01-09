package ru.job4j.loop;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for Factorial class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 09.01.2018
 */
public class FactorialTest {

    /**
     * Test calc method.
     */
    @Test
    public void whenZeroThenOne() {
        Factorial fact = new Factorial();
        int result = fact.calc(0);
        int expected = 1;
        assertThat(result, is(expected));
    }

    @Test
    public void whenOneThenOne() {
        Factorial fact = new Factorial();
        int result = fact.calc(1);
        int expected = 1;
        assertThat(result, is(expected));
    }

    @Test
    public void whenTwoThenTwo() {
        Factorial fact = new Factorial();
        int result = fact.calc(2);
        int expected = 2;
        assertThat(result, is(expected));
    }

    @Test
    public void whenThreeThenSix() {
        Factorial fact = new Factorial();
        int result = fact.calc(3);
        int expected = 6;
        assertThat(result, is(expected));
    }

    @Test
    public void whenFiveThenOneHundredAndTwenty() {
        Factorial fact = new Factorial();
        int result = fact.calc(5);
        int expected = 120;
        assertThat(result, is(expected));
    }

    @Test
    public void whenMinusFiveThenZero() {
        Factorial fact = new Factorial();
        int result = fact.calc(-5);
        int expected = 0;
        assertThat(result, is(expected));
    }
}