package ru.job4j.tree.simple;

import java.util.*;

public class SimpTree<E extends Comparable<E>> implements SimpleTree<E> {

    /**
     * Root - the first element of the tree.
     */
    private Node<E> root;

    /**
     * Constructs a tree with a root associated with given value.
     *
     * @param rootValue value of the first root element.
     */
    SimpTree(E rootValue) {
        this.root = new Node<>(rootValue);
    }

    /**
     * Add sub-element (child) to parent element.
     * One parent can have many children elements.
     * All children must be unique. If adding duplicate, it is rejected.
     *
     * @param parent parent element.
     * @param child  child element to add.
     * @return <tt>true</tt> if added, <tt>false</tt> if duplicate child element found or root not found.
     */
    @Override
    public boolean add(E parent, E child) {
        Node<E> par;
        Optional<Node<E>> found = this.findBy(parent);
        boolean result = found.isPresent();
        if (result) {
            par = found.get();
            result = par.add(new Node<>(child));
        }
        return result;
    }

    /**
     * Finds node by its value.
     *
     * @param value value to look for.
     * @return node associated with the value or empty node if not found any.
     */
    @Override
    public Optional<Node<E>> findBy(E value) {
        Optional<Node<E>> result = Optional.empty();
        Queue<Node<E>> data = new LinkedList<>();
        data.offer(this.root);
        while (!data.isEmpty()) {
            Node<E> node = data.poll();
            if (node.valueIs(value)) {
                result = Optional.of(node);
                break;
            }
            for (Node<E> child : node.children()) {
                data.offer(child);
            }
        }
        return result;
    }

    public boolean isBinary() {
        boolean result = true;
        Queue<Node<E>> data = new LinkedList<>();
        data.offer(this.root);
        while (!data.isEmpty()) {
            Node<E> node = data.poll();
            if (node.children().size() > 2) {
                result = false;
                break;
            }
            for (Node<E> child : node.children()) {
                data.offer(child);
            }
        }
        return result;
    }

    /**
     * Returns an iterator over nodes in the tree.
     *
     * @return an Iterator over nodes.
     */
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {

            /**
             * Queue of the tree's elements. When becomes empty - means no more elements.
             */
            private Queue<Node<E>> data = new LinkedList<>();

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
                for (Node<E> child : result.children()) {
                    data.offer(child);
                }
                return result.value();
            }
        };
    }
}
