package ru.job4j.exchange;

import java.util.*;

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
            if (task.operation() == OperationEnum.ASK) {
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
        boolean buy = added.operation() == OperationEnum.ASK;
        if (buy) {
            for (int i = this.sellList.size() - 1; i >= 0; i--) {
                Task temp = this.sellList.get(i);
                if (added.price() < temp.price()) {
                    break;
                }
                if (this.uniteTasksAndStop(added, temp, this.buyList, this.sellList)) {
                    break;
                }
            }
        } else {
            for (int i = 0; i < this.buyList.size(); i++) {
                Task temp = this.buyList.get(i);
                if (added.price() > temp.price()) {
                    break;
                }
                if (this.uniteTasksAndStop(added, temp, this.sellList, this.buyList)) {
                    break;
                } else {
                    i--;
                }
            }
        }
    }

    private boolean uniteTasksAndStop(Task added, Task existing, List<Task> addedList, List<Task> oppositeList) {
        boolean stop = false;
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

    public String toStringForTests() {
        String format = "%12s%8s%12s";
        StringJoiner buffer = new StringJoiner(System.lineSeparator());
        buffer.add(String.format("%s", this.issuer));
        buffer.add(String.format(format, "[Продажа]", "[Цена]", "[Покупка]"));
        Iterator<Task> buyIt = this.buyList.iterator();
        Iterator<Task> sellIt = this.sellList.iterator();
        Task buy = buyIt.hasNext() ? buyIt.next() : null;
        Task sell = sellIt.hasNext() ? sellIt.next() : null;
        while (buy != null && sell != null) {
            if (buy.price() > sell.price()) {
                int amount = buy.volume();
                Task buyNext = buyIt.hasNext() ? buyIt.next() : null;
                while (buyNext != null && buy.price() == buyNext.price()) {
                    amount += buyNext.volume();
                    buyNext = buyIt.hasNext() ? buyIt.next() : null;
                }
                buffer.add(String.format(format, "", buy.price(), amount));
                buy = buyNext;
            } else if (buy.price() == sell.price()) {
                int amountBuy = buy.volume();
                int amountSell = sell.volume();
                Task buyNext = buyIt.hasNext() ? buyIt.next() : null;
                Task sellNext = sellIt.hasNext() ? buyIt.next() : null;
                while (buyNext != null && buy.price() == buyNext.price()) {
                    amountBuy += buyNext.volume();
                    buyNext = buyIt.hasNext() ? buyIt.next() : null;
                }
                while (sellNext != null && sell.price() == sellNext.price()) {
                    amountSell += sellNext.volume();
                    sellNext = sellIt.hasNext() ? sellIt.next() : null;
                }
                buffer.add(String.format(format, amountSell, buy.price(), amountBuy));
                buy = buyNext;
                sell = sellNext;
            } else {
                int amount = sell.volume();
                Task sellNext = sellIt.hasNext() ? sellIt.next() : null;
                while (sellNext != null && sell.price() == sellNext.price()) {
                    amount += sellNext.volume();
                    sellNext = sellIt.hasNext() ? sellIt.next() : null;
                }
                buffer.add(String.format(format, amount, sell.price(), ""));
                sell = sellNext;
            }
        }
        while (buy != null) {
            int amount = buy.volume();
            Task buyNext = buyIt.hasNext() ? buyIt.next() : null;
            while (buyNext != null && buy.price() == buyNext.price()) {
                amount += buyNext.volume();
                buyNext = buyIt.hasNext() ? buyIt.next() : null;
            }
            buffer.add(String.format(format, "", buy.price(), amount));
            buy = buyNext;
        }
        while (sell != null) {
            int amount = sell.volume();
            Task sellNext = sellIt.hasNext() ? sellIt.next() : null;
            while (sellNext != null && sell.price() == sellNext.price()) {
                amount += sellNext.volume();
                sellNext = sellIt.hasNext() ? sellIt.next() : null;
            }
            buffer.add(String.format(format, amount, sell.price(), ""));
            sell = sellNext;
        }
        return buffer.toString();
    }
}
