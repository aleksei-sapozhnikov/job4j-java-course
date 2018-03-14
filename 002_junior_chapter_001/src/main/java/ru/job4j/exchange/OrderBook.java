package ru.job4j.exchange;

import java.util.Set;
import java.util.TreeSet;

public class OrderBook {
    /**
     * Shares issuer (company).
     */
    private String issuer;
    /**
     * Asks to buy shares.
     */
    private Set<Task> asks = new TreeSet<>();
    /**
     * Shares for selling.
     */
    private Set<Task> bids = new TreeSet<>();

    /**
     * Add new task to the book.
     *
     * @param task task to add.
     */
    void add(Task task) {
        if (task.operation() == Task.OperationEnum.ASK) {
            this.addToSet(this.asks, task);
        } else {
            this.addToSet(this.bids, task);
        }
        this.uniteTasks();
    }

    // Удалить заявку из стакана.
    boolean delete(Task task) {
        return task.operation() == Task.OperationEnum.ASK
                ? this.deleteFromSet(this.asks, task)
                : this.deleteFromSet(this.bids, task);
    }


    // сверяем заявки в bids и asks и выбираем, можно ли продать акций.
    private void uniteTasks() {

    }

    // перебираем заявки с ценой. Если найдем такую же цену - добавляем к ней amount.
    // не найдем - добавляем новую.
    private void addToSet(Set<Task> set, Task task) {

    }

    /**
     * Delete task from the set.
     *
     * @param task task to delete.
     * @return <tt>true</tt> if found and deleted, <tt>false</tt> if not found.
     */
    private boolean deleteFromSet(Set<Task> set, Task task) {
        boolean result = set.contains(task);
        if (result) {
            set.remove(task);
        }
        return result;
    }
}
