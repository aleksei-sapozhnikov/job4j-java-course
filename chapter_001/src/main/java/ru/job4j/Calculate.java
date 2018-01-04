package ru.job4j;

/**
 * Simple class to print messages.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 04.01.2018
 */
public class Calculate {

    /**
     * Main method - program starts from here.
     *
     * @param args - arguments.
     */
    public static void main(String[] args) {
        System.out.println("Hello world.");
    }

    /**
     * Method echo.
     *
     * @param name Your name.
     * @return Echo plus your name.
     */
    public String echo(String name) {
        return "Echo, echo, echo : " + name;
    }

}
