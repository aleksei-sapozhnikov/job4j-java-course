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
     * When percent of buckets filled with elements is more than this, the number of buckets must be enlarged.
     */
    private static final int MAX_PERCENT_OF_BUCKETS_FILLED = 75;

    /**
     * Container for elements.
     */
    private E[] buckets;

    /**
     * Number of buckets filled with elements.
     */
    private int filledBuckets = 0;

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
        growIfNeeded();
        boolean adding = !this.contains(value);
        if (adding) {
            try {
                int index = Math.abs(value.hashCode() % this.buckets.length);
                this.checkCollision(index);
                this.buckets[index] = value;
                this.filledBuckets++;
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
     * Checks if the hash table in the set is full (most of the buckets are filled).
     *
     * @return {@code true} if hash table is full enough, {@code false} otherwise.
     */
    private boolean isFull() {
        return this.buckets.length > 0
                && this.filledBuckets * 100 / this.buckets.length > MAX_PERCENT_OF_BUCKETS_FILLED;
    }

    /**
     * Checks if there is need to grow bucket and makes it grow if needed.
     */
    private void growIfNeeded() {
        if (this.isFull()) {
            this.changeNumberOfBuckets(this.buckets.length * 3 / 2 + 1);
        }
    }

    /**
     * Changes number of buckets in the hast able of the set.
     * Also moves elements to the new hash table.
     *
     * @param newNumber new number of buckets.
     */
    @SuppressWarnings("unchecked")
    private void changeNumberOfBuckets(int newNumber) {
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
            this.filledBuckets--;
        }
        return removing;
    }

    /**
     * Prints current status of set and elements contained.
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
