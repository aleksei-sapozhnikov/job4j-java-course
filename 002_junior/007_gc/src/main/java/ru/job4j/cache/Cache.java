package ru.job4j.cache;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Cache for objects using Soft References to HashMap.
 *
 * @param <K> Key type.
 * @param <V> Value type.
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
@ThreadSafe
public class Cache<K, V> implements ICache<K, V> {
    /**
     * Reader of object value (if it is not cached).
     */
    private final IReadObjectValue<K, V> readObjectValue;
    /**
     * Map of cached values.
     */
    @GuardedBy("this")
    private volatile SoftReference<Map<K, V>> cached = new SoftReference<>(new ConcurrentHashMap<>());

    /**
     * Constructor.
     *
     * @param readObjectValue Reader of object value.
     */
    public Cache(IReadObjectValue<K, V> readObjectValue) {
        this.readObjectValue = readObjectValue;
    }

    /**
     * Returns value for the given key: cached
     * if found in cache, or the one read from
     * object and saved to cache.
     *
     * @param key Key to find.
     * @return Value to given key.
     * @throws Exception In case of problems reading value.
     */
    @Override
    public V get(K key) throws Exception {
        V result;
        var current = this.getStrongReferenceMap();
        if (current.containsKey(key)) {
            result = current.get(key);
        } else {
            result = this.readObjectValue.read(key);
            current.put(key, result);
        }
        return result;
    }

    /**
     * Returns reference to inner storage
     * of cached entries if the storage exists.
     * <p>
     * Otherwise creates new storage
     * and return reference to it.
     *
     * @return Strong
     */
    private Map<K, V> getStrongReferenceMap() {
        var current = this.cached.get();
        if (current == null) {
            synchronized (this) {
                current = this.cached.get();
                if (current == null) {
                    current = new ConcurrentHashMap<>();
                    this.cached = new SoftReference<>(current);
                }
            }
        }
        return current;
    }
}
