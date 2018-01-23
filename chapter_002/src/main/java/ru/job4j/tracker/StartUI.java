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
    private StartUI(Input input, Tracker tracker) {
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
    private void init() {
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
        System.out.println(ADD + " : Add new Item");
        System.out.println(SHOW_ALL + " : Show all items");
        System.out.println(EDIT + " : Edit item");
        System.out.println(DELETE + " : Delete item");
        System.out.println(FIND_BY_ID + " : Find item by Id");
        System.out.println(FIND_BY_NAME + " : Find items by name");
        System.out.println(EXIT + " : Exit Program");
        System.out.println();
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
        System.out.println("=== New item added. Item id : " + item.getId());
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
            System.out.println("== Item id : " + item.getId());
            System.out.println("name : " + item.getName());
            System.out.println("description : " + item.getDescription());
            System.out.println("created : " + new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date(item.getCreateTime())));
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
        System.out.println("id : " + oldItem.getId());
        System.out.println("name : " + oldItem.getName());
        System.out.println("description : " + oldItem.getDescription());
        System.out.println("created : " + new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date(oldItem.getCreateTime())));
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
        if (item != null) {
            System.out.println("=== Item information : ");
            System.out.println("id : " + item.getId());
            System.out.println("name : " + item.getName());
            System.out.println("description : " + item.getDescription());
            System.out.println("created : " + new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date(item.getCreateTime())));
        } else {
            System.out.println("== Unknown id");
        }
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
            System.out.println("== Item id : " + item.getId());
            System.out.println("name : " + item.getName());
            System.out.println("description : " + item.getDescription());
            System.out.println("created : " + new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date(item.getCreateTime())));
        }
    }

}
