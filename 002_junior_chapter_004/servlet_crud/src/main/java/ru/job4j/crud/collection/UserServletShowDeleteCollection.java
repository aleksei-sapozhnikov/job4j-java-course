package ru.job4j.crud.collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.crud.AbstractUserServletShowDelete;

public class UserServletShowDeleteCollection extends AbstractUserServletShowDelete {
    private static final Logger LOG = LogManager.getLogger(UserServletShowDeleteCollection.class);

    public UserServletShowDeleteCollection() {
        super(UserValidatorCollection.getInstance());
    }

}
