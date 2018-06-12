package ru.job4j.vacancies;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Date parser class which converts strings like: "31 май 11, 22:44",
 * "сегодня, 07:59", "вчера, 12:07" to date in milliseconds.
 * <p>
 * Had to make it because parsing appeared to take a lot of code and
 * sub-methods.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 08.06.2018
 */
public class DateParser {
    /**
     * Zone id used in the web-resource "sql.ru".
     */
    public static final ZoneId ZONE_ID = ZoneId.of("Europe/Moscow");
    /**
     * Start of the date string meaning "today".
     */
    public static final String TODAY = "сегодня";
    /**
     * Start of the date string meaning "yesterday".
     */
    public static final String YESTERDAY = "вчера";
    /**
     * Format of date and time used in the web-resource.
     * Example: "3 окт 16, 13:21"
     */
    public static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern(
            "d MMM uu, kk:mm"
    ).withLocale(Locale.forLanguageTag("ru"));
    /**
     * Date format used in this website. Needed to create strings to change
     * "today" and "yesterday" strings. For example: if today is June 12, 2018, than
     * "сегодня" will change to "12 июн 18".
     */
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern(
            "d MMM uu"
    ).withLocale(Locale.forLanguageTag("ru"));
    /**
     * Map converting month name string on web-site to "normal" which can be parsed
     * by LocalDateTimeFormatter.
     * <p>
     * The main reason if "май" on site which could not be parsed.
     */
    private final Map<String, String> MONTHS_CONVERT = new HashMap<String, String>() {
        {
            put("янв", "янв");
            put("фев", "фев");
            put("мар", "мар");
            put("апр", "апр");
            put("май", "мая");
            put("июн", "июн");
            put("июл", "июл");
            put("авг", "авг");
            put("сен", "сен");
            put("окт", "окт");
            put("ноя", "ноя");
            put("дек", "дек");
        }
    };

    /**
     * Converts given string to datetime in milliseconds. String can be in formats:
     * "31 май 11, 22:44", "сегодня, 07:59", "вчера, 12:07".
     *
     * @param string String to convert.
     * @return Datetime in milliseconds.
     */
    long stringToMillis(String string) {
        long result;
        if (string.startsWith(TODAY)) {
            result = this.normalStringToMillis(
                    this.todYestToNormal(string, true));
        } else if (string.startsWith(YESTERDAY)) {
            result = this.normalStringToMillis(
                    this.todYestToNormal(string, false));
        } else {
            result = this.normalStringToMillis(
                    this.otherToNormal(string));
        }
        return result;
    }

    /**
     * Parses string in "normal" format and returns
     * datetime in milliseconds.
     * <p>
     * Example of the "normal" format: "25 мая 17, 12:43".
     *
     * @param normal String in "normal" format.
     * @return Found datetime in milliseconds.
     */
    private long normalStringToMillis(String normal) {
        return LocalDateTime.parse(normal, DATE_TIME_FORMAT)
                .atZone(ZONE_ID)
                .toInstant().toEpochMilli();
    }

    /**
     * Converts string starting with "today" or "yesterday" to
     * "normal" date string format which can be parsed by LocalDateTimeFormatter..
     *
     * @param string         String starting with "today" or "yesterday".
     * @param todayYesterday if <tt>true</tt>, we are converting "today" string,
     *                       if <tt>false</tt> - "yesterday" string.
     * @return "Normal" date string which can be parsed as usual.
     */
    private String todYestToNormal(String string, boolean todayYesterday) {
        ZonedDateTime day = todayYesterday
                ? Instant.now().atZone(ZONE_ID)
                : Instant.now().minus(1, ChronoUnit.DAYS).atZone(ZONE_ID);
        LocalDate date = day.toLocalDate();
        String time = string.substring(todayYesterday
                ? TODAY.length()
                : YESTERDAY.length());
        return String.format("%s%s", date.format(DATE_FORMAT), time);
    }

    /**
     * Converts dateTime string from web-site to "normal" datetime
     * string which can be parsed by LocalDateTimeFormatter.
     *
     * @param other Datetime string to convert.
     * @return "Nornal" datetime string.
     */
    private String otherToNormal(String other) {
        String[] elements = other.split(" ");
        elements[1] = MONTHS_CONVERT.get(elements[1]);
        return String.format("%s %s %s %s",
                elements[0], elements[1], elements[2], elements[3]);
    }

}
