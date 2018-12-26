package ru.job4j.streams.list;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Compare two List objects.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 14.02.2018
 */
public class ListCompare implements Comparator<List<Integer>> {

    /**
     * Compare two lists.
     * <p>
     * Из задания мне не удалось понять, по какому условию мы сравниваем листы.
     * Лично я могу придумать несколько равноправных вариантов.
     * Поэтому воспользовался тем, что предлагали ранее в чате.
     * <p>
     * Павел Корчагин Feb 1, 12:24 PM
     * концепция такая, мы сравниваем элементы пока не дойдем до первой пары различающихся
     * сравниваем их и прекращаем сравнение
     *
     * @param left  our list.
     * @param right other list.
     * @return positive integer, negative integer or zero, as
     * left list is bigger, smaller or equal to right list.
     */
    @Override
    public int compare(List<Integer> left, List<Integer> right) {
        Iterator<Integer> iLeft = left.iterator();
        Iterator<Integer> iRight = right.iterator();
        int result = 0;
        while (result == 0 && iLeft.hasNext() && iRight.hasNext()) {
            result = Integer.compare(iLeft.next(), iRight.next());
        }
        if (result == 0 && iLeft.hasNext() && !iRight.hasNext()) {
            result = 1;
        }
        if (result == 0 && iRight.hasNext() && !iLeft.hasNext()) {
            result = -1;
        }
        return result;
    }
}
