package ru.job4j.vacancies;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
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
public class VacancyParser {
    public static final ZoneId ZONE_ID = ZoneId.of("Europe/Moscow");

    public static final String TODAY = "сегодня";

    public static final String YESTERDAY = "вчера";

    public static final DateTimeFormatter DATE_TIME_STRING_FORMAT = DateTimeFormatter.ofPattern(
            "d MMM uu, kk:mm"
    ).withLocale(Locale.forLanguageTag("ru"));

    public static final DateTimeFormatter DATE_FORMAT_TODAY_YESTERDAY = DateTimeFormatter.ofPattern(
            "d MMM uu"
    ).withLocale(Locale.forLanguageTag("ru"));

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

    private long stringToDate(String date) throws ParseException {
        long result;
        if (date.startsWith(TODAY)) {
            result = this.dateStringToMillis(
                    this.tyToDateString(date, true));
        } else if (date.startsWith(YESTERDAY)) {
            result = this.dateStringToMillis(
                    this.tyToDateString(date, false));
        } else {
            result = this.dateStringToMillis(date);
        }
        return result;
    }

    private long dateStringToMillis(String date) {
        return LocalDateTime.parse(date, DATE_TIME_STRING_FORMAT)
                .atZone(ZONE_ID)
                .toInstant().toEpochMilli();
    }

    private String tyToDateString(String string, boolean todayYesterday) {
        Instant day = todayYesterday
                ? Instant.now()
                : Instant.now().minus(1, ChronoUnit.DAYS);
        LocalDate date = ZonedDateTime.ofInstant(day, ZONE_ID).toLocalDate();
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
