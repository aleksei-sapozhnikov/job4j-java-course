package ru.job4j.exchange;

import java.util.HashSet;
import java.util.Set;

public class Exchange {
    private Set<OrderBook> books = new HashSet<>();

    void add(Task task) {
        OrderBook book = this.findOrderBook(task.issuer());

        if (task.action() == ActionEnum.ADD) {
            book.add(task);
        } else {
            book.delete(task);
        }
    }

    /**
     * Returns order book with the needed issuer.
     *
     * @param issuer issuer we are looking for.
     * @return order book with the needed issuer or <tt>null</tt> if such a book not found.
     */
    OrderBook findOrderBook(String issuer) {
        OrderBook result = null;
        for (OrderBook temp : this.books) {
            if (issuer.equals(temp.issuer())) {
                result = temp;
                break;
            }
        }
        return result;
    }

}