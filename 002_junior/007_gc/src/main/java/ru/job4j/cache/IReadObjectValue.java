package ru.job4j.cache;

/**
 * Interface to read object value by given key.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
@FunctionalInterface
public interface IReadObjectValue<K, V> {
    /**
     * Returns value from the object
     * defined by key.
     *
     * @param key Key to find.
     * @return Object value.
     * @throws Exception In case of problems.
     */
    V read(K key) throws Exception;
}
