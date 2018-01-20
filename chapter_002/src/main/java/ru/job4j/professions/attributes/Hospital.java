package ru.job4j.professions.attributes;

/**
 * Hospital class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 15.01.2018
 */
public class Hospital {

    /**
     * Fields.
     */
    private String name;

    /**
     * Constructor.
     *
     * @param name Hospital name.
     */
    public Hospital(String name) {
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
