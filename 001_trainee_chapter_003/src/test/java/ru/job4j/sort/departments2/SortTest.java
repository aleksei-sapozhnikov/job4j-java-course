package ru.job4j.sort.departments2;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for the Sort class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 25.02.2018
 */
public class SortTest {

    /**
     * Test sortArray(String array)
     */
    @Test
    public void whenArrayThenNewArrayNaturalOrder() {
        String[] input = {
                "K1\\SK1\\SSK2",
                "K1\\SK2",
                "K1\\SK1\\SSK1",
                "K2\\SK1\\SSK2",
                "K1\\SK1",
                "K2\\SK1\\SSK1",
                "K2"
        };
        String[] result = new Sort(new ComparatorHierarchic(true)).sortArray(input);
        String[] expected = {
                "K1",
                "K1\\SK1",
                "K1\\SK1\\SSK1",
                "K1\\SK1\\SSK2",
                "K1\\SK2",
                "K2",
                "K2\\SK1",
                "K2\\SK1\\SSK1",
                "K2\\SK1\\SSK2"
        };
        assertThat(result, is(expected));
    }

    @Test
    public void whenArrayThenNewArrayReverseOrder() {
        String[] input = {
                "K1\\SK1\\SSK2",
                "K1\\SK2",
                "K1\\SK1\\SSK1",
                "K1\\SK1",
                "K2\\SK1\\SSK2",
                "K2",
                "K2\\SK1\\SSK1"
        };
        String[] result = new Sort(new ComparatorHierarchic(false)).sortArray(input);
        String[] expected = {
                "K2",
                "K2\\SK1",
                "K2\\SK1\\SSK2",
                "K2\\SK1\\SSK1",
                "K1",
                "K1\\SK2",
                "K1\\SK1",
                "K1\\SK1\\SSK2",
                "K1\\SK1\\SSK1"
        };
        assertThat(result, is(expected));
    }
}