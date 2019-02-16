package ru.job4j.calculator.decorator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Engineer calculator as decorator.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class EngineerCalc implements ICalc {
    public static final String SIN = "sin";
    public static final String COS = "cos";
    public static final String TAN = "tan";

    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(EngineerCalc.class);

    /**
     * Inner Calculator.
     */
    private final ICalc calc;

    /**
     * Result stored.
     */
    private double result;

    /**
     * Trigonometry actions dispatch.
     */
    private Map<String, Consumer<Double>> trigonometry = new HashMap<>();

    /**
     * Constructs new instance.
     *
     * @param calc Inner calculator.
     */
    public EngineerCalc(ICalc calc) {
        this.calc = calc;
        this.initTrigonometry();
    }

    /**
     * Calculate given one operand.
     *
     * @param operation Operation to perform.
     * @param second    Second operand.
     * @return Operation result.
     * @throws Exception In case of problems.
     */
    @Override
    public double calculate(String operation, double second) throws Exception {
        Consumer<Double> method = this.trigonometry.get(operation);
        if (method != null) {
            method.accept(second);
        } else {
            this.result = this.calc.calculate(operation, this.result, second);
        }
        return this.result;
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
        this.result = this.calc.calculate(operation, first, second);
        return this.result;
    }

    /**
     * Calculate on result.
     *
     * @param operation Operation to perform.
     * @return Operation result.
     * @throws Exception In case of problems.
     */
    public double calculate(String operation) throws Exception {
        Consumer<Double> method = this.trigonometry.get(operation);
        if (method != null) {
            method.accept(this.result);
        } else {
            throw new Exception("Unknown operation");
        }
        return this.result;
    }

    /**
     * Puts values into trigonometry dispatch.
     */
    private void initTrigonometry() {
        this.trigonometry.put(EngineerCalc.SIN, this::sin);
        this.trigonometry.put(EngineerCalc.COS, this::cos);
        this.trigonometry.put(EngineerCalc.TAN, this::tan);
    }

    /**
     * Calculates sin.
     *
     * @param from Value to calculate for.
     */
    private void sin(Double from) {
        this.result = Math.sin(from);
    }

    /**
     * Calculates cos.
     *
     * @param from Value to calculate for.
     */
    private void cos(Double from) {
        this.result = Math.cos(from);
    }

    /**
     * Calculates tan.
     *
     * @param from Value to calculate for.
     */
    private void tan(Double from) {
        this.result = Math.tan(from);
    }


}
