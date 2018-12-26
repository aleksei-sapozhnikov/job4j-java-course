package ru.job4j.exchange;

import org.junit.Test;

import java.util.StringJoiner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static ru.job4j.exchange.ActionEnum.ADD;
import static ru.job4j.exchange.ActionEnum.DELETE;
import static ru.job4j.exchange.OperationEnum.ASK;
import static ru.job4j.exchange.OperationEnum.BID;


/**
 * Tests for the Exchange class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 12.03.2018
 */
public class ExchangeTest {

    /**
     * Test findById()
     */
    @Test
    public void whenTasksExistThenFindByIdFindsThem() {
        Exchange exchange = new Exchange();
        exchange.processNewTask(new Task("BUY-1", ADD, ASK, "Right", 10, 20));
        exchange.processNewTask(new Task("SELL-1", ADD, BID, "Right", 33, 12));
        // buy
        Task resultBuy1 = exchange.findById("BUY-1");
        assertThat(resultBuy1.id(), is("BUY-1"));
        assertThat(resultBuy1.action(), is(ADD));
        assertThat(resultBuy1.operation(), is(ASK));
        assertThat(resultBuy1.issuer(), is("Right"));
        assertThat(resultBuy1.price(), is(10));
        assertThat(resultBuy1.volume(), is(20));
        // sell
        Task resultSell1 = exchange.findById("SELL-1");
        assertThat(resultSell1.id(), is("SELL-1"));
        assertThat(resultSell1.action(), is(ADD));
        assertThat(resultSell1.operation(), is(BID));
        assertThat(resultSell1.issuer(), is("Right"));
        assertThat(resultSell1.price(), is(33));
        assertThat(resultSell1.volume(), is(12));
        // not found
        assertThat(exchange.findById("Something-339"), is((Task) null));
    }

    /**
     * Test processNewTask()
     */
    @Test
    public void whenAddTasksThenTheyAreAdded() {
        Exchange exchange = new Exchange();
        assertThat(exchange.processNewTask(
                new Task("BUY-1", ADD, ASK, "Right", 10, 20)),
                is(true)
        );
        assertThat(exchange.processNewTask(
                new Task("SELL-1", ADD, BID, "Right", 20, 32)),
                is(true)
        );
        assertThat(exchange.findById("BUY-1").volume(), is(20));
        assertThat(exchange.findById("SELL-1").volume(), is(32));
    }

    @Test
    public void whenDeleteTasksThenTheyAreDeletedByIdAndIssuer() {
        Exchange exchange = new Exchange();
        exchange.processNewTask(
                new Task("BUY-1", ADD, ASK, "Right", 10, 20)
        );
        assertThat(exchange.findById("BUY-1").volume(), is(20));
        // wrong id
        assertThat(exchange.processNewTask(
                new Task("BUY-19", DELETE, ASK, "Right", 0, 0)),
                is(false)
        );
        assertThat(exchange.findById("BUY-1").volume(), is(20));
        // wrong issuer
        assertThat(exchange.processNewTask(
                new Task("BUY-1", DELETE, ASK, "Left", 0, 0)),
                is(false)
        );
        assertThat(exchange.findById("BUY-1").volume(), is(20));
        // id and issuer ok
        assertThat(exchange.processNewTask(
                new Task("BUY-1", DELETE, ASK, "Right", 0, 0)),
                is(true)
        );
        assertThat(exchange.findById("BUY-1"), is((Task) null));
    }

    /**
     * Test toString()
     */
    @Test
    public void whenToStringThenAllOrderBooksPrinted() {
        Exchange exchange = new Exchange();
        exchange.processNewTask(
                new Task("SELL-LEFT", ADD, BID, "Left", 6, 10)
        );
        exchange.processNewTask(
                new Task("BUY-LEFT-UNITES", ADD, ASK, "Left", 10, 5)
        );
        exchange.processNewTask(
                new Task("BUY-RIGHT-1", ADD, ASK, "Right", 5, 10)
        );
        exchange.processNewTask(
                new Task("BUY-RIGHT-2", ADD, ASK, "Right", 5, 15)
        );
        exchange.processNewTask(
                new Task("SELL-RIGHT", ADD, BID, "Right", 10, 20)
        );
        assertThat(exchange.toString(), is(new StringJoiner(System.lineSeparator())
                .add(String.format("%s", "Left"))
                .add(String.format("%12s%8s%12s", "[Продажа]", "[Цена]", "[Покупка]"))
                .add(String.format("%12s%8s%12s", 5, 6, ""))
                .add("")
                .add(String.format("%s", "Right"))
                .add(String.format("%12s%8s%12s", "[Продажа]", "[Цена]", "[Покупка]"))
                .add(String.format("%12s%8s%12s", 20, 10, ""))
                .add(String.format("%12s%8s%12s", "", 5, 25))
                .add("")
                .toString()
        ));
    }
}