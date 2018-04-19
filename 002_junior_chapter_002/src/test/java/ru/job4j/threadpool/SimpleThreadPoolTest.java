package ru.job4j.threadpool;

import com.sun.corba.se.spi.orbutil.threadpool.Work;
import org.junit.Test;

public class SimpleThreadPoolTest {

    @Test
    public void whenGivenWorksThenWorksAreDone() {
        SimpleThreadPool pool = new SimpleThreadPool();
        // setting works
        Work[] works = new Work[1000];
        for (int i = 0; i < works.length; i++) {
            int finalI = i;
            works[i] = new Work() {
                @Override
                public void doWork() {
                    long start = System.currentTimeMillis();
                    int count = 0;
                    while (System.currentTimeMillis() - start < 1000) {
                        count++;
                    }
                }

                @Override
                public void setEnqueueTime(long timeInMillis) {
                    // stub
                }

                @Override
                public long getEnqueueTime() {
                    return 0;
                }

                @Override
                public String getName() {
                    return String.format("work #%s", finalI);
                }
            };
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