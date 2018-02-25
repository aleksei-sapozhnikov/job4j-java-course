package ru.job4j.calculator;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Test for the Calculator.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 06.01.2018
 */
public class CalculatorTest {

    /**
     * Test add
     */
    @Test
    public void whenAddOnePlusOneThenTwo() {
        Calculator calc = new Calculator();
        calc.add(1D, 1D);
        double result = calc.getResult();
        double expected = 2D;
        assertThat(result, is(expected));

    }

    /**
     * Test subtract
     */
    @Test
    public void whenSubtractTwoMinusOneThenOne() {
        Calculator calc = new Calculator();
        calc.subtract(2D, 1D);
        double result = calc.getResult();
        double expected = 1D;
        assertThat(result, is(expected));

    }

    /**
     * Test multiple
     */
    @Test
    public void whenMultipleTwoByThreeThenSix() {
        Calculator calc = new Calculator();
        calc.multiple(2D, 3D);
        double result = calc.getResult();
        double expected = 6D;
        assertThat(result, is(expected));

    }

    /**
     * Test div
     */
    @Test
    public void whenDivTwoByThreeThenOnePointFive() {
        Calculator calc = new Calculator();
        calc.div(3D, 2D);
        double result = calc.getResult();
        double expected = 1.5D;
        assertThat(result, is(expected));

    }
}