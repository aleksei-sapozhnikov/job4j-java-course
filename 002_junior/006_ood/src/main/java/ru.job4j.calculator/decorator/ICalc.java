package ru.job4j.calculator.decorator;

/**
 * Simple calculator.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public interface ICalc {
    String ADD = "add";
    String SUBTRACT = "subtract";
    String MULTIPLE = "multiple";
    String DIV = "div";

    /**
     * Calculate using stored result as first operand.
     *
     * @param operation Operation to perform.
     * @param second    Second operand.
     * @return Operation result.
     * @throws Exception In case of problems.
     */
    double calculate(String operation, double second) throws Exception;

    /**
     * Calculate using two given operands.
     *
     * @param operation Operation to perform.
     * @param first     First operand.
     * @param second    Second operand.
     * @return Operation result.
     * @throws Exception In case of problems.
     */
    double calculate(String operation, double first, double second) throws Exception;
}
