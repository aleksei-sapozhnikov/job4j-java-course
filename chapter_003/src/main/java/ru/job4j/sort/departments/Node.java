package ru.job4j.sort.departments;

import java.util.*;

/**
 * Node of the hierarchy tree.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 24.02.2018
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
     * @param name       node's name.
     * @param comparator comparator to sort nodes.
     */
    Node(String name, Comparator<Node> comparator) {
        this.name = name;
        this.subNodes = new TreeSet<>(comparator);
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
     * @return node if found or null if not found any.
     */
    private Node findSub(Node sub) {
        Node result = null;
        for (Node temp : this.subNodes) {
            if (sub.compareTo(temp) == 0) {
                result = temp;
                break;
            }
        }
        return result;
    }

    /**
     * Returns array of Strings, showing hierarchy from this node (head) to subsequent (recursive).
     * Each strings corresponds to one of the found nodes.
     *
     * @return array of strings, hierarchy from this node (head) to subsequent nodes.
     */
    String[] toStringArray() {
        List<String> buffer = new LinkedList<>();
        buffer.add(this.name);
        for (Node node : this.subNodes) {
            buffer.addAll(Arrays.asList(node.subToStringArray(this.name)));
        }
        return buffer.toArray(new String[buffer.size()]);
    }

    /**
     * Return sub-nodes hierarchy (recursive).
     *
     * @param parents parents hierarchy for this node.
     * @return array of strings, each string with hierarchy from this node to all sub-nodes.
     */
    private String[] subToStringArray(String parents) {
        List<String> buffer = new LinkedList<>();
        buffer.add(String.format("%s\\%s", parents, this.name)
        );
        for (Node node : this.subNodes) {
            buffer.addAll(Arrays.asList(node.subToStringArray(String.format("%s\\%s", parents, this.name))));
        }
        return buffer.toArray(new String[buffer.size()]);
    }

    /**
     * Compare this node and other node.
     *
     * @param other other node.
     * @return negative, zero or positive integer, as this node is smaller, equal or larger then other node.
     */
    @Override
    public int compareTo(Node other) {
        return this.name.compareTo(other.name);
    }


}