package ru.job4j.list.array;

/**
 * Allows to store, add and get elements.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 02.03.2018
 */
public interface SimpleContainer<E> extends Iterable<E> {

    /**
     * Adds new element to the container.
     *
     * @param value element to add.
     */
    void add(E value);

    /**
     * Returns element from the container.
     *
     * @param index index of the element to get.
     * @return element if found or {@code null} if not.
     */
    E get(int index);

}
