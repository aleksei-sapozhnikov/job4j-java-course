package ru.job4j.orderbook;

import java.util.HashSet;
import java.util.Set;

public class Exchange {
    private Set<OrderBook> books = new HashSet<>();

    void add(Task task) {
        OrderBook book = this.findOrderBook(task.issuer());

        if (task.action() == Task.ActionEnum.ADD) {
            book.add(task);
        } else {
            book.delete(task);
        }
    }

    OrderBook findOrderBook(String issuer) {
        // перебираем стаканы и находим тот, где эмитент совпадает с нужным нам. Возвращаем его.
        return new OrderBook();
    }

}