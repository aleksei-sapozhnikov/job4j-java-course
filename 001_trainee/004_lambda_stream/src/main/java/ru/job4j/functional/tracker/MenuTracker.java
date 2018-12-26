package ru.job4j.functional.tracker;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.TreeMap;
import java.util.function.Consumer;

/**
 * Shows menu, launches user actions.
 */
@SuppressWarnings("Duplicates")
public class MenuTracker {

    /**
     * Where the user's requests are taken from.
     */
    private Input input;

    /**
     * Where to store information about items.
     */
    private Tracker tracker;

    private Consumer<String> output;


    /**
     * Map containing all possible user actions.
     */
    private Map<Integer, UserAction> userActions = new TreeMap<>();

    /**
     * Constructor.
     *
     * @param input   Where to take user input from.
     * @param tracker Where to store information.
     * @param output  Consumer object to use for output.
     */
    public MenuTracker(Input input, Tracker tracker, Consumer<String> output) {
        this.input = input;
        this.tracker = tracker;
        this.output = output;
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
        this.userActions.put(position, new EditItem(position++, "Edit item", this.output));
        this.userActions.put(position, this.new DeleteItem(position++, "Delete item"));
        this.userActions.put(position, new FindItemById(position++, "Find item by Id"));
        this.userActions.put(position, new FindItemsByName(position++, "Find items by name", this.output));
        this.userActions.put(position, new Exit(position, "Exit Program", this.output));
    }

    public void show() {
        StringJoiner buffer = new StringJoiner(System.lineSeparator())
                .add("")
                .add("============ Action menu ============");
        for (UserAction action : this.userActions.values()) {
            if (action != null) {
                buffer.add(action.menuLine());
            }
        }
        buffer.add("");
        output.accept(buffer.toString());
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
    private class ShowAllItems extends BaseAction {

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
            StringJoiner buffer = new StringJoiner(System.lineSeparator())
                    .add("")
                    .add("------------ Show all items contained ------------");
            List<Item> items = tracker.findAll();
            for (Item item : items) {
                buffer
                        .add("")
                        .add(String.format("=== Item id : %s", item.getId()))
                        .add(String.format("name : %s", item.getName()))
                        .add(String.format("description : %s", item.getDescription()));
            }
            buffer.add("");
            output.accept(buffer.toString());
        }
    }

    /**
     * Action : find item by id.
     */
    private class FindItemById extends BaseAction {

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
            StringJoiner buffer = new StringJoiner(System.lineSeparator())
                    .add("")
                    .add("------------ Find item by id ------------");
            String id = input.ask("Enter item id : ");
            try {
                Item item = tracker.findById(id);
                buffer
                        .add("=== Item information : ")
                        .add(String.format("id : %s", item.getId()))
                        .add(String.format("name : %s", item.getName()))
                        .add(String.format("description : %s", item.getDescription()));
            } catch (NoSuchIdException nside) {
                buffer.add("=== Exception : Item with such id not found. Try again.");
            }
            buffer.add("");
            output.accept(buffer.toString());
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
            StringJoiner buffer = new StringJoiner(System.lineSeparator())
                    .add("")
                    .add("------------ Add new item ------------");
            String name = input.ask("Enter item name : ");
            String desc = input.ask("Enter item description : ");
            Item item = tracker.add(new Item(name, desc, System.currentTimeMillis()));
            buffer.add(String.format("=== New item added. Item id : %s", item.getId()));
            buffer.add("");
            output.accept(buffer.toString());
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
            StringJoiner buffer = new StringJoiner(System.lineSeparator())
                    .add("")
                    .add("------------ Delete item ------------");
            String id = input.ask("Enter item id : ");
            try {
                if ("y".equals(input.ask("Confirm delete (y to continue)? : "))) {
                    tracker.delete(id);
                    buffer.add("=== Item deleted.");
                } else {
                    buffer.add("=== Operation aborted.");
                }
            } catch (NoSuchIdException nside) {
                buffer.add("=== Exception : Item with such id not found. Try again.");
            }
            buffer.add("");
            output.accept(buffer.toString());
        }
    }
}

/**
 * Action : edit item information.
 */
@SuppressWarnings("Duplicates")
class EditItem extends BaseAction {

    /**
     * Consumer to get information for user interaction.
     */
    private final Consumer<String> output;

    /**
     * Constructor inherited from superclass.
     *
     * @param key         Number (key) of the action.
     * @param description Description - what action is.
     */
    EditItem(int key, String description, Consumer<String> output) {
        super(key, description);
        this.output = output;
    }

    /**
     * Performs operations made by this this action.
     *
     * @param input   Where to take user input from.
     * @param tracker Where to store items.
     */
    public void execute(Input input, Tracker tracker) {
        StringJoiner buffer = new StringJoiner(System.lineSeparator())
                .add("")
                .add("------------ Edit item ------------");
        try {
            String editId = input.ask("Enter item id : ");
            Item old = tracker.findById(editId);
            //show item data
            buffer
                    .add(String.format("id : %s", old.getId()))
                    .add(String.format("name : %s", old.getName()))
                    .add(String.format("description : %s", old.getDescription()));
            //get new data
            buffer.add("");
            String name = input.ask("Enter item new name : ");
            String desc = input.ask("Enter item new description : ");
            //store old information
            Item newer = new Item(name, desc, old.getCreateTime());
            newer.setId(old.getId());
            //confirm
            if ("y".equals(input.ask("Confirm change (y to continue)? : "))) {
                tracker.replace(editId, newer);
                buffer.add("=== Item information updated.");
            } else {
                buffer.add("=== Operation aborted.");
            }
        } catch (NoSuchIdException nside) {
            buffer.add("=== Exception : Item with such id not found. Try again.");
        }
        buffer.add("");
        this.output.accept(buffer.toString());
    }
}

/**
 * Action : find items by given name.
 */
@SuppressWarnings("Duplicates")
class FindItemsByName extends BaseAction {

    /**
     * Consumer to get information for user interaction.
     */
    private final Consumer<String> output;

    /**
     * Constructor inherited from superclass.
     *
     * @param key         Number (key) of the action.
     * @param description Description - what action is.
     */
    FindItemsByName(int key, String description, Consumer<String> output) {
        super(key, description);
        this.output = output;
    }

    /**
     * Performs operations made by this this action.
     *
     * @param input   Where to take user input from.
     * @param tracker Where to store items.
     */
    public void execute(Input input, Tracker tracker) {
        StringJoiner buffer = new StringJoiner(System.lineSeparator())
                .add("")
                .add("------------ Find items with given name ------------");
        String name = input.ask("Enter name : ");
        List<Item> items = tracker.findByName(name);
        for (Item item : items) {
            buffer
                    .add("")
                    .add(String.format("== Item id : %s", item.getId()))
                    .add(String.format("name : %s", item.getName()))
                    .add(String.format("description : %s", item.getDescription()));
        }
        buffer.add("");
        this.output.accept(buffer.toString());
    }
}

/**
 * Action : exit from program.
 */
class Exit extends BaseAction {

    /**
     * Consumer to get information for user interaction.
     */
    private final Consumer<String> output;

    /**
     * Constructor inherited from superclass.
     *
     * @param key         Number (key) of the action.
     * @param description Description - what action is.
     */
    Exit(int key, String description, Consumer<String> output) {
        super(key, description);
        this.output = output;
    }

    /**
     * Performs operations made by this this action.
     *
     * @param input   Where to take user input from.
     * @param tracker Where to store items.
     */
    public void execute(Input input, Tracker tracker) {
        StringJoiner buffer = new StringJoiner(System.lineSeparator())
                .add("=== Exit program.");
        buffer.add("");
        this.output.accept(buffer.toString());
    }
}
