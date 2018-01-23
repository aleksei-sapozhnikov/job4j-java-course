package ru.job4j.tracker;

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
}
