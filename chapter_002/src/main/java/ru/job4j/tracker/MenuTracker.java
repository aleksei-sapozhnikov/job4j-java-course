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
     * Numbers (keys) of actions which can be entered by user.
     */
    private int[] actionKeys = {0, 1, 2, 3, 4, 5, 6};

    /**
     * List of action names.
     * Position in this array corresponds with the key in the actionKeys array.
     * Action with key actionKeys[0] will have key actionKeys[0]
     */
    private String[] actionDescriptions = {
            "Add new Item",
            "Show all items",
            "Edit item",
            "Delete item",
            "Find item by Id",
            "Find items by name",
            "Exit Program"
    };

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
    public int[] getActionKeys() {
        return this.actionKeys;
    }

    /**
     * Fill array with possible actions.
     */
    public void fillUserActions() {
        if (actionKeys.length == actionDescriptions.length) {
            int position = 0;
            this.userActions[position] = this.new AddItem(
                    this.actionKeys[position], this.actionDescriptions[position++]
            );
            this.userActions[position] = new MenuTracker.ShowAllItems(
                    this.actionKeys[position], this.actionDescriptions[position++]
            );
            this.userActions[position] = new EditItem(
                    this.actionKeys[position], this.actionDescriptions[position++]
            );
            this.userActions[position] = this.new DeleteItem(
                    this.actionKeys[position], this.actionDescriptions[position++]
            );
            this.userActions[position] = new MenuTracker.FindItemById(
                    this.actionKeys[position], this.actionDescriptions[position++]
            );
            this.userActions[position] = new FindItemsByName(
                    this.actionKeys[position], this.actionDescriptions[position++]
            );
            this.userActions[position] = new Exit(
                    this.actionKeys[position], this.actionDescriptions[position]
            );
        } else {
            throw new RuntimeException(
                    "Length of array with action keys is not equal to length of array with actions descriptions."
            );
        }
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
     * Action : add item.
     */
    private class AddItem extends BaseAction {

        /**
         * Constructor inherited from superclass.
         *
         * @param key         Number (key) of the action.
         * @param description Description - what action is.
         */
        private AddItem(int key, String description) {
            super(key, description);
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
    }

    /**
     * Action : delete item.
     */
    private class DeleteItem extends BaseAction {
        /**
         * Constructor inherited from superclass.
         *
         * @param key         Number (key) of the action.
         * @param description Description - what action is.
         */
        private DeleteItem(int key, String description) {
            super(key, description);
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
                String id = input.ask("Enter item id : ");
                //confirm
                if ("y".equals(input.ask("Confirm delete (y to continue)? : "))) {
                    tracker.delete(id);
                    System.out.println("=== Item deleted.");
                } else {
                    System.out.println("=== Operation aborted.");
                }
            } catch (NoSuchIdException nside) {
                System.out.println("=== Exception : Item with such id not found. Try again.");
            }
        }
    }

    /**
     * Action : show all items contained.
     */
    private static class ShowAllItems extends BaseAction {

        /**
         * Constructor inherited from superclass.
         *
         * @param key         Number (key) of the action.
         * @param description Description - what action is.
         */
        private ShowAllItems(int key, String description) {
            super(key, description);
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
    }

    /**
     * Action : find item by id.
     */
    private static class FindItemById extends BaseAction {

        /**
         * Constructor inherited from superclass.
         *
         * @param key         Number (key) of the action.
         * @param description Description - what action is.
         */
        private FindItemById(int key, String description) {
            super(key, description);
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
                String id = input.ask("Enter item id : ");
                Item item = tracker.findById(id);
                System.out.println("=== Item information : ");
                System.out.println(String.format("id : %s", item.getId()));
                System.out.println(String.format("name : %s", item.getName()));
                System.out.println(String.format("description : %s", item.getDescription()));
                System.out.println(String.format("created : %s", new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date(item.getCreateTime()))));
            } catch (NoSuchIdException nside) {
                System.out.println("=== Exception : Item with such id not found. Try again.");
            }
        }
    }
}

/**
 * Action : edit item information.
 */
class EditItem extends BaseAction {

    /**
     * Constructor inherited from superclass.
     *
     * @param key         Number (key) of the action.
     * @param description Description - what action is.
     */
    EditItem(int key, String description) {
        super(key, description);
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
            Item old = tracker.findById(editId);
            //show item data
            System.out.println(String.format("id : %s", old.getId()));
            System.out.println(String.format("name : %s", old.getName()));
            System.out.println(String.format("description : %s", old.getDescription()));
            System.out.println(String.format("created : %s", new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date(old.getCreateTime()))));
            //get new data
            System.out.println();
            String name = input.ask("Enter item new name : ");
            String desc = input.ask("Enter item new description : ");
            //store old information
            Item newer = new Item(name, desc, old.getCreateTime());
            newer.setId(old.getId());
            //confirm
            if ("y".equals(input.ask("Confirm change (y to continue)? : "))) {
                tracker.replace(editId, newer);
                System.out.println("=== Item information updated.");
            } else {
                System.out.println("=== Operation aborted.");
            }
        } catch (NoSuchIdException nside) {
            System.out.println("=== Exception : Item with such id not found. Try again.");
        }
    }
}

/**
 * Action : find items by given name.
 */
class FindItemsByName extends BaseAction {

    /**
     * Constructor inherited from superclass.
     *
     * @param key         Number (key) of the action.
     * @param description Description - what action is.
     */
    FindItemsByName(int key, String description) {
        super(key, description);
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
        String name = input.ask("Enter name : ");
        Item[] items = tracker.findByName(name);
        for (Item item : items) {
            System.out.println();
            System.out.println(String.format("== Item id : %s", item.getId()));
            System.out.println(String.format("name : %s", item.getName()));
            System.out.println(String.format("description : %s", item.getDescription()));
            System.out.println(String.format("created : %s", new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date(item.getCreateTime()))));
        }
    }
}

/**
 * Action : exit from program.
 */
class Exit extends BaseAction {

    /**
     * Constructor inherited from superclass.
     *
     * @param key         Number (key) of the action.
     * @param description Description - what action is.
     */
    Exit(int key, String description) {
        super(key, description);
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
}
