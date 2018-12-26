package ru.job4j.arrays;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Test for the FindElement class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 10.01.2018
 */
public class FindElementTest {

    /**
     * Test for indexOf method.
     */
    @Test
    public void whenElementIsInArrayThenIndex() {
        FindElement find = new FindElement();
        int[] data = new int[]{1, 3, 5, 7, 4, 2};
        int element = 3;
        int result = find.indexOf(data, element);
        int expected = 1;
        assertThat(result, is(expected));
    }

    @Test
    public void whenNoSuchElementInArrayThenMinusOne() {
        FindElement find = new FindElement();
        int[] data = new int[]{1, 3, 5, 7, 4, 2};
        int element = 12;
        int result = find.indexOf(data, element);
        int expected = -1;
        assertThat(result, is(expected));
    }

}