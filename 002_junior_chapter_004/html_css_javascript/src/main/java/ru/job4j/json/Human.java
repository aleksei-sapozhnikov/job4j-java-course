package ru.job4j.json;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

enum Sex {
    MALE,
    FEMALE
}

/**
 * TODO: description
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

    private String firstName;
    private String secondName;
    private Sex sex;
    private String description;

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public Sex getSex() {
        return sex;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return String.format(
                "[human firstName=%s, secondName=%s, sex=%s, description=%s]",
                this.firstName, this.secondName, this.sex, this.description
        );
    }
}
