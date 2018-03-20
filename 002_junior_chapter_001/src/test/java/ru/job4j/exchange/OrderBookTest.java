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
        assertThat(book.getTask("123"), is((Task) null));
        assertThat(book.getTask("321"), is((Task) null));
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
        Task result123 = book.getTask("123");
        // task to buy
        assertThat(result123.id(), is("123"));
        assertThat(result123.action(), is(ADD));
        assertThat(result123.operation(), is(ASK));
        assertThat(result123.issuer(), is("Right"));
        assertThat(result123.price(), is(10));
        assertThat(result123.volume(), is(5));
        // task to sell
        Task result321 = book.getTask("321");
        assertThat(result321.id(), is("321"));
        assertThat(result321.action(), is(ADD));
        assertThat(result321.operation(), is(BID));
        assertThat(result321.issuer(), is("Right"));
        assertThat(result321.price(), is(20));
        assertThat(result321.volume(), is(7));
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
        // lower price - not united
        book.processTask(new Task("BUY-3", ADD, ASK, "Right", 8, 7));
        assertThat(book.getTask("SELL-1").volume(), is(10));
        assertThat(book.getTask("BUY-3").volume(), is(7));
        // higher volume - all sold
        book.processTask(new Task("BUY-4", ADD, ASK, "Right", 20, 15));
        assertThat(book.getTask("SELL-1"), is((Task) null)); // all sold
        assertThat(book.getTask("BUY-4").volume(), is(5)); // left to buy
    }

    @Test
    public void whenAdSellingPriceNotHigherThenBuyingThenTasksUnited() {
        OrderBook book = new OrderBook("Right");
        book.processTask(new Task("BUY-1", ADD, ASK, "Right", 10, 30));
        assertThat(book.getTask("BUY-1").volume(), is(30));
        // same price
        book.processTask(new Task("SELL-1", ADD, BID, "Right", 10, 5));
        assertThat(book.getTask("BUY-1").volume(), is(25));
        assertThat(book.getTask("SELL-1"), is((Task) null));
        // lower price
        book.processTask(new Task("SELL-2", ADD, BID, "Right", 5, 5));
        assertThat(book.getTask("BUY-1").volume(), is(20));
        assertThat(book.getTask("SELL-2"), is((Task) null));
        // lower price - not united
        book.processTask(new Task("SELL-3", ADD, BID, "Right", 13, 6));
        assertThat(book.getTask("BUY-1").volume(), is(20));
        assertThat(book.getTask("SELL-3").volume(), is(6));
        // higher volume - all sold
        book.processTask(new Task("SELL-4", ADD, BID, "Right", 8, 25));
        assertThat(book.getTask("BUY-1"), is((Task) null)); // all sold
        assertThat(book.getTask("SELL-4").volume(), is(5)); // left to buy
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

    /**
     * Test toString()
     */
    @Test
    public void whenTaskWithTheSamePriceThenPrintedAsOneTask() {
        // only buys, sum == 27
        OrderBook buyBook = new OrderBook("Buy");
        buyBook.processTask(new Task("Buy-1", ADD, ASK, "Buy", 8, 5));
        buyBook.processTask(new Task("Buy-2", ADD, ASK, "Buy", 8, 15));
        buyBook.processTask(new Task("Buy-2", ADD, ASK, "Buy", 8, 7));
        assertThat(buyBook.toString(), is(new StringJoiner(System.lineSeparator())
                .add(String.format("%s", "Buy"))
                .add(String.format("%12s%8s%12s", "[Продажа]", "[Цена]", "[Покупка]"))
                .add(String.format("%12s%8s%12s", "", 8, 27))
                .toString()
        ));
        // only sells, sum == 28
        OrderBook sellBook = new OrderBook("Sell");
        sellBook.processTask(new Task("Sell-1", ADD, BID, "Sell", 8, 5));
        sellBook.processTask(new Task("Sell-2", ADD, BID, "Sell", 8, 15));
        sellBook.processTask(new Task("Sell-2", ADD, BID, "Sell", 8, 8));
        assertThat(sellBook.toString(), is(new StringJoiner(System.lineSeparator())
                .add(String.format("%s", "Sell"))
                .add(String.format("%12s%8s%12s", "[Продажа]", "[Цена]", "[Покупка]"))
                .add(String.format("%12s%8s%12s", 28, 8, ""))
                .toString()
        ));
        // mixed, buys sum == 21, sells sum = 11
        OrderBook mixedBook = new OrderBook("Mixed");
        mixedBook.processTask(new Task("Buy-1", ADD, ASK, "Mixed", 8, 5));
        mixedBook.processTask(new Task("Buy-2", ADD, ASK, "Mixed", 8, 16));
        mixedBook.processTask(new Task("Sell-1", ADD, BID, "Mixed", 10, 6)); //higher price, not united with buys
        mixedBook.processTask(new Task("Sell-2", ADD, BID, "Mixed", 10, 5));
        assertThat(mixedBook.toString(), is(new StringJoiner(System.lineSeparator())
                .add(String.format("%s", "Mixed"))
                .add(String.format("%12s%8s%12s", "[Продажа]", "[Цена]", "[Покупка]"))
                .add(String.format("%12s%8s%12s", 11, 10, ""))
                .add(String.format("%12s%8s%12s", "", 8, 21))
                .toString()
        ));
    }
}