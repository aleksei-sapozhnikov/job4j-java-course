package ru.job4j.exchange;

import javafx.util.Pair;

import java.util.*;

import static ru.job4j.exchange.ActionEnum.ADD;
import static ru.job4j.exchange.OperationEnum.ASK;
import static ru.job4j.exchange.OperationEnum.BID;

/**
 * Order book for holding shares of one issuer. Can process addition and deletion tasks.
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
     * Buying tasks organized by key - price.
     */
    private NavigableMap<Integer, Set<Task>> buys = new TreeMap<>(Comparator.reverseOrder());
    /**
     * Selling tasks organized by key - price.
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
                this.uniteWithOppositeAndAddToMap(task, this.buys, this.sells);
            } else if (task.operation() == BID) {
                this.uniteWithOppositeAndAddToMap(task, this.sells, this.buys);
            } else {
                result = false;
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
     * @param addedPrice
     * @param existingPrice
     * @param addedIsBuy
     * @return
     */
    private boolean uniteTasksPossible(int addedPrice, int existingPrice, boolean addedIsBuy) {
        return addedIsBuy
                ? addedPrice >= existingPrice
                : addedPrice <= existingPrice;
    }

    /**
     * @param task
     * @param oppositeMap
     * @param reverseOrder
     */
    private void uniteTaskWithOppositeMap(Task task, NavigableMap<Integer, Set<Task>> oppositeMap, boolean reverseOrder) {
        int price = task.price();
        Iterator<Integer> opIterator = reverseOrder ? oppositeMap.descendingKeySet().iterator() : oppositeMap.keySet().iterator();
        while (opIterator.hasNext() && task.volume() > 0) {
            int opPrice = opIterator.next();
            if (this.uniteTasksPossible(price, opPrice, reverseOrder)) {
                Set<Task> opTasks = oppositeMap.get(opPrice);
                this.uniteTaskWithOppositeSet(task, opTasks);
                if (opTasks.isEmpty()) {
                    opIterator.remove();
                }
            }
        }
    }

    /**
     * @param task
     * @param oppositeSet
     */
    private void uniteTaskWithOppositeSet(Task task, Set<Task> oppositeSet) {
        Iterator<Task> opposIt = oppositeSet.iterator();
        while (opposIt.hasNext() && task.volume() > 0) {
            Task opposite = opposIt.next();
            this.uniteTasks(task, opposite);
            if (opposite.volume() == 0) {
                opposIt.remove();
            }
        }
    }

    /**
     * Unites two opposite-operation tasks.
     *
     * @param added    task newly added.
     * @param existing already existing task.
     */
    private void uniteTasks(Task added, Task existing) {
        int minus = added.volume() <= existing.volume() ? added.volume() : existing.volume();
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
     * Returns pair with task and list where it is contained.
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
     * @return Pair: 1) task with needed id or <tt>null</tt> if not found;
     * 2) set in which this task is or <tt>null</tt> if task not found.
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
     * Returns string lines of tasks from given buying map.
     *
     * @param format format style (for String.format).
     * @param map    map of tasks.
     * @return lines of tasks or "" if no tasks found.
     */
    private String toStringTasksFromMap(String format, Map<Integer, Set<Task>> map, boolean priceLeftSide) {
        StringJoiner buffer = new StringJoiner(System.lineSeparator());
        for (Map.Entry<Integer, Set<Task>> entry : map.entrySet()) {
            int price = entry.getKey();
            int volume = 0;
            for (Task temp : entry.getValue()) {
                volume += temp.volume();
            }
            buffer.add(priceLeftSide
                    ? String.format(format, price, volume, "")
                    : String.format(format, "", volume, price)
            );
        }
        return buffer.toString();
    }

}
