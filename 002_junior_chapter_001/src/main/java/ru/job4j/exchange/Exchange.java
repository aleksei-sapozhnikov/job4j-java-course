package ru.job4j.exchange;

import java.util.*;

public class Exchange {
    private Set<OrderBook> books = new HashSet<>();

    public static void main(String[] args) {
        List<String> list = new LinkedList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        ListIterator<String> it = list.listIterator(list.size());
        System.out.println(it.previous());
        System.out.println(it.previous());
        System.out.println(it.previous());

    }

    boolean processTask(Task task) {
        OrderBook book = getOrderBook(task.issuer());
        return book.processTask(task);
    }

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

    public String toStringForTests() {
        StringJoiner buffer = new StringJoiner(System.lineSeparator());
        for (OrderBook temp : this.books) {
            buffer.add(temp.toStringForTests());
            buffer.add("");
        }
        return buffer.toString();
    }

}