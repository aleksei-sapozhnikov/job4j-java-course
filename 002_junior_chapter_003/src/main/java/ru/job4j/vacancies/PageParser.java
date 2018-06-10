package ru.job4j.vacancies;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.*;
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

    public static final DateTimeFormatter DATE_TIME_STRING_FORMAT = DateTimeFormatter.ofPattern(
            "d MMM uu, kk:mm"
    ).withLocale(Locale.forLanguageTag("ru"));

    public static final DateTimeFormatter DATE_FORMAT_TODAY_YESTERDAY = DateTimeFormatter.ofPattern(
            "d MMM uu"
    ).withLocale(Locale.forLanguageTag("ru"));

    public static void main(String[] args) throws ParseException, IOException {
        PageParser parser = new PageParser();

        //List<Vacancy> found = parser.parseUrl("http://www.sql.ru/forum/job-offers");

        List<Vacancy> found = parser.parseFile(
                Paths.get("002_junior_chapter_003/src/main/resources/ru/job4j/vacancies/test_sources/test_parser_sample_1.htm")
        );

        for (Vacancy f : found) {
            System.out.println(f);
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
            Element theme = row.selectFirst("td.postslisttopic a[href]");
            Element published = row.selectFirst("td.altCol[style='text-align:center']");
            if (isValid(theme, published)) {
                result.add(new Vacancy(
                        theme.text(), theme.attr("abs:href"),
                        this.stringToDate(published.text()))
                );
            }
        }
        return result;
    }

    private LocalDateTime stringToDate(String date) throws ParseException {
        LocalDateTime result;
        if (date.startsWith(TODAY)) {
            result = this.parseDateString(
                    this.todayYesterdayToDate(date, true));
        } else if (date.startsWith(YESTERDAY)) {
            result = this.parseDateString(
                    this.todayYesterdayToDate(date, false));
        } else {
            result = this.parseDateString(date);
        }
        return result;
    }

    private LocalDateTime parseDateString(String date) {
        return LocalDateTime.parse(date, DATE_TIME_STRING_FORMAT);
    }

    private String todayYesterdayToDate(String string, boolean todayYesterday) {
        Instant day = todayYesterday
                ? Instant.now()
                : Instant.now().minus(1, ChronoUnit.DAYS);
        LocalDate date = ZonedDateTime.ofInstant(day, ZoneId.of("Europe/Moscow")).toLocalDate();
        String time = string.substring(todayYesterday
                ? TODAY.length()
                : YESTERDAY.length());
        return String.format("%s%s", date.format(DATE_FORMAT_TODAY_YESTERDAY), time);
    }

    private boolean isValid(Element theme, Element date) {
        boolean notNull = theme != null && date != null;
        String lower = notNull ? theme.text().toLowerCase() : "";
        return notNull
                && lower.contains("java")
                && !lower.contains("script");
    }

}
