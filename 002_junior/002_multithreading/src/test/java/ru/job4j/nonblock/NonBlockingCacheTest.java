package ru.job4j.nonblock;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
     * Try multi-thread cases.
     */
    @Test
    public void multipleThreadsCanReadFromCache() throws InterruptedException {
        int nThreads = 100;
        NonBlockingCache<SimpleModel> cache = new NonBlockingCache<>();
        cache.add(new SimpleModel(1, "one"));
        List<SimpleModel> result = Collections.synchronizedList(new ArrayList<>(nThreads));
        // every thread task
        Runnable task = () -> {
            SimpleModel taken = cache.get(1);
            result.add(taken);
        };
        // run
        Thread[] threads = new Thread[nThreads];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(task);
        }
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
        // expected
        List<SimpleModel> expected = Collections.synchronizedList(new ArrayList<>(nThreads));
        for (int i = 0; i < nThreads; i++) {
            expected.add(new SimpleModel(1, "one"));
        }
        // assert
        assertThat(result, is(expected));
    }

    @Test
    public void onlyFirstThreadUpdatesEntryOthersThrowException() throws InterruptedException {
        int nOthers = 100;
        boolean[] exceptionThrownWhereNeeded = {true};
        NonBlockingCache<SimpleModel> cache = new NonBlockingCache<>();
        cache.add(new SimpleModel(1, "one"));
        // first thread task
        Runnable firstTask = () -> {
            try {
                SimpleModel taken = cache.get(1);
                Thread.sleep(2500);                                 // so others take the same model from cache
                cache.update(taken.changeName("changed by first"));
            } catch (OptimisticException | InterruptedException e) {
                exceptionThrownWhereNeeded[0] = false;
            }
        };
        // other threads task
        Runnable otherTask = () -> {
            try {
                SimpleModel taken = cache.get(1);
                Thread.sleep(5000);                                 // so "first" will be first to write
                cache.update(taken.changeName("changed by other"));
            } catch (InterruptedException | OptimisticException e) {
                System.out.format("%s: InterruptedException or OptimisticException%n", Thread.currentThread().getName());
            }
        };
        // run
        Thread first = new Thread(firstTask);
        Thread[] others = new Thread[nOthers];
        for (int i = 0; i < others.length; i++) {
            others[i] = new Thread(otherTask);
        }
        first.start();
        for (Thread other : others) {
            other.start();
        }
        first.join();
        for (Thread other : others) {
            other.join();
        }
        // assert
        assertThat(exceptionThrownWhereNeeded[0], is(true));
        assertThat(cache.get(1).name(), is("changed by first"));
    }

    @Test
    public void allThreadsGetTheSameObject() throws InterruptedException {
        int nThreads = 100;
        boolean[] allSame = {true};
        NonBlockingCache<SimpleModel> cache = new NonBlockingCache<>();
        SimpleModel added = new SimpleModel(2, "two");
        cache.add(added);
        // every thread task
        Runnable task = () -> {
            SimpleModel taken = cache.get(2);
            if (allSame[0] && taken != added) {
                allSame[0] = false;
            }
        };
        // run
        Thread[] threads = new Thread[nThreads];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(task);
        }
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
        assertThat(allSame[0], is(true));
    }

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