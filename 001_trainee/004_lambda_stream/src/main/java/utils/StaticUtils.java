package utils;

import java.util.Iterator;
import java.util.List;

/**
 * Some static methods which would be useful to complete tasks.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public class StaticUtils {

    /**
     * Function comparing two arrays of Double. If corresponding values in arrays
     *
     * @param listFirst  First array.
     * @param listSecond Second Array.
     * @param delta      Maximum difference between two values to say they are equal.
     * @return <tt>true</tt> if arrays have equal values, <tt>false</tt> if not.
     */
    public static boolean haveEqualValues(List<Double> listFirst, List<Double> listSecond, double delta) {
        boolean result = listFirst.size() == listSecond.size();
        if (result) {
            Iterator<Double> first = listFirst.iterator();
            Iterator<Double> second = listSecond.iterator();
            while (first.hasNext()) {
                result = Math.abs((first.next() - second.next())) < delta;
                if (!result) {
                    break;
                }
            }
        }
        return result;
    }
}
