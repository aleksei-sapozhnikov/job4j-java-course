package ru.job4j.producerconsumer;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class SimpleBlockingQueueTest {

    @Test
    public void whenOfferElementsThenPollThem() throws InterruptedException {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>();
        List<Integer> result = new ArrayList<>();
        Producer producer = new Producer(queue);
        Consumer consumer = new Consumer(queue, result);
        //expected
        List<Integer> expected = new ArrayList<Integer>() {
            {
                for (int i = 0; i < 50; i++) {
                    add(i * 2);
                }
            }
        };
        //run
        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
        assertThat(result, is(expected));

    }
}