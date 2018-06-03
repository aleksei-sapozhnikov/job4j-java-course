package ru.job4j.tracker;

import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

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
     * Map containing all possible user actions.
     */
    private Map<Integer, UserAction> userActions = new TreeMap<>();

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

    public int[] getUserActionsKeys() {
        int[] result = new int[this.userActions.keySet().size()];
        int pos = 0;
        for (Integer key : this.userActions.keySet()) {
            result[pos++] = key;
        }
        return result;
    }

    /**
     * Fill array with possible actions.
     */
    public void fillUserActions() {
        int position = 0;
        this.userActions.put(position, this.new AddItem(position++, "Add new Item"));
        this.userActions.put(position, new ShowAllItems(position++, "Show all items"));
        this.userActions.put(position, new EditItem(position++, "Edit item"));
        this.userActions.put(position, this.new DeleteItem(position++, "Delete item"));
        this.userActions.put(position, new FindItemById(position++, "Find item by Id"));
        this.userActions.put(position, new FindItemsByName(position++, "Find items by name"));
        this.userActions.put(position, new Exit(position, "Exit Program"));
    }

    public void show() {
        System.out.println();
        System.out.println("============ Action menu ============");
        for (UserAction action : this.userActions.values()) {
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
        this.userActions.get(key).execute(this.input, this.tracker);
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
            try {
                System.out.println();
                System.out.println("------------ Show all items contained ------------");
                Item[] items = tracker.findAll();
                for (Item item : items) {
                    System.out.println();
                    System.out.println(String.format("=== Item id : %s", item.getId()));
                    System.out.println(String.format("name : %s", item.getName()));
                    System.out.println(String.format("description : %s", item.getDescription()));
                }
            } catch (SQLException e) {
                e.printStackTrace();
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
            } catch (NoSuchIdException nside) {
                System.out.println("=== Exception : Item with such id not found. Try again.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
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
            try {
                System.out.println();
                System.out.println("------------ Add new item ------------");
                String name = input.ask("Enter item name : ");
                String desc = input.ask("Enter item description : ");
                Item item = tracker.add(new Item(name, desc, System.currentTimeMillis()));
                System.out.println(String.format("=== New item added. Item id : %s", item.getId()));
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
            } catch (SQLException e) {
                e.printStackTrace();
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
        } catch (SQLException e) {
            e.printStackTrace();
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
        try {
            System.out.println();
            System.out.println("------------ Find items with given name ------------");
            String name = input.ask("Enter name : ");
            Item[] items = tracker.findByName(name);
            for (Item item : items) {
                System.out.println();
                System.out.println(String.format("== Item id : %s", item.getId()));
                System.out.println(String.format("name : %s", item.getName()));
                System.out.println(String.format("description : %s", item.getDescription()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
