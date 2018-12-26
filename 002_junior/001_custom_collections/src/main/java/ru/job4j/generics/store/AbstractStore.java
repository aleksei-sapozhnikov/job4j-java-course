package ru.job4j.generics.store;

import ru.job4j.generics.simplelist.SimpleList;

/**
 * Store of some objects, based on list.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 01.03.2018
 */
public abstract class AbstractStore<T extends Base> implements Store<T> {

    /**
     * Default initial capacity of the store.
     */
    private static final int DEFAULT_CAPACITY = 10;

    /**
     * Storage of the objects.
     */
    private SimpleList<T> store = new SimpleList<>(DEFAULT_CAPACITY);

    /**
     * Adds Base object to store.
     *
     * @param model object to add.
     */
    @Override
    public void add(T model) {
        this.store.add(model);
    }

    /**
     * Replaces Base object in store with another one.
     *
     * @param id    id of the object to replace.
     * @param model new object.
     * @return {@code true} or {@code false} as replacement was successful or the object with id was not found.
     */
    @Override
    public boolean replace(String id, T model) {
        boolean result;
        T old = this.findById(id);
        if (old != null) {
            this.store.set(this.store.indexOf(old), model);
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    /**
     * Deletes Base object from the store.
     *
     * @param id id of the object to delete.
     * @return {@code true} or {@code false} as deleted successfully or the object with id was not found.
     */
    @Override
    public boolean delete(String id) {
        boolean result;
        T obj = this.findById(id);
        if (obj != null) {
            this.store.delete(this.store.indexOf(obj));
            result = true;
        } else {
            result = false;
        }
        return result;
    }


    /**
     * Finds Base object in store by its id.
     *
     * @param id id of the object.
     * @return object with corresponding id or {@code null} if such id was not found in the store.
     */
    @Override
    public T findById(String id) {
        T result = null;
        for (T temp : this.store) {
            if (id.equals(temp.id())) {
                result = temp;
                break;
            }
        }
        return result;
    }

}
