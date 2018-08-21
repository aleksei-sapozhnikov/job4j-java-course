package ru.job4j.music.dao.dao;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Before;
import org.junit.Test;
import ru.job4j.music.dao.general.Dao;
import ru.job4j.music.dao.general.DaoPool;
import ru.job4j.music.dao.general.DbConnector;
import ru.job4j.music.dao.general.DbStructureChanger;
import ru.job4j.music.entities.Address;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AddressDaoTest {

    private final Dao<Address> dao;
    private final DbStructureChanger structure;

    private final Address addressWithId = new Address(34, "Id street, 532");
    private final Address addressWithoutId = new Address("Main street");

    public AddressDaoTest() throws IOException, SQLException {
        BasicDataSource connections = new DbConnector().getPool();
        DaoPool daoPool = new DaoPool(connections);
        this.structure = new DbStructureChanger(connections);
        this.dao = daoPool.getDao(DaoPool.DaoType.ADDRESS);
    }

    @Before
    public void before() {
        this.structure.clear();
    }

    @Test
    public void add() {
        Address added = this.dao.add(this.addressWithId);
        Address added2 = this.dao.add(this.addressWithoutId);
        Address found = this.dao.get(added.getId());
        Address found2 = this.dao.get(added2.getId());
        List<Address> list = this.dao.getAll();
        System.out.println(found);
        System.out.println(found2);
        System.out.println();
        System.out.println(list);
    }

    @Test
    public void get() {
    }

    @Test
    public void update() {
    }

    @Test
    public void delete() {
    }

    @Test
    public void getAll() {
    }
}