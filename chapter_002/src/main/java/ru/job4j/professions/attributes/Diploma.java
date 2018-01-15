package ru.job4j.professions.attributes;

/**
 * Diploma class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 15.01.2018
 */
public class Diploma {

    /**
     * Fields.
     */
    public String name;

    /**
     * Constructor.
     *
     * @param name Diploma name.
     */
    public Diploma(String name) {
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
