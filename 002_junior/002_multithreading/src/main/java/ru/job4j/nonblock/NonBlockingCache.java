package ru.job4j.nonblock;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Non-blocking cache of models. Checks version of model and
 * throws Exception if model of older or the same version tries
 * to re-write existing model.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 20.04.2018
 */
@ThreadSafe
class NonBlockingCache<T extends SimpleModel> {
    /**
     * Map containing elements.
     */
    private ConcurrentHashMap<Integer, T> map = new ConcurrentHashMap<>();

    /**
     * Adds new model to cache.
     *
     * @param model model to add.
     * @return <tt>true</tt> if added successfully, <tt>false</tt> if model already exists or found other problems.
     */
    boolean add(final T model) {
        return this.map.putIfAbsent(model.id(), model) == null;
    }

    /**
     * Updates existing model.
     *
     * @param update new model.
     * @return <tt>true</tt> if updated successfully, <tt>false</tt> if not.
     * @throws OptimisticException if new model has older or the same version than existing.
     */
    boolean update(final T update) throws OptimisticException {
        boolean[] changed = {false};
        this.map.computeIfPresent(update.id(), (id, existing) -> {
            T writing = existing;
            if (update.version() > existing.version()) {
                changed[0] = true;
                writing = update;
            }
            return writing;
        });
        if (!changed[0]) {
            throw new OptimisticException("Model for update doesn't have newer version than the existing one.");
        }
        return changed[0];
    }

    /**
     * Deletes model from map.
     *
     * @param id id of the model to delete.
     * @return model associated with given id or <tt>null</tt> if such id was not found.
     */
    T delete(int id) {
        return this.map.remove(id);
    }

    /**
     * * Returns the value to which the specified key is mapped,
     * or <tt>null</tt> if this map contains no mapping for the key.
     *
     * @param id element id.
     * @return element value or <tt>null</tt> if id not found.
     */
    T get(int id) {
        return map.get(id);
    }
}
