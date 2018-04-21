package ru.job4j.nonblock;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for NonBlockingCache class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 21.04.2018
 */
public class NonBlockingCacheTest {

    /**
     * Test add()
     */
    @Test
    public void whenAddNonExistingIdThenTrue() {
        NonBlockingCache<SimpleModel> cache = new NonBlockingCache<>();
        assertThat(cache.add(new SimpleModel(1, "one")), is(true));
        assertThat(cache.add(new SimpleModel(2, "two")), is(true));
        assertThat(cache.add(new SimpleModel(3, "two")), is(true));
    }

    @Test
    public void whenAddExistingIdThenFalseAndValueNotChanged() {
        NonBlockingCache<SimpleModel> cache = new NonBlockingCache<>();
        assertThat(cache.add(new SimpleModel(1, "one")), is(true));
        assertThat(cache.add(new SimpleModel(1, "one")), is(false));
        assertThat(cache.add(new SimpleModel(1, "two")), is(false));
        assertThat(cache.get(1).name(), is("one"));
    }

    /**
     * Test get()
     */
    @Test
    public void whenGetElementThenTheElementOrNullIfNotFound() {
        NonBlockingCache<SimpleModel> cache = new NonBlockingCache<>();
        cache.add(new SimpleModel(2, "two"));
        cache.add(new SimpleModel(5, "five"));
        cache.add(new SimpleModel(3, "three"));
        SimpleModel resultTwo = cache.get(2);
        SimpleModel resultThree = cache.get(3);
        SimpleModel resultFive = cache.get(5);
        SimpleModel resultSix = cache.get(6);
        assertThat(resultTwo.name(), is("two"));
        assertThat(resultThree.name(), is("three"));
        assertThat(resultFive.name(), is("five"));
        assertThat(resultSix, is((SimpleModel) null));
    }

    /**
     * Test delete()
     */
    @Test
    public void whenDeleteExistingThenReturnItsValueAndDeleteFromMap() {
        NonBlockingCache<SimpleModel> cache = new NonBlockingCache<>();
        cache.add(new SimpleModel(1, "one"));
        cache.add(new SimpleModel(4, "four"));
        cache.add(new SimpleModel(6, "six"));
        cache.add(new SimpleModel(8, "eight"));
        assertThat(cache.get(4).name(), is("four"));
        assertThat(cache.delete(4).name(), is("four"));
        assertThat(cache.get(4), is((SimpleModel) null));
    }

    @Test
    public void whenDeleteNonExistingThenFalse() {
        NonBlockingCache<SimpleModel> cache = new NonBlockingCache<>();
        cache.add(new SimpleModel(4, "four"));
        assertThat(cache.get(4).name(), is("four"));
        assertThat(cache.delete(2), is((SimpleModel) null));
        assertThat(cache.get(4).name(), is("four"));
    }

    /**
     * Test update()
     */
    @Test
    public void whenUpdateNewerVersionThenSetNewValue() throws OptimisticException {
        NonBlockingCache<SimpleModel> cache = new NonBlockingCache<>();
        cache.add(new SimpleModel(4, "four"));
        SimpleModel taken = cache.get(4);
        SimpleModel modified = taken.changeName("new four");
        assertThat(cache.update(modified), is(true));
        assertThat(cache.get(4).name(), is("new four"));
    }

    @Test(expected = OptimisticException.class)
    public void whenUpdateOlderVersionThenThrowOptimisticException() throws OptimisticException {
        NonBlockingCache<SimpleModel> cache = new NonBlockingCache<>();
        cache.add(new SimpleModel(5, "five"));
        SimpleModel modified2x = cache.get(5).changeName("new five").changeName("new new five");
        SimpleModel modified1x = cache.get(5).changeName("other five");
        assertThat(cache.update(modified2x), is(true));
        assertThat(cache.get(5).name(), is("new new five"));
        cache.update(modified1x);
    }


}