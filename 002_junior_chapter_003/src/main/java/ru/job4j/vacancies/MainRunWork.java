package ru.job4j.vacancies;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

public class MainRunWork extends Thread {
    public static final String BASE_ADDRESS = "http://www.sql.ru/forum/job-offers";
    public static final String DATABASE_CONFIG = "/ru/job4j/vacancies/test.properties";
    private volatile boolean notFirstRun = false;
    private volatile boolean work = true;

    @Override
    public void run() {
        while (this.work) {
            try {
                doEverydayActions();
                Thread.sleep(24 * 3600_000);    // 24 hours
            } catch (IOException | SQLException | ParseException | InterruptedException e) {
                e.printStackTrace();
                this.work = false;
            }
        }
    }

    private void doEverydayActions() throws IOException, SQLException, ParseException {
        List<Vacancy> found;
        if (notFirstRun) {
            found = this.parse(BASE_ADDRESS, true);
        } else {
            found = this.parse(BASE_ADDRESS, false);
            this.notFirstRun = true;
        }
        this.store(found);
    }

    private List<Vacancy> parse(String baseAddress, boolean yesterdayOrYear) throws IOException, ParseException {
        VacancyParser parser = new VacancyParser();
        return yesterdayOrYear
                ? parser.parseVacanciesFromNowToYesterday(baseAddress)
                : parser.parseVacanciesFromNowToStartOfTheYear(baseAddress, LocalDate.now().getYear());
    }

    private void store(List<Vacancy> vacancies) throws IOException, SQLException {
        try (VacancyStorage storage = new VacancyStorage(DATABASE_CONFIG)) {
            storage.addOrUpdateAll(vacancies);
        }
    }

    public void stopWorking() {
        this.work = false;
        Thread.currentThread().interrupt();
    }


}
