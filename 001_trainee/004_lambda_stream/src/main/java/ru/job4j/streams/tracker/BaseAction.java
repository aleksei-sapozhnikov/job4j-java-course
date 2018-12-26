package ru.job4j.streams.tracker;

/**
 * Base class for all user action.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 30.01.2018
 */
public abstract class BaseAction implements UserAction {
    /**
     * Key of this action.
     */
    private final int key;

    /**
     * Description of the action
     */
    private final String description;

    /**
     * Constructor.
     *
     * @param key         Number (key) of the action.
     * @param description Description of the action.
     */
    public BaseAction(int key, String description) {
        this.key = key;
        this.description = description;
    }

    /**
     * Get key of the action.
     *
     * @return Value of the KEY constant.
     */
    @Override
    public int getActionKey() {
        return this.key;
    }

    /**
     * Forms menu line for the action.
     *
     * @return String to print in the menu.
     */
    public String menuLine() {
        return String.format("%s : %s", this.key, this.description);
    }

}
