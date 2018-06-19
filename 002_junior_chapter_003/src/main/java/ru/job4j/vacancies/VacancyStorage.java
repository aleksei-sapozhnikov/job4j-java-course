package ru.job4j.vacancies;

import ru.job4j.CommonMethods;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Stores/gets Vacancies to/from database.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 08.06.2018
 */
public class VacancyStorage implements AutoCloseable {
    /**
     * Common useful methods used in different classes.
     */
    public static final CommonMethods METHODS = CommonMethods.getInstance();
    /**
     * Database connection.
     */
    private final Connection connection;
    /**
     * Map containing sql queries.
     */
    private final Map<String, String> sqlQueries = new HashMap<>();

    /**
     * Constructs new VacancyStore object and connects it to database.
     *
     * @param config Config file path for database, will be read by Classloader.
     *               Example format: 'ru/job4j/vacancies/main.properties'.
     * @throws IOException  Signals that an I/O exception of some sort has occurred.
     * @throws SQLException Provides information on a database access
     *                      error or other errors.
     */
    public VacancyStorage(String config) throws IOException, SQLException {
        this(config, false);
    }

    /**
     * Constructor for tests.
     * Creates database and can drop all existing tables there.
     *
     * @param config        Config file path for database, will be read by Classloader.
     *                      Example format: 'ru/job4j/vacancies/main.properties'.
     * @param eraseExisting if <tt>true</tt>, drops all existing tables in database and
     *                      created new table structure.
     * @throws IOException  Signals that an I/O exception of some sort has occurred.
     * @throws SQLException Provides information on a database access
     *                      error or other errors.
     */
    VacancyStorage(String config, boolean eraseExisting) throws IOException, SQLException {
        Properties prop = METHODS.loadProperties(this, config);
        this.connection = METHODS.getConnectionToDatabase(
                prop.getProperty("db.type"), prop.getProperty("db.address"), prop.getProperty("db.name"),
                prop.getProperty("db.user"), prop.getProperty("db.password"));
        String pkg = prop.getProperty("app.package");
        this.loadSqlQueries(pkg, prop);
        this.initDatabase(eraseExisting);
    }

    /**
     * Loads sql queries from files to map.
     *
     * @param pkg  Package where to load queries from.
     *             Example format: 'ru/job4j/vacancies'.
     * @param prop Properties object where to read filenames from.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     */
    private void loadSqlQueries(String pkg, Properties prop) throws IOException {
        this.sqlQueries.put("dropTables",
                METHODS.loadFileAsString(this, pkg, prop.getProperty("sql.drop_all_tables"), "UTF-8"));
        this.sqlQueries.put("createTables",
                METHODS.loadFileAsString(this, pkg, prop.getProperty("sql.create_tables"), "UTF-8"));
        this.sqlQueries.put("addVacancy",
                METHODS.loadFileAsString(this, pkg, prop.getProperty("sql.add_vacancy"), "UTF-8"));
        this.sqlQueries.put("findVacancyById",
                METHODS.loadFileAsString(this, pkg, prop.getProperty("sql.find_vacancy_by_id"), "UTF-8"));
        this.sqlQueries.put("findIdByTitleAndUrl",
                METHODS.loadFileAsString(this, pkg, prop.getProperty("sql.find_id_by_title_and_url"), "UTF-8"));
        this.sqlQueries.put("updateVacancy",
                METHODS.loadFileAsString(this, pkg, prop.getProperty("sql.update_vacancy"), "UTF-8"));
    }

    /**
     * Creates table structure if needed. Also can drop
     * existing tables, if specified.
     *
     * @param eraseExisting if <tt>true</tt>, drops all existing tables
     *                      before creating new ones.
     * @throws SQLException Provides information on a database access
     *                      error or other errors.
     */
    private void initDatabase(boolean eraseExisting) throws SQLException {
        if (eraseExisting) {
            this.dbDropAllTables();
        }
        this.dbCreateTables();
    }

    /**
     * Drops all existing tables in database.
     *
     * @throws SQLException Provides information on a database access
     *                      error or other errors.
     */
    private void dbDropAllTables() throws SQLException {
        METHODS.dbPerformUpdate(this.connection, this.sqlQueries.get("dropTables"));
    }

    /**
     * Creates needed table structure if needed.
     *
     * @throws SQLException Provides information on a database access
     *                      error or other errors.
     */
    private void dbCreateTables() throws SQLException {
        METHODS.dbPerformUpdate(this.connection, this.sqlQueries.get("createTables"));
    }

    /**
     * Performs "addOrUpdate' operation on every object given.
     *
     * @param vacancies List of vacancies to add or update.
     * @throws SQLException Provides information on a database access
     *                      error or other errors.
     */
    public void addOrUpdateAll(List<Vacancy> vacancies) throws SQLException {
        for (Vacancy vacancy : vacancies) {
            this.addOrUpdate(vacancy);
        }
    }

    /**
     * Adds vacancy to database if didn't find it in base or updates it if such vacancy already
     * is in the database (vacancies are found by their title and url)
     *
     * @param vacancy Vacancy to add or updating vacancy.
     * @return Id given by database to added vacancy or id of the vacancy if it was updated.
     * @throws SQLException Provides information on a database access
     *                      error or other errors.
     */
    public int addOrUpdate(Vacancy vacancy) throws SQLException {
        int id = this.findIdByTitleAndUrl(vacancy.getTitle(), vacancy.getUrl());
        if (id == -1) {
            id = this.add(vacancy);
        } else {
            this.update(id, vacancy);
        }
        return id;
    }

    /**
     * Adds vacancy to database.
     *
     * @param vacancy Vacancy to add.
     * @return Id given by database to added vacancy.
     * @throws SQLException Provides information on a database access
     *                      error or other errors.
     */
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

    /**
     * Updates vacancy with given id.
     *
     * @param id     Id of the vacancy to update.
     * @param update Vacancy object which will update the old one.
     * @throws SQLException Provides information on a database access
     *                      error or other errors.
     */
    void update(int id, Vacancy update) throws SQLException {
        String query = String.format(this.sqlQueries.get("updateVacancy"),
                update.getTitle(), update.getUrl(),
                new java.sql.Timestamp(update.getUpdated()), id);
        METHODS.dbPerformUpdate(this.connection, query);
    }

    /**
     * Returns vacancy found by given id.
     *
     * @param id Id of the vacancy to find.
     * @return Vacancy object of the found vacancy or <tt>null</tt> if not found any.
     * @throws SQLException Provides information on a database access
     *                      error or other errors.
     */
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

    /**
     * Returns id of the vacancy which has
     * both the same title and same url as given.
     *
     * @param title Title to look for.
     * @param url   Url to look for.
     * @return Id of the found vacancy or <tt>-1</tt> if not found.
     * @throws SQLException Provides information on a database access
     *                      error or other errors.
     */
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
