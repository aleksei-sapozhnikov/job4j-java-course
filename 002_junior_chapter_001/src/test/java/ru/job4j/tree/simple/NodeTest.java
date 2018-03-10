package ru.job4j.tree.simple;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class NodeTest {

    /**
     * Test value()
     */
    @Test
    public void whenGetValueThenValue() {
        Node<String> node = new Node<>("111");
        assertThat(node.value(), is("111"));
    }

    /**
     * Test valueIs()
     */
    @Test
    public void whenCheckValueThenTrueIfRightAndViceVersa() {
        Node<Integer> node = new Node<>(1);
        assertThat(node.valueIs(1), is(true));
        assertThat(node.valueIs(2), is(false));
    }

    /**
     * Test add() and children
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void whenAddChildThenRejectsDuplicates() {
        Node<String> parent = new Node<>("111");
        assertThat(parent.add(new Node<>("222")), is(true));
        assertThat(parent.add(new Node<>("222")), is(false));
        assertThat(parent.add(new Node<>("222")), is(false));
        assertThat(parent.add(new Node<>("333")), is(true));
        List<Node<String>> result = parent.children();
        assertThat(result.get(0).valueIs("222"), is(true)); // only one, no duplicates
        assertThat(result.get(1).valueIs("333"), is(true));
        result.get(2);
    }


}