package ru.job4j.array;

import org.junit.Test;

import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import static org.junit.Assert.assertThat;

/**
 * Test for the ArrayDuplicate class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 12.01.2018
 */
public class ArrayDuplicateTest {

    @Test
    public void whenArrayHasDuplicatesThenWithoutDuplicates() {
        ArrayDuplicate duplicate = new ArrayDuplicate();
        String[] input = {"Привет", "Мир", "Привет", "Супер", "Мир"};
        String[] result = duplicate.remove(input);
        String[] expected = {"Привет", "Мир", "Супер"};
        assertThat(result, arrayContainingInAnyOrder(expected));
    }

    @Test
    public void whenArrayHasNoDuplicatesThenTheSameElements() {
        ArrayDuplicate duplicate = new ArrayDuplicate();
        String[] input = {"Вася", "Петя", "Саша", "Маша", "Катя"};
        String[] result = duplicate.remove(input);
        String[] expected = {"Вася", "Петя", "Саша", "Маша", "Катя"};
        assertThat(result, arrayContainingInAnyOrder(expected));
    }


}