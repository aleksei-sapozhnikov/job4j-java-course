package ru.job4j.vacancies;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DateParser {
    public static final ZoneId ZONE_ID = ZoneId.of("Europe/Moscow");

    public static final String TODAY = "сегодня";

    public static final String YESTERDAY = "вчера";

    public static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern(
            "d MMM uu, kk:mm"
    ).withLocale(Locale.forLanguageTag("ru"));

    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern(
            "d MMM uu"
    ).withLocale(Locale.forLanguageTag("ru"));

    public static final Map<String, String> MONTHS = new HashMap<>();

    {
        MONTHS.put("янв", "янв");
        MONTHS.put("фев", "фев");
        MONTHS.put("мар", "мар");
        MONTHS.put("апр", "апр");
        MONTHS.put("май", "мая");
        MONTHS.put("июн", "июн");
        MONTHS.put("июл", "июл");
        MONTHS.put("авг", "авг");
        MONTHS.put("сен", "сен");
        MONTHS.put("окт", "окт");
        MONTHS.put("ноя", "ноя");
        MONTHS.put("дек", "дек");
    }

    long stringToMillis(String date) {
        long result;
        if (date.startsWith(TODAY)) {
            result = this.normalStringToMillis(
                    this.todYestToNormal(date, true));
        } else if (date.startsWith(YESTERDAY)) {
            result = this.normalStringToMillis(
                    this.todYestToNormal(date, false));
        } else {
            result = this.normalStringToMillis(
                    this.otherToNormal(date));
        }
        return result;
    }

    private long normalStringToMillis(String normal) {
        return LocalDateTime.parse(normal, DATE_TIME_FORMAT)
                .atZone(ZONE_ID)
                .toInstant().toEpochMilli();
    }

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

    private String otherToNormal(String date) {
        String[] elements = date.split(" ");
        elements[1] = MONTHS.get(elements[1]);
        return String.format("%s %s %s %s",
                elements[0], elements[1], elements[2], elements[3]);
    }


}
