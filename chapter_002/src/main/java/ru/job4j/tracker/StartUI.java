package ru.job4j.tracker;

/**
 * Object to store in tracker.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 23.01.2018
 */
public class StartUI {

    /**
     * Menu constant to add new item.
     */
    private static final String ADD = "0";

    /**
     * Menu constant to show all items in storage.
     */
    private static final String SHOW_ALL = "1";

    /**
     * Menu constant to edit item.
     */
    private static final String EDIT = "2";

    /**
     * Menu constant to delete item.
     */
    private static final String DELETE = "3";

    /**
     * Menu constant to find item by id.
     */
    private static final String FIND_BY_ID = "4";

    /**
     * Menu constant to find items by name.
     */
    private static final String FIND_BY_NAME = "5";

    /**
     * Menu constant to exit from program.
     */
    private static final String EXIT = "6";


    /**
     * Where from input is taken.
     */
    private final Input input;

    /**
     * Storage of items.
     */
    private final Tracker tracker;


    /**
     * Constructor
     *
     * @param input   Where to take input data from.
     * @param tracker Storage of items.
     */
    public StartUI(Input input, Tracker tracker) {
        this.input = input;
        this.tracker = tracker;
    }

    /**
     * Display menu with action variants.
     */
    private void showMenu() {
        System.out.println(
                "============ Action menu ============"
                        + ADD + " : Add new Item"
                        + SHOW_ALL + " : Show all items"
                        + EDIT + " : Edit item"
                        + DELETE + " : Delete item"
                        + FIND_BY_ID + " : Find item by Id"
                        + FIND_BY_NAME + " : Find items by name"
                        + EXIT + " : Exit Program"
        );
    }

    /**
     * Add new item to storage.
     */
    private void createItem() {
        System.out.println("------------ Add new item ------------");
        String name = this.input.ask("Enter item name :");
        String desc = this.input.ask("Enter item description :");
        Item item = this.tracker.add(new Item(name, desc, System.currentTimeMillis()));
        System.out.println("------------ New item added. Item id : " + item.getId());
    }

    /**
     * Main method of program.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        new StartUI(new ConsoleInput(), new Tracker()).init();
    }

    /**
     * Initialize program.
     */
    public void init() {
        boolean exit = false;
        while (!exit) {
            this.showMenu();
            String answer = this.input.ask("Enter number for action : ");
            if (ADD.equals(answer)) {
                this.createItem();
            } else if (SHOW_ALL.equals(answer)) {
                // show all
            } else if (EDIT.equals(answer)) {
                // edit
            } else if (DELETE.equals(answer)) {
                // delete
            } else if (FIND_BY_ID.equals(answer)) {
                // find by id
            } else if (FIND_BY_NAME.equals(answer)) {
                // find by name
            } else if (EXIT.equals(answer)) {
                exit = true;
            } else {
                System.out.println("Unknown operation.");
            }
        }
    }
}
