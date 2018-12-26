package ru.job4j.streams.sort.departments;

import java.util.*;
import java.util.stream.Stream;

/**
 * Node of the hierarchy tree.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 22.02.2018
 */
public class Node implements Comparable<Node> {

    /**
     * Node name.
     */
    private final String name;

    /**
     * Sub-nodes of this node.
     */
    private final Set<Node> subNodes;

    /**
     * Constructor.
     *
     * @param name          node's name.
     * @param subComparator comparator to sort nodes.
     */
    Node(String name, Comparator<Node> subComparator) {
        this.name = name;
        this.subNodes = new TreeSet<>(subComparator);
    }

    /**
     * Add new node as sub-node or get one if exists.
     *
     * @param sub node to add.
     * @return new node if not found equal, or existing node if found.
     */
    Node addOrGetSub(Node sub) {
        return this.subNodes.add(sub) ? sub : this.findSub(sub);
    }

    /**
     * Find node by name.
     *
     * @param sub node to look for.
     * @return node if found or <tt>null</tt> if not found any.
     */
    private Node findSub(Node sub) {
        Optional<Node> result = this.subNodes.stream()
                .filter(temp -> sub.compareTo(temp) == 0)
                .findFirst();
        return result.orElse(null);
    }

    /**
     * Returns array of Strings, showing hierarchy from this node (head) to subsequent (recursive).
     * Each strings corresponds to one of the found nodes.
     *
     * @return array of strings, hierarchy from this node (head) to subsequent nodes.
     */
    String[] toStringArray() {
        Stream<String> ofThis = Stream.of(this.name);
        Stream<String> ofSubs = this.subNodes.stream()
                .flatMap(node -> Arrays.stream(node.subToStringArray(this.name)));
        return Stream.concat(ofThis, ofSubs).toArray(String[]::new);
    }

    /**
     * Return sub-nodes hierarchy (recursive).
     *
     * @param parents parents hierarchy for this node.
     * @return array of strings, each string with hierarchy from this node to all sub-nodes.
     */
    private String[] subToStringArray(String parents) {
        String strThis = String.format("%s\\%s", parents, this.name);
        Stream<String> ofThis = Stream.of(strThis);
        Stream<String> ofSubs = this.subNodes.stream()
                .flatMap(node -> Arrays.stream(node.subToStringArray(strThis)));
        return Stream.concat(ofThis, ofSubs).toArray(String[]::new);
    }

    /**
     * Natural order for the nodes.
     *
     * @param other other node.
     * @return negative, zero or positive integer, as this node is smaller, equal or larger then other node.
     */
    @Override
    public int compareTo(Node other) {
        return this.name.compareTo(other.name);
    }


}