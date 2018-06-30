package ru.job4j.crud.logic;

import java.util.List;

/**
 * Logic layer for objects store. Validates each object before adding it to storage.
 *
 * @param <T> object to store.
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public interface Validator<T> {

    /**
     * Validates and adds new object to store.
     *
     * @param model Object to add.
     * @return Unique id given to the object by the
     * storage system or <tt>-1</tt> if object is not
     * valid or if store couldn't add it.
     */
    int add(T model);

    /**
     * Tries to update object with given id using new fields
     * values from the given object. If the result is valid,
     * updates object in the storage, otherwise leaves it untouched.
     *
     * @param id        Id of the object to update.
     * @param newFields Object with new fields values.
     * @return <tt>true</tt> if the object was updated, <tt>false</tt> if not.
     */
    boolean update(int id, T newFields);

    /**
     * Deletes object with given id.
     *
     * @param id Id of the object to delete.
     * @return Deleted object if deleted suucessfull, <tt>null</tt> if not.
     */
    T delete(int id);

    /**
     * Returns array of all objects stored now.
     *
     * @return Array of objects stored now.
     */
    List<T> findAll();

    /**
     * Finds object in the storage by id.
     *
     * @param id Id of the needed object.
     * @return Object with given id if found, <tt>null</tt> if not found.
     */
    T findById(int id);

    /**
     * Clears currently existing storage structure and creates it again.
     */
    void clear();

    /**
     * Closes all resources opened by this store.
     */
    void close() throws Exception;
}
