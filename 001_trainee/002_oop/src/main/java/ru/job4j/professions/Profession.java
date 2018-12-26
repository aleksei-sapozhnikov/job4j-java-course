package ru.job4j.professions;

import ru.job4j.professions.attributes.Diploma;

/**
 * Basic class for professions.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 15.01.2018
 */
public class Profession {
    /**
     * Fields.
     */
    private String name;
    private Diploma diploma;
    private int age;

    /**
     * Default constructor.
     */
    public Profession() {
        name = "";
        diploma = null;
        age = 0;
    }

    /**
     * Constructor.
     *
     * @param name    Name.
     * @param diploma Diploma.
     * @param age     Age (full years).
     */
    public Profession(String name, Diploma diploma, int age) {
        this.name = name;
        this.diploma = diploma;
        this.age = age;
    }

    /**
     * Getters.
     *
     * @return Value of the corresponding field.
     */
    public String getName() {
        return this.name;
    }

    public Diploma getDiploma() {
        return this.diploma;
    }

    public int getAge() {
        return this.age;
    }

}
