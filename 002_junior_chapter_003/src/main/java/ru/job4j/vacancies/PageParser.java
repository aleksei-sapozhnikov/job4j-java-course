package ru.job4j.vacancies;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Page parser.
 */
public class PageParser {
    public static final String TODAY = "сегодня";
    public static final String YESTERDAY = "вчера";

    public static void main(String[] args) throws ParseException, IOException {
        PageParser parser = new PageParser();

        List<Vacancy> found = parser.parseFile(Paths.get(
                "002_junior_chapter_003/src/main/resources/ru/job4j/vacancies/test_sources/test_parser_sample_1.htm"
        ));

        List<Vacancy> found1 = parser.parseUrl("http://www.sql.ru/forum/job-offers");
        for (int i = 0; i < found.size(); i++) {
            System.out.println("F I L E: ");
            System.out.println(found.get(i));
            System.out.println("U R L: ");
            System.out.println(found1.get(i));
            System.out.println("===============================");
        }
    }


    public List<Vacancy> parseUrl(String address) throws IOException, ParseException {
        Document document = Jsoup.connect(address).get();
        return this.parseDocument(document);
    }

    public List<Vacancy> parseFile(Path file) throws IOException, ParseException {
        Document doc = Jsoup.parse(file.toFile(), null);
        return this.parseDocument(doc);
    }

    private List<Vacancy> parseDocument(Document document) throws ParseException {
        List<Vacancy> result = new LinkedList<>();
        Elements rows = document.select("tr");
        for (Element row : rows) {
            Element themeEl = row.selectFirst("td.postslisttopic a[href]");
            Element dateEl = row.selectFirst("td.altCol[style='text-align:center']");
            if (validateThemeAndDate(themeEl, dateEl)) {
                result.add(new Vacancy(
                        themeEl.text(),
                        themeEl.attr("abs:href"),
                        this.parseDateStringDispatch(dateEl.text())
                ));
            }
        }
        return result;
    }

    private LocalDateTime parseDateStringDispatch(String date) throws ParseException {
        LocalDateTime result;
        if (date.startsWith(TODAY)) {
            result = this.parseDateStringTodayYesterday(date, true);
        } else if (date.startsWith(YESTERDAY)) {
            result = this.parseDateStringTodayYesterday(date, false);
        } else {
            result = this.parseDateStringOtherDay(date);
        }
        return result;
    }

    private LocalDateTime parseDateStringOtherDay(String date) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern(
                "d MMM uu, kk:mm"
        ).withLocale(Locale.forLanguageTag("ru"));
        return LocalDateTime.parse(date, format);
    }

    private LocalDateTime parseDateStringTodayYesterday(String date, boolean todayYesterday)
            throws ParseException {
        LocalDateTime result;
        try {
            int startHour = date.length() - 5;
            int startMinute = date.length() - 2;
            int endHour = startHour + 2;
            int endMinute = startMinute + 2;
            int hour = Integer.valueOf(date.substring(startHour, endHour));
            int minute = Integer.valueOf(date.substring(startMinute, endMinute));
            result = this.getTimeAsObjectTodayYesterday(todayYesterday, hour, minute);
        } catch (NumberFormatException e) {
            throw new ParseException("Could not get integer hour or minute value from string", 0);
        }
        return result;
    }

    private LocalDateTime getTimeAsObjectTodayYesterday(boolean todayYesterday, int hour, int minute) {
        long fromMidnight = hour * 60 + minute;
        ZonedDateTime getDay = ZonedDateTime.ofInstant(
                todayYesterday
                        ? Instant.now()
                        : Instant.now().minus(1, ChronoUnit.DAYS),
                ZoneId.of("Europe/Moscow")
        );
        LocalDateTime startDay = getDay.toLocalDate().atStartOfDay();
        return startDay.plus(fromMidnight, ChronoUnit.MINUTES);
    }

    private boolean validateThemeAndDate(Element theme, Element date) {
        boolean notNull = theme != null && date != null;
        String lower = notNull ? theme.text().toLowerCase() : "";
        return notNull
                && lower.contains("java")
                && !lower.contains("script");
    }

}
