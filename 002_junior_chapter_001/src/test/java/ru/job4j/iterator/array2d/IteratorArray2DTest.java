package ru.job4j.iterator.array2d;

import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Tests for Iterator2DArray class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 26.02.2018
 */
public class IteratorArray2DTest {

    private Iterator<Integer> itMatrix, itJagged, itWithEmpty;

    @Before
    public void setUp() {
        itMatrix = new IteratorArray2D(new int[][]{{1, 2, 3}, {4, 5, 6}});
        itJagged = new IteratorArray2D(new int[][]{{1}, {3, 4}, {7}});
        itWithEmpty = new IteratorArray2D(new int[][]{{1}, {}, {5, 7}, {}});
    }

    /**
     * Test matrix.
     */
    @Test
    public void matrixHasNextNextSequentialInvocation() {
        assertThat(itMatrix.hasNext(), is(true));
        assertThat(itMatrix.next(), is(1));
        assertThat(itMatrix.hasNext(), is(true));
        assertThat(itMatrix.next(), is(2));
        assertThat(itMatrix.hasNext(), is(true));
        assertThat(itMatrix.next(), is(3));
        assertThat(itMatrix.hasNext(), is(true));
        assertThat(itMatrix.next(), is(4));
        assertThat(itMatrix.hasNext(), is(true));
        assertThat(itMatrix.next(), is(5));
        assertThat(itMatrix.hasNext(), is(true));
        assertThat(itMatrix.next(), is(6));
        assertThat(itMatrix.hasNext(), is(false));
    }

    @Test
    public void matrixTestsThatNextMethodDoesntDependsOnPriorHasNextInvocation() {
        assertThat(itMatrix.next(), is(1));
        assertThat(itMatrix.next(), is(2));
        assertThat(itMatrix.next(), is(3));
        assertThat(itMatrix.next(), is(4));
        assertThat(itMatrix.next(), is(5));
        assertThat(itMatrix.next(), is(6));
    }

    @Test
    public void matrixSequentialHasNextInvocationDoesntAffectRetrievalOrder() {
        assertThat(itMatrix.hasNext(), is(true));
        assertThat(itMatrix.hasNext(), is(true));
        assertThat(itMatrix.next(), is(1));
        assertThat(itMatrix.next(), is(2));
        assertThat(itMatrix.next(), is(3));
        assertThat(itMatrix.next(), is(4));
        assertThat(itMatrix.next(), is(5));
        assertThat(itMatrix.next(), is(6));
    }

    @Test(expected = NoSuchElementException.class)
    public void whenMatrixGetNextAndNoNextThenNoSuchElementException() {
        assertThat(itMatrix.next(), is(1));
        assertThat(itMatrix.next(), is(2));
        assertThat(itMatrix.next(), is(3));
        assertThat(itMatrix.next(), is(4));
        assertThat(itMatrix.next(), is(5));
        assertThat(itMatrix.next(), is(6));
        itMatrix.next();
    }

    /**
     * Test jagged
     */
    @Test
    public void jaggedTestsThatNextMethodDoesntDependsOnPriorHasNextInvocation() {
        assertThat(itJagged.next(), is(1));
        assertThat(itJagged.next(), is(3));
        assertThat(itJagged.next(), is(4));
        assertThat(itJagged.next(), is(7));
    }

    @Test
    public void jaggedSequentialHasNextInvocationDoesntAffectRetrievalOrder() {
        assertThat(itJagged.hasNext(), is(true));
        assertThat(itJagged.hasNext(), is(true));
        assertThat(itJagged.next(), is(1));
        assertThat(itJagged.next(), is(3));
        assertThat(itJagged.next(), is(4));
        assertThat(itJagged.next(), is(7));
    }

    @Test
    public void jaggedHasNextNextSequentialInvocation() {
        assertThat(itJagged.hasNext(), is(true));
        assertThat(itJagged.next(), is(1));
        assertThat(itJagged.hasNext(), is(true));
        assertThat(itJagged.next(), is(3));
        assertThat(itJagged.hasNext(), is(true));
        assertThat(itJagged.next(), is(4));
        assertThat(itJagged.hasNext(), is(true));
        assertThat(itJagged.next(), is(7));
        assertThat(itJagged.hasNext(), is(false));
    }

    @Test(expected = NoSuchElementException.class)
    public void whenJaggedGetNextAndNoNextThenNoSuchElementException() {
        assertThat(itJagged.next(), is(1));
        assertThat(itJagged.next(), is(3));
        assertThat(itJagged.next(), is(4));
        assertThat(itJagged.next(), is(7));
        itJagged.next();
    }

    /**
     * Test array with empty sub-arrays.
     */
    @Test
    public void withEmptyHasNextNextSequentialInvocation() {
        assertThat(itWithEmpty.hasNext(), is(true));
        assertThat(itWithEmpty.next(), is(1));
        assertThat(itWithEmpty.hasNext(), is(true));
        assertThat(itWithEmpty.next(), is(5));
        assertThat(itWithEmpty.hasNext(), is(true));
        assertThat(itWithEmpty.next(), is(7));
        assertThat(itWithEmpty.hasNext(), is(false));
    }

    @Test
    public void withEmptyTestsThatNextMethodDoesntDependsOnPriorHasNextInvocation() {
        assertThat(itWithEmpty.next(), is(1));
        assertThat(itWithEmpty.next(), is(5));
        assertThat(itWithEmpty.next(), is(7));
    }

    @Test
    public void withEmptySequentialHasNextInvocationDoesntAffectRetrievalOrder() {
        assertThat(itWithEmpty.hasNext(), is(true));
        assertThat(itWithEmpty.hasNext(), is(true));
        assertThat(itWithEmpty.next(), is(1));
        assertThat(itWithEmpty.next(), is(5));
        assertThat(itWithEmpty.next(), is(7));
    }

    @Test(expected = NoSuchElementException.class)
    public void whenWithEmptyGetNextAndNoNextThenNoSuchElementException() {
        assertThat(itWithEmpty.next(), is(1));
        assertThat(itWithEmpty.next(), is(5));
        assertThat(itWithEmpty.next(), is(7));
        itWithEmpty.next();
    }

    @Test
    public void whenFirstSubArrayEmptyThenMoveToNeededElementAtStart() {
        Iterator<Integer> it = new IteratorArray2D(new int[][]{{}, {}, {}, {3, 4}, {}});
        assertThat(it.next(), is(3));
        assertThat(it.next(), is(4));
    }

    @Test(expected = NoSuchElementException.class)
    public void whenEmptyArrayThenStillWorksFine() {
        Iterator<Integer> it = new IteratorArray2D(new int[][]{});
        assertThat(it.hasNext(), is(false));
        it.next();
    }


}