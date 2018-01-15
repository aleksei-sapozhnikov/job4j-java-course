package ru.job4j.professions.attributes;

/**
 * Construction class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 15.01.2018
 */
public class Construction {

    /**
     * Fields.
     */
    public String name;

    /**
     * Constructor.
     *
     * @param name Construction name.
     */
    public Construction(String name) {
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
