package ru.job4j.functional.calculator;

import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * Simple calculator to show how functional interfaces work.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public class Calculator {

    /**
     * Performs operation in given range of values and stores results into given media.
     *
     * @param firstStart  First value of 1st argument range (inclusive).
     * @param firstFinish Last value of 1st argument range (inclusive).
     * @param second      Second argument value.
     * @param operation   Operation to perform.
     * @param media       Consumer to write results into.
     */
    public void calculate(int firstStart, int firstFinish, int second,
                          BiFunction<Integer, Integer, Double> operation,
                          Consumer<Double> media
    ) {
        for (int first = firstStart; first <= firstFinish; first++) {
            media.accept(
                    operation.apply(first, second)
            );
        }
    }

}