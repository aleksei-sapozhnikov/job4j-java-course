package ru.job4j.sort.departments;

import java.util.Comparator;
import java.util.Objects;

public class Department implements Comparable<Department> {

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

    /**
     * Checks if this department is equal to another object.
     *
     * @param other object to compare to.
     * @return true or false, as objects are equal or not.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Department that = (Department) other;
        return Objects.equals(parent, that.parent)
                && Objects.equals(name, that.name);
    }

    /**
     * Calculates integer hashcode for this department.
     *
     * @return integer hashcode.
     */
    @Override
    public int hashCode() {
        return Objects.hash(parent, name);
    }

    /**
     * Compares two departments.
     *
     * @param other department to compare to.
     * @return positive, zero or negative value as this department is higher, equal or lower then other.
     */
    @Override
    public int compareTo(Department other) {
        int result;
        if (this.parent != null && other.parent != null) {
            int res = this.parent.compareTo(other.parent);
            result = res != 0 ? res : this.name.compareTo(other.name);
        } else if (this.parent == null && other.parent == null) {
            result = this.name.compareTo(other.name);
        } else {
            result = this.parent == null ? 1 : -1;
        }
        return result;
    }
}

