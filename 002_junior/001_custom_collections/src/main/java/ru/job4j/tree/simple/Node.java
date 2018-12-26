package ru.job4j.tree.simple;

import java.util.ArrayList;
import java.util.List;

/**
 * Node of the simple tree.
 * Can have a list of children nodes.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 10.03.2018
 */
class Node<E extends Comparable<E>> {

    /**
     * List of this node's children nodes.
     */
    private final List<Node<E>> children = new ArrayList<>();
    /**
     * Object associated with this node.
     */
    private final E value;

    /**
     * Constructs a new node associated with object value.
     *
     * @param value object value.
     */
    Node(final E value) {
        this.value = value;
    }

    /**
     * Get this node's value.
     *
     * @return node's "value" field value.
     */
    E value() {
        return this.value;
    }

    /**
     * Add new child node to this node.
     * Rejects duplicates.
     *
     * @param child child node.
     */
    public boolean add(Node<E> child) {
        boolean result = true;
        for (Node<E> node : this.children) {
            if (child.valueIs(node.value)) {
                result = false;
                break;
            }
        }
        if (result) {
            this.children.add(child);
        }
        return result;
    }

    /**
     * Returns list of this node's children nodes.
     *
     * @return list of children nodes.
     */
    public List<Node<E>> children() {
        return this.children;
    }

    /**
     * Checks if value associated with this node is equal to given.
     *
     * @param value value to compare with.
     * @return <tt>true</tt> if values are equal, <tt>false</tt> otherwise.
     */
    public boolean valueIs(E value) {
        return this.value.compareTo(value) == 0;
    }
}