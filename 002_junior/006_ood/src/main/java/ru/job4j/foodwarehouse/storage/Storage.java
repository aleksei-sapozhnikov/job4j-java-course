package ru.job4j.foodwarehouse.storage;

import java.util.List;

public interface Storage<E> {
    /**
     * Adds element to storage.
     *
     * @param obj Element to add.
     */
    void add(E obj);

    /**
     * Returns list of all elements from storage.
     *
     * @return List of all elements.
     */
    List<E> getAll();

    /**
     * Clears current storage.
     */
    void clear();
}
