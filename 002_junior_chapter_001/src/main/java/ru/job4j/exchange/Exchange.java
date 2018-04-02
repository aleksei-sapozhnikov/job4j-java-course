package ru.job4j.exchange;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.StringJoiner;

/**
 * Exchange for shares. Can process addition and deletion of tasks.
 * Processes opposite corresponding buy/sell tasks and deletes if they are processed fully.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 12.03.2018
 */
class Exchange {
    /**
     * Order books, one book for every shares issuer.
     */
    private Set<OrderBook> books = new LinkedHashSet<>();

    /**
     * Process new task which came to the system - add  new task
     * or delete existing (with corresponding issuer and id).
     *
     * @param task new task.
     * @return <tt>true</tt> if processed successfully, <tt>false</tt> if it was not possible.
     */
    boolean processNewTask(Task task) {
        OrderBook book = getOrderBook(task.issuer());
        return book.processNewTask(task);
    }

    /**
     * Finds task by its id.
     *
     * @param id task's id.
     * @return task if found, <tt>null</tt> if not found.
     */
    Task findById(String id) {
        Task result = null;
        for (OrderBook book : this.books) {
            result = book.findTaskById(id);
            if (result != null) {
                break;
            }
        }
        return result;
    }

    /**
     * Returns existing order book for the given issuer
     * or creates new if not found existing.
     *
     * @param issuer shares issuer.
     * @return order book for the given issuer.
     */
    private OrderBook getOrderBook(String issuer) {
        OrderBook result = null;
        for (OrderBook temp : this.books) {
            if (issuer.equals(temp.issuer())) {
                result = temp;
                break;
            }
        }
        if (result == null) {
            result = new OrderBook(issuer);
            this.books.add(result);
        }
        return result;
    }

    /**
     * Returns string showing current state of all order books in the exchange.
     *
     * @return string showing current state of the exchange's order books.
     */
    @Override
    public String toString() {
        StringJoiner buffer = new StringJoiner(System.lineSeparator());
        for (OrderBook temp : this.books) {
            buffer.add(temp.toString());
            buffer.add("");
        }
        return buffer.toString();
    }

}