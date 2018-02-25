package ru.job4j.sort.departments;

import org.junit.Test;

import java.util.Comparator;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for the HeadNodes class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 24.02.2018
 */
public class SorterTest {

    /**
     * Test parseArray() method.
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
        String[] result = new Sorter().sortArray(input);
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
                "K2\\SK1\\SSK2",
                "K1\\SK1",
                "K2\\SK1\\SSK1",
                "K2"
        };
        String[] result = new Sorter(Comparator.reverseOrder()).sortArray(input);
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