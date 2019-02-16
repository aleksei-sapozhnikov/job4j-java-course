package ru.job4j.calculator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.util.function.ProcedureEx;
import ru.job4j.util.function.SupplierEx;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Interactive calculator - as decorator.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class InteractCalcDecorator extends Calculator {
    /**
     * Entering this as one of values means using stored result value.
     */
    public static final String RESULT_VALUE = "res";
    /**
     * Entering this as operation mean exit the application.
     */
    public static final String EXIT = "exit";
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(InteractCalcDecorator.class);
    /**
     * Calculator object to perform actions.
     */
    private final Calculator calculator;
    /**
     * User commands input.
     */
    private final SupplierEx<String> input;
    /**
     * To-user messages.
     */
    private final Consumer<String> output;
    /**
     * Dispatch to map user command and action to do.
     */
    private final Map<String, ProcedureEx> actions = new HashMap<>();

    /**
     * Constructs new instance.
     *
     * @param calculator Calculator object to use.
     * @param input      User commands input.
     * @param output     To-user messages output.
     */
    public InteractCalcDecorator(Calculator calculator, SupplierEx<String> input, Consumer<String> output) {
        this.calculator = calculator;
        this.input = input;
        this.output = output;
        this.initActions();
    }

    /**
     * Initiates map of commands-actions.
     */
    private void initActions() {
        this.actions.put("add", this::add);
        this.actions.put("subtract", this::subtract);
        this.actions.put("multiple", this::multiple);
        this.actions.put("div", this::div);
        this.actions.put("result", this::printResult);
    }

    /**
     * Starts working with user.
     *
     * @throws Exception In case of exception thrown by input.
     */
    public void start() throws Exception {
        this.printGreeting();
        var input = this.getInputOperation();
        while (!EXIT.equals(input)) {
            this.actions.getOrDefault(input, this::unknownOperation).action();
            input = this.getInputOperation();
        }
    }

    /**
     * Prints greeting to user.
     */
    private void printGreeting() {
        this.output.accept(String.join(System.lineSeparator(),
                "Welcome! Available operations:",
                " - add      : sum two values",
                " - subtract : subtract second value from first",
                " - div      : divide first value by second",
                " - multiple : multiply two values",
                " - result   : print result",
                " - exit     : exit application",
                "   To use stored result value instead of first or second value print \"res\" when asked to enter value"
        ));
    }

    /**
     * Gets user input what operation to perform.
     *
     * @return User input.
     * @throws Exception In case of exception thrown by input.
     */
    private String getInputOperation() throws Exception {
        this.output.accept("Enter operation:");
        return this.input.get();
    }

    /**
     * Interactively adds two values given by user.
     *
     * @throws Exception In case of exception thrown by input.
     */
    public void add() throws Exception {
        var twoDoubles = this.getInputAsTwoDoubles();
        this.calculator.add(twoDoubles.first, twoDoubles.second);
        this.output.accept("added");
    }

    /**
     * Interactively subtracts two values given by user.
     *
     * @throws Exception In case of exception thrown by input.
     */
    public void subtract() throws Exception {
        var twoDoubles = this.getInputAsTwoDoubles();
        this.calculator.subtract(twoDoubles.first, twoDoubles.second);
        this.output.accept("subtracted");
    }

    /**
     * Interactively multiplies two values given by user.
     *
     * @throws Exception In case of exception thrown by input.
     */
    public void multiple() throws Exception {
        var twoDoubles = this.getInputAsTwoDoubles();
        this.calculator.multiple(twoDoubles.first, twoDoubles.second);
        this.output.accept("multipled");
    }

    /**
     * Interactively divides two values given by user.
     *
     * @throws Exception In case of exception thrown by input.
     */
    public void div() throws Exception {
        var twoDoubles = this.getInputAsTwoDoubles();
        this.calculator.div(twoDoubles.first, twoDoubles.second);
        this.output.accept("divided");
    }

    /**
     * Prints currently stored result.
     */
    public void printResult() {
        this.output.accept(String.valueOf(
                this.calculator.getResult()));
    }

    /**
     * Is called in case of unknown operation.
     */
    public void unknownOperation() {
        this.output.accept("Unknown operation, try again");
    }

    /**
     * Asks user to input two double values and returns them as one object.
     *
     * @return Object with two doubles.
     * @throws Exception In case of exception thrown by input.
     */
    private TwoDoubles getInputAsTwoDoubles() throws Exception {
        this.output.accept("Enter first value");
        var first = this.getInputAsDouble();
        this.output.accept("Enter second value");
        var second = this.getInputAsDouble();
        return new TwoDoubles(first, second);
    }

    /**
     * Interactively gets double value from user.
     *
     * @return Double value.
     * @throws Exception In case of exception thrown by input.
     */
    private double getInputAsDouble() throws Exception {
        var str = this.input.get();
        while (!(RESULT_VALUE.equals(str) || isDouble(str))) {
            this.output.accept("Wrong value, enter double value, or 'res' to use result:");
            str = this.input.get();
        }
        double result;
        if (RESULT_VALUE.equals(str)) {
            result = this.calculator.getResult();
        } else {
            result = Double.parseDouble(str);
        }
        return result;
    }

    /**
     * Checks if given string represents double value.
     *
     * @param str Given string.
     * @return <tt>true</tt> if represents, <tt>false</tt> if not.
     */
    private boolean isDouble(String str) {
        var regDouble = "^(\\d+|\\d+\\.\\d+)$";
        return str.matches(regDouble);
    }

    /**
     * Class to store two double values.
     */
    private static class TwoDoubles {
        /**
         * First value.
         */
        private double first;
        /**
         * Second value.
         */
        private double second;

        /**
         * Constructs new object.
         *
         * @param first  First value.
         * @param second Second value.
         */
        private TwoDoubles(double first, double second) {
            this.first = first;
            this.second = second;
        }
    }
}
