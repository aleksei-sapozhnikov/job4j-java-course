package ru.job4j.professions.attributes;

/**
 * Mark class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 15.01.2018
 */
public class Mark {

    /**
     * Fields.
     */
    private String name;

    /**
     * Constructor.
     *
     * @param name Mark name.
     */
    public Mark(String name) {
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
