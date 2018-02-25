package ru.job4j.sort.departments2;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Implements natural order comparator, but respecting hierarchy.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 25.02.2018
 */
public class ComparatorHierarchic implements Comparator<List<String>> {

    /**
     * Compare as natural or reverse order?
     * true == natural, false == reverse.
     */
    private boolean natural;

    /**
     * Constructor.
     *
     * @param natural use natural or reverse sorting order?
     *                true: natural, false: reverse.
     */
    ComparatorHierarchic(boolean natural) {
        this.natural = natural;
    }

    /**
     * Natural order with hierarchy.
     *
     * @param left  left list.
     * @param right right list.
     * @return positive negative or zero integer, as left list is bigger, smaller or equal to right list.
     */
    @Override
    public int compare(List<String> left, List<String> right) {
        Iterator<String> itLeft = left.iterator();
        Iterator<String> itRight = right.iterator();
        int result = 0;
        while (result == 0 && itLeft.hasNext() && itRight.hasNext()) {
            result = this.natural
                    ? itLeft.next().compareTo(itRight.next())
                    : itRight.next().compareTo(itLeft.next());
        }
        if (result == 0 && itLeft.hasNext()) {
            result = 1;
        }
        if (result == 0 && itRight.hasNext()) {
            result = -1;
        }
        return result;
    }
}
