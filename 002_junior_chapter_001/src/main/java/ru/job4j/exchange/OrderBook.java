package ru.job4j.exchange;

import javafx.util.Pair;

import java.util.*;

import static ru.job4j.exchange.ActionEnum.ADD;
import static ru.job4j.exchange.OperationEnum.ASK;

/**
 * Order book for shares. Can process addition and deletion tasks.
 * Processes opposite corresponding buy/sell tasks and deletes if they are processed fully.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 12.03.2018
 */
class OrderBook {
    /**
     * Shares issuer.
     */
    private String issuer;
    /**
     * List of tasks buying shares.
     */
    private List<Task> buyList = new ArrayList<>();
    /**
     * List of tasks selling shares.
     */
    private List<Task> sellList = new ArrayList<>();

    /**
     * Constructs new order book for given issuer.
     *
     * @param issuer shares issuer.
     */
    OrderBook(String issuer) {
        this.issuer = issuer;
    }

    /**
     * Get issuer.
     *
     * @return issuer field value.
     */
    public String issuer() {
        return issuer;
    }

    /**
     * Processes new task - adds or removes it.
     * When removing task - finds it by given task's id and action "DELETE"
     *
     * @param task new task added to the order book.
     * @return <tt>true</tt> if processed successfully, <tt>false</tt> if task was not added or removed.
     */
    boolean processNewTask(Task task) {
        return task.action() == ADD
                ? this.addNewTask(task)
                : this.findAndRemoveTask(task);
    }

    /**
     * Adds new task to the order book.
     *
     * @param task task to add.
     * @return <tt>true</tt> if added successfully, <tt>false</tt> if not.
     */
    private boolean addNewTask(Task task) {
        boolean result = task.issuer().equals(this.issuer);
        if (result) {
            if (task.operation() == ASK) {
                this.addToListByPrice(this.buyList, task);
                this.uniteWithTasksInOppositeList(task);
            } else {
                this.addToListByPrice(this.sellList, task);
                this.uniteWithTasksInOppositeList(task);
            }
        }
        return result;
    }

    /**
     * Finds task by id and removes it.
     *
     * @param task task to delete (using only task's id to find task in the order book).
     * @return <tt>true if deleted</tt>, <tt>false</tt> if task not found.
     */
    private boolean findAndRemoveTask(Task task) {
        boolean result = false;
        Pair<Task, List<Task>> found = this.findTaskAndListById(task.id());
        if (found != null) {
            result = found.getValue().remove(found.getKey()); //second check
        }
        return result;
    }

    /**
     * Adds new task to list, placing it to the needed place to save ordering.
     * Ordering: from the biggest price to the lowest.
     *
     * @param list list where we are adding the task.
     * @param task task to add.
     */
    private void addToListByPrice(List<Task> list, Task task) {
        boolean added = false;
        ListIterator<Task> it = list.listIterator();
        while (it.hasNext()) {
            if (task.price() > it.next().price()) {
                it.previous();
                it.add(task);
                added = true;
                break;
            }
        }
        if (!added) {
            it.add(task);
        }
    }

    /**
     * Checks if the newly added task can be combined with opposite existing tasks.
     * If possible, unites the tasks.
     *
     * @param added task newly added to the order book.
     */
    private void uniteWithTasksInOppositeList(Task added) {
        boolean buy = added.operation() == ASK;
        int i = buy ? this.sellList.size() - 1 : 0;
        while (buy ? i >= 0 : i < this.buyList.size()) {
            Task temp = buy ? this.sellList.get(i) : this.buyList.get(i);
            if ((buy ? added.price() < temp.price() : added.price() > temp.price())
                    || this.uniteTasksAndStop(added, temp)) {
                break;
            }
            i = buy ? i - 1 : i;
        }
    }

    /**
     * Unites two opposite-operation tasks.
     * If one of the tasks becomes empty (volume == 0) it is removed from the order list.
     *
     * @param added    task newly added to the order book.
     * @param existing already existing task.
     * @return <tt>false</tt> if needed to continue searching tasks to unite,
     * <tt>true</tt> if we must stop because added task was removed from its list.
     */
    private boolean uniteTasksAndStop(Task added, Task existing) {
        boolean stop = false;
        List<Task> addedList = added.operation() == ASK ? this.buyList : this.sellList;
        List<Task> oppositeList = added.operation() == ASK ? this.sellList : this.buyList;
        if (added.volume() < existing.volume()) {
            existing.subtractVolume(added.volume());
            addedList.remove(added);
            stop = true;
        } else if (added.volume() == existing.volume()) {
            addedList.remove(added);
            oppositeList.remove(existing);
            stop = true;
        } else {
            added.subtractVolume(existing.volume());
            oppositeList.remove(existing);
        }
        return stop;
    }

    /**
     * Finds task by id in the order book.
     *
     * @param id task's id.
     * @return task with given id or <tt>null</tt> if task not found.
     */
    Task findTaskById(String id) {
        Pair<Task, List<Task>> found = this.findTaskAndListById(id);
        return found != null ? found.getKey() : null;
    }

    /**
     * Returns pair with task and list where it is contained.
     *
     * @param id task's id.
     * @return pair of found task and list containing or <tt>null</tt> if task was not found.
     */
    private Pair<Task, List<Task>> findTaskAndListById(String id) {
        List<Task> list = this.buyList;
        Task task = this.findTaskInListById(list, id);
        list = task != null ? list : this.sellList;
        task = task != null ? task : this.findTaskInListById(list, id);
        return task != null ? new Pair<>(task, list) : null;
    }

    /**
     * Finds task from the list by task's id.
     *
     * @param list list of tasks.
     * @param id   needed task's id.
     * @return task with needed id or <tt>null</tt> if not found.
     */
    private Task findTaskInListById(List<Task> list, String id) {
        Task result = null;
        for (Task temp : list) {
            if (id.equals(temp.id())) {
                result = temp;
                break;
            }
        }
        return result;
    }

    /**
     * Returns string showing current state of the order book.
     * Assert that in order book price of buying tasks is lower then price
     * of selling tasks (otherwise they were united).
     *
     * @return string showing current state of the order book.
     */
    @Override
    public String toString() {
        String format = "%12s%8s%12s";
        StringJoiner buffer = new StringJoiner(System.lineSeparator());
        buffer.add(this.toStringHeading(format));
        String sellTasks = this.toStringTasksFromList(format, this.sellList, false);
        if (!"".equals(sellTasks)) {
            buffer.add(sellTasks);
        }
        String buyTasks = this.toStringTasksFromList(format, this.buyList, true);
        if (!"".equals(buyTasks)) {
            buffer.add(buyTasks);
        }
        return buffer.toString();
    }

    /**
     * Returns heading for this order book.
     *
     * @param format format style (for String.format).
     * @return heading.
     */
    private String toStringHeading(String format) {
        return new StringJoiner(System.lineSeparator())
                .add(String.format("%s", this.issuer))
                .add(String.format(format, "[Продажа]", "[Цена]", "[Покупка]"))
                .toString();
    }

    /**
     * Returns string lines of tasks from given list.
     *
     * @param format  format style (for String.format).
     * @param list    list of tasks.
     * @param buyList is it a list of buying task? <tt>true</tt> - buying, <tt>false</tt> selling.
     * @return lines of tasks or "" if no tasks found.
     */
    private String toStringTasksFromList(String format, List<Task> list, boolean buyList) {
        StringJoiner buffer = new StringJoiner(System.lineSeparator());
        Iterator<Task> iterator = list.iterator();
        Task task = iterator.hasNext() ? iterator.next() : null;
        while (task != null) {
            int amount = task.volume();
            Task next = iterator.hasNext() ? iterator.next() : null;
            while (next != null && task.price() == next.price()) {
                amount += next.volume();
                next = iterator.hasNext() ? iterator.next() : null;
            }
            buffer
                    .add(String.format(format,
                            buyList ? "" : amount,
                            task.price(),
                            buyList ? amount : ""));
            task = next;
        }
        return buffer.toString();
    }
}
