package ru.job4j.threadpool;

import com.sun.corba.se.spi.orbutil.threadpool.Work;
import org.junit.Test;

public class SimpleThreadPoolTest {

    @Test
    public void whenGivenWorksThenWorksAreDone() {
        SimpleThreadPool pool = new SimpleThreadPool();
        // setting works
        Work[] works = new Work[25];
        for (int i = 0; i < works.length; i++) {
            int finalI = i;
            works[i] = new Work() {
                @Override
                public void doWork() {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void setEnqueueTime(long timeInMillis) {
                    //
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
        // starting pool, adding works
        pool.start();
        try {
            for (Work work : works) {
                Thread.sleep(150);
                pool.add(work);
            }
            // wait
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // stop
        pool.stop();
    }
}