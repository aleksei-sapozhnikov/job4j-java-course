package ru.job4j.crud.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.crud.AbstractUserServletCreate;

public class UserServletCreateDatabase extends AbstractUserServletCreate {
    private static final Logger LOG = LogManager.getLogger(UserServletCreateDatabase.class);

    public UserServletCreateDatabase() {
        super(UserValidatorDatabase.getInstance());
    }

}
