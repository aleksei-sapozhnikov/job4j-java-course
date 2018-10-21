package ru.job4j.functional.tracker;

import java.util.function.Consumer;

/**
 * Object to store in tracker.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 23.01.2018
 */
@SuppressWarnings("Duplicates")
public class StartUI {

    /**
     * Menu constant to exit from program.
     */
    private static final int EXIT = 6;

    /**
     * Where from input is taken.
     */
    private final Input input;

    /**
     * Where to put output to.
     */
    private final Consumer<String> output;

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
        this(input, tracker, System.out::print);
    }

    /**
     * Constructor
     *
     * @param input   Where to take input data from.
     * @param tracker Storage of items.
     * @param output  Consumer to receive result messages.
     */
    public StartUI(Input input, Tracker tracker, Consumer<String> output) {
        this.input = input;
        this.tracker = tracker;
        this.output = output;
    }

    /**
     * Main method, starts the program.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        new StartUI(new ValidateInput(new ConsoleInput()), new Tracker()).init();
    }

    /**
     * Initialization part.
     */
    public void init() {
        boolean exit = false;
        MenuTracker menu = new MenuTracker(this.input, this.tracker, this.output);
        menu.fillUserActions();
        while (!exit) {
            menu.show();
            int answer = this.input.ask("Enter number for action : ", menu.getUserActionsKeys());
            menu.launchAction(answer);
            if (EXIT == answer) {
                exit = true;
            }
        }
    }

}