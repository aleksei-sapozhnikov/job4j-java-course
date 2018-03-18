package ru.job4j.exchange;

import org.junit.Test;

public class OrderBookTest {

    @Test
    public void tryToString() {
        OrderBook book = new OrderBook("Газпром");
        book.processNew(new Task("123", ActionEnum.ADD, OperationEnum.ASK, "Газпром", 5, 2));
        book.processNew(new Task("123", ActionEnum.ADD, OperationEnum.ASK, "Газпром", 5, 7));
        book.processNew(new Task("123", ActionEnum.ADD, OperationEnum.ASK, "Газпром", 5, 7));
        book.processNew(new Task("123", ActionEnum.ADD, OperationEnum.BID, "Газпром", 3, 20));
        book.processNew(new Task("123", ActionEnum.ADD, OperationEnum.ASK, "Газпром", 10, 1));
        book.processNew(new Task("123", ActionEnum.ADD, OperationEnum.ASK, "Газпром", 10, 10));
        System.out.println(book);

    }
}