package ru.job4j.map.hashmap;

import javafx.util.Pair;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.StringJoiner;

/**
 * Simple hash map realisation.
 * Can store only one element in one bucket.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 07.03.2018
 */
public class SimpleHashMap<K, V> implements Iterable<Pair<K, V>> {

    /**
     * Maximum percent of occupied buckets.
     * If exceeded, the buckets array with keys will grow up.
     */
    private static final int LOAD_PERCENT_THRESHOLD = 75;

    /**
     * Array of buckets, containing map entries.
     */
    private Pair<K, V>[] buckets;

    /**
     * Number of buckets filled with keys (and values).
     */
    private int filledBuckets = 0;

    /**
     * Modifications count (to prevent concurrent modification in iterator).
     */
    private int modCount = 0;

    /**
     * Constructs new SimpleHashMap with given number of buckets.
     *
     * @param size number of buckets.
     */
    @SuppressWarnings("unchecked")
    SimpleHashMap(int size) {
        this.buckets = new Pair[size];
    }

    /**
     * Returns index of the bucket where the object must be stored,
     * calculated by the object's hashcode.
     *
     * @param hash     object's hashcode.
     * @param nBuckets number of buckets in the map.
     * @return index of the bucket for the object.
     */
    private int bucketIndex(int hash, int nBuckets) {
        return hash % nBuckets;
    }

    /**
     * Insert new element into map
     *
     * @param key   entry key.
     * @param value value associated with the key.
     * @return <tt>true</tt> if added or <tt>false</tt> if collision happened (bucket already occupied).
     */
    boolean insert(K key, V value) {
        int index = this.bucketIndex(key.hashCode(), this.buckets.length);
        boolean result = this.buckets[index] == null
                || key.equals(this.buckets[index].getKey());
        if (result) {
            this.buckets[index] = new Pair<>(key, value);
            this.modCount++;
            this.filledBuckets++;
        }
        growIfNeeded();
        return result;
    }

    /**
     * Checks that the map is not too full, otherwise makes it bigger,
     */
    private void growIfNeeded() {
        if (this.filledBuckets * 100 / this.buckets.length > LOAD_PERCENT_THRESHOLD) {
            grow();
        }
    }

    /**
     * Makes the map bigger.
     */
    @SuppressWarnings("unchecked")
    private void grow() {
        int newLength = this.buckets.length * 3 / 2 + 1;
        Pair<K, V>[] newBuckets = new Pair[newLength];
        for (Pair<K, V> entry : this.buckets) {
            if (entry != null) {
                int newIndex = this.bucketIndex(entry.getKey().hashCode(), newLength);
                newBuckets[newIndex] = entry;
            }
        }
        this.buckets = newBuckets;
    }

    /**
     * Returns value corresponding to the key.
     *
     * @param key entry key.
     * @return value corresponding to the key or <tt>null</tt> if key not found.
     */
    V get(K key) {
        V result = null;
        int index = this.bucketIndex(key.hashCode(), this.buckets.length);
        if (this.buckets[index] != null
                && key.equals(this.buckets[index].getKey())) {
            result = this.buckets[index].getValue();
        }
        return result;
    }

    /**
     * Deletes value corresponding to the key.
     *
     * @param key entry key.
     * @return <tt>true</tt> if deleted successfully or <tt>false</tt> if element not found.
     */
    boolean delete(K key) {
        int index = this.bucketIndex(key.hashCode(), this.buckets.length);
        boolean result = this.buckets[index] != null
                && key.equals(this.buckets[index].getKey());
        if (result) {
            this.buckets[index] = null;
            this.modCount++;
            this.filledBuckets--;
        }
        return result;
    }

    /**
     * Returns current map state as string.
     *
     * @return string showing current map state.
     */
    @Override
    public String toString() {
        StringJoiner buffer = new StringJoiner(System.lineSeparator());
        for (int i = 0; i < this.buckets.length; i++) {
            boolean isNull = this.buckets[i] == null;
            buffer.add(String.format(
                    "bucket #%s: %s = %s",
                    i,
                    isNull ? null : this.buckets[i].getKey(),
                    isNull ? null : this.buckets[i].getValue()
            ));
        }
        return buffer.toString();
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<Pair<K, V>> iterator() {
        return new Iterator<Pair<K, V>>() {

            /**
             * Modification count value at the time when the iterator was created.
             * If collection will change during the work of iterator, there will be {@code }ConcurrentModificationException}.
             */
            private final int expectedModCount = modCount;

            /**
             * Pointer moving over buckets.
             * Shows number of next bucket whose element to return.
             */
            private int cursor = 0;

            /**
             * Returns {@code true} if the iteration has more elements.
             * (In other words, returns {@code true} if {@link #next} would
             * return an element rather than throwing an exception.)
             *
             * @return {@code true} if the iteration has more elements
             * @throws ConcurrentModificationException if map was modified during the work of the iterator.
             */
            @Override
            public boolean hasNext() {
                if (modCount != this.expectedModCount) {
                    throw new ConcurrentModificationException();
                }
                return cursor < buckets.length;
            }

            /**
             * Returns the next element in the iteration.
             *
             * @return the next element in the iteration
             * @throws NoSuchElementException if the iteration has no more elements
             */
            @Override
            public Pair<K, V> next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                Pair<K, V> result = new Pair<>(buckets[this.cursor].getKey(), buckets[this.cursor].getValue());
                moveCursor();
                return result;
            }

            /**
             * Moves cursor to the next bucket with non-null entry.
             */
            private void moveCursor() {
                cursor++;
                while (this.cursor < buckets.length && buckets[cursor] == null) {
                    cursor++;
                }
            }
        };

    }
}
