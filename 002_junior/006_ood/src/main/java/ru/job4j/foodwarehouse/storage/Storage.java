package ru.job4j.foodwarehouse.storage;

import java.util.List;

public interface Storage<E> {
    void add(E obj);

    List<E> getAll();
}
