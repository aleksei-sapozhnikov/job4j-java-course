package ru.job4j.crud.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.crud.AbstractUserServletCreate;

public class UserServletCreateInDatabase extends AbstractUserServletCreate {
    private static final Logger LOG = LogManager.getLogger(UserServletCreateInDatabase.class);

    public UserServletCreateInDatabase() {
        super(UserValidatorInDatabase.getInstance());
    }

}
