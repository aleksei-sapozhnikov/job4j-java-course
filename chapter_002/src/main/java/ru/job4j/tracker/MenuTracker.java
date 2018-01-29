package ru.job4j.tracker;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Shows menu, launches user actions.
 */
public class MenuTracker {

    /**
     * Where the user's requests are taken from.
     */
    private Input input;

    /**
     * Where to store information about items.
     */
    private Tracker tracker;

    /**
     * Array containing all possible user actions.
     */
    private UserAction[] userActions = new UserAction[7];

    /**
     * Range of actions which can be entered by user.
     */
    private int[] actionRange = {0, 1, 2, 3, 4, 5, 6};


    /**
     * Constructor.
     *
     * @param input   Where to take user input from.
     * @param tracker Where to store information.
     */
    public MenuTracker(Input input, Tracker tracker) {
        this.input = input;
        this.tracker = tracker;
    }

    /**
     * Get range of action numbers which user can enter.
     *
     * @return Array of action numbers (keys).
     */
    public int[] getActionRange() {
        return this.actionRange;
    }

    /**
     * Fill array with possible actions.
     */
    public void fillUserActions() {
        this.userActions[0] = this.new AddItem();
        this.userActions[1] = new MenuTracker.ShowAllItems();
        this.userActions[2] = new EditItem();
        this.userActions[3] = this.new DeleteItem();
        this.userActions[4] = new MenuTracker.FindItemById();
        this.userActions[5] = new FindItemsByName();
        this.userActions[6] = new Exit();
    }

    public void show() {
        System.out.println();
        System.out.println("============ Action menu ============");
        for (UserAction action : this.userActions) {
            if (action != null) {
                System.out.println(action.menuLine());
            }
        }
    }

    /**
     * Launches action chosen by key.
     *
     * @param key Action key.
     */
    public void launchAction(int key) {
        this.userActions[key].execute(this.input, this.tracker);
    }

    /**
     * Program initialization.
     */
    public void init() {
        //
    }


    /**
     * Action : add item.
     */
    private class AddItem implements UserAction {

        /**
         * Key of this action.
         */
        private static final int KEY = 0;

        /**
         * Description of the action.
         */
        private static final String DESCRIPTION = "Add new Item";

        /**
         * Get key of the action.
         *
         * @return Value of the KEY constant.
         */
        public int getActionKey() {
            return KEY;
        }

        /**
         * Performs operations made by this this action.
         *
         * @param input   Where to take user input from.
         * @param tracker Where to store items.
         */
        public void execute(Input input, Tracker tracker) {
            System.out.println();
            System.out.println("------------ Add new item ------------");
            String name = input.ask("Enter item name : ");
            String desc = input.ask("Enter item description : ");
            Item item = tracker.add(new Item(name, desc, System.currentTimeMillis()));
            System.out.println(String.format("=== New item added. Item id : %s", item.getId()));

        }

        public String menuLine() {
            return String.format("%s : %s", KEY, DESCRIPTION);
        }
    }

    /**
     * Action : delete item.
     */
    private class DeleteItem implements UserAction {

        /**
         * Key of this action.
         */
        private static final int KEY = 3;

        /**
         * Description of the action.
         */
        private static final String DESCRIPTION = "Delete item";

        /**
         * Get key of the action.
         *
         * @return Value of the KEY constant.
         */
        public int getActionKey() {
            return KEY;
        }

        /**
         * Performs operations made by this this action.
         *
         * @param input   Where to take user input from.
         * @param tracker Where to store items.
         */
        public void execute(Input input, Tracker tracker) {
            try {
                System.out.println();
                System.out.println("------------ Delete item ------------");
                String delId = input.ask("Enter item id : ");
                //confirm
                if ("y".equals(input.ask("Confirm delete (y to continue)? : "))) {
                    tracker.delete(delId);
                    System.out.println("=== Item deleted.");
                } else {
                    System.out.println("=== Operation aborted.");
                }
            } catch (NoSuchIdException nside) {
                System.out.println("=== Exception : Item with such id not found. Try again.");
            }
        }

        public String menuLine() {
            return String.format("%s : %s", KEY, DESCRIPTION);
        }
    }

    /**
     * Action : show all items contained.
     */
    private static class ShowAllItems implements UserAction {

        /**
         * Key of this action.
         */
        private static final int KEY = 1;

        /**
         * Description of the action.
         */
        private static final String DESCRIPTION = "Show all items";

        /**
         * Get key of the action.
         *
         * @return Value of the KEY constant.
         */
        public int getActionKey() {
            return KEY;
        }

        /**
         * Performs operations made by this this action.
         *
         * @param input   Where to take user input from.
         * @param tracker Where to store items.
         */
        public void execute(Input input, Tracker tracker) {
            System.out.println();
            System.out.println("------------ Show all items contained ------------");
            Item[] items = tracker.findAll();
            for (Item item : items) {
                System.out.println();
                System.out.println(String.format("=== Item id : %s", item.getId()));
                System.out.println(String.format("name : %s", item.getName()));
                System.out.println(String.format("description : %s", item.getDescription()));
                System.out.println(String.format("created : %s", new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date(item.getCreateTime()))));
            }
        }

        public String menuLine() {
            return String.format("%s : %s", KEY, DESCRIPTION);
        }
    }

    /**
     * Action : find item by id.
     */
    private static class FindItemById implements UserAction {

        /**
         * Key of this action.
         */
        private static final int KEY = 4;

        /**
         * Description of the action.
         */
        private static final String DESCRIPTION = "Find item by Id";

        /**
         * Get key of the action.
         *
         * @return Value of the KEY constant.
         */
        public int getActionKey() {
            return KEY;
        }

        /**
         * Performs operations made by this this action.
         *
         * @param input   Where to take user input from.
         * @param tracker Where to store items.
         */
        public void execute(Input input, Tracker tracker) {
            try {
                System.out.println();
                System.out.println("------------ Find item by id ------------");
                String findId = input.ask("Enter item id : ");
                Item item = tracker.findById(findId);
                System.out.println("=== Item information : ");
                System.out.println(String.format("id : %s", item.getId()));
                System.out.println(String.format("name : %s", item.getName()));
                System.out.println(String.format("description : %s", item.getDescription()));
                System.out.println(String.format("created : %s", new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date(item.getCreateTime()))));
            } catch (NoSuchIdException nside) {
                System.out.println("=== Exception : Item with such id not found. Try again.");
            }
        }

        public String menuLine() {
            return String.format("%s : %s", KEY, DESCRIPTION);
        }
    }
}

/**
 * Action : edit item information.
 */
class EditItem implements UserAction {

    /**
     * Key of this action.
     */
    private static final int KEY = 2;

    /**
     * Description of the action
     */
    private static final String DESCRIPTION = "Edit item";

    /**
     * Get key of the action.
     *
     * @return Value of the KEY constant.
     */
    public int getActionKey() {
        return KEY;
    }

    /**
     * Performs operations made by this this action.
     *
     * @param input   Where to take user input from.
     * @param tracker Where to store items.
     */
    public void execute(Input input, Tracker tracker) {
        try {
            System.out.println();
            System.out.println("------------ Edit item ------------");
            String editId = input.ask("Enter item id : ");
            Item oldItem = tracker.findById(editId);
            //show item data
            System.out.println(String.format("id : %s", oldItem.getId()));
            System.out.println(String.format("name : %s", oldItem.getName()));
            System.out.println(String.format("description : %s", oldItem.getDescription()));
            System.out.println(String.format("created : %s", new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date(oldItem.getCreateTime()))));
            //get new data
            System.out.println();
            String name = input.ask("Enter item new name : ");
            String desc = input.ask("Enter item new description : ");
            //store old information
            Item newItem = new Item(name, desc, oldItem.getCreateTime());
            newItem.setId(oldItem.getId());
            //confirm
            if ("y".equals(input.ask("Confirm change (y to continue)? : "))) {
                tracker.replace(editId, newItem);
                System.out.println("=== Item information updated.");
            } else {
                System.out.println("=== Operation aborted.");
            }
        } catch (NoSuchIdException nside) {
            System.out.println("=== Exception : Item with such id not found. Try again.");
        }
    }

    public String menuLine() {
        return String.format("%s : %s", KEY, DESCRIPTION);
    }

}

/**
 * Action : find items by given name.
 */
class FindItemsByName implements UserAction {

    /**
     * Key of this action.
     */
    private static final int KEY = 5;

    /**
     * Description of the action
     */
    private static final String DESCRIPTION = "Find items by name";

    /**
     * Get key of the action.
     *
     * @return Value of the KEY constant.
     */
    public int getActionKey() {
        return KEY;
    }

    /**
     * Performs operations made by this this action.
     *
     * @param input   Where to take user input from.
     * @param tracker Where to store items.
     */
    public void execute(Input input, Tracker tracker) {
        System.out.println();
        System.out.println("------------ Find items with given name ------------");
        String findName = input.ask("Enter name : ");
        Item[] items = tracker.findByName(findName);
        for (Item item : items) {
            System.out.println();
            System.out.println(String.format("== Item id : %s", item.getId()));
            System.out.println(String.format("name : %s", item.getName()));
            System.out.println(String.format("description : %s", item.getDescription()));
            System.out.println(String.format("created : %s", new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date(item.getCreateTime()))));
        }
    }

    public String menuLine() {
        return String.format("%s : %s", KEY, DESCRIPTION);
    }

}

/**
 * Action : exit from program.
 */
class Exit implements UserAction {

    /**
     * Key of this action.
     */
    private static final int KEY = 6;

    /**
     * Description of the action
     */
    private static final String DESCRIPTION = "Exit Program";

    /**
     * Get key of the action.
     *
     * @return Value of the KEY constant.
     */
    public int getActionKey() {
        return KEY;
    }

    /**
     * Performs operations made by this this action.
     *
     * @param input   Where to take user input from.
     * @param tracker Where to store items.
     */
    public void execute(Input input, Tracker tracker) {
        System.out.println("=== Exit program.");
    }

    public String menuLine() {
        return String.format("%s : %s", KEY, DESCRIPTION);
    }

}
