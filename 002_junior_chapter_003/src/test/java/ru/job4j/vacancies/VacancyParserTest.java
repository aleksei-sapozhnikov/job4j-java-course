package ru.job4j.vacancies;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class VacancyParserTest {
    public static final ZoneId ZONE_ID = ZoneId.of("Europe/Moscow");

    public static final String TEST_1 = "ru/job4j/vacancies/parser_sample_1.htm";


    @Test
    public void whenParseSampleFileGetsResultAsNeeded() throws IOException, ParseException, URISyntaxException {
        ClassLoader loader = VacancyParserTest.class.getClassLoader();
        String content;
        try (InputStream stream = loader.getResourceAsStream(TEST_1)) {
            content = this.inputStreamToString(stream);
        }
        VacancyParser parser = new VacancyParser();
        List<Vacancy> vacancies = parser.parseString(content);
        Vacancy today = vacancies.get(0);       // TEST_1: 12:09
        Vacancy yesterday = vacancies.get(1);   // TEST_1: 12:07
        Vacancy other = vacancies.get(4);       // TEST_1: 2018-06-05 23:51
        // 'сегодня, 12:09' in milliseconds
        Instant todayStart = LocalDate.now().atStartOfDay().atZone(ZONE_ID).toInstant();
        Instant todayTime = todayStart
                .plus(12, ChronoUnit.HOURS)
                .plus(9, ChronoUnit.MINUTES);
        long todayMillis = todayTime.toEpochMilli();
        // 'вчера, 12:07' in milliseconds
        Instant yesterdayStart = todayStart.minus(1, ChronoUnit.DAYS);
        Instant yesterdayTime = yesterdayStart
                .plus(12, ChronoUnit.HOURS)
                .plus(7, ChronoUnit.MINUTES);
        long yesterdayMillis = yesterdayTime.toEpochMilli();
        // '5 июн 18, 23:51' in milliseconds
        long otherMillis = LocalDateTime.of(2018, 6, 5, 23, 51)
                .atZone(ZONE_ID).toInstant().toEpochMilli();
        // assert
        assertThat(today.getPublished(), is(todayMillis));
        assertThat(yesterday.getPublished(), is(yesterdayMillis));
        assertThat(other.getPublished(), is(otherMillis));
    }


    private String inputStreamToString(InputStream stream) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = stream.read(buffer)) != -1) {
            bytes.write(buffer, 0, length);
        }
        return bytes.toString("windows-1251");
    }


}