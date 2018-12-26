package ru.job4j.interrupt;

import org.junit.Test;

/**
 * Tests for the Time class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 28.03.2018
 */
public class TimeTest {

    /**
     * Not a test, demonstrating multithreading.
     */
    @Test
    public void notATest() throws InterruptedException {
        String input = "Работа";
        Thread time = new Thread(new Time(input, 2500));
        time.start();
        time.join();
    }
}