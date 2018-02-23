package ru.job4j.sort.departments;

import java.util.*;

public class SortDepartments {

    /**
     * Map containing departments.
     * Key value means hierarchy level. The highest level is 0, then 1, 2, 3, etc...
     */
    private Map<Department, Set<Department>> departments = new TreeMap<>();

    /**
     * Analyze string, and add found departments to map.
     *
     * @param str       given string.
     * @param delimiter sign delimiting different departments.
     */
    void addString(String str, String delimiter) {
        if ("\\".equals(delimiter)) {
            delimiter = "\\\\";
        }
        String[] depts = str.split(delimiter);
        if (depts.length > 0) {
            Department head = new Department(depts[0], null);
            this.departments.putIfAbsent(head, new TreeSet<>());
            int index = 1;
            Department parent = head;
            for (; index < depts.length; index++) {
                Department current = new Department(depts[index], parent);
                this.departments.get(head).add(current);
                parent = current;
            }
        }
    }

    void printAll(String delimiter) {
        for (Map.Entry<Department, Set<Department>> entry :this.departments.entrySet()) {
            System.out.println(entry.getKey().hierarchyString(delimiter));
            for (Department dept : entry.getValue()) {
                System.out.println(dept.hierarchyString(delimiter));
            }

        }
    }

}