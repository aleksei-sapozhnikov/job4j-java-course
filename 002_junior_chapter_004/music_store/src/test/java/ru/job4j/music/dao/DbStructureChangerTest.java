package ru.job4j.music.dao;

import org.junit.Test;

import static org.junit.Assert.*;

public class DbStructureChangerTest {

    @Test
    public void createTables() {
        DbConnector connector = new DbConnector();
        DbStructureChanger structure = new DbStructureChanger(connector);
        structure.clear();
        structure.createTables();
    }
}