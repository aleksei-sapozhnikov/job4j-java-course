package ru.job4j.set.onlist;

import ru.job4j.list.array.SimpleArrayList;

public class SimpleSetOnArrayList<E> extends AbstractSetOnSimpleContainer<E> {

    /**
     * Default size for the new set.
     */
    private static final int DEFAULT_SIZE = 10;

    /**
     * Constructs set with array list as a container, with given initial size.
     *
     * @param size initial size of the array list.
     */
    public SimpleSetOnArrayList(int size) {
        super(new SimpleArrayList<>(size));
    }

    /**
     * Constructs set with array list as a container, with default initial size.
     */
    public SimpleSetOnArrayList() {
        this(DEFAULT_SIZE);
    }
}
