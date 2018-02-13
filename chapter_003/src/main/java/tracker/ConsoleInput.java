package tracker;

import java.util.Scanner;

/**
 * Console-based input from a human user.
 */
public class ConsoleInput implements Input {

    /**
     * Scanner used to get information entered by user in console.
     */
    private Scanner scanner = new Scanner(System.in);


    /**
     * Ask question and get answer from user.
     *
     * @param question Question we ask.
     * @return User's answer.
     */
    public String ask(String question) {
        System.out.print(question);
        return scanner.nextLine();
    }

    /**
     * Ask question and get answer from user what to do.
     * Answer must be an integer in a definite range.
     *
     * @param question Question we ask.
     * @param range    Answer number must be in that range.
     * @return Number of the action chosen by user.
     */
    public int ask(String question, int[] range) {
        int key = Integer.valueOf(this.ask(question));
        boolean inRange = false;
        for (int item : range) {
            if (key == item) {
                inRange = true;
                break;
            }
        }
        if (inRange) {
            return key;
        } else {
            throw new MenuActionOutOfRangeException("Entered menu action is out of allowed range");
        }
    }

}
