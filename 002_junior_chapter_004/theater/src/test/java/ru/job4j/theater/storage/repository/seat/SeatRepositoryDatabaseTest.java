package ru.job4j.theater.storage.repository.seat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import ru.job4j.theater.model.Account;
import ru.job4j.theater.model.Seat;
import ru.job4j.theater.storage.database.Database;
import ru.job4j.util.methods.CommonUtils;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class SeatRepositoryDatabaseTest {
    @SuppressWarnings("unused")
    private static final Logger LOG = LogManager.getLogger(SeatRepositoryDatabaseTest.class);

    static {
        try {
            Database.getInstance().dropAndRecreateStructure();
        } catch (SQLException e) {
            LOG.error(CommonUtils.describeThrowable(e));
        }
    }

    private final SeatRepository repository = SeatRepositoryDatabase.getInstance();

    private final Account accountOne = new Account.Builder("acc_1", "123-456").build();
    private final Seat seatOne = new Seat.Builder(1, 1, 50).build();
    private final Seat seatTwo = new Seat.Builder(1, 2, 43).owner(this.accountOne).build();
    private final Seat seatThree = new Seat.Builder(5, 8, 512).owner(this.accountOne).build();


    @Before
    public void clearRepository() throws SQLException {
        this.repository.clear();
    }

    /**
     * Test add() and getAll()
     */
    @Test
    public void whenAddSeatsThenGetAllReturnsThem() {
        this.repository.add(this.seatOne);
        this.repository.add(this.seatTwo);
        this.repository.add(this.seatThree);
        List<Seat> result = this.repository.getAll();
        result.sort(Comparator.naturalOrder());
        List<Seat> expected = Arrays.asList(this.seatOne, this.seatTwo, this.seatThree);
        assertThat(result, is(expected));
    }

    @Test
    public void whenAddSeatWithEmptyAccountThenGetWithEmptyAccount() {
        this.repository.add(this.seatOne);
        Seat found = this.repository.getAll().get(0);
        assertThat(found, is(this.seatOne));
        assertThat(found.getOwner() == Account.getEmptyAccount(), is(true));
    }

    @Test
    public void whenAddSeatWithOwnerThenGetEqualSeat() {
        this.repository.add(this.seatThree);
        Seat found = this.repository.getAll().get(0);
        assertThat(found == this.seatThree, is(false));
        assertThat(found.getOwner() == this.accountOne, is(false));
        assertThat(found, is(this.seatThree));
        assertThat(found.getOwner(), is(this.accountOne));


    }

    /**
     * Test getByPlace()
     */
    @Test
    public void whenGivenPlaceThenFoundSeatOrEmptySeat() {
        this.repository.add(this.seatOne);
        this.repository.add(this.seatThree);
        int[] cOne = {this.seatOne.getRow(), this.seatOne.getColumn()};
        int[] cThree = {this.seatThree.getRow(), this.seatThree.getColumn()};
        int[] cWrong = {12, 45};
        Seat one = this.repository.getByPlace(cOne[0], cOne[1]);
        Seat two = this.repository.getByPlace(cThree[0], cThree[1]);
        Seat wrong = this.repository.getByPlace(cWrong[0], cWrong[1]);
        assertThat(one == this.seatOne, is(false));
        assertThat(one, is(this.seatOne));
        assertThat(two == this.seatThree, is(false));
        assertThat(two, is(this.seatThree));
        assertThat(wrong == Seat.getEmptySeat(), is(true));
    }
}