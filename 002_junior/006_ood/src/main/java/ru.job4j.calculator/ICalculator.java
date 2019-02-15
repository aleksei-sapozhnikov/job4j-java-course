package ru.job4j.calculator;

public interface ICalculator {
    /**
     * Adds two numbers and stores the value.
     *
     * @param first  First number.
     * @param second Second number.
     */
    void add(double first, double second);

    /**
     * Makes subtraction with two numbers and stores the value.
     *
     * @param first  Minuend.
     * @param second Subtrahend.
     */
    void subtract(double first, double second);

    /**
     * Multiplies two numbers and stores the value..
     *
     * @param first  First factor.
     * @param second Second factor.
     */
    void multiple(double first, double second);

    /**
     * Makes division with two numbers and stores the value..
     *
     * @param first  Dividend.
     * @param second Divisor.
     */
    void div(double first, double second);

    /**
     * Returns result.
     *
     * @return Value stored in the @result field.
     */
    double getResult();
}
