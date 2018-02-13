package tracker;

/**
 * Takes user input and checks if it is valid.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 29.01.2018
 */
public class ValidateInput implements Input {

    /**
     * Where class takes user-input from.
     */
    private final Input input;

    /**
     * Constructor.
     *
     * @param input Where we take user-input from.
     */
    public ValidateInput(final Input input) {
        this.input = input;
    }

    /**
     * Ask question and get answer from user.
     *
     * @param question Question we ask.
     * @return User answer string.
     */
    @Override
    public String ask(String question) {
        return this.input.ask(question);
    }

    /**
     * Ask question and get answer from user what to do.
     * Answer must be an integer in a definite range.
     * Method validates input and insists user to input correct value.
     *
     * @param question Question we ask.
     * @param range    Answer number must be in that range.
     * @return Number (key) of the action chosen by user.
     */
    @Override
    public int ask(String question, int[] range) {
        boolean valid = false;
        int value = -1;
        do {
            try {
                value = this.input.ask(question, range);
                valid = true;
            } catch (NumberFormatException npe) {
                System.out.println("=== Exception : Unknown action. Enter correct value.");
                System.out.println();
            } catch (MenuActionOutOfRangeException mre) {
                System.out.println("=== Exception : Action number is out of range in menu. Enter correct value.");
                System.out.println();
            }
        } while (!valid);
        return value;
    }
}
