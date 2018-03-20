package ru.job4j.exchange;

import java.util.*;

import static ru.job4j.exchange.OperationEnum.ASK;

public class OrderBook {

    private String issuer;

    private List<Task> buyList = new ArrayList<>();

    private List<Task> sellList = new ArrayList<>();

    OrderBook(String issuer) {
        this.issuer = issuer;
    }

    public String issuer() {
        return issuer;
    }

    boolean processTask(Task task) {
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

    Task getTask(String id) {
        Task result = this.getTaskByIdFromList(this.buyList, id);
        result = result != null ? result : this.getTaskByIdFromList(this.sellList, id);
        return result;

    }

    private Task getTaskByIdFromList(List<Task> list, String id) {
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
        StringBuilder buffer = new StringBuilder();
        buffer.append(this.toStringHeading(format)).append(System.lineSeparator());
        String sellTasks = this.toStringTasksFromList(format, this.sellList, false);
        buffer.append(sellTasks);
        if (!"" .equals(sellTasks)) {
            buffer.append(System.lineSeparator());
        }
        String buyTasks = this.toStringTasksFromList(format, this.buyList, true);
        buffer.append(buyTasks);

//        Iterator<Task> buyIt = this.buyList.iterator();
//        Iterator<Task> sellIt = this.sellList.iterator();
//        Task buy = buyIt.hasNext() ? buyIt.next() : null;
//        Task sell = sellIt.hasNext() ? sellIt.next() : null;
//        while (sell != null) {
//            int amount = sell.volume();
//            Task sellNext = sellIt.hasNext() ? sellIt.next() : null;
//            while (sellNext != null && sell.price() == sellNext.price()) {
//                amount += sellNext.volume();
//                sellNext = sellIt.hasNext() ? sellIt.next() : null;
//            }
//            buffer.add(String.format(format, amount, sell.price(), ""));
//            sell = sellNext;
//        }
//        while (buy != null) {
//            int amount = buy.volume();
//            Task buyNext = buyIt.hasNext() ? buyIt.next() : null;
//            while (buyNext != null && buy.price() == buyNext.price()) {
//                amount += buyNext.volume();
//                buyNext = buyIt.hasNext() ? buyIt.next() : null;
//            }
//            buffer.add(String.format(format, "", buy.price(), amount));
//            buy = buyNext;
//        }

        return buffer.toString();
    }

    private String toStringHeading(String format) {
        return new StringJoiner(System.lineSeparator())
                .add(String.format("%s", this.issuer))
                .add(String.format(format, "[Продажа]", "[Цена]", "[Покупка]"))
                .toString();
    }

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
            buffer.add(String.format(format,
                    buyList ? "" : amount,
                    task.price(),
                    buyList ? amount : "")
            );
            task = next;
        }
        return buffer.toString();
    }
}
