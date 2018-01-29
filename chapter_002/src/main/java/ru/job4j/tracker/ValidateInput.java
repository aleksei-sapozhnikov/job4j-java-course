package ru.job4j.tracker;

public class ValidateInput extends ConsoleInput {

    /**
     * Ask question and get answer from user what to do.
     * Answer must be an integer in a definite range.
     * Method validates input and insists user to input correct value.
     *
     * @param question Question we ask.
     * @param range    Answer number must be in that range.
     * @return Number (key) of the action chosen by user.
     */
    public int ask(String question, int[] range) {
        boolean valid = false;
        int value = -1;
        do {
            try {
                value = super.ask(question, range);
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
