package ru.job4j.tree.binary;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * Simple binary tree. Can add and store elements.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 11.03.2018
 */
public class BinaryTree<E extends Comparable> implements Iterable<E> {
    /**
     * Root of the tree.
     */
    private Node<E> root;

    /**
     * Constructs a new tree with given root element.
     *
     * @param rootValue value associated with the root element.
     */
    BinaryTree(E rootValue) {
        this.root = new Node<>(rootValue, null, null);
    }

    /**
     * Adds new value to the tree.
     *
     * @param value value to add.
     */
    void add(E value) {
        this.addNode(
                this.root,
                new Node<>(value, null, null));
    }

    /**
     * Adds new node to the node leaf (if possible) or goes to the next leaf (left or right).
     * The metod is recursive.
     *
     * @param current node to which we are adding the new node (if possible).
     * @param adding  node which is added to the tree.
     */
    @SuppressWarnings("unchecked")
    private void addNode(Node<E> current, Node<E> adding) {
        if (current.value.compareTo(adding.value) >= 0) {
            if (current.left != null) {
                this.addNode(current.left, adding);
            } else {
                current.left = adding;
            }
        } else {
            if (current.right != null) {
                this.addNode(current.right, adding);
            } else {
                current.right = adding;
            }
        }
    }

    /**
     * Just for tests. Goes through the tree from the root using given route.
     * If route is empty - returns root element.
     * Each element of the route means "go to the left leaf (yes/no).
     * E.g.: way "true-false-true" means (go to left leaf, then to right leaf, then to left and return element found".
     *
     * @param leftLeaves way to go: <tt>true</tt> means go to left leaf, <tt>false</tt> - go to the right leaf.
     * @return element found when completed the route.
     */
    E getElementByRoute(boolean... leftLeaves) {
        Node<E> current = this.root;
        for (boolean toLeft : leftLeaves) {
            current = toLeft ? current.left : current.right;
        }
        return current.value;
    }

    /**
     * Returns an iterator over elements of type <tt>E</tt>.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {

            /**
             * Queue of the elements to return.
             */
            Queue<Node<E>> data = new LinkedList<>();

            {
                data.offer(root);
            }

            /**
             * Returns {@code true} if the iteration has more elements.
             * (In other words, returns {@code true} if {@link #next} would
             * return an element rather than throwing an exception.)
             *
             * @return {@code true} if the iteration has more elements, {@code false} otherwise.
             */
            @Override
            public boolean hasNext() {
                return !data.isEmpty();
            }

            /**
             * Returns the next node value in the tree.
             *
             * @return the next value contained in the tree.
             * @throws NoSuchElementException if the iteration has no more elements.
             */
            @Override
            public E next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                Node<E> result = data.poll();
                if (result.left != null) {
                    data.offer(result.left);
                }
                if (result.right != null) {
                    data.offer(result.right);
                }
                return result.value;
            }
        };
    }

    /**
     * Node for the binary tree.
     *
     * @param <E> generic parameter - value of the node.
     */
    private static class Node<E> {
        /**
         * Left leaf.
         */
        private Node<E> left;
        /**
         * Right leaf.
         */
        private Node<E> right;
        /**
         * Value associated with the node.
         */
        private E value;

        /**
         * Constructs a new node with given value and empty leaves
         *
         * @param value value of the node.
         * @param left  left leaf.
         * @param right right leaf.
         */
        Node(E value, Node<E> left, Node<E> right) {
            this.value = value;
            this.left = left;
            this.right = right;
        }
    }
}
