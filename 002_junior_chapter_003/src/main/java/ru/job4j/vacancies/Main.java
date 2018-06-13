package ru.job4j.vacancies;

import java.util.Scanner;

/**
 * Starts the application.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 08.06.2018
 */
public class Main {
    /**
     * Default base url value.
     */
    public static final String DEFAULT_BASE_URL = "http://www.sql.ru/forum/job-offers";
    /**
     * Default database config value.
     */
    public static final String DEFAULT_DB_CONFIG = "ru/job4j/vacancies/main.properties";
    /**
     * Default database config value.
     */
    public static final long DEFAULT_SLEEP_TIME = 24 * 3600_000;

    /**
     * Runnable objects doing all work.
     */
    private final ActionsRunnable actionsRunnable;

    /**
     * Constructs new object with parameters.
     *
     * @param baseUrl   Base url address to look vacancies in. Assert that pages are
     *                  shown as numbers.
     *                  For example if  base address is "http://www.sql.ru/forum/job-offers", then
     *                  first page: "http://www.sql.ru/forum/job-offers/1",
     *                  second page: "http://www.sql.ru/forum/job-offers/2",
     *                  435-th page:  "http://www.sql.ru/forum/job-offers/435"
     * @param dbConfig  Path to the config file for database storing vacancies.
     *                  The file will be read by Classloader.
     *                  Example format: 'ru/job4j/vacancies/main.properties'.
     * @param sleepTime Time to wait between "everyday actions" cycle.
     *                  E.g. : 'time = 24 * 3600_000' means 24 hours.
     */
    public Main(String baseUrl, String dbConfig, long sleepTime) {
        this.actionsRunnable = new ActionsRunnable(baseUrl, dbConfig, sleepTime);
    }

    /**
     * Starts the application and runs thread.
     *
     * @param args command-line arguments. If given <3 arguments, default
     *             values will be taken instead. Arguments are: 1) base url
     *             (default: "http://www.sql.ru/forum/job-offers"),
     *             2) database config file (default: "main.properties'),
     *             3) sleep time between actions (default: 24 hours).
     */
    public static void main(String[] args) {
        Main main = args.length < 3
                ? new Main(DEFAULT_BASE_URL, DEFAULT_DB_CONFIG, DEFAULT_SLEEP_TIME)
                : new Main(args[0], args[1], Long.valueOf(args[3]));
        Thread actions = new Thread(main.actionsRunnable);
        actions.start();
        main.waitForStop();
        main.actionsRunnable.markStop();
        actions.interrupt();
    }

    /**
     * Waits for a signal to stop working.
     */
    private void waitForStop() {
        Scanner scanner = new Scanner(System.in);
        String value;
        do {
            System.out.print("Enter \"stop\" to stop working: ");
            value = scanner.nextLine();
        } while (!"stop".equals(value));
    }

}
