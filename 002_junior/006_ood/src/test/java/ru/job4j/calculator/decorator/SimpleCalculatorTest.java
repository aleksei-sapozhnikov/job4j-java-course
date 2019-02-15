package ru.job4j.calculator.decorator;

import org.junit.Test;
import org.mockito.Mockito;
import ru.job4j.calculator.ICalculator;

import static org.junit.Assert.assertTrue;

public class SimpleCalculatorTest {

    @Test
    public void whenAddThenCalculatorCalled() throws Exception {
        var innerCalc = Mockito.mock(ICalculator.class);
        var calculator = new SimpleCalculator(innerCalc);
        var wasCalled = new boolean[]{false};
        // two args
        Mockito.doAnswer(i -> wasCalled[0] = true).when(innerCalc).add(1.0, 2.0);
        calculator.calculate("add", 1.0, 2.0);
        assertTrue(wasCalled[0]);
        // result and second arg
        wasCalled[0] = false; //reset
        Mockito.when(innerCalc.getResult()).thenReturn(3.0, 5.0);
        Mockito.doAnswer(i -> wasCalled[0] = true).when(innerCalc).add(3.0, 2.0);
        calculator.calculate("add", 2.0);
        assertTrue(wasCalled[0]);
    }

    @Test
    public void whenSubtractThenCalculatorCalled() throws Exception {
        var innerCalc = Mockito.mock(ICalculator.class);
        var calculator = new SimpleCalculator(innerCalc);
        var wasCalled = new boolean[]{false};
        // two args
        Mockito.doAnswer(i -> wasCalled[0] = true).when(innerCalc).subtract(5.0, 3.0);
        calculator.calculate("subtract", 5.0, 3.0);
        assertTrue(wasCalled[0]);
        // result and second arg
        wasCalled[0] = false; //reset
        Mockito.when(innerCalc.getResult()).thenReturn(8.0, 5.0);
        Mockito.doAnswer(i -> wasCalled[0] = true).when(innerCalc).subtract(8.0, 3.0);
        calculator.calculate("subtract", 3.0);
        assertTrue(wasCalled[0]);
    }

    @Test
    public void whenMultipleThenCalculatorCalled() throws Exception {
        var innerCalc = Mockito.mock(ICalculator.class);
        var calculator = new SimpleCalculator(innerCalc);
        var wasCalled = new boolean[]{false};
        // two args
        Mockito.doAnswer(i -> wasCalled[0] = true).when(innerCalc).multiple(5.0, 3.0);
        calculator.calculate("multiple", 5.0, 3.0);
        assertTrue(wasCalled[0]);
        // result and second arg
        wasCalled[0] = false; //reset
        Mockito.when(innerCalc.getResult()).thenReturn(15.0, 30.0);
        Mockito.doAnswer(i -> wasCalled[0] = true).when(innerCalc).multiple(15.0, 2.0);
        calculator.calculate("multiple", 2.0);
        assertTrue(wasCalled[0]);
    }

    @Test
    public void whenDivThenCalculatorCalled() throws Exception {
        var innerCalc = Mockito.mock(ICalculator.class);
        var calculator = new SimpleCalculator(innerCalc);
        var wasCalled = new boolean[]{false};
        // two args
        Mockito.doAnswer(i -> wasCalled[0] = true).when(innerCalc).div(6.0, 3.0);
        calculator.calculate("div", 6.0, 3.0);
        assertTrue(wasCalled[0]);
        // result and second arg
        wasCalled[0] = false; //reset
        Mockito.when(innerCalc.getResult()).thenReturn(6.0, 3.0);
        Mockito.doAnswer(i -> wasCalled[0] = true).when(innerCalc).div(6.0, 2.0);
        calculator.calculate("div", 3.0);
        assertTrue(wasCalled[0]);
    }

    @Test
    public void whenUnknownActionThenException() {
        var wasException = new boolean[]{false};
        try {
            new SimpleCalculator(Mockito.mock(ICalculator.class)).calculate("abaasdfgerasdf", 3.4, 2.4);
        } catch (Exception e) {
            wasException[0] = true;
        }
        assertTrue(wasException[0]);
    }
}