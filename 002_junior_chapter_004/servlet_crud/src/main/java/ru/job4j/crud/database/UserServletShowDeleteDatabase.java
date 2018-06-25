package ru.job4j.crud.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.crud.AbstractUserServletShowDelete;

public class UserServletShowDeleteDatabase extends AbstractUserServletShowDelete {
    private static final Logger LOG = LogManager.getLogger(UserServletShowDeleteDatabase.class);

    public UserServletShowDeleteDatabase() {
        super(UserValidatorDatabase.getInstance());
    }

}
