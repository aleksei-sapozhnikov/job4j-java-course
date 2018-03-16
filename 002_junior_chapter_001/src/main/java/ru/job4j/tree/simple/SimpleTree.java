package ru.job4j.tree.simple;

import java.util.Optional;

/**
 * Simple tree interface.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 10.03.2018
 */
public interface SimpleTree<E extends Comparable<E>> extends Iterable<E> {

    /**
     * Add sub-element (child) to parent element.
     * One parent can have many children elements.
     * All children must be unique. If adding duplicate, it is rejected.
     *
     * @param parent parent element.
     * @param child  child element to add.
     * @return <tt>true</tt> if added, <tt>false</tt> if duplicate child element found.
     */
    boolean add(E parent, E child);

    /**
     * Finds node by its value.
     *
     * @param value value to look for.
     * @return node associated with the value or <tt>null</tt> if not found any.
     */
    Optional<Node<E>> findBy(E value);
}