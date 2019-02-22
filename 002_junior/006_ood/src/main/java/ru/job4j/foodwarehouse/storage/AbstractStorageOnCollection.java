package ru.job4j.foodwarehouse.storage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Simple storage based on ArrayList.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class AbstractStorageOnCollection<E> implements Storage<E> {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(AbstractStorageOnCollection.class);

    /**
     * Inner collection storing objects.
     */
    private final Collection<E> collection;

    /**
     * Constructor. Assures we get collection.
     *
     * @param collection Collection to store objects.
     */
    public AbstractStorageOnCollection(Collection<E> collection) {
        this.collection = collection;
    }

    /**
     * Returns inner collection. For subclasses.
     *
     * @return Value of storage field.
     */
    protected Collection<E> getCollection() {
        return this.collection;
    }

    /**
     * Adds element to storage.
     *
     * @param obj Element to add.
     */
    @Override
    public void add(E obj) {
        this.collection.add(obj);
    }

    /**
     * Returns list of all elements from storage.
     *
     * @return List of all elements.
     */
    @Override
    public List<E> getAll() {
        return new ArrayList<>(this.collection);
    }

    /**
     * Clears current storage.
     */
    @Override
    public void clear() {
        this.collection.clear();
    }
}
