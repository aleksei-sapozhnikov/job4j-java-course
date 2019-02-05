package ru.job4j.calculator;

import org.junit.Test;
import org.mockito.stubbing.Stubber;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class InteractCalcTest {

    /**
     * Testing simple calculations.
     */
    @Test
    public void whenAddThenCalculatorCalledAndResultPrinted() throws Exception {
        var commands = new LinkedList<>(Arrays.asList("1 + 2", "exit"));
        var calculator = mock(Calculator.class);
        var calcResult = 3.0;
        var invoked = new boolean[]{false};
        this.setInvokedTrue(invoked).when(calculator).add(1, 2);
        this.simpleCalculationTest(commands, calculator, calcResult, invoked);
    }

    @Test
    public void whenSubtractThenCalculatorCalledAndResultPrinted() throws Exception {
        var commands = new LinkedList<>(Arrays.asList("5 - 3", "exit"));
        var calcResult = 2.0;
        var invoked = new boolean[]{false};
        var calculator = mock(Calculator.class);
        this.setInvokedTrue(invoked).when(calculator).subtract(5, 3);
        this.simpleCalculationTest(commands, calculator, calcResult, invoked);
    }

    @Test
    public void whenMultipleThenCalculatorCalledAndResultPrinted() throws Exception {
        var commands = new LinkedList<>(Arrays.asList("5 * 3", "exit"));
        var calculator = mock(Calculator.class);
        var calcResult = 15.0;
        var invoked = new boolean[]{false};
        this.setInvokedTrue(invoked).when(calculator).multiple(5, 3);
        this.simpleCalculationTest(commands, calculator, calcResult, invoked);
    }

    @Test
    public void whenDivideThenCalculatorCalledAndResultPrinted() throws Exception {
        var commands = new LinkedList<>(Arrays.asList("6 / 3", "exit"));
        var calculator = mock(Calculator.class);
        var calcResult = 2.0;
        var invoked = new boolean[]{false};
        this.setInvokedTrue(invoked).when(calculator).div(6, 3);
        this.simpleCalculationTest(commands, calculator, calcResult, invoked);
    }

    private Stubber setInvokedTrue(boolean[] invoked) {
        return doAnswer(i -> {
            invoked[0] = true;
            return null;
        });
    }

    private void simpleCalculationTest(LinkedList<String> commands, Calculator calculator, double calcResult, boolean[] invoked) throws Exception {
        when(calculator.getResult()).thenReturn(calcResult);
        var result = new ArrayList<String>();
        new InteractCalc(calculator, commands::poll, result::add).start();
        var expected = List.of(
                InteractCalc.GREETING_MSG,
                String.format(InteractCalc.RESULT_MSG_FORMAT, String.valueOf(calcResult)),
                InteractCalc.EXITING_MSG);
        assertThat(result, is(expected));
        assertTrue(invoked[0]);
    }


    /**
     * Testing using previous result in calculation.
     */
    @Test
    public void whenFirstArgumentResultThenThenCalculatorCalledAndResultPrinted() throws Exception {
        var calculator = mock(Calculator.class);
        var commands = new LinkedList<>(Arrays.asList(
                "1 + 2",
                String.format("%s * 2", InteractCalc.RES),
                "exit"));
        when(calculator.getResult()).thenReturn(
                3.0,
                3.0,
                6.0);
        // setting invocations to check
        var invoked = new boolean[]{false, false};
        doAnswer(i -> {
            invoked[0] = true;
            return null;
        }).when(calculator).add(1, 2);
        doAnswer(i -> {
            invoked[1] = true;
            return null;
        }).when(calculator).multiple(3, 2);
        // getting result
        var result = new ArrayList<String>();
        new InteractCalc(calculator, commands::poll, result::add).start();
        var expected = List.of(
                InteractCalc.GREETING_MSG,
                String.format(InteractCalc.RESULT_MSG_FORMAT, String.valueOf(3.0)),
                String.format(InteractCalc.RESULT_MSG_FORMAT, String.valueOf(6.0)),
                InteractCalc.EXITING_MSG);
        assertTrue(invoked[0]);
        assertTrue(invoked[1]);
        assertThat(result, is(expected));
    }

    @Test
    public void whenSecondArgumentResultThenThenCalculatorCalledAndResultPrinted() throws Exception {
        var calculator = mock(Calculator.class);
        var commands = new LinkedList<>(Arrays.asList(
                "1 + 2",
                String.format("2 * %s", InteractCalc.RES),
                "exit"));
        when(calculator.getResult()).thenReturn(
                3.0,
                3.0,
                6.0);
        // setting invocations to check
        var invoked = new boolean[]{false, false};
        doAnswer(i -> {
            invoked[0] = true;
            return null;
        }).when(calculator).add(1, 2);
        doAnswer(i -> {
            invoked[1] = true;
            return null;
        }).when(calculator).multiple(2, 3);
        // getting result
        var result = new ArrayList<String>();
        new InteractCalc(calculator, commands::poll, result::add).start();
        var expected = List.of(
                InteractCalc.GREETING_MSG,
                String.format(InteractCalc.RESULT_MSG_FORMAT, String.valueOf(3.0)),
                String.format(InteractCalc.RESULT_MSG_FORMAT, String.valueOf(6.0)),
                InteractCalc.EXITING_MSG);
        assertTrue(invoked[0]);
        assertTrue(invoked[1]);
        assertThat(result, is(expected));
    }

    /**
     * Testing cases of wrong arguments.
     */
    @Test
    public void whenWrongFirstArgumentThenExceptionPrinted() throws Exception {
        var calculator = mock(Calculator.class);
        var commands = new LinkedList<>(Arrays.asList(
                "ab + 2",
                "exit"));
        var result = new ArrayList<String>();
        new InteractCalc(calculator, commands::poll, result::add).start();
        assertTrue(result.get(1).startsWith("Exception"));
    }

    @Test
    public void whenWrongSecondArgumentThenExceptionPrinted() throws Exception {
        var calculator = mock(Calculator.class);
        var commands = new LinkedList<>(Arrays.asList(
                "2 + ab",
                "exit"));
        var result = new ArrayList<String>();
        new InteractCalc(calculator, commands::poll, result::add).start();
        assertTrue(result.get(1).startsWith("Exception"));
    }

    @Test
    public void whenWrongOperationThenExceptionPrinted() throws Exception {
        var calculator = mock(Calculator.class);
        var commands = new LinkedList<>(Arrays.asList(
                "2 ! 3",
                "exit"));
        var result = new ArrayList<String>();
        new InteractCalc(calculator, commands::poll, result::add).start();
        assertTrue(result.get(1).startsWith("Exception"));
    }
}