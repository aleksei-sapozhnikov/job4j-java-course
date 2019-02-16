package ru.job4j.calculator;

/**
 * Simple calculator.
 * Operations: add, subtract, multiply, divide.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 06.01.2018
 */
public class Calculator implements ICalculator {

    /**
     * Result of the operations.
     */
    private double result;

    /**
     * Adds two numbers and stores the value.
     *
     * @param first  First number.
     * @param second Second number.
     */
    @Override
    public void add(double first, double second) {
        this.result = first + second;
    }

    /**
     * Makes subtraction with two numbers and stores the value.
     *
     * @param first  Minuend.
     * @param second Subtrahend.
     */
    @Override
    public void subtract(double first, double second) {
        this.result = first - second;
    }

    /**
     * Multiplies two numbers and stores the value..
     *
     * @param first  First factor.
     * @param second Second factor.
     */
    @Override
    public void multiple(double first, double second) {
        this.result = first * second;
    }

    /**
     * Makes division with two numbers and stores the value..
     *
     * @param first  Dividend.
     * @param second Divisor.
     */
    @Override
    public void div(double first, double second) {
        this.result = first / second;
    }

    /**
     * Returns result.
     *
     * @return Value stored in the @result field.
     */
    @Override
    public double getResult() {
        return this.result;
    }

}
