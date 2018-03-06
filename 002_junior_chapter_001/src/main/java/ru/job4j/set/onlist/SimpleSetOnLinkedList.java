package ru.job4j.set.onlist;

import ru.job4j.list.linked.SimpleLinkedList;

public class SimpleSetOnLinkedList<E> extends AbstractSetOnSimpleContainer<E> {

    /**
     * Constructs set with linked list as a container,.
     */
    public SimpleSetOnLinkedList() {
        super(new SimpleLinkedList<>());
    }

}
