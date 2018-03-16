package ru.job4j.tree.binary;

import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for the BinaryTree class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 11.03.2018
 */
public class BinaryTreeTest {

    /**
     * Test add()
     */
    @Test
    public void whenAddToRootThenBiggerToRightSmallerToLeft() {
        BinaryTree<String> tree = new BinaryTree<>("5");
        tree.add("4");
        tree.add("6");
        assertThat(tree.getElementByRoute(), is("5")); // root
        assertThat(tree.getElementByRoute(true), is("4")); // left leaf
        assertThat(tree.getElementByRoute(false), is("6")); //right leaf
    }

    /**
     * Test add() and structure
     */
    @Test
    public void whenAddElementsTheyAreAddedRecursively() {
        BinaryTree<String> tree = new BinaryTree<>("5");
        tree.add("2"); // left
        tree.add("4"); // right of "2"
        tree.add("3"); // left of "4"
        tree.add("3"); // left of "3"
        assertThat(tree.getElementByRoute(), is("5"));
        assertThat(tree.getElementByRoute(true), is("2"));
        assertThat(tree.getElementByRoute(true, false), is("4"));
        assertThat(tree.getElementByRoute(true, false, true), is("3"));
        assertThat(tree.getElementByRoute(true, false, true, true), is("3"));
    }

    /**
     * Test iterator(), next() and hasNext().
     */
    @Test(expected = NoSuchElementException.class)
    public void whenIteratorThenWorksAsExpected() {
        BinaryTree<String> tree = new BinaryTree<>("4");
        tree.add("2");
        tree.add("6");
        tree.add("3");
        tree.add("6");
        tree.add("8");
        Iterator<String> iterator = tree.iterator();
        assertThat(iterator.hasNext(), Matchers.is(true));
        assertThat(iterator.next(), Matchers.is("4"));       // root
        assertThat(iterator.hasNext(), Matchers.is(true));
        assertThat(iterator.hasNext(), Matchers.is(true));
        assertThat(iterator.hasNext(), Matchers.is(true));
        assertThat(iterator.hasNext(), Matchers.is(true));
        assertThat(iterator.next(), Matchers.is("2"));      // "4" left
        assertThat(iterator.hasNext(), Matchers.is(true));
        assertThat(iterator.next(), Matchers.is("6"));      // "4" right
        assertThat(iterator.hasNext(), Matchers.is(true));
        assertThat(iterator.next(), Matchers.is("3"));      // "2" right (no left)
        assertThat(iterator.hasNext(), Matchers.is(true));
        assertThat(iterator.next(), Matchers.is("6"));      // "6" left
        assertThat(iterator.hasNext(), Matchers.is(true));
        assertThat(iterator.next(), Matchers.is("8"));      // "6" right
        assertThat(iterator.hasNext(), Matchers.is(false));
        assertThat(iterator.hasNext(), Matchers.is(false));
        assertThat(iterator.hasNext(), Matchers.is(false));
        iterator.next();
    }
}