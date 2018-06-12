package ru.job4j.vacancies;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class VacancyStorage implements AutoCloseable {
    private final Connection connection;

    private final Map<String, String> sqlQueries = new HashMap<>();

    public VacancyStorage(String config) throws IOException, SQLException {
        this(config, false);
    }

    VacancyStorage(String config, boolean eraseExisting) throws IOException, SQLException {
        Properties prop = UsefulMethods.loadProperties(this, config);
        this.connection = UsefulMethods.getConnectionToDatabase(
                prop.getProperty("db.type"), prop.getProperty("db.address"), prop.getProperty("db.name"),
                prop.getProperty("db.user"), prop.getProperty("db.password"));
        String pkg = prop.getProperty("sql.package");
        this.loadSqlQueries(pkg, prop);
        this.initDatabase(eraseExisting);
    }

    private void loadSqlQueries(String pkg, Properties prop) throws IOException {
        this.sqlQueries.put("dropTables",
                UsefulMethods.loadSqlScript(this, pkg, prop.getProperty("sql.drop_all_tables"), "UTF-8"));
        this.sqlQueries.put("createTables",
                UsefulMethods.loadSqlScript(this, pkg, prop.getProperty("sql.create_tables"), "UTF-8"));
        this.sqlQueries.put("addVacancy",
                UsefulMethods.loadSqlScript(this, pkg, prop.getProperty("sql.add_vacancy"), "UTF-8"));
        this.sqlQueries.put("findVacancyById",
                UsefulMethods.loadSqlScript(this, pkg, prop.getProperty("sql.find_vacancy_by_id"), "UTF-8"));
        this.sqlQueries.put("findIdByTitleAndUrl",
                UsefulMethods.loadSqlScript(this, pkg, prop.getProperty("sql.find_id_by_title_and_url"), "UTF-8"));
        this.sqlQueries.put("updateVacancy",
                UsefulMethods.loadSqlScript(this, pkg, prop.getProperty("sql.update_vacancy"), "UTF-8"));
    }

    private void initDatabase(boolean eraseExisting) throws SQLException {
        if (eraseExisting) {
            this.dbDropAllTables();
        }
        this.dbCreateTables();
    }

    private void dbDropAllTables() throws SQLException {
        UsefulMethods.dbPerformUpdate(this.connection, this.sqlQueries.get("dropTables"));
    }

    private void dbCreateTables() throws SQLException {
        UsefulMethods.dbPerformUpdate(this.connection, this.sqlQueries.get("createTables"));
    }

    public void addOrUpdateAll(List<Vacancy> vacancies) throws SQLException {
        for (Vacancy vacancy : vacancies) {
            this.addOrUpdate(vacancy);
        }
    }

    public int addOrUpdate(Vacancy vacancy) throws SQLException {
        int id = this.findIdByTitleAndUrl(vacancy.getTitle(), vacancy.getUrl());
        if (id == -1) {
            id = this.add(vacancy);
        } else {
            this.update(id, vacancy);
        }
        return id;
    }

    int add(Vacancy vacancy) throws SQLException {
        int id;
        String query = String.format(this.sqlQueries.get("addVacancy"),
                vacancy.getTitle(), vacancy.getUrl(), new java.sql.Timestamp(vacancy.getUpdated())
        );
        try (ResultSet result = this.connection.createStatement().executeQuery(query)) {
            result.next();
            id = result.getInt(1);
        }
        return id;
    }

    void update(int id, Vacancy update) throws SQLException {
        String query = String.format(this.sqlQueries.get("updateVacancy"),
                update.getTitle(), update.getUrl(),
                new java.sql.Timestamp(update.getUpdated()), id);
        UsefulMethods.dbPerformUpdate(this.connection, query);
    }

    public Vacancy findById(int id) throws SQLException {
        Vacancy result = null;
        String query = String.format(this.sqlQueries.get("findVacancyById"), id);
        try (ResultSet res = this.connection.createStatement().executeQuery(query)) {
            if (res.next()) {
                result = new Vacancy(
                        res.getInt(1),
                        res.getString(2),
                        res.getString(3),
                        res.getTimestamp(4).getTime());
            }
        }
        return result;
    }

    public int findIdByTitleAndUrl(String title, String url) throws SQLException {
        int id = -1;
        String query = String.format(this.sqlQueries.get("findIdByTitleAndUrl"), title, url);
        try (ResultSet res = this.connection.createStatement().executeQuery(query)) {
            if (res.next()) {
                id = res.getInt(1);
            }
        }
        return id;
    }

    /**
     * Closes this resource connection. Is invoked automatically on
     * objects managed by the <tt>try-with-resources</tt> statement.
     *
     * @throws SQLException Provides information on a database access
     *                      error or other errors.
     */
    @Override
    public void close() throws SQLException {
        if (this.connection != null) {
            this.connection.close();
        }
    }
}
