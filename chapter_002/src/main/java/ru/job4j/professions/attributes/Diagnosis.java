package ru.job4j.professions.attributes;

/**
 * Diagnosis class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 15.01.2018
 */
public class Diagnosis {

    /**
     * Fields.
     */
    private String name;

    /**
     * Constructor.
     *
     * @param name Diagnosis' name.
     */
    public Diagnosis(String name) {
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
