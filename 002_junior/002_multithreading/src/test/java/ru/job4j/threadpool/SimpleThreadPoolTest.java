package ru.job4j.threadpool;

import org.junit.Test;

public class SimpleThreadPoolTest {

    @Test
    public void whenGivenWorksThenWorksAreDone() {
        SimpleThreadPool pool = new SimpleThreadPool();
        // setting works
        Work[] works = new Work[1000];
        for (int i = 0; i < works.length; i++) {
            works[i] = new Work(String.format("work #%s", i));
        }
        // processing
        try {
            pool.start();               // start
            for (Work work : works) {
                pool.add(work);
            }
            Thread.sleep(2000);         //wait
            pool.stop();                // stop
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}