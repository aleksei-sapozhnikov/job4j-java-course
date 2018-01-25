package ru.job4j.tracker;

import java.text.SimpleDateFormat;
import java.util.Date;

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
     * Main method, starts the program.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        new StartUI(new ConsoleInput(), new Tracker()).init();
    }

    /**
     * Initialization part.
     */
    public void init() {
        boolean exit = false;
        while (!exit) {
            this.showMenu();
            String answer = this.input.ask("Enter number for action : ");
            if (ADD.equals(answer)) {
                this.addItem();
            } else if (SHOW_ALL.equals(answer)) {
                this.showAllItems();
            } else if (EDIT.equals(answer)) {
                this.editItem();
            } else if (DELETE.equals(answer)) {
                this.deleteItem();
            } else if (FIND_BY_ID.equals(answer)) {
                this.findByIdItem();
            } else if (FIND_BY_NAME.equals(answer)) {
                this.findByNameItem();
            } else if (EXIT.equals(answer)) {
                exit = true;
                System.out.println("=== Exit program.");
            } else {
                System.out.println("=== Unknown operation.");
            }
        }
    }

    /**
     * Display menu with action variants.
     */
    private void showMenu() {
        System.out.println();
        System.out.println("============ Action menu ============");
        System.out.println(String.format("%s : Add new Item", ADD));
        System.out.println(String.format("%s : Show all items", SHOW_ALL));
        System.out.println(String.format("%s : Edit item", EDIT));
        System.out.println(String.format("%s : Delete item", DELETE));
        System.out.println(String.format("%s : Find item by Id", FIND_BY_ID));
        System.out.println(String.format("%s : Find items by name", FIND_BY_NAME));
        System.out.println(String.format("%s : Exit Program", EXIT));
    }

    /**
     * Add new item to storage.
     */
    private void addItem() {
        System.out.println();
        System.out.println("------------ Add new item ------------");
        String name = this.input.ask("Enter item name : ");
        String desc = this.input.ask("Enter item description : ");
        Item item = this.tracker.add(new Item(name, desc, System.currentTimeMillis()));
        System.out.println(String.format("=== New item added. Item id : %s", item.getId()));
    }

    /**
     * Show all items contained.
     */
    private void showAllItems() {
        System.out.println();
        System.out.println("------------ Show all items contained ------------");
        Item[] items = this.tracker.findAll();
        for (Item item : items) {
            System.out.println();
            System.out.println(String.format("=== Item id : %s", item.getId()));
            System.out.println(String.format("name : %s", item.getName()));
            System.out.println(String.format("description : %s", item.getDescription()));
            System.out.println(String.format("created : %s", new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date(item.getCreateTime()))));
        }
    }

    /**
     * Edit item.
     */
    private void editItem() {
        System.out.println();
        System.out.println("------------ Edit item ------------");
        String editId = this.input.ask("Enter item id : ");
        Item oldItem = this.tracker.findById(editId);
        //show item data
        System.out.println(String.format("id : %s", oldItem.getId()));
        System.out.println(String.format("name : %s", oldItem.getName()));
        System.out.println(String.format("description : %s", oldItem.getDescription()));
        System.out.println(String.format("created : %s", new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date(oldItem.getCreateTime()))));
        //get new data
        System.out.println();
        String name = this.input.ask("Enter item new name : ");
        String desc = this.input.ask("Enter item new description : ");
        Item newItem = new Item(name, desc, oldItem.getCreateTime());
        newItem.setId(oldItem.getId());
        //confirm
        if ("y".equals(input.ask("Confirm change (y to continue)? : "))) {
            this.tracker.replace(editId, newItem);
            System.out.println("=== Item information updated.");
        } else {
            System.out.println("=== Operation aborted.");
        }
    }

    /**
     * Delete item.
     */
    private void deleteItem() {
        System.out.println();
        System.out.println("------------ Delete item ------------");
        String delId = this.input.ask("Enter item id : ");
        //confirm
        if ("y".equals(input.ask("Confirm delete (y to continue)? : "))) {
            this.tracker.delete(delId);
            System.out.println("=== Item deleted.");
        } else {
            System.out.println("=== Operation aborted.");
        }
    }

    /**
     * Find item by id.
     */
    private void findByIdItem() {
        System.out.println();
        System.out.println("------------ Find item by id ------------");
        String findId = this.input.ask("Enter item id : ");
        Item item = this.tracker.findById(findId);
        System.out.println("=== Item information : ");
        System.out.println(String.format("id : %s", item.getId()));
        System.out.println(String.format("name : %s", item.getName()));
        System.out.println(String.format("description : %s", item.getDescription()));
        System.out.println(String.format("created : %s", new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date(item.getCreateTime()))));
    }

    /**
     * Find items with given name.
     */
    private void findByNameItem() {
        System.out.println();
        System.out.println("------------ Find items with given name ------------");
        String findName = this.input.ask("Enter name : ");
        Item[] items = tracker.findByName(findName);
        for (Item item : items) {
            System.out.println();
            System.out.println(String.format("== Item id : %s", item.getId()));
            System.out.println(String.format("name : %s", item.getName()));
            System.out.println(String.format("description : %s", item.getDescription()));
            System.out.println(String.format("created : %s", new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date(item.getCreateTime()))));
        }
    }

}
