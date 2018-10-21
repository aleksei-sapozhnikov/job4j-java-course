package ru.job4j.functional.function;

import org.junit.Test;
import utils.StaticUtils;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for FunctionInRange class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public class FunctionInRangeTest {

    private FunctionInRange function = new FunctionInRange();

    private Function<Integer, Double> linear = Double::valueOf;
    private Function<Integer, Double> square = x -> Math.pow(x, 2);
    private Function<Integer, Double> logarithm = Math::log;

    private double delta = 0.0001d;

    @Test
    public void whenLinearThenValuesRight() {
        List<Double> result = this.function.valuesInRange(0, 5, this.linear);
        List<Double> expected = Arrays.asList(0d, 1d, 2d, 3d, 4d, 5d);
        assertThat(StaticUtils.haveEqualValues(result, expected, this.delta), is(true));
    }

    @Test
    public void whenSquareThenValuesRight() {
        List<Double> result = this.function.valuesInRange(0, 5, this.square);
        List<Double> expected = Arrays.asList(0d, 1d, 4d, 9d, 16d, 25d);
        assertThat(StaticUtils.haveEqualValues(result, expected, this.delta), is(true));
    }

    @Test
    public void whenLogarithmThenValuesRight() {
        List<Double> result = this.function.valuesInRange(1, 7, this.logarithm);
        List<Double> expected = Arrays.asList(0d, 0.6931471805599453, 1.0986122886681098,
                1.3862943611198906, 1.6094379124341003, 1.791759469228055, 1.9459101490553132);
        assertThat(StaticUtils.haveEqualValues(result, expected, this.delta), is(true));
    }


}