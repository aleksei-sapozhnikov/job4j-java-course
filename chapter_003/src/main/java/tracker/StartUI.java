package tracker;

/**
 * Object to store in tracker.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 23.01.2018
 */
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
        new StartUI(new ValidateInput(new ConsoleInput()), new Tracker()).init();
    }

    /**
     * Initialization part.
     */
    public void init() {
        boolean exit = false;
        MenuTracker menu = new MenuTracker(this.input, this.tracker);
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