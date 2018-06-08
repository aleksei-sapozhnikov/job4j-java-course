package ru.job4j.vacancies;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;

/**
 * Parses pages, finds needed entries and
 * returns them as Vacancy objects.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 08.06.2018
 */
public class VacancyParser {
    /**
     * Zone id used in the web resource "sql.ru".
     */
    public static final ZoneId ZONE_ID = ZoneId.of("Europe/Moscow");
    /**
     * Object having methods to parse date strings from
     * this web resource to milliseconds.
     */
    private final DateParser dateParser = new DateParser();

    /**
     * Parses resource from the first page and stops when Vacancies published time
     * becomes older then the beginning of the given year.
     * <p>
     * Assert that the vacancies on site are sorted as "from newest to oldest".
     *
     * @param baseAddress Base url address to look vacancies in. Assert that pages are
     *                    shown as numbers. For example if  base address
     *                    is "http://www.sql.ru/forum/job-offers", then
     *                    first page: "http://www.sql.ru/forum/job-offers/1",
     *                    second page: "http://www.sql.ru/forum/job-offers/2",
     *                    435-th page:  "http://www.sql.ru/forum/job-offers/435"
     * @param year        year to beginning of which to parse. E.g.: "2018"
     * @return List of Vacancy objects fulfilling time criteria.
     * @throws IOException    Signals that an I/O exception of some sort has occurred.
     * @throws ParseException Signals that an error has been reached unexpectedly
     *                        while parsing.
     */
    public List<Vacancy> parseVacanciesFromNowToStartOfTheYear(String baseAddress, int year) throws IOException, ParseException {
        long yearStart = LocalDateTime.of(
                year, 1, 1, 0, 0
        ).atZone(ZONE_ID).toInstant().toEpochMilli();
        return this.parseVacanciesUntilOldestNeeded(baseAddress, yearStart);
    }

    /**
     * Parses resource from the first page and stops when Vacancies published time
     * becomes older then the beginning of the previous day (yesterday).
     * <p>
     * Assert that the vacancies on site are sorted as "from newest to oldest".
     *
     * @param baseAddress Base url address to look vacancies in. Assert that pages are
     *                    shown as numbers. For example if  base address
     *                    is "http://www.sql.ru/forum/job-offers", then
     *                    first page: "http://www.sql.ru/forum/job-offers/1",
     *                    second page: "http://www.sql.ru/forum/job-offers/2",
     *                    435-th page:  "http://www.sql.ru/forum/job-offers/435"
     * @return List of Vacancy objects fulfilling time criteria.
     * @throws IOException    Signals that an I/O exception of some sort has occurred.
     * @throws ParseException Signals that an error has been reached unexpectedly
     *                        while parsing.
     */
    public List<Vacancy> parseVacanciesFromNowToYesterday(String baseAddress) throws IOException, ParseException {
        long yesterdayStart = LocalDate.now().minus(1, ChronoUnit.DAYS)
                .atTime(0, 0).atZone(ZONE_ID)
                .toInstant().toEpochMilli();
        return this.parseVacanciesUntilOldestNeeded(baseAddress, yesterdayStart);
    }

    /**
     * Parses resource from the first page and stops when Vacancies published time
     * becomes older then the given time.
     * <p>
     * Assert that the vacancies on site are sorted as "from newest to oldest". Assert that
     * parsing won't go further than page #600 (more than 5 years ago).
     *
     * @param baseAddress Base url address to look vacancies in. Assert that pages are
     *                    shown as numbers. For example if  base address
     *                    is "http://www.sql.ru/forum/job-offers", then
     *                    first page: "http://www.sql.ru/forum/job-offers/1",
     *                    second page: "http://www.sql.ru/forum/job-offers/2",
     *                    435-th page:  "http://www.sql.ru/forum/job-offers/435"
     * @param oldestTime  If vacancies time is "after" (bigger) then given, then it's ok.
     *                    Otherwise, parsing will be stopped after finishing this page.
     * @return List of Vacancy objects fulfilling time criteria.
     * @throws IOException      Signals that an I/O exception of some sort has occurred.
     * @throws ParseException   Signals that an error has been reached unexpectedly
     *                          while parsing.
     * @throws RuntimeException Shows that current page is too far, that shouldn't happen.
     */
    List<Vacancy> parseVacanciesUntilOldestNeeded(String baseAddress, long oldestTime) throws IOException, ParseException {
        List<Vacancy> result = new LinkedList<>();
        boolean work = true;
        int pageNum = 1;
        while (work) {
            this.checkPageNum(pageNum, 600);
            String url = String.format("%s/%s", baseAddress, pageNum++);
            work = this.parseVacanciesCheckPublishedTimeAndProceed(url, result, oldestTime);
        }
        return result;
    }

    /**
     * Checks if parsing did not go too far to an infinite loop.
     * <p>
     * If parsing goes too far, it indicates problems with 'stop-by-published-date'
     * criteria while parsing vacancies. They can't be so old to be on this pages
     * (or, maybe, you made such criteria).
     *
     * @param pageNum Current page number to parse.
     * @param max     Threshold value. If current page number is bigger than that,
     *                the RuntimeException is thrown.
     * @throws RuntimeException Shows that current page is too far, that shouldn't happen.
     */
    private void checkPageNum(int pageNum, int max) {
        if (pageNum > max) {
            throw new RuntimeException(String.format("Parser went too far (page #%s)", pageNum));
        }
    }

    /**
     * Parses one page given by html and checks every found Vacancy objects if it
     * is "newer" than the oldest time threshold given. Vacancies that match time
     * criteria are added to the given list.
     * <p>
     * When found that the vacancy is too "old", gives a signal to stop working by turning
     * "flagWork" to false.
     *
     * @param url        Url of page to parse.
     * @param result     List to add "matching" vacancies into.
     * @param oldestTime Vacancies published before that time won't match
     *                   and won't go to result list.
     * @return <tt>true</tt> if "too old" vacancies were not found, <tt>false</tt>
     * if found and it's time to stop parsing pages.
     * @throws IOException    Signals that an I/O exception of some sort has occurred.
     * @throws ParseException Signals that an error has been reached unexpectedly
     *                        while parsing.
     */
    private boolean parseVacanciesCheckPublishedTimeAndProceed(
            String url, List<Vacancy> result, long oldestTime)
            throws IOException, ParseException {
        boolean proceed = true;
        List<Vacancy> found = this.parseUrl(url);
        for (Vacancy vacancy : found) {
            if (vacancy.getUpdated() >= oldestTime) {
                result.add(vacancy);
            } else {
                proceed = false;
            }
        }
        return proceed;
    }

    /**
     * Parses html page given by url for vacancies.
     *
     * @param address Url of the page.
     * @return List of vacancies found.
     * @throws IOException    Signals that an I/O exception of some sort has occurred.
     * @throws ParseException Signals that an error has been reached unexpectedly
     *                        while parsing.
     */
    private List<Vacancy> parseUrl(String address) throws IOException, ParseException {
        Document document = Jsoup.connect(address).get();
        return this.parseDocument(document);
    }

    /**
     * Parses html page given by string for vacancies.
     *
     * @param string String with html contents.
     * @return List of vacancies found.
     * @throws ParseException Signals that an error has been reached unexpectedly
     *                        while parsing.
     */
    List<Vacancy> parseString(String string) throws ParseException {
        Document doc = Jsoup.parse(string);
        return this.parseDocument(doc);
    }


    /**
     * Parses jsoup document for vacancies.
     *
     * @param document JSOUP document object to parse.
     * @return List of vacancies found.
     */
    private List<Vacancy> parseDocument(Document document) {
        List<Vacancy> result = new LinkedList<>();
        Elements rows = document.select("tr");
        for (Element row : rows) {
            Element title = row.selectFirst("td.postslisttopic a[href]");
            Element updated = row.selectFirst("td.altCol[style='text-align:center']");
            if (this.isElementValid(title, updated)) {
                result.add(new Vacancy(
                        title.text(), title.attr("abs:href"),
                        this.dateParser.stringToMillis(updated.text()))
                );
            }
        }
        return result;
    }

    /**
     * Checks if elements found in document are not null
     * and fulfil criteria to add them to vacancy list.
     *
     * @param title Element with vacancy description and url. Check
     *              if it contains needed words.
     * @param date  Element with date string. Check if it is not null.
     * @return <tt>true</tt> if elements are good, <tt>false</tt> otherwise.
     */
    private boolean isElementValid(Element title, Element date) {
        boolean notNull = title != null && date != null;
        String lower = notNull ? title.text().toLowerCase() : "";
        return notNull
                && lower.contains("java")
                && !lower.contains("script");
    }
}
