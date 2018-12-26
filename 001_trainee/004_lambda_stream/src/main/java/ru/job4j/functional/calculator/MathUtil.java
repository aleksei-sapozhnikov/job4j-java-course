package ru.job4j.functional.calculator;

/**
 * Sample util class with static methods.
 * <p>
 * For use in functional interface showing.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public class MathUtil {

    /**
     * Returns sum of two values.
     *
     * @param left  First value.
     * @param right Second value.
     * @return Sum of two values.
     */
    public static double add(int left, int right) {
        return left + right;
    }

    /**
     * Returns subtraction of two values.
     *
     * @param left  First value.
     * @param right Second value.
     * @return Subtraction of two values.
     */
    public static double subt(int left, int right) {
        return left - right;
    }

    /**
     * Returns multiplication of two values.
     *
     * @param left  First value.
     * @param right Second value.
     * @return Multiplication of two values.
     */
    public static double mult(int left, int right) {
        return left * right;
    }

    /**
     * Returns division of two values.
     *
     * @param left  First value.
     * @param right Second value.
     * @return Division of two values.
     */
    public static double div(int left, int right) {
        return (double) left / right;
    }
}
