package ru.job4j.problems;

import org.junit.Test;

/**
 * Tests for RacerValue.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 30.03.2018
 */
public class RacerValueTest {

    /**
     * Race condition and mutable object - every run result is different.
     */
    @Test
    public void sameOperationsDifferentResults() throws InterruptedException {
        int times = 10;
        for (int i = 0; i < times; i++) {
            MutableInteger shared = new MutableInteger(3);
            Thread[] threads = new Thread[100];
            for (int num = 0; num < threads.length; num++) {
                threads[num] = new Thread(new RacerValue(shared, num + 1));
            }
            for (Thread thread : threads) {
                thread.start();
            }
            for (Thread thread : threads) {
                thread.join();
            }
            System.out.printf("Попытка №%s, результат: %s%n", i, shared.getValue());
        }
    }
}