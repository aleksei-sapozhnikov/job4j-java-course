package ru.job4j.calculator.decorator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.calculator.ICalculator;
import ru.job4j.util.function.BiConsumerEx;

import java.util.HashMap;
import java.util.Map;

/**
 * Interactive calculator (using Decorator (?) )
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class SimpleCalculator implements ICalc {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(SimpleCalculator.class);
    /**
     * Calculator for doing operations.
     */
    private final ICalculator calculator;
    /**
     * Operations dispatch.
     */
    private final Map<String, BiConsumerEx<Double, Double>> dispatch = new HashMap<>();

    /**
     * Constructor.
     *
     * @param calculator Calculator for doing operations.
     */
    public SimpleCalculator(ICalculator calculator) {
        this.calculator = calculator;
        this.initDispatch();
    }

    /**
     * Calculate using stored result as first operand.
     *
     * @param operation Operation to perform.
     * @param second    Second operand.
     * @return Operation result.
     * @throws Exception In case of problems.
     */
    @Override
    public double calculate(String operation, double second) throws Exception {
        this.dispatch.getOrDefault(operation, this::unknownAction).accept(
                this.calculator.getResult(), second
        );
        return this.calculator.getResult();
    }

    /**
     * Calculate using two given operands.
     *
     * @param operation Operation to perform.
     * @param first     First operand.
     * @param second    Second operand.
     * @return Operation result.
     * @throws Exception In case of problems.
     */
    @Override
    public double calculate(String operation, double first, double second) throws Exception {
        this.dispatch.getOrDefault(operation, this::unknownAction).accept(
                first, second
        );
        return this.calculator.getResult();
    }

    /**
     * Fill dispatch with actions.
     */
    private void initDispatch() {
        this.dispatch.put(ICalc.ADD, this::add);
        this.dispatch.put(ICalc.SUBTRACT, this::subtract);
        this.dispatch.put(ICalc.MULTIPLE, this::multiple);
        this.dispatch.put(ICalc.DIV, this::div);
    }

    /**
     * Performs add.
     *
     * @param first  First arg
     * @param second Second arg.
     */
    private void add(Double first, Double second) {
        this.calculator.add(first, second);
    }

    /**
     * Performs subtract.
     *
     * @param first  First arg
     * @param second Second arg.
     */
    private void subtract(Double first, Double second) throws Exception {
        this.calculator.subtract(first, second);
    }

    /**
     * Performs multiple.
     *
     * @param first  First arg
     * @param second Second arg.
     */
    private void multiple(Double first, Double second) throws Exception {
        this.calculator.multiple(first, second);
    }

    /**
     * Performs div.
     *
     * @param first  First arg
     * @param second Second arg.
     */
    private void div(Double first, Double second) throws Exception {
        this.calculator.div(first, second);
    }

    /**
     * Is called in case of unknown action.
     *
     * @param first  First arg
     * @param second Second arg.
     */
    private void unknownAction(Double first, Double second) throws Exception {
        throw new Exception("Unknown action");
    }
}
