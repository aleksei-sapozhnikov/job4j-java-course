package ru.job4j.vacancies;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

/**
 * Runnable object to run once a needed time, parse web pages for
 * vacancies and store them to database.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 08.06.2018
 */
public class ActionsRunnable implements Runnable {
    /**
     * Base url address to look vacancies in. Assert that pages are
     * shown as numbers.
     * <p>
     * For example if  base address is "http://www.sql.ru/forum/job-offers", then
     * first page: "http://www.sql.ru/forum/job-offers/1",
     * second page: "http://www.sql.ru/forum/job-offers/2",
     * 435-th page:  "http://www.sql.ru/forum/job-offers/435"
     */
    private final String baseUrl;
    /**
     * Path to the config file for database storing vacancies.
     * The file will be read by Classloader.
     * <p>
     * Example format: 'ru/job4j/vacancies/main.properties'.
     */
    private final String dbConfig;
    /**
     * Time to wait between "everyday actions" cycle.
     * <p>
     * E.g. : 'time = 24 * 3600_000' means 24 hours.
     */
    private final long sleepTime;
    /**
     * Flag showing if this is the first "everyday" cycle or not.
     */
    private volatile boolean firstRun = true;
    /**
     * Flag showing that the thread should continue working.
     */
    private volatile boolean work = true;

    /**
     * Constructs new Runnable object with parameters.
     *
     * @param baseUrl   Base url address to look vacancies in. Assert that pages are
     *                  shown as numbers. For example if  base address is "http://www.sql.ru/forum/job-offers", then
     *                  first page: "http://www.sql.ru/forum/job-offers/1",
     *                  second page: "http://www.sql.ru/forum/job-offers/2",
     *                  435-th page:  "http://www.sql.ru/forum/job-offers/435"
     * @param dbConfig  Config file path for database, will be read by Classloader.
     *                  Example format: 'ru/job4j/vacancies/main.properties'.
     * @param sleepTime How long to wait between "everyday cycles".
     */
    public ActionsRunnable(String baseUrl, String dbConfig, long sleepTime) {
        this.baseUrl = baseUrl;
        this.dbConfig = dbConfig;
        this.sleepTime = sleepTime;
    }

    /**
     * Method doing "everyday cycle": parses pages for vacancies,
     * stores them in database.
     *
     * @throws IOException    Signals that an I/O exception of some sort has occurred.
     * @throws ParseException Signals that an error has been reached unexpectedly
     *                        while parsing.
     * @throws SQLException   Provides information on a database access
     *                        error or other errors.
     */
    void doEverydayActions() throws IOException, SQLException, ParseException {
        List<Vacancy> found;
        if (firstRun) {
            found = this.parse(baseUrl, false);
            this.firstRun = false;
        } else {
            found = this.parse(baseUrl, true);
        }
        this.store(found);
    }

    /**
     * Runs parsing for vacancies checking if it is the first run or not.
     *
     * @param baseUrl         Base url address to look vacancies in. Assert that pages are
     *                        shown as numbers. For example if  base address is "http://www.sql.ru/forum/job-offers", then
     *                        first page: "http://www.sql.ru/forum/job-offers/1",
     *                        second page: "http://www.sql.ru/forum/job-offers/2",
     *                        435-th page:  "http://www.sql.ru/forum/job-offers/435"
     * @param yesterdayOrYear If <tt>true</tt> the oldest needed vacancy is "beginning of yesterday",
     *                        if <tt>false</tt> the oldest needed vacancy is "beginning of this year".
     * @return List of found vacancies matching time criteria.
     * @throws IOException    Signals that an I/O exception of some sort has occurred.
     * @throws ParseException Signals that an error has been reached unexpectedly
     *                        while parsing.
     */
    private List<Vacancy> parse(String baseUrl, boolean yesterdayOrYear) throws IOException, ParseException {
        VacancyParser parser = new VacancyParser();
        return yesterdayOrYear
                ? parser.parseVacanciesFromNowToYesterday(baseUrl)
                : parser.parseVacanciesFromNowToStartOfTheYear(baseUrl, LocalDate.now().getYear());
    }

    /**
     * Stores found vacancies in database or updates them if they are already
     * existing in the database.
     *
     * @param vacancies List of vacancies to add or update in database.
     * @throws IOException  Signals that an I/O exception of some sort has occurred.
     * @throws SQLException Provides information on a database access
     *                      error or other errors.
     */
    private void store(List<Vacancy> vacancies) throws IOException, SQLException {
        try (VacancyStorage storage = new VacancyStorage(dbConfig)) {
            storage.addOrUpdateAll(vacancies);
        }
    }

    /**
     * Runs all actions.
     */
    @Override
    public void run() {
        try {
            while (this.work) {
                doEverydayActions();
                Thread.sleep(this.sleepTime);
            }
        } catch (InterruptedException | IOException | SQLException | ParseException e) {
            e.printStackTrace();
            this.work = false;
        }
    }

    /**
     * Stops this thread from running. Interrupts waiting.
     */
    public void markStop() {
        this.work = false;
        Thread.currentThread().interrupt();
    }


}
