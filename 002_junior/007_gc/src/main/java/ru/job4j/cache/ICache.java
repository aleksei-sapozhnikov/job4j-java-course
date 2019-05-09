package ru.job4j.cache;

/**
 * Cache for objects.
 *
 * @param <K> Key type.
 * @param <V> Value type.
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public interface ICache<K, V> {
    /**
     * Returns value defined by given key:
     * either one saved in cache or value
     * read from object defined by the key.
     *
     * @param key Key to find.
     * @return Object corresponding to the key.
     * @throws Exception In case of problems.
     */
    V get(K key) throws Exception;
}
