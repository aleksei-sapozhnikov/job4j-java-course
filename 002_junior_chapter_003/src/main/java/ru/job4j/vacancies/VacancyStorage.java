package ru.job4j.vacancies;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class VacancyStorage implements AutoCloseable {
    private final Connection connection;

    private final String sqlDropAllTables;
    private final String sqlCreateTables;
    private final String sqlAddVacancy;
    private final String sqlFindVacancyById;

    public VacancyStorage(String config) throws IOException, SQLException {
        this(config, false);
    }

    VacancyStorage(String config, boolean eraseValues) throws IOException, SQLException {
        Properties prop = UsefulMethods.loadProperties(this, config);
        String pkg = prop.getProperty("pkg.resources");
        this.connection = UsefulMethods.getConnectionToDatabase(
                prop.getProperty("db.type"), prop.getProperty("db.address"), prop.getProperty("db.name"),
                prop.getProperty("db.user"), prop.getProperty("db.password"));
        this.sqlDropAllTables = UsefulMethods.loadSqlScript(this, pkg, prop.getProperty("sql.drop_all_tables"), "UTF-8");
        this.sqlCreateTables = UsefulMethods.loadSqlScript(this, pkg, prop.getProperty("sql.create_tables"), "UTF-8");
        this.sqlAddVacancy = UsefulMethods.loadSqlScript(this, pkg, prop.getProperty("sql.add_vacancy"), "UTF-8");
        this.sqlFindVacancyById = UsefulMethods.loadSqlScript(this, pkg, prop.getProperty("sql.find_vacancy_by_id"), "UTF-8");
        if (eraseValues) {
            this.dbDropAllTables();
        }
        this.init();
    }

    private void init() throws SQLException {
        this.dbCreateTables();
    }

    private void dbDropAllTables() throws SQLException {
        UsefulMethods.dbPerformUpdate(this.connection, this.sqlDropAllTables);
    }

    private void dbCreateTables() throws SQLException {
        UsefulMethods.dbPerformUpdate(this.connection, this.sqlCreateTables);
    }

    public int add(Vacancy vacancy) throws SQLException {
        int id;
        String query = String.format(this.sqlAddVacancy,
                vacancy.getTheme(), vacancy.getUrl(), new java.sql.Timestamp(vacancy.getPublished())
        );
        try (ResultSet result = this.connection.createStatement().executeQuery(query)) {
            result.next();
            id = result.getInt(1);
        }
        return id;
    }

    public Vacancy findById(int id) throws SQLException {
        Vacancy result;
        String query = String.format(this.sqlFindVacancyById, id);
        try (ResultSet res = this.connection.createStatement().executeQuery(query)) {
            res.next();
            result = new Vacancy(
                    res.getInt(1),
                    res.getString(2),
                    res.getString(3),
                    res.getTimestamp(4).getTime()
            );
        }
        return result;
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
