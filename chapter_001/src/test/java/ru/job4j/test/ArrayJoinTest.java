package ru.job4j.test;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Test for ArrayJoin class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 14.01.2018
 */
public class ArrayJoinTest {

    /**
     * Test joinSortedAscending method.
     */
    @Test
    public void whenTwoArrayThenUnitedArray() {
        ArrayJoin join = new ArrayJoin();
        int[] inputFirst = {1, 3, 5, 7, 9};
        int[] inputSecond = {2, 3, 6, 8, 10};
        int[] result = join.joinSortedAscending(inputFirst, inputSecond);
        int[] expected = {1, 2, 3, 3, 5, 6, 7, 8, 9, 10};
        assertThat(result, is(expected));
    }

    @Test
    public void whenHighestElementInTheSecondArrayThenUnitedArray() {
        ArrayJoin join = new ArrayJoin();
        int[] inputFirst = {1, 3, 5, 7, 9};
        int[] inputSecond = {12};
        int[] result = join.joinSortedAscending(inputFirst, inputSecond);
        int[] expected = {1, 3, 5, 7, 9, 12};
        assertThat(result, is(expected));
    }

    @Test
    public void whenLowestElementInTheSecondArrayThenUnitedArray() {
        ArrayJoin join = new ArrayJoin();
        int[] inputFirst = {3, 5, 7, 9};
        int[] inputSecond = {2};
        int[] result = join.joinSortedAscending(inputFirst, inputSecond);
        int[] expected = {2, 3, 5, 7, 9};
        assertThat(result, is(expected));
    }

    @Test
    public void whenSecondArrayIsEmptyThenArrayEqualToFirstArray() {
        ArrayJoin join = new ArrayJoin();
        int[] inputFirst = {1, 3, 5, 7, 9};
        int[] inputSecond = {};
        int[] result = join.joinSortedAscending(inputFirst, inputSecond);
        int[] expected = {1, 3, 5, 7, 9};
        assertThat(result, is(expected));
    }

    @Test
    public void whenFirstArrayIsEmptyThenArrayEqualToSecondArray() {
        ArrayJoin join = new ArrayJoin();
        int[] inputFirst = {};
        int[] inputSecond = {2, 3, 6, 8, 10};
        int[] result = join.joinSortedAscending(inputFirst, inputSecond);
        int[] expected = {2, 3, 6, 8, 10};
        assertThat(result, is(expected));
    }

    @Test
    public void whenTwoEmptyArraysThenEmptyArray() {
        ArrayJoin join = new ArrayJoin();
        int[] inputFirst = {};
        int[] inputSecond = {};
        int[] result = join.joinSortedAscending(inputFirst, inputSecond);
        int[] expected = {};
        assertThat(result, is(expected));
    }

}