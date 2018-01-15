package ru.job4j.professions.attributes;

/**
 * School class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 15.01.2018
 */
public class School {

    /**
     * Fields.
     */
    public String name;

    /**
     * Constructor.
     *
     * @param name School name.
     */
    public School(String name) {
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
