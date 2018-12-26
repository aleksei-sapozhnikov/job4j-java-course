package ru.job4j.functional.calculator;

import org.junit.Before;
import org.junit.Test;
import utils.StaticUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for Calculator class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public class CalculatorTest {

    private Calculator calculator = new Calculator();
    private List<Double> buffer = new ArrayList<>();
    private double delta = 0.0001D;

    private BiFunction<Integer, Integer, Double> plus = (a, b) -> (double) a + b;
    private BiFunction<Integer, Integer, Double> minus = (a, b) -> (double) a - b;
    private BiFunction<Integer, Integer, Double> multiply = (a, b) -> (double) a * b;
    private BiFunction<Integer, Integer, Double> divide = (a, b) -> (double) a / b;

    private BiFunction<Integer, Integer, Double> plusStatic = MathUtil::add;
    private BiFunction<Integer, Integer, Double> minusStatic = MathUtil::subt;
    private BiFunction<Integer, Integer, Double> multiplyStatic = MathUtil::mult;
    private BiFunction<Integer, Integer, Double> divideStatic = MathUtil::div;

    @Before
    public void clearBuffer() {
        this.buffer.clear();
    }

    /**
     * Test calculate() with lambdas
     */
    @Test
    public void whenPlusThenListOfResults() {
        this.calculator.calculate(0, 3, 1, this.plus, this.buffer::add);
        List<Double> expected = Arrays.asList(1D, 2D, 3D, 4D);
        assertThat(StaticUtils.haveEqualValues(this.buffer, expected, this.delta), is(true));
    }

    @Test
    public void whenMinusThenListOfResults() {
        this.calculator.calculate(0, 3, 1, this.minus, this.buffer::add);
        List<Double> expected = Arrays.asList(-1D, 0D, 1D, 2D);
        assertThat(StaticUtils.haveEqualValues(this.buffer, expected, this.delta), is(true));
    }

    @Test
    public void whenMultiplyThenListOfResults() {
        this.calculator.calculate(0, 3, 1, this.multiply, this.buffer::add);
        List<Double> expected = Arrays.asList(0D, 1D, 2D, 3D);
        assertThat(StaticUtils.haveEqualValues(this.buffer, expected, this.delta), is(true));
    }

    @Test
    public void whenDivideThenListOfResults() {
        this.calculator.calculate(0, 3, 2, this.divide, this.buffer::add);
        List<Double> expected = Arrays.asList(0D, 0.5D, 1D, 1.5D);
        assertThat(StaticUtils.haveEqualValues(this.buffer, expected, this.delta), is(true));
    }

    /**
     * Test calculate() with static methods references
     */
    @Test
    public void whenPlusStaticThenListOfResults() {
        this.calculator.calculate(0, 3, 1, this.plusStatic, this.buffer::add);
        List<Double> expected = Arrays.asList(1D, 2D, 3D, 4D);
        assertThat(StaticUtils.haveEqualValues(this.buffer, expected, this.delta), is(true));
    }

    @Test
    public void whenMinusStaticThenListOfResults() {
        this.calculator.calculate(0, 3, 1, this.minusStatic, this.buffer::add);
        List<Double> expected = Arrays.asList(-1D, 0D, 1D, 2D);
        assertThat(StaticUtils.haveEqualValues(this.buffer, expected, this.delta), is(true));
    }

    @Test
    public void whenMultiplyStaticThenListOfResults() {
        this.calculator.calculate(0, 3, 1, this.multiplyStatic, this.buffer::add);
        List<Double> expected = Arrays.asList(0D, 1D, 2D, 3D);
        assertThat(StaticUtils.haveEqualValues(this.buffer, expected, this.delta), is(true));
    }

    @Test
    public void whenDivideStaticThenListOfResults() {
        this.calculator.calculate(0, 3, 2, this.divideStatic, this.buffer::add);
        List<Double> expected = Arrays.asList(0D, 0.5D, 1D, 1.5D);
        assertThat(StaticUtils.haveEqualValues(this.buffer, expected, this.delta), is(true));
    }

}