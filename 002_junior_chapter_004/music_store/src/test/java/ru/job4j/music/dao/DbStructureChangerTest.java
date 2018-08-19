package ru.job4j.music.dao;

import org.junit.Test;
import ru.job4j.music.dao.general.DbConnector;
import ru.job4j.music.dao.general.DbStructureChanger;

import java.sql.SQLException;

public class DbStructureChangerTest {

    @Test
    public void createTables() throws SQLException {
        DbConnector connector = new DbConnector();
        DbStructureChanger structure = new DbStructureChanger(connector.getPool());
        structure.clear();
        structure.createTables();
    }
}