package ru.job4j.interrupt;

import org.junit.Test;

public class CountTimeTest {

    @Test
    public void notATest() throws InterruptedException {
        String input = "Работа";
        Thread time = new Thread(new CountTime(input, 2500));
        time.start();
        time.join();
    }
}