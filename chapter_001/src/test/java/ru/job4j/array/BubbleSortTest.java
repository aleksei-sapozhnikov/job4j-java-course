package ru.job4j.array;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Test for the BubbleSort class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 10.01.2018
 */
public class BubbleSortTest {

    /**
     * Test sort method.
     */
    @Test
    public void whenSortArrayWithFiveElementsThenSortedArray() {
        BubbleSort bubble = new BubbleSort();
        int[] input = new int[]{5, 1, 2, 7, 3};
        int[] result = bubble.sort(input);
        int[] expected = new int[]{1, 2, 3, 5, 7};
        assertThat(result, is(expected));
    }

    @Test
    public void whenSortArrayWithTenElementsThenSortedArray() {
        BubbleSort bubble = new BubbleSort();
        int[] input = new int[]{1, 5, 4, 2, 3, 1, 7, 8, 0, 5};
        int[] result = bubble.sort(input);
        int[] expected = new int[]{0, 1, 1, 2, 3, 4, 5, 5, 7, 8};
        assertThat(result, is(expected));
    }

}