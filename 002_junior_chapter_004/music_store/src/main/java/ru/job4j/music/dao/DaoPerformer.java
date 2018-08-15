package ru.job4j.music.dao;

import java.util.List;

public interface DaoPerformer<E> {
    /**
     * Creates new entity in database.
     */
    public E add(E entity);

    /**
     * Returns entity from database by key.
     */
    public E get(int key);

    /**
     * Updates entity in database.
     */
    public E update(E newEntity);

    /**
     * Deletes entity from database.
     */
    public E delete(int key);

    /**
     * Returns list of all entities.
     */
    public List<E> getAll();

}
