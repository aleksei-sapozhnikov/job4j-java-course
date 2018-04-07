package ru.job4j.counter;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

/**
 * Simple counter. Stores and increments value.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 07.04.2018
 */
@ThreadSafe
public class Counter {
    /**
     * Stored value.
     */
    @GuardedBy("this")
    private int value;

    /**
     * Adds 1 to value.
     */
    public synchronized void increment() {
        this.value++;
    }

    /**
     * Returns value.
     *
     * @return stored value.
     */
    public synchronized int get() {
        return this.value;
    }
}
