package ru.job4j.theater.model;

import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class SeatTest {

    private Account accountOne = new Account.Builder("acc_1", "213-342").build();

    @Test
    public void testEmptyEntity() {
        Seat expected = new Seat.Builder(-1, -1, -1)
                .owner(Account.getEmptyAccount())
                .build();
        assertThat(Seat.getEmptySeat(), is(expected));
    }

    @Test
    public void testGetters() {
        Seat seat = new Seat.Builder(2, 4, 50).owner(this.accountOne).build();
        assertThat(seat.getRow(), is(2));
        assertThat(seat.getColumn(), is(4));
        assertThat(seat.getPrice(), is(50));
        assertThat(seat.getOwner(), is(this.accountOne));
    }

    @Test
    public void testToString() {
        Seat seat = new Seat.Builder(3, 5, 50).owner(this.accountOne).build();
        assertThat(seat.toString(), is(String.format(
                "Seat[row='3',column='5',price='50',owner='%s']", this.accountOne)));
    }

    @Test
    public void testEqualsAndHashCode() {
        Seat main = new Seat.Builder(1, 2, 100).build();
        Seat same = new Seat.Builder(1, 2, 100).build();
        Seat rowOther = new Seat.Builder(3, 2, 100).build();
        Seat columnOther = new Seat.Builder(1, 3, 100).build();
        Seat priceOther = new Seat.Builder(1, 2, 90).build();
        Seat ownerOther = new Seat.Builder(1, 2, 100).owner(this.accountOne).build();
        assertThat(main == same, is(false));
        assertThat(main.equals(same), is(true));
        assertThat(main.hashCode() == same.hashCode(), is(true));
        assertThat(main.equals(rowOther), is(false));
        assertThat(main.equals(columnOther), is(false));
        assertThat(main.equals(priceOther), is(false));
        assertThat(main.equals(ownerOther), is(false));
    }

    @Test
    public void testNaturalOrdering() {
        Seat one = new Seat.Builder(1, 1, 34).build();
        Seat two = new Seat.Builder(1, 2, 39).build();
        Seat three = new Seat.Builder(2, 1, 120).build();
        Seat four = new Seat.Builder(2, 2, 56).build();
        Seat five = new Seat.Builder(5, 1, 43).build();
        List<Seat> result = Arrays.asList(four, two, five, one, three);
        result.sort(Comparator.naturalOrder());
        List<Seat> expected = Arrays.asList(one, two, three, four, five);
        assertThat(result, is(expected));
    }

    @Test
    public void whenSameRowAndColumnThenCompareResultIsZero() {
        Seat one = new Seat.Builder(1, 1, 34).build();
        Seat two = new Seat.Builder(1, 1, 243).owner(this.accountOne).build();
        assertThat(one.compareTo(two), is(0));
    }

    /**
     * Test isFree()
     */
    @Test
    public void whenSeatOwnerIsEmptyAccountThenFreeSeatElseNotFree() {
        Seat freeOne = new Seat.Builder(2, 4, 50).build();
        Seat freeTwo = new Seat.Builder(2, 4, 50).owner(Account.getEmptyAccount()).build();
        Seat occupied = new Seat.Builder(2, 4, 50).owner(this.accountOne).build();
        assertThat(freeOne.isFree(), is(true));
        assertThat(freeTwo.isFree(), is(true));
        assertThat(occupied.isFree(), is(false));
    }

    /**
     * Test occupy()
     */
    @Test
    public void whenOccupyFreeSeatThenOccupiedByGivenAccount() {
        Seat free = new Seat.Builder(2, 4, 50).build();
        Seat occupied = free.occupy(this.accountOne);
        assertThat(free.isFree(), is(true));
        assertThat(occupied.isFree(), is(false));
        assertThat(occupied.getOwner(), is(accountOne));
    }
}