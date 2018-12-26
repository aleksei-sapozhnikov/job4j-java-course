package ru.job4j.generics.store;

/**
 * Can store objects extending Base class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 01.03.2018
 */
public interface Store<T extends Base> {

    /**
     * Adds object to store.
     *
     * @param model object to add.
     */
    void add(T model);

    /**
     * Replaces object in store with another one.
     *
     * @param id    id of the object to replace.
     * @param model new object.
     * @return {@code true} or {@code false} as replacement was successful or the object with id was not found.
     */
    boolean replace(String id, T model);

    /**
     * Deletes object from the store.
     *
     * @param id id of the object to delete.
     * @return {@code true} or {@code false} as deleted successfully or the object with id was not found.
     */
    boolean delete(String id);

    /**
     * Finds object in store by its id.
     *
     * @param id id of the object.
     * @return object with corresponding id or {@code null} if such id was not found in the store.
     */
    T findById(String id);

}
