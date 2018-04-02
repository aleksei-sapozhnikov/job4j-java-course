package ru.job4j.exchange;

import javafx.util.Pair;

import java.util.*;

import static ru.job4j.exchange.ActionEnum.ADD;
import static ru.job4j.exchange.ActionEnum.DELETE;
import static ru.job4j.exchange.OperationEnum.ASK;
import static ru.job4j.exchange.OperationEnum.BID;

/**
 * Order book for holding shares of one issuer. Can process addition and deletion tasks.
 * Before addition checks opposite operation tasks and unites them with added task.
 * If nothing left after unite, the task is deleted.
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
     * Buying tasks organized by key - price.
     * Sorted from highest to lowest.
     */
    private NavigableMap<Integer, Set<Task>> buys = new TreeMap<>(Comparator.reverseOrder());
    /**
     * Selling tasks organized by key - price.
     * Sorted from highest to lowest.
     */
    private NavigableMap<Integer, Set<Task>> sells = new TreeMap<>(Comparator.reverseOrder());

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
    String issuer() {
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
        boolean result = false;
        if (task.action() == ADD) {
            result = this.addNewTask(task);
        } else if (task.action() == DELETE) {
            result = this.findAndRemoveTask(task);
        }
        return result;
    }

    /**
     * Adds new task to the order book.
     *
     * @param task task to add.
     * @return <tt>true</tt> if added successfully, <tt>false</tt> if not.
     */
    private boolean addNewTask(Task task) {
        boolean result = false;
        if (task.issuer().equals(this.issuer)) {
            if (task.operation() == ASK) {
                result = true;
                this.uniteWithOppositeAndAddToMap(task, this.buys, this.sells);
            } else if (task.operation() == BID) {
                result = true;
                this.uniteWithOppositeAndAddToMap(task, this.sells, this.buys);
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
        Pair<Task, Map<Integer, Set<Task>>> found = this.findTaskAndItsMapById(task.id());
        Task fTask = found.getKey();
        if (fTask != null) {
            Integer fPrice = fTask.price();
            Map<Integer, Set<Task>> fMap = found.getValue();
            Set<Task> fSet = fMap.get(fPrice);
            result = fSet.remove(fTask);
            if (fSet.isEmpty()) {
                fMap.remove(fPrice);
            }
        }
        return result;
    }

    /**
     * Adds new task to map, placing it by key - price.
     * If there is no such price in the map, the new key and list of tasks is created.
     *
     * @param taskMap list where we are adding the task.
     * @param task    task to add.
     */
    private void uniteWithOppositeAndAddToMap(Task task, Map<Integer, Set<Task>> taskMap, NavigableMap<Integer, Set<Task>> oppositeMap) {
        boolean taskIsBuy = task.operation() == ASK;
        this.uniteTaskWithOppositeMap(task, oppositeMap, taskIsBuy);
        if (task.volume() > 0) {
            if (taskMap.containsKey(task.price())) {
                taskMap.get(task.price()).add(task);
            } else {
                taskMap.put(task.price(), new LinkedHashSet<>(Collections.singletonList(task)));
            }
        }
    }

    /**
     * Checks if it is possible to unite newly added task with existing tasks by their price.
     * If added tasks is buy task, it's price must be not less than of existing sell tasks and vice versa.
     *
     * @param addedPrice    price of the added task.
     * @param existingPrice price of existing tasks.
     * @param addedIsBuy    <tt>true</tt> means that added task is buy task, and existing tasks are sell tasks.
     *                      <tt>false</tt> means added task is sell task, and existing tasks are buy tasks.
     * @return <tt>true</tt> if it is possible to unite tasks with that prices, <tt>false</tt> otherwise.
     */
    private boolean uniteTasksPossible(int addedPrice, int existingPrice, boolean addedIsBuy) {
        return addedIsBuy
                ? addedPrice >= existingPrice
                : addedPrice <= existingPrice;
    }

    /**
     * Checks map containing sets of tasks with operation opposite to the operation
     * of given task (operation == buy/sell) and unites tasks if possible.
     * <p>
     * Stops if task.volume becomes == 0.
     * If existing task's volume becomes zero, the task is deleted from its set.
     * If set connected with some price becomes empty, map entry with that price is removed.
     *
     * @param task        given task.
     * @param oppositeMap map containing opposite-operation tasks (operation == buy/sell).
     * @param taskIsBuy   <tt>true</tt> means task is buy task and opposite operation is sell,
     *                    <tt>false</tt> means vice versa.
     */
    private void uniteTaskWithOppositeMap(Task task, NavigableMap<Integer, Set<Task>> oppositeMap, boolean taskIsBuy) {
        int price = task.price();
        Iterator<Integer> opIterator = taskIsBuy ? oppositeMap.descendingKeySet().iterator() : oppositeMap.keySet().iterator();
        while (opIterator.hasNext() && task.volume() > 0) {
            int opPrice = opIterator.next();
            if (!this.uniteTasksPossible(price, opPrice, taskIsBuy)) {
                break;
            }
            Set<Task> opTasks = oppositeMap.get(opPrice);
            this.uniteTaskWithOppositeSet(task, opTasks);
            if (opTasks.isEmpty()) {
                opIterator.remove();
            }
        }
    }

    /**
     * Checks set containing tasks with operation opposite to the operation
     * of given task (operation == buy/sell) and unites tasks if possible.
     * <p>
     * Stops if given task's volume becomes == 0.
     * If existing task's volume becomes == 0, the task is deleted from the set.
     *
     * @param task        given task.
     * @param oppositeSet set of tasks with opposite operation (buy/sell).
     */
    private void uniteTaskWithOppositeSet(Task task, Set<Task> oppositeSet) {
        Iterator<Task> opIterator = oppositeSet.iterator();
        while (opIterator.hasNext() && task.volume() > 0) {
            Task opposite = opIterator.next();
            this.uniteTasks(task, opposite);
            if (opposite.volume() == 0) {
                opIterator.remove();
            }
        }
    }

    /**
     * Unites single opposite-operation tasks by subtracting their volume.
     *
     * @param added    task newly added.
     * @param existing already existing task.
     */
    private void uniteTasks(Task added, Task existing) {
        int minus = Math.min(added.volume(), existing.volume());
        added.subtractVolume(minus);
        existing.subtractVolume(minus);
    }

    /**
     * Finds task by id in the order book.
     *
     * @param id task's id.
     * @return task with given id or <tt>null</tt> if task not found.
     */
    Task findTaskById(String id) {
        Pair<Task, Map<Integer, Set<Task>>> found = this.findTaskAndItsMapById(id);
        return found.getKey();
    }

    /**
     * Returns pair with task and map where the task is contained.
     *
     * @param id task's id.
     * @return pair of found task and map containing it, or Pair with <tt>null</tt> values if task was not found.
     */
    private Pair<Task, Map<Integer, Set<Task>>> findTaskAndItsMapById(String id) {
        Map<Integer, Set<Task>> map = this.buys;
        Pair<Task, Set<Task>> found = this.findTaskAndItsSetByIdInOneMap(map, id);
        if (found.getKey() == null) {
            map = this.sells;
            found = this.findTaskAndItsSetByIdInOneMap(map, id);
        }
        return new Pair<>(found.getKey(), map);
    }

    /**
     * Finds task from the list by task's id.
     *
     * @param map collection of tasks.
     * @param id  needed task's id.
     * @return Pair with KEY: task with needed id or <tt>null</tt> if not found,
     * VALUE: set in which this task is or <tt>null</tt> if task not found.
     */
    private Pair<Task, Set<Task>> findTaskAndItsSetByIdInOneMap(Map<Integer, Set<Task>> map, String id) {
        Task task = null;
        Set<Task> set = null;
        out:
        for (Set<Task> tasks : map.values()) {
            for (Task temp : tasks) {
                if (id.equals(temp.id())) {
                    task = temp;
                    set = tasks;
                    break out;
                }
            }
        }
        return new Pair<>(task, set);
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
        String sellTasks = this.toStringTasksFromMap(format, this.sells, true);
        if (!"".equals(sellTasks)) {
            buffer.add(sellTasks);
        }
        String buyTasks = this.toStringTasksFromMap(format, this.buys, false);
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
     * Returns string lines of tasks from given map.
     *
     * @param format         format style (for String.format).
     * @param map            map of tasks.
     * @param volumeLeftSide <tt>true</tt> means print tasks volume on the left to price, <tt>false</tt> means on right.
     * @return lines of tasks or "" if no tasks found.
     */
    private String toStringTasksFromMap(String format, Map<Integer, Set<Task>> map, boolean volumeLeftSide) {
        StringJoiner buffer = new StringJoiner(System.lineSeparator());
        for (Map.Entry<Integer, Set<Task>> entry : map.entrySet()) {
            int price = entry.getKey();
            int volume = 0;
            for (Task temp : entry.getValue()) {
                volume += temp.volume();
            }
            buffer.add(volumeLeftSide
                    ? String.format(format, volume, price, "")
                    : String.format(format, "", price, volume)
            );
        }
        return buffer.toString();
    }

}
