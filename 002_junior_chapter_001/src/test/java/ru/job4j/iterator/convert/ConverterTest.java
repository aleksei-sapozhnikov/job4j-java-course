package ru.job4j.iterator.convert;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Tests for Converter class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 27.02.2018
 */
public class ConverterTest {

    private Iterator<Integer> itOfIt;

    @Before
    public void setUp() {
        Iterator<Integer> it1 = Arrays.asList(1, 2, 3).iterator();
        Iterator<Integer> it2 = Arrays.asList(4, 5, 6).iterator();
        Iterator<Integer> it3 = Arrays.asList(7, 8, 9).iterator();
        Iterator<Iterator<Integer>> iterators = Arrays.asList(it1, it2, it3).iterator();
        Converter converter = new Converter();
        itOfIt = converter.convert(iterators);
    }

    /**
     * Tests for next() and hasNext().
     */
    @Test
    public void hasNextNextSequentialInvocation() {
        assertThat(itOfIt.hasNext(), is(true));
        assertThat(itOfIt.next(), is(1));
        assertThat(itOfIt.hasNext(), is(true));
        assertThat(itOfIt.next(), is(2));
        assertThat(itOfIt.hasNext(), is(true));
        assertThat(itOfIt.next(), is(3));
        assertThat(itOfIt.hasNext(), is(true));
        assertThat(itOfIt.next(), is(4));
        assertThat(itOfIt.hasNext(), is(true));
        assertThat(itOfIt.next(), is(5));
        assertThat(itOfIt.hasNext(), is(true));
        assertThat(itOfIt.next(), is(6));
        assertThat(itOfIt.hasNext(), is(true));
        assertThat(itOfIt.next(), is(7));
        assertThat(itOfIt.hasNext(), is(true));
        assertThat(itOfIt.next(), is(8));
        assertThat(itOfIt.hasNext(), is(true));
        assertThat(itOfIt.next(), is(9));
        assertThat(itOfIt.hasNext(), is(false));
    }

    @Test
    public void testsThatNextMethodDoesntDependsOnPriorHasNextInvocation() {
        assertThat(itOfIt.next(), is(1));
        assertThat(itOfIt.next(), is(2));
        assertThat(itOfIt.next(), is(3));
        assertThat(itOfIt.next(), is(4));
        assertThat(itOfIt.next(), is(5));
        assertThat(itOfIt.next(), is(6));
        assertThat(itOfIt.next(), is(7));
        assertThat(itOfIt.next(), is(8));
        assertThat(itOfIt.next(), is(9));
    }

    @Test
    public void sequentialHasNextInvocationDoesntAffectRetrievalOrder() {
        assertThat(itOfIt.hasNext(), is(true));
        assertThat(itOfIt.hasNext(), is(true));
        assertThat(itOfIt.next(), is(1));
        assertThat(itOfIt.next(), is(2));
        assertThat(itOfIt.next(), is(3));
        assertThat(itOfIt.next(), is(4));
        assertThat(itOfIt.next(), is(5));
        assertThat(itOfIt.next(), is(6));
        assertThat(itOfIt.next(), is(7));
        assertThat(itOfIt.next(), is(8));
        assertThat(itOfIt.next(), is(9));
    }

    @Test
    public void hasNextShouldReturnFalseInCaseOfEmptyIterators() {
        Iterator<Integer> it1 = (new ArrayList<Integer>()).iterator();
        Iterator<Integer> it2 = (new ArrayList<Integer>()).iterator();
        Iterator<Integer> it3 = (new ArrayList<Integer>()).iterator();
        Iterator<Iterator<Integer>> its = Arrays.asList(it1, it2, it3).iterator();
        Converter converter = new Converter();
        itOfIt = converter.convert(its);
        assertThat(itOfIt.hasNext(), is(false));
    }

    @Test(expected = NoSuchElementException.class)
    public void invocationOfNextMethodShouldThrowNoSuchElementException() {
        Iterator<Integer> it1 = Arrays.asList(1, 2, 3).iterator();
        Iterator<Iterator<Integer>> its = Arrays.asList(it1).iterator();
        Converter converter = new Converter();
        itOfIt = converter.convert(its);
        assertThat(itOfIt.next(), is(1));
        assertThat(itOfIt.next(), is(2));
        assertThat(itOfIt.next(), is(3));
        itOfIt.next();
    }

    @Test(expected = NoSuchElementException.class)
    public void whenGiverIteratorNullThenIteratorHasNextFalseAndNextThrowsException() {
        Iterator<Integer> it1 = null;
        Iterator<Iterator<Integer>> its = Arrays.asList(it1).iterator();
        Converter converter = new Converter();
        itOfIt = converter.convert(its);
        assertThat(itOfIt.hasNext(), is(false));
        itOfIt.next();
    }


}