package ru.job4j.json;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Enum for human sex.
 */
enum Sex {
    MALE,
    FEMALE
}

/**
 * Object to store and pass as JSON object.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class Human {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(Human.class);
    /**
     * First name.
     */
    private String firstName;
    /**
     * Second name.
     */
    private String secondName;
    /**
     * Sex
     */
    private Sex sex;
    /**
     * Description.
     */
    private String description;

    /**
     * Returns first name.
     *
     * @return First name.
     */
    public String getFirstName() {
        return this.firstName;
    }

    /**
     * Returns second name.
     *
     * @return Second name.
     */
    public String getSecondName() {
        return this.secondName;
    }

    /**
     * Returns sex.
     *
     * @return Sex.
     */
    public Sex getSex() {
        return this.sex;
    }

    /**
     * Returns description.
     *
     * @return Description.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Returns string describing object.
     *
     * @return String describing object.
     */
    @Override
    public String toString() {
        return String.format(
                "[human firstName=%s, secondName=%s, sex=%s, description=%s]",
                this.firstName, this.secondName, this.sex, this.description
        );
    }
}
