package ru.job4j.professions.attributes;

/**
 * Bureau class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 15.01.2018
 */
public class Bureau {

    /**
     * Fields.
     */
    public String name;

    /**
     * Constructor.
     *
     * @param name Bureau' name.
     */
    public Bureau(String name) {
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
