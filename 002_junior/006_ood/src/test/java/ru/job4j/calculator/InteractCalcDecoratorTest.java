package ru.job4j.calculator;

import org.junit.Test;
import org.mockito.Mockito;
import ru.job4j.util.function.SupplierEx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Consumer;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class InteractCalcDecoratorTest {

    @Test
    public void whenAddThenCallCalculator() throws Exception {
        Queue<String> commands = new LinkedList<>(Arrays.asList("4", "2"));
        SupplierEx<String> input = commands::poll;
        Consumer<String> output = (s) -> {
        };
        Calculator calculator = Mockito.mock(Calculator.class);
        var wasCalled = new boolean[]{false};
        Mockito.doAnswer(i -> {
            wasCalled[0] = true;
            return null;
        }).when(calculator).add(4, 2);
        new InteractCalcDecorator(calculator, input, output).add();
        assertTrue(wasCalled[0]);
    }


    @Test
    public void whenSubtractThenCallCalculator() throws Exception {
        Queue<String> commands = new LinkedList<>(Arrays.asList("4", "2"));
        SupplierEx<String> input = commands::poll;
        Consumer<String> output = (s) -> {
        };
        Calculator calculator = Mockito.mock(Calculator.class);
        var wasCalled = new boolean[]{false};
        Mockito.doAnswer(i -> {
            wasCalled[0] = true;
            return null;
        }).when(calculator).subtract(4, 2);
        new InteractCalcDecorator(calculator, input, output).subtract();
        assertTrue(wasCalled[0]);
    }

    @Test
    public void whenMultipleThenCallCalculator() throws Exception {
        Queue<String> commands = new LinkedList<>(Arrays.asList("4", "2"));
        SupplierEx<String> input = commands::poll;
        Consumer<String> output = (s) -> {
        };
        Calculator calculator = Mockito.mock(Calculator.class);
        var wasCalled = new boolean[]{false};
        Mockito.doAnswer(i -> {
            wasCalled[0] = true;
            return null;
        }).when(calculator).multiple(4, 2);
        new InteractCalcDecorator(calculator, input, output).multiple();
        assertTrue(wasCalled[0]);
    }

    @Test
    public void whenDivThenCallCalculator() throws Exception {
        Queue<String> commands = new LinkedList<>(Arrays.asList("4", "2"));
        SupplierEx<String> input = commands::poll;
        Consumer<String> output = (s) -> {
        };
        Calculator calculator = Mockito.mock(Calculator.class);
        var wasCalled = new boolean[]{false};
        Mockito.doAnswer(i -> {
            wasCalled[0] = true;
            return null;
        }).when(calculator).div(4, 2);
        new InteractCalcDecorator(calculator, input, output).div();
        assertTrue(wasCalled[0]);
    }

    @Test
    public void whenPrintResultThenPrintResultFromCalculatorPrinted() {
        Queue<String> commands = new LinkedList<>();
        var answers = new ArrayList<String>();
        SupplierEx<String> input = commands::poll;
        Consumer<String> output = answers::add;
        Calculator calculator = Mockito.mock(Calculator.class);
        when(calculator.getResult()).thenReturn(6.4);
        new InteractCalcDecorator(calculator, input, output).printResult();
        assertThat(answers.get(0), is("6.4"));
    }

    @Test
    public void whenStartThenCommandsDoOperation() throws Exception {
        Queue<String> commands = new LinkedList<>(Arrays.asList(
                "add", "4", "2",            // 6
                "subtract", "res", "2",     // 4
                "multiple", "res", "3",     // 12
                "div", "res", "24",         // 0.5
                "result",
                "exit"
        ));
        var answers = new ArrayList<String>();
        SupplierEx<String> input = commands::poll;
        Consumer<String> output = answers::add;
        Calculator calculator = Mockito.mock(Calculator.class);
        when(calculator.getResult()).thenReturn(6.0, 4.0, 12.0, 0.5, 0.5);
        // check all calls to calculator were made
        var allWereCalled = new boolean[]{false, false, false, false};
        Mockito.doAnswer(i -> {
            allWereCalled[0] = true;
            return null;
        }).when(calculator).add(4, 2);
        Mockito.doAnswer(i -> {
            allWereCalled[1] = true;
            return null;
        }).when(calculator).subtract(6, 2);
        Mockito.doAnswer(i -> {
            allWereCalled[2] = true;
            return null;
        }).when(calculator).multiple(4, 3);
        Mockito.doAnswer(i -> {
            allWereCalled[3] = true;
            return null;
        }).when(calculator).div(12, 24);
        new InteractCalcDecorator(calculator, input, output).start();
        assertThat(allWereCalled, is(new boolean[]{true, true, true, true}));
        assertThat(answers.get(answers.size() - 2), is("0.5"));
    }

    @Test
    public void whenUnknownOperationThenPrintedMessage() throws Exception {
        Queue<String> commands = new LinkedList<>(Arrays.asList("abrakadabra", "exit"));
        var answers = new ArrayList<String>();
        SupplierEx<String> input = commands::poll;
        Consumer<String> output = answers::add;
        Calculator calculator = Mockito.mock(Calculator.class);
        new InteractCalcDecorator(calculator, input, output).start();
        assertThat(answers.get(answers.size() - 2), startsWith("Unknown operation"));
    }

    @Test
    public void whenEnteredValueNotDoubleThenEnterAgainWorks() throws Exception {
        Queue<String> commands = new LinkedList<>(Arrays.asList(
                "add",
                "one", "two", "1.5",
                "five", "marta", "2",
                "exit"));
        SupplierEx<String> input = commands::poll;
        Consumer<String> output = s -> {
        };
        Calculator calculator = Mockito.mock(Calculator.class);
        var wasCalled = new boolean[]{false};
        Mockito.doAnswer(i -> {
            wasCalled[0] = true;
            return null;
        }).when(calculator).add(1.5, 2);
        new InteractCalcDecorator(calculator, input, output).start();
        assertTrue(wasCalled[0]);
    }
}