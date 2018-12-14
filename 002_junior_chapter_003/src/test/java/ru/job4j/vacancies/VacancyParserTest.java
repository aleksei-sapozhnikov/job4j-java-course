package ru.job4j.vacancies;

import org.junit.Test;
import ru.job4j.CommonMethods;
import ru.job4j.util.methods.CommonUtils;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class VacancyParserTest {
    public static final ZoneId ZONE_ID = ZoneId.of("Europe/Moscow");

    public static final String TEST_1 = "ru/job4j/vacancies/parser_sample_1.htm";

    public static final CommonMethods METHODS = CommonMethods.getInstance();

    /**
     * Test parseString(). Checking if vacancies fields are filled properly
     * when parsing sample file.
     * <p>
     * Three variants of date: when it is written
     * as 'today', 'yesterday' and other days, as in site.
     */
    @Test
    public void whenParseSampleFileGetVacanciesRight() throws IOException, ParseException {
        // get sample and parser
        VacancyParser parser = new VacancyParser();
        ClassLoader loader = VacancyParserTest.class.getClassLoader();
        String content;
        try (InputStream stream = loader.getResourceAsStream(TEST_1)) {
            content = CommonUtils.inputStreamToString(stream, "windows-1251");
        }
        // get vacancies
        List<Vacancy> vacancies = parser.parseString(content);
        Vacancy today = vacancies.get(0);
        Vacancy yesterday = vacancies.get(1);
        Vacancy other = vacancies.get(4);
        // today: 'сегодня, 12:09'
        Instant tStart = ZonedDateTime.now(ZONE_ID).toLocalDate().atStartOfDay().atZone(ZONE_ID).toInstant();
        Instant tTime = tStart
                .plus(12, ChronoUnit.HOURS)
                .plus(9, ChronoUnit.MINUTES);
        long todayMillis = tTime.toEpochMilli();
        // yesterday: 'вчера, 12:07'
        Instant yStart = tStart.minus(1, ChronoUnit.DAYS);
        Instant yTime = yStart
                .plus(12, ChronoUnit.HOURS)
                .plus(7, ChronoUnit.MINUTES);
        long yesterdayMillis = yTime.toEpochMilli();
        // other date: '5 июн 18, 23:51'
        long otherMillis = LocalDateTime.of(
                2018, 6, 5, 23, 51)
                .atZone(ZONE_ID).toInstant().toEpochMilli();
        //today
        assertThat(today.getId(), is(-1));
        assertThat(today.getUpdated(), is(todayMillis));
        assertThat(today.getTitle(), is(
                "Java Developer/J2EE-Разработчик, Москва, 150-200, УДАЛЕННО"));
        assertThat(today.getUrl(), is(
                "http://www.sql.ru/forum/1294701/java-developer-j2ee-razrabotchik-moskva-150-200-udalenno"));
        // yesterday
        assertThat(yesterday.getId(), is(-1));
        assertThat(yesterday.getUpdated(), is(yesterdayMillis));
        assertThat(yesterday.getTitle(), is(
                "(КА) Java разработчик (г. Воронеж)"));
        assertThat(yesterday.getUrl(), is(
                "http://www.sql.ru/forum/1294451/ka-java-razrabotchik-g-voronezh"));
        // other
        assertThat(other.getId(), is(-1));
        assertThat(other.getUpdated(), is(otherMillis));
        assertThat(other.getTitle(), is(
                "Java разработчики уровней Junior и Middle, з/п до 150000 Gross Мск Динамо или Зеленоград"));
        assertThat(other.getUrl(), is(
                "http://www.sql.ru/forum/1295227/java-razrabotchiki-urovney-junior-i-middle-z-p-do-150000-gross-msk-dinamo-ili-zelenograd"));
    }
}