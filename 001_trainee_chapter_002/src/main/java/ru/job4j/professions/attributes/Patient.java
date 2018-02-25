package ru.job4j.professions.attributes;

/**
 * Patient class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 15.01.2018
 */
public class Patient {

    /**
     * Fields.
     */
    private String name;

    /**
     * Constructor.
     *
     * @param name Patient's name.
     */
    public Patient(String name) {
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
