package ru.job4j.arrays;

import org.junit.Test;

import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import static org.junit.Assert.assertThat;

/**
 * Test for the RemoveDyplicates class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 12.01.2018
 */
public class RemoveDyplicatesTest {

    @Test
    public void whenArrayHasDuplicatesThenWithoutDuplicates() {
        RemoveDyplicates duplicate = new RemoveDyplicates();
        String[] input = {"Привет", "Мир", "Привет", "Супер", "Мир"};
        String[] result = duplicate.remove(input);
        String[] expected = {"Привет", "Мир", "Супер"};
        assertThat(result, arrayContainingInAnyOrder(expected));
    }

    @Test
    public void whenArrayHasNoDuplicatesThenTheSameElements() {
        RemoveDyplicates duplicate = new RemoveDyplicates();
        String[] input = {"Вася", "Петя", "Саша", "Маша", "Катя"};
        String[] result = duplicate.remove(input);
        String[] expected = {"Вася", "Петя", "Саша", "Маша", "Катя"};
        assertThat(result, arrayContainingInAnyOrder(expected));
    }


}