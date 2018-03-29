package ru.job4j.interrupt;

import org.junit.Test;

public class TimeTest {

    @Test
    public void notATest() throws InterruptedException {
        String input = "Работа";
        Thread time = new Thread(new Time(input, 2500));
        time.start();
        time.join();
    }
}