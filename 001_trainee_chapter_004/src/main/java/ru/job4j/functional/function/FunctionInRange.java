package ru.job4j.functional.function;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Implements counting function in given range of values.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public class FunctionInRange {

    /**
     * Returns list of function values in given argument range.
     *
     * @param first First value in range (inclusive).
     * @param last  Last value in range (inclusive).
     * @param func  Function to apply to arguments.
     * @return List of function values.
     */
    public List<Double> valuesInRange(int first, int last, Function<Integer, Double> func) {
        List<Double> result = new ArrayList<>();
        for (int current = first; current <= last; current++) {
            result.add(
                    func.apply(current)
            );
        }
        return result;
    }
}
