package ru.job4j.exchange;

import org.junit.Test;

import java.util.StringJoiner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static ru.job4j.exchange.ActionEnum.ADD;
import static ru.job4j.exchange.OperationEnum.ASK;
import static ru.job4j.exchange.OperationEnum.BID;

public class OrderBookTest {

    /**
     * Test issuer()
     */
    @Test
    public void whenGetIssuerThenIssuer() {
        OrderBook book = new OrderBook("Right");
        assertThat(book.issuer(), is("Right"));
    }

    /**
     * Test processTask()
     */
    @Test
    public void whenAddTaskWithWrongIssuerThenFalseAndNotAdded() {
        OrderBook book = new OrderBook("Right");
        assertThat(
                book.processTask(new Task("123", ADD, ASK, "Wrong", 10, 5)),
                is(false)
        );
        assertThat(
                book.processTask(new Task("321", ADD, BID, "Wrong", 10, 5)),
                is(false)
        );
        String result = book.toStringForTests();
        String expected = new StringJoiner(System.lineSeparator())
                .add("Right")
                .add(String.format("%12s%8s%12s", "[Продажа]", "[Цена]", "[Покупка]"))
                .toString();
        assertThat(result, is(expected));
    }

    @Test
    public void whenAddTaskWithRightIssuerThenItIsAdded() {
        OrderBook book = new OrderBook("Right");
        assertThat(
                book.processTask(new Task("123", ADD, ASK, "Right", 10, 5)),
                is(true)
        );
        assertThat(
                book.processTask(new Task("321", ADD, BID, "Right", 20, 7)),
                is(true)
        );
        String result = book.toStringForTests();
        String expected = new StringJoiner(System.lineSeparator())
                .add("Right")
                .add(String.format("%12s%8s%12s", "[Продажа]", "[Цена]", "[Покупка]"))
                .add(String.format("%12s%8s%12s", 7, 20, ""))
                .add(String.format("%12s%8s%12s", "", 10, 5))
                .toString();
        assertThat(result, is(expected));
    }

    /**
     * Test getTask()
     */
    @Test
    public void whenGetTaskThenReturnExistingAndNullIfNotExisting() {
        OrderBook book = new OrderBook("Right");
        book.processTask(
                new Task("A-812", ADD, ASK, "Right", 10, 5)
        );
        Task result = book.getTask("A-812");
        assertThat(result.id(), is("A-812"));
        assertThat(result.action(), is(ADD));
        assertThat(result.operation(), is(ASK));
        assertThat(result.issuer(), is("Right"));
        assertThat(result.price(), is(10));
        assertThat(result.volume(), is(5));
        assertThat(book.getTask("B-233"), is((Task) null));
    }

    /**
     * Test private methods uniteWithTasksInOppositeList() and uniteTasksAndStop()
     */
    @Test
    public void whenAddBuyingPriceNotLowerThenSellingThenTasksUnited() {
        OrderBook book = new OrderBook("Right");
        book.processTask(new Task("SELL-1", ADD, BID, "Right", 10, 20));
        assertThat(book.getTask("SELL-1").volume(), is(20));
        // same price
        book.processTask(new Task("BUY-1", ADD, ASK, "Right", 10, 5));
        assertThat(book.getTask("SELL-1").volume(), is(15));
        assertThat(book.getTask("BUY-1"), is((Task) null));
        // higher price
        book.processTask(new Task("BUY-2", ADD, ASK, "Right", 20, 5));
        assertThat(book.getTask("SELL-1").volume(), is(10));
        assertThat(book.getTask("BUY-2"), is((Task) null));
        // lower price
        book.processTask(new Task("BUY-3", ADD, ASK, "Right", 8, 7));
        assertThat(book.getTask("SELL-1").volume(), is(10)); //not changed
        assertThat(book.getTask("BUY-3").volume(), is(7));
        // higher volume
        book.processTask(new Task("BUY-4", ADD, ASK, "Right", 20, 15));
        assertThat(book.getTask("SELL-1"), is((Task) null)); // all sold
        assertThat(book.getTask("BUY-4").volume(), is(5)); // left to buy
    }

    @Test
    public void whenUnitedTwoTasksWithTheSameVolumeThenBothRemoved() {
        OrderBook book = new OrderBook("Right");
        book.processTask(new Task("SELL-2", ADD, BID, "Right", 10, 20));
        assertThat(book.getTask("SELL-2").volume(), is(20));
        book.processTask(new Task("BUY-5", ADD, ASK, "Right", 11, 20));
        assertThat(book.getTask("SELL-5"), is((Task) null));
        assertThat(book.getTask("BUY-5"), is((Task) null));
    }


}