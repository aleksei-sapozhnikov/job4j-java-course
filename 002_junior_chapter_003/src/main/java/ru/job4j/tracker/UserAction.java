package ru.job4j.tracker;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Common methods for different user actions.
 */
public interface UserAction {

    /**
     * Returns position of the action in the array of actions.
     *
     * @return position.
     */
    int getActionKey();

    /**
     * Performs operations made by this this action.
     */
    void execute(Input input, Tracker tracker) throws SQLException, IOException;

    /**
     * Describes this action to user in menu.
     *
     * @return A line with the information.
     */
    String menuLine();

}
