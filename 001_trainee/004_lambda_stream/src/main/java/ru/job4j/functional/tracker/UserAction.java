package ru.job4j.functional.tracker;

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
    void execute(Input input, Tracker tracker);

    /**
     * Describes this action to user in menu.
     *
     * @return A line with the information.
     */
    String menuLine();

}
