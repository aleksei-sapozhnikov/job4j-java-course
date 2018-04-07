package ru.job4j.counter.increment;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for Counter class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 07.04.2018
 */
public class CounterTest {

    /**
     * Test multi-thread behaviour.
     */
    @Test
    public void whenExecute2ThreadThenResultIs2() throws InterruptedException {
        final Counter counter = new Counter();
        Thread first = new ThreadCount(counter);
        Thread second = new ThreadCount(counter);
        first.start();
        second.start();
        first.join();
        second.join();
        assertThat(counter.get(), is(2));
    }

    /**
     * Test get() and increment()
     */
    @Test
    public void whenIncrementThenValueIncrementsByOne() {
        Counter counter = new Counter();
        assertThat(counter.get(), is(0));
        counter.increment();
        assertThat(counter.get(), is(1));
        counter.increment();
        counter.increment();
        counter.increment();
        counter.increment();
        assertThat(counter.get(), is(5));
    }

    /**
     * Class for thread with Counter.
     */
    private class ThreadCount extends Thread {
        /**
         * Counter object.
         */
        private final Counter counter;

        /**
         * Constructs new thread with Counter object.
         *
         * @param counter Counter object to use.
         */
        private ThreadCount(final Counter counter) {
            this.counter = counter;
        }

        /**
         * Runs thread.
         */
        @Override
        public void run() {
            this.counter.increment();
        }
    }
}