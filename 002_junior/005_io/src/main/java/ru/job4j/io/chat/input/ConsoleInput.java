package ru.job4j.io.chat.input;

import java.util.Scanner;

/**
 * Class to invite user for input and take it back.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class ConsoleInput implements Input, AutoCloseable {
    /**
     * Scanner to read user input.
     */
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Prints input invite and gets input from user.
     *
     * @param invite Invite message for input.
     * @return User input string.
     */
    @Override
    public String get(String invite) {
        System.out.print(invite);
        return this.scanner.nextLine();
    }

    /**
     * Closes resources.
     */
    @Override
    public void close() {
        this.scanner.close();
    }
}
