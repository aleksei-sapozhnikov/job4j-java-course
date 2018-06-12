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
 * Page parser.
 */
public class VacancyParser {
    public static final ZoneId ZONE_ID = ZoneId.of("Europe/Moscow");
    private final DateParser dateParser = new DateParser();

    public List<Vacancy> parseVacanciesFromNowToStartOfTheYear(String baseAddress, int year) throws IOException, ParseException {
        long yearStart = LocalDateTime.of(
                year, 1, 1, 0, 0
        ).atZone(ZONE_ID).toInstant().toEpochMilli();
        return this.parseVacanciesUntilOldestNeeded(baseAddress, yearStart);
    }

    public List<Vacancy> parseVacanciesFromNowToYesterday(String baseAddress) throws IOException, ParseException {
        long yesterdayStart = LocalDate.now().minus(1, ChronoUnit.DAYS)
                .atTime(0, 0).atZone(ZONE_ID)
                .toInstant().toEpochMilli();
        return this.parseVacanciesUntilOldestNeeded(baseAddress, yesterdayStart);
    }

    public List<Vacancy> parseVacanciesUntilOldestNeeded(String baseAddress, long oldestTime) throws IOException, ParseException {
        List<Vacancy> result = new LinkedList<>();
        boolean[] work = {true};
        int pageNum = 1;
        while (work[0]) {
            this.checkPageNum(pageNum, 600);
            String url = String.format("%s/%s", baseAddress, pageNum++);
            this.parseVacanciesAndCheckPublishedTime(url, result, oldestTime, work);
        }
        return result;
    }

    private void checkPageNum(int pageNum, int max) {
        if (pageNum > max) {
            throw new RuntimeException(String.format("Parser went too far (page #%s)", pageNum));
        }
    }

    private void parseVacanciesAndCheckPublishedTime(String url, List<Vacancy> result,
                                                     long oldestTime, boolean[] flagWork)
            throws IOException, ParseException {
        List<Vacancy> found = this.parseUrl(url);
        for (Vacancy vacancy : found) {
            if (vacancy.getUpdated() >= oldestTime) {
                result.add(vacancy);
            } else {
                flagWork[0] = false;
            }
        }
    }

    public List<Vacancy> parseUrl(String address) throws IOException, ParseException {
        Document document = Jsoup.connect(address).get();
        return this.parseDocument(document);
    }

    public List<Vacancy> parseString(String string) throws IOException, ParseException {
        Document doc = Jsoup.parse(string);
        return this.parseDocument(doc);
    }

    private List<Vacancy> parseDocument(Document document) throws ParseException {
        List<Vacancy> result = new LinkedList<>();
        Elements rows = document.select("tr");
        for (Element row : rows) {
            Element theme = row.selectFirst("td.postslisttopic a[href]");
            Element updated = row.selectFirst("td.altCol[style='text-align:center']");
            if (this.isElementValid(theme, updated)) {
                result.add(new Vacancy(
                        theme.text(), theme.attr("abs:href"),
                        this.dateParser.stringToMillis(updated.text()))
                );
            }
        }
        return result;
    }

    boolean isElementValid(Element theme, Element date) {
        boolean notNull = theme != null && date != null;
        String lower = notNull ? theme.text().toLowerCase() : "";
        return notNull
                && lower.contains("java")
                && !lower.contains("script");
    }
}
