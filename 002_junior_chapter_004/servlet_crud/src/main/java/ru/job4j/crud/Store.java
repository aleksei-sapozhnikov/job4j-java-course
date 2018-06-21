package ru.job4j.crud;

public interface Store<T> {

    int add(T model);

    boolean update(T newModel);

    T delete(int id);

    T[] findAll();

    T findById(int id);
}
