package ru.job4j.calculator;

import ru.job4j.util.function.ConsumerEx;
import ru.job4j.util.function.SupplierEx;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Interactive calculator.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class InteractCalc {
    /**
     * Message written to user on application start.
     */
    public static final String GREETING_MSG = "Enter command: ";
    /**
     * Message to user on application exit.
     */
    public static final String EXITING_MSG = "Exiting...";
    /**
     * Command to exit the app.
     */
    public static final String EXIT = "exit";
    /**
     * Using this as one of numbers means using last result value.
     */
    public static final String RES = "res";
    /**
     * Result output message format.
     */
    public static final String RESULT_MSG_FORMAT = "result: %s";

    /**
     * Calculator performing actions.
     */
    private final Calculator calculator;
    /**
     * Supplier of user commands.
     */
    private final SupplierEx<String> in;
    /**
     * Consumer of to-user messages.
     */
    private final ConsumerEx<String> out;

    /**
     * Dispatch of calculation operations.
     */
    private final Map<String, Function<String[], String>> actionDispatch = new HashMap<>();

    /**
     * Constructs new instance.
     *
     * @param calculator Calculator tp perform actions.
     * @param in         Supplier of user commands.
     * @param out        Consumer of to-user messages.
     */
    public InteractCalc(final Calculator calculator, final SupplierEx<String> in, final ConsumerEx<String> out) {
        this.calculator = calculator;
        this.in = in;
        this.out = out;
        this.initActionDispatch();
    }

    /**
     * Puts values into the action dispatch.
     */
    private void initActionDispatch() {
        this.actionDispatch.put("+", this::add);
        this.actionDispatch.put("-", this::subtract);
        this.actionDispatch.put("*", this::multiple);
        this.actionDispatch.put("/", this::div);
        this.actionDispatch.put("res", s -> this.getResult());
    }

    /**
     * Starts interactive work.
     *
     * @throws Exception In case of problems in reading from input or
     *                   writing to output.
     */
    public void start() throws Exception {
        this.writeOut(GREETING_MSG);
        var input = this.readIn();
        while (!(EXIT.equals(input))) {
            String result;
            try {
                result = this.process(input);
            } catch (Exception e) {
                this.printException(e);
                result = this.getResult();
            }
            this.writeOut(String.format(RESULT_MSG_FORMAT, result));
            input = this.readIn();
        }
        this.writeOut(EXITING_MSG);
    }

    /**
     * Does actions needed by input and returns result.
     *
     * @param input Input string.
     * @return Result of the actions.
     * @throws Exception In case of invalid arguments.
     */
    private String process(final String input) throws Exception {
        var args = this.getArgs(input);
        this.validateArgs(args);
        var action = this.getAction(args);
        return this.actionDispatch.get(action).apply(args);
    }

    /**
     * Prints exception to user.
     *
     * @param e Exception thrown.
     */
    private void printException(Exception e) throws Exception {
        var stackLast = e.getStackTrace()[0];
        this.writeOut(String.format(
                "Exception happened%n   --class: %s%n   --method: %s%n   --exc-class: %s%n   --exc-message: %s%nProbably wrong command, try again",
                stackLast.getClassName(), stackLast.getMethodName(), e.getClass().getName(), e.getMessage()));
    }

    /**
     * Splits input string into array of arguments.
     *
     * @param input Input string.
     * @return Array of arguments.
     */
    private String[] getArgs(final String input) {
        return input.split("\\s+");
    }

    /**
     * Throws exception in case of invalid arguments.
     *
     * @param args Arguments array.
     * @throws Exception If one or more arguments invalid.
     */
    private void validateArgs(String[] args) throws Exception {
        if (!(this.argsValid(args))) {
            throw new Exception("Wrong input");
        }
    }

    /**
     * Checks if input arguments are valid.
     *
     * @param args Input arguments array.
     * @return <tt>true</tt> if arguments valid, <tt>false</tt> if not.
     */
    private boolean argsValid(final String[] args) {
        var regDouble = "^(\\d+|\\d+\\.\\d+)$";
        return args.length == 3
                && (RES.equals(args[0]) || args[0].matches(regDouble))
                && actionDispatch.containsKey(args[1])
                && (RES.equals(args[2]) || args[2].matches(regDouble));
    }

    /**
     * Returns calculation operation given by arguments.
     *
     * @param args Arguments array.
     * @return Operation to use in action dispatch.
     */
    private String getAction(String[] args) {
        return args[1];
    }

    /**
     * Reads user input.
     *
     * @return User input string.
     */
    private String readIn() throws Exception {
        return this.in.get();
    }

    /**
     * Writes output to user.
     *
     * @param data Data to write.
     */
    private void writeOut(String data) throws Exception {
        this.out.accept(data);
    }

    /**
     * Performs add and returns result.
     *
     * @param args Input arguments.
     * @return Add result.
     */
    private String add(final String[] args) {
        return this.calculate(args, this.calculator::add);
    }

    /**
     * Performs subtract and returns result.
     *
     * @param args Input arguments.
     * @return Subtract result.
     */
    private String subtract(final String[] args) {
        return this.calculate(args, this.calculator::subtract);
    }

    /**
     * Performs multiple and returns result.
     *
     * @param args Input arguments.
     * @return Multiple result.
     */
    private String multiple(final String[] args) {
        return this.calculate(args, this.calculator::multiple);
    }

    /**
     * Performs division and returns result.
     *
     * @param args Input arguments.
     * @return Division result.
     */
    private String div(final String[] args) {
        return this.calculate(args, this.calculator::div);
    }

    /**
     * Returns current result stored in the calculator.
     *
     * @return Current result stored.
     */
    private String getResult() {
        return String.valueOf(this.calculator.getResult());
    }

    /**
     * Performs common operations for calculation methods.
     *
     * @param args         Input arguments.
     * @param calcFunction Calculator function to use in operation.
     * @return Calculation result.
     */
    private String calculate(final String[] args, final BiConsumer<Double, Double> calcFunction) {
        var first = RES.equals(args[0]) ? this.calculator.getResult() : Double.valueOf(args[0]);
        var second = RES.equals(args[2]) ? this.calculator.getResult() : Double.valueOf(args[2]);
        calcFunction.accept(first, second);
        return String.valueOf(this.calculator.getResult());
    }

}
