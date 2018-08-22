package ru.job4j.music.dao.dao;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Before;
import org.junit.Test;
import ru.job4j.music.dao.general.DaoPool;
import ru.job4j.music.dao.general.DbConnector;
import ru.job4j.music.dao.general.DbStructureChanger;
import ru.job4j.music.entities.Address;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

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

    /**
     * Test add() and get()
     */
    @Test
    public void whenAddThenReturnedAdded() {
        Address added = this.dao.add(this.addressWithId);
        Address found = this.dao.get(added.getId());
        List<Address> list = this.dao.getAll();
        assertThat(added, is(found));
        assertThat(found, is(this.addressWithId));
        assertThat(list, is(Collections.singletonList(this.addressWithId)));
    }

    /**
     * Test update()
     */
    @Test
    public void whenUpdateThenNewValuesAndUpdaterIdDoesNotMatter() {
        Address added = this.dao.add(this.addressWithoutId);
        Address result = this.dao.update(added.getId(), this.addressWithId);
        assertThat(result, is(addressWithId));
        assertThat(result.getId(), is(added.getId()));
    }

    /**
     * Test delete()
     */
    @Test
    public void whenDeleteThenReturnDeletedAndNoMoreInDatabase() {
        Address added = this.dao.add(this.addressWithId);
        Address deleted = this.dao.delete(added.getId());
        assertThat(added, is(deleted));
        assertThat(this.dao.get(added.getId()), is(Address.EMPTY_ADDRESS));
        assertThat(this.dao.getAll(), is(Collections.emptyList()));
    }

    /**
     * Test getAll()
     */
    @Test
    public void whenGetAllThenListOfAddressesInDatabase() {
        this.dao.add(this.addressWithId);
        this.dao.add(this.addressWithoutId);
        List<Address> result = this.dao.getAll();
        List<Address> expected = Arrays.asList(this.addressWithId, this.addressWithoutId);
        assertThat(result, is(expected));
    }
}