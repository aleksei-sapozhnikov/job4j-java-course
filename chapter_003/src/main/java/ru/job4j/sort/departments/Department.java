package ru.job4j.sort.departments;

public class Department {

    /**
     * Department over this.
     * null for the head department.
     */
    private Department parent;

    /**
     * Name of the department.
     */
    private String name;

    /**
     * Constructor.
     *
     * @param parent department over this.
     */
    Department(String name, Department parent) {
        this.name = name;
        this.parent = parent;
    }

    /**
     * Return string which shows all departments hierarchy from the highest to this.
     *
     * @param delimiter sign delimiting parent department from child.
     * @return string of departments hierarchy.
     */
    public String hierarchyString(String delimiter) {
        Department temp = this;
        StringBuilder buffer = new StringBuilder(temp.name);
        while (temp.parent != null) {
            temp = temp.parent;
            buffer.insert(0, delimiter);
            buffer.insert(0, temp.name);
        }
        return buffer.toString();
    }
}
