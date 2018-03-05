package ru.job4j.set.hashset;

import java.util.StringJoiner;

/**
 * Allows to store and add unique elements.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 05.03.2018
 */
public class SimpleHashSet<E> {

    /**
     * Default size for the new set.
     */
    private static final int DEFAULT_NUMBER_OF_BUCKETS = 16;

    /**
     * Container for elements.
     */
    private E[] buckets;

    /**
     * Modifications count (to prevent concurrent modification in iterator).
     */
    private int modCount = 0;

    /**
     * Constructs set with given number of buckets.
     *
     * @param nBuckets initial number of buckets in the hash table.
     */
    @SuppressWarnings("unchecked")
    public SimpleHashSet(int nBuckets) {
        this.buckets = (E[]) new Object[nBuckets];
    }

    /**
     * Constructs set with default number of buckets.
     */
    public SimpleHashSet() {
        this(DEFAULT_NUMBER_OF_BUCKETS);
    }

    /**
     * Adds the specified element to this set if it is not already present.
     *
     * @param value element to be added to this set.
     * @return {@code true} if this set did not already contain the specified element,
     * {@code false} otherwise.
     */
    public boolean add(E value) {
        boolean adding = !this.contains(value);
        if (adding) {
            try {
                int index = Math.abs(value.hashCode() % this.buckets.length);
                this.checkCollision(index);
                this.buckets[index] = value;
                this.modCount++;
            } catch (Exception e) {
                e.printStackTrace();
                adding = false;
            }
        }
        return adding;
    }

    /**
     * Throws an exception if there already is element in the bucket.
     *
     * @param index index of the bucket to check.
     * @throws Exception when found non-null element in the bucket.
     */
    private void checkCollision(int index) throws Exception {
        if (this.buckets[index] != null) {
            throw new Exception("Hashcode collision happened");
        }
    }

    /**
     * Checks if this set contains the specified element.
     *
     * @param value element whose presence in this set is to be tested
     * @return {@code true} if this set contains the specified element, {@code false} if not.
     */
    public boolean contains(E value) {
        int index = value.hashCode() % this.buckets.length;
        return value.equals(this.buckets[index]);
    }

    /**
     * Removes given value from the set.
     *
     * @param value element whose presence in this set is to be tested
     * @return {@code true} if succesfully removed, {@code false} if element was not found.
     */
    public boolean remove(E value) {
        boolean removing = this.contains(value);
        if (removing) {
            int index = value.hashCode() % this.buckets.length;
            this.buckets[index] = null;
        }
        return removing;
    }

    /**
     * Changes number of buckets in the hastable of the set.
     * Also moves elements to the new hashtable.
     *
     * @param newNumber new number of buckets.
     */
    @SuppressWarnings("unchecked")
    void changeNumberOfBuckets(int newNumber) {
        E[] newBuckets = (E[]) new Object[newNumber];
        for (E temp : this.buckets) {
            if (temp != null) {
                int newIndex = temp.hashCode() % newBuckets.length;
                newBuckets[newIndex] = temp;
            }
        }
        this.buckets = newBuckets;
    }

    /**
     * Returns an array of elements contained in buckets.
     */
    @Override
    public String toString() {
        StringJoiner buffer = new StringJoiner(System.lineSeparator());
        for (int i = 0; i < this.buckets.length; i++) {
            buffer.add(
                    String.format("bucket %s: %s", i, this.buckets[i])
            );
        }
        return buffer.toString();
    }

}
