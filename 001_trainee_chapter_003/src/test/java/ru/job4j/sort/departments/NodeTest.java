package ru.job4j.sort.departments;

import org.junit.Test;

import java.util.Comparator;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for the Node class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 25.02.2018
 */
public class NodeTest {

    /**
     * Test addOrGetSubNode()
     */
    @Test
    public void whenAddAbsentSubThenAddAndReturnThisSub() {
        Node node = new Node("Node", Comparator.naturalOrder());
        Node sub = new Node("SubNode", Comparator.naturalOrder());
        Node resultOne = node.addOrGetSub(sub);
        String[] resultTwo = node.toStringArray();
        assert (resultOne == sub);
        assertThat(resultTwo, is(new String[]{"Node", "Node\\SubNode"}));
    }

    @Test
    public void whenAddExistingSubThenNoAddAndReturnExistingSub() {
        Node node = new Node("Node", Comparator.naturalOrder());
        Node existent = new Node("SubNode", Comparator.naturalOrder());
        Node same = new Node("SubNode", Comparator.naturalOrder());
        node.addOrGetSub(existent);
        Node resultOne = node.addOrGetSub(same);
        String[] resultTwo = node.toStringArray();
        assert (resultOne == existent);
        assertThat(resultTwo, is(new String[]{"Node", "Node\\SubNode"}));
    }

    /**
     * Test toStringArray()
     */
    @Test
    public void whenToStringArrayThenRightStringArray() {
        Node node = new Node("Node", Comparator.naturalOrder());
        Node sub1 = new Node("Sub1", Comparator.naturalOrder());
        Node sub2 = new Node("Sub2", Comparator.naturalOrder());
        Node sub1SubSub1 = new Node("SubSub1", Comparator.naturalOrder());
        Node sub1SubSub2 = new Node("SubSub2", Comparator.naturalOrder());
        Node sub2SubSub1 = new Node("SubSub1", Comparator.naturalOrder());
        node.addOrGetSub(sub1);
        node.addOrGetSub(sub2);
        sub1.addOrGetSub(sub1SubSub1);
        sub1.addOrGetSub(sub1SubSub2);
        sub2.addOrGetSub(sub2SubSub1);
        String[] result = node.toStringArray();
        String[] expected = {
                "Node",
                "Node\\Sub1",
                "Node\\Sub1\\SubSub1",
                "Node\\Sub1\\SubSub2",
                "Node\\Sub2",
                "Node\\Sub2\\SubSub1"
        };
        assertThat(result, is(expected));
    }

    /**
     * Test compareTo()
     */
    @Test
    public void whenTheSameNameThenCompareIsZero() {
        Node left = new Node("Node", Comparator.naturalOrder());
        Node right = new Node("Node", Comparator.naturalOrder());
        assertThat(left.compareTo(right), is(0));
    }

    @Test
    public void whenSwitchNodesThenChangesResultSign() {
        Node left = new Node("Left", Comparator.naturalOrder());
        Node right = new Node("Right", Comparator.naturalOrder());
        boolean result = left.compareTo(right) == -(right.compareTo(left));
        assertThat(result, is(true));
    }

    @Test
    public void whenLeftIsSmallerThenResultPositive() {
        Node left = new Node("Node1", Comparator.naturalOrder());
        Node right = new Node("Node2", Comparator.naturalOrder());
        boolean result = left.compareTo(right) < 0;
        assertThat(result, is(true));
    }
}