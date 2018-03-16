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
     * Get issuer.
     *
     * @return issuer field value.
     */
    String issuer() {
        return this.issuer;
    }

    /**
     * Add new task to the book.
     *
     * @param task task to add.
     */
    void add(Task task) {
        if (task.operation() == OperationEnum.ASK) {
            this.addToSet(this.asks, task);
        } else {
            this.addToSet(this.bids, task);
        }
        this.uniteTasks();
    }

    /**
     * Deletes task from the order book (by id number).
     *
     * @param task task to delete.
     * @return true if deleted, false if task not found.
     */
    boolean delete(Task task) {
        return task.operation() == OperationEnum.ASK
                ? this.deleteFromSet(this.asks, task)
                : this.deleteFromSet(this.bids, task);
    }


    // сверяем заявки в bids и asks и выбираем, можно ли продать акций.
    private void uniteTasks() {

    }

    /**
     * Adds new task to set or adds to the volume of existent if task with that price is already in the set.
     *
     * @param set  set where to add task.
     * @param task task to add.
     * @return true
     */
    private void addToSet(Set<Task> set, Task task) {
        boolean finished = false;
        for (Task existent : set) {
            if (task.equals(existent)) {
                existent.addVolumeOfTask(task);
                finished = true;
                break;
            }
        }
        if (!finished) {
            set.add(task);
        }
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
