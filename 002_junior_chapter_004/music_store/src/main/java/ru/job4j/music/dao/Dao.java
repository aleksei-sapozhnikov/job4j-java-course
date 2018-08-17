package ru.job4j.music.dao;

import java.util.List;

public interface Dao<E> {
    /**
     * Creates new entity in database.
     *
     * @param entity Entity to add.
     * @return Added entity from database.
     */
    E add(E entity);

    /**
     * Returns entity from database by key.
     *
     * @param key Entity identifier.
     * @return Entity from database or empty entity if not found.
     */
    E get(int key);

    /**
     * Updates entity in database.
     *
     * @param key       Key of the entity to update.
     * @param newEntity Entity with updated fields.
     * @return Updated entity from database.
     */
    E update(int key, E newEntity);

    /**
     * Deletes entity from database.
     *
     * @param key Identifier of the entity to delete.
     * @return Deleted entity as it was found in database.
     */
    E delete(int key);

    /**
     * Returns list of all entities.
     *
     * @return List with all entity objects found from database.
     */
    List<E> getAll();

}
