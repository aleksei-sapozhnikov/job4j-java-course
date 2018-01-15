package ru.job4j.professions.attributes;

/**
 * Student class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 15.01.2018
 */
public class Student {

    /**
     * Fields.
     */
    public String name;

    /**
     * Constructor.
     *
     * @param name Student's name.
     */
    public Student(String name) {
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
