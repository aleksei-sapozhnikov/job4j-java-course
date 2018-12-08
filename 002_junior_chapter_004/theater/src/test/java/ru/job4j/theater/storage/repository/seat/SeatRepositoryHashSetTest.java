package ru.job4j.theater.storage.repository.seat;

import org.junit.Before;
import org.junit.Test;
import ru.job4j.theater.model.Account;
import ru.job4j.theater.model.Seat;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class SeatRepositoryHashSetTest {

    private final SeatRepository repository = SeatRepositoryHashSet.getInstance();

    private final Account accountOne = new Account.Builder("acc_1", "123-456").build();
    private final Account accountTwo = new Account.Builder("acc_2", "777-666").build();
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
    public void whenAddSeatsThenGetAllReturnsThem() throws SQLException {
        this.repository.add(this.seatOne);
        this.repository.add(this.seatTwo);
        List<Seat> result = this.repository.getAll();
        result.sort(Comparator.naturalOrder());
        List<Seat> expected = Arrays.asList(this.seatOne, this.seatTwo);
        assertThat(result, is(expected));
    }

    /**
     * Test getByPlace()
     */
    @Test
    public void whenGivenRowAndColumnFindSeat() throws SQLException {
        this.repository.add(this.seatOne);
        this.repository.add(this.seatTwo);
        this.repository.add(this.seatThree);
        assertThat(this.repository.getByPlace(this.seatOne.getRow(), this.seatOne.getColumn()), is(this.seatOne));
        assertThat(this.repository.getByPlace(this.seatTwo.getRow(), this.seatTwo.getColumn()), is(this.seatTwo));
        assertThat(this.repository.getByPlace(this.seatThree.getRow(), this.seatThree.getColumn()), is(this.seatThree));
    }

    @Test
    public void whenUpdateByRowAndColumnThenNewSeatInRepositoryOne() throws SQLException {
        this.repository.add(this.seatOne);
        int[] placeOne = {this.seatOne.getRow(), this.seatOne.getColumn()};
        Seat newOne = new Seat.Builder(434, 32, 1111).owner(this.accountOne).build();
        this.repository.updateByPlace(placeOne[0], placeOne[1], newOne);
        Seat beforeOne = this.repository.getByPlace(placeOne[0], placeOne[1]);
        Seat afterOne = this.repository.getByPlace(434, 32);
        assertThat(beforeOne == Seat.getEmptySeat(), is(true));
        assertThat(afterOne == Seat.getEmptySeat(), is(false));
        assertThat(afterOne, is(newOne));
    }

    @Test
    public void whenUpdateByRowAndColumnThenNewSeatInRepositoryTwo() throws SQLException {
        this.repository.add(this.seatTwo);
        int[] placeTwo = {this.seatTwo.getRow(), this.seatTwo.getColumn()};
        Seat newTwo = new Seat.Builder(32, 111, 0).owner(this.accountTwo).build();
        this.repository.updateByPlace(placeTwo[0], placeTwo[1], newTwo);
        Seat beforeTwo = this.repository.getByPlace(placeTwo[0], placeTwo[1]);
        Seat afterTwo = this.repository.getByPlace(32, 111);
        assertThat(beforeTwo == Seat.getEmptySeat(), is(true));
        assertThat(afterTwo == Seat.getEmptySeat(), is(false));
        assertThat(afterTwo, is(newTwo));
    }
}