package ru.job4j.professions.attributes;

public class Project {

    /**
     * Fields.
     */
    public String name;

    /**
     * Constructor.
     *
     * @param name Project name.
     */
    public Project(String name) {
        this.name = name;
    }

    /**
     * Getter.
     *
     * @return Value of the corresponding field.
     */
    public String getName() {
        return this.name;
    }
}
