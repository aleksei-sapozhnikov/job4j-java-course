package ru.job4j.sort.departments2;

import java.util.*;

/**
 * Sort given array of Strings, saving hierarchy.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 25.02.2018
 */
public class Sort {

    /**
     * Contains all departments with hierarchy.
     * Each department is presented by a list of strings.
     * Rejects duplicates (i.e. departments with the same name and hierarchy).
     */
    private Set<List<String>> departments;

    /**
     * Constructor.
     *
     * @param comparator comparator for departments.
     */
    Sort(Comparator<List<String>> comparator) {
        this.departments = new TreeSet<>(comparator);
    }

    /**
     * Sorts given array and returns sorted with added hierarchy elements (if needed).
     *
     * @param array given array of strings.
     * @return sorted array of strings with added hierarchy elements (if needed).
     */
    String[] sortArray(String[] array) {
        this.addArray(array);
        return this.toArray();
    }

    /**
     * Parse array of hierarchy strings and add all of them.
     *
     * @param array given array of strings.
     */
    private void addArray(String[] array) {
        for (String str : array) {
            this.addString(str);
        }
    }

    /**
     * Add all elements found from one hierarchy string.
     *
     * @param given string to add.
     */
    private void addString(String given) {
        this.departments.addAll(
                this.parseString(given)
        );
    }

    /**
     * Parse given string and find all departments from it.
     *
     * @param given string to parse.
     * @return list of all found departments.
     */
    private List<List<String>> parseString(String given) {
        final List<List<String>> result = new LinkedList<>();
        final String[] departments = given.split("\\\\");
        for (int current = 0; current < departments.length; current++) {
            final List<String> element = new LinkedList<>();
            element.addAll(Arrays.asList(departments).subList(0, current + 1));
            result.add(element);
        }
        return result;
    }

    /**
     * Convert stored departments into array of strings.
     *
     * @return array of strings, sorted as defined by this.comparator.
     */
    private String[] toArray() {
        final List<String> result = new LinkedList<>();
        this.departments.forEach(list -> {
                    StringJoiner buffer = new StringJoiner("\\");
                    list.forEach(buffer::add);
                    result.add(buffer.toString());
                }
        );
        return result.toArray(new String[0]);
    }
}
