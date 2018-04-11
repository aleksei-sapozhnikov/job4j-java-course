package ru.job4j.producerconsumer;

import net.jcip.annotations.ThreadSafe;

/**
 * Simple producer for a simple blocking queue.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 11.04.2018
 */
@ThreadSafe
public class Producer extends Thread {
    /**
     * Queue to add elements to.
     */
    private final SimpleBlockingQueue<Integer> queue;

    /**
     * Constructs new producer to given queue.
     *
     * @param queue queue to add elements to.
     */
    Producer(SimpleBlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    /**
     * Runs producer. His work is to produce numbers from 0 to 50 and to add
     * them into the queue.
     */
    @Override
    public void run() {
        try {
            int length = 50;
            for (int i = 0; i < length; i++) {
                this.queue.offer(i);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

