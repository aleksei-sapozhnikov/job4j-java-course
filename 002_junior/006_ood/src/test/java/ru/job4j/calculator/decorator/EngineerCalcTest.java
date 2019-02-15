package ru.job4j.calculator.decorator;

import org.junit.Test;
import org.mockito.Mockito;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class EngineerCalcTest {

    @Test
    public void whenTwoArgsThenInnerCalled() throws Exception {
        var inner = Mockito.mock(ICalc.class);
        Mockito.when(inner.calculate(ICalc.ADD, 1, 2)).thenReturn(3.0);
        var result = new EngineerCalc(inner).calculate(ICalc.ADD, 1, 2);
        assertThat(result, is(3.0));
    }

    @Test
    public void whenOneArgNotTrigonometryThenInnerCalled() throws Exception {
        var inner = Mockito.mock(ICalc.class);
        var calculator = new EngineerCalc(inner);
        // make stored result == 0.0
        Mockito.when(inner.calculate(ICalc.ADD, 1, 2)).thenReturn(0.0);
        var stored = calculator.calculate(ICalc.ADD, 1, 2);
        assertThat(stored, closeTo(0.0, 0.0001));
        // do action with stored result
        Mockito.when(inner.calculate(ICalc.ADD, 0.0, 5.0)).thenReturn(10.0);
        var result = new EngineerCalc(inner).calculate(ICalc.ADD, 5.0);
        assertThat(result, closeTo(10.0, 0.0001));
    }

    @Test
    public void whenOneArgSinThenCalculate() throws Exception {
        var inner = Mockito.mock(ICalc.class);
        var result = new EngineerCalc(inner).calculate(EngineerCalc.SIN, Math.PI / 2);
        var expected = 1.0;
        assertThat(result, closeTo(expected, 0.0001));
    }

    @Test
    public void whenOneArgCosThenCalculate() throws Exception {
        var inner = Mockito.mock(ICalc.class);
        var result = new EngineerCalc(inner).calculate(EngineerCalc.COS, Math.PI);
        var expected = -1.0;
        assertThat(result, closeTo(expected, 0.0001));
    }

    @Test
    public void whenOneArgTanThenCalculate() throws Exception {
        var inner = Mockito.mock(ICalc.class);
        var result = new EngineerCalc(inner).calculate(EngineerCalc.TAN, Math.PI / 4);
        var expected = 1.0;
        assertThat(result, closeTo(expected, 0.0001));
    }

    @Test
    public void whenNoArgSinThenCalculateFromResult() throws Exception {
        var inner = Mockito.mock(ICalc.class);
        var calculator = new EngineerCalc(inner);
        // make result be PI/2
        Mockito.when(inner.calculate(ICalc.ADD, 1, 2)).thenReturn(Math.PI / 2);
        calculator.calculate(ICalc.ADD, 1, 2);
        // make calculations on result
        var result = calculator.calculate(EngineerCalc.SIN);
        var expected = 1.0;
        assertThat(result, closeTo(expected, 0.0001));
    }

    @Test
    public void whenNoArgCosThenCalculateFromResult() throws Exception {
        var inner = Mockito.mock(ICalc.class);
        var calculator = new EngineerCalc(inner);
        // make result be -PI
        Mockito.when(inner.calculate(ICalc.ADD, 1, 2)).thenReturn(-Math.PI);
        calculator.calculate(ICalc.ADD, 1, 2);
        // make calculations on result
        var result = calculator.calculate(EngineerCalc.COS);
        var expected = -1.0;
        assertThat(result, closeTo(expected, 0.0001));
    }

    @Test
    public void whenNoArgTanThenCalculateFromResult() throws Exception {
        var inner = Mockito.mock(ICalc.class);
        var calculator = new EngineerCalc(inner);
        // make result be PI/4
        Mockito.when(inner.calculate(ICalc.ADD, 1, 2)).thenReturn(Math.PI / 4);
        calculator.calculate(ICalc.ADD, 1, 2);
        // make calculations on result
        var result = calculator.calculate(EngineerCalc.TAN);
        var expected = 1.0;
        assertThat(result, closeTo(expected, 0.0001));
    }

    @Test
    public void whenAbsolutelyUnknownOperationThenException() {
        var wasException = new boolean[]{false};
        try {
            new EngineerCalc(Mockito.mock(ICalc.class))
                    .calculate("asdfkjasdflkjsdf");
        } catch (Exception e) {
            if (e.getMessage().startsWith("Unknown operation")) {
                wasException[0] = true;
            }
        }
        assertTrue(wasException[0]);
    }


}