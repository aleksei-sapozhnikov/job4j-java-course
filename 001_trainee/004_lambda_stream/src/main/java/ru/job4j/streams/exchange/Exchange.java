package ru.job4j.streams.exchange;

import java.util.*;

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
    private Map<String, OrderBook> books = new LinkedHashMap<>();

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
        Optional<Task> result = this.books.values().stream()
                .map(book -> book.findTaskById(id))
                .filter(Objects::nonNull)
                .findFirst();
        return result.orElse(null);
    }

    /**
     * Returns existing order book for the given issuer
     * or creates new if not found existing.
     *
     * @param issuer shares issuer.
     * @return order book for the given issuer.
     */
    private OrderBook getOrderBook(String issuer) {
        OrderBook result = books.get(issuer);
        if (result == null) {
            result = new OrderBook(issuer);
            this.books.put(issuer, result);
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
        for (OrderBook temp : this.books.values()) {
            buffer.add(temp.toString());
            buffer.add("");
        }
        return buffer.toString();
    }

}