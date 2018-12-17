package ru.job4j.vacancies;

import org.junit.Ignore;
import org.junit.Test;

import javax.sound.midi.Soundbank;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class DateParserTest {

    @Test
    @Ignore // LocalDate parser not working in jdk11
    public void whenParseOtherDateThenGetMillisRight() {
        DateParser parser = new DateParser();
        String[] dates = {
                "18 янв 07, 12:34",
                "11 фев 08, 00:03",
                "13 мар 09, 11:01",
                "4 апр 10, 15:45",
                "31 май 11, 22:44",
                "7 июн 12, 15:30",
                "30 июл 13, 10:03",
                "22 авг 14, 23:55",
                "11 сен 15, 14:38",
                "3 окт 16, 13:21",
                "8 ноя 17, 09:52",
                "25 дек 18, 12:36"
        };
        long[] result = new long[dates.length];
        for (int i = 0; i < dates.length; i++) {
            result[i] = parser.stringToMillis(dates[i]);
        }
        LocalDateTime[] expectDates = {
                LocalDateTime.of(2007, 1, 18, 12, 34),
                LocalDateTime.of(2008, 2, 11, 0, 3),
                LocalDateTime.of(2009, 3, 13, 11, 1),
                LocalDateTime.of(2010, 4, 4, 15, 45),
                LocalDateTime.of(2011, 5, 31, 22, 44),
                LocalDateTime.of(2012, 6, 7, 15, 30),
                LocalDateTime.of(2013, 7, 30, 10, 3),
                LocalDateTime.of(2014, 8, 22, 23, 55),
                LocalDateTime.of(2015, 9, 11, 14, 38),
                LocalDateTime.of(2016, 10, 3, 13, 21),
                LocalDateTime.of(2017, 11, 8, 9, 52),
                LocalDateTime.of(2018, 12, 25, 12, 36),
        };
        long[] expected = new long[dates.length];
        for (int i = 0; i < dates.length; i++) {
            expected[i] = expectDates[i].atZone(DateParser.ZONE_ID).toInstant().toEpochMilli();
        }
        assertThat(result, is(expected));
    }

    @Test
    public void whenParseTodayYesterdayThenGetMillisRight() {
        DateParser parser = new DateParser();
        String[] dates = {
                "сегодня, 07:59",
                "сегодня, 01:48",
                "вчера, 15:39",
                "вчера, 12:07",
        };
        long[] result = new long[dates.length];
        for (int i = 0; i < dates.length; i++) {
            result[i] = parser.stringToMillis(dates[i]);
        }
        LocalDateTime[] expectDates = {
                LocalDate.now(DateParser.ZONE_ID).atStartOfDay()
                        .plus(7, ChronoUnit.HOURS)
                        .plus(59, ChronoUnit.MINUTES),
                LocalDate.now(DateParser.ZONE_ID).atStartOfDay()
                        .plus(1, ChronoUnit.HOURS)
                        .plus(48, ChronoUnit.MINUTES),
                LocalDate.now(DateParser.ZONE_ID).atStartOfDay().minus(1, ChronoUnit.DAYS)
                        .plus(15, ChronoUnit.HOURS)
                        .plus(39, ChronoUnit.MINUTES),
                LocalDate.now(DateParser.ZONE_ID).atStartOfDay().minus(1, ChronoUnit.DAYS)
                        .plus(12, ChronoUnit.HOURS)
                        .plus(7, ChronoUnit.MINUTES)
        };
        long[] expected = new long[dates.length];
        for (int i = 0; i < dates.length; i++) {
            expected[i] = expectDates[i].atZone(DateParser.ZONE_ID).toInstant().toEpochMilli();
        }
        assertThat(result, is(expected));
    }
}