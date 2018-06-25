package ru.job4j.crud.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.crud.AbstractUserServletUpdate;

public class UserServletUpdateInDatabase extends AbstractUserServletUpdate {
    private static final Logger LOG = LogManager.getLogger(UserServletUpdateInDatabase.class);

    public UserServletUpdateInDatabase() {
        super(UserValidatorInDatabase.getInstance());
    }

}
