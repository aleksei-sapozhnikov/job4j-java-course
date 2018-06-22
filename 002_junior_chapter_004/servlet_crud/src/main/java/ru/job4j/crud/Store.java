package ru.job4j.crud;

/**
 * Storage for objects. Each object is identified by integer id given by
 * the storage as the result of the "add" operation.
 *
 * @param <T> object to store.
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public interface Store<T> {

    /**
     * Adds new object to the storage.
     *
     * @param model Object to add.
     * @return Unique id given to the object by the
     * storage system or <tt>-1</tt> if couldn't add it.
     */
    int add(T model);

    /**
     * Updates object fields.
     *
     * @param newModel object with the same unique id as of the object
     *                 being updated and with new fields values.
     * @return <tt>true</tt> if updated successfully, <tt>false</tt> if not
     * (e.g. object with this id was not found).
     */
    boolean update(T newModel);

    /**
     * Deletes object with given id from the storage.
     *
     * @param id Id of the object to delete.
     * @return Deleted object if found and deleted, <tt>null</tt> if not.
     */
    T delete(int id);

    /**
     * Returns object with given id.
     *
     * @param id Id of the needed object.
     * @return Object with given id or <tt>null</tt> if object not found.
     */
    T findById(int id);

    /**
     * Returns array of all objects stored in the storage.
     *
     * @return Array of stored objects.
     */
    T[] findAll();
}
