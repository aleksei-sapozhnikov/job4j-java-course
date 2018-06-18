package ru.job4j.crud;

public interface Store<T> {

    int add(T model);

    T update(int id, T newModel);

    T delete(int id);

    T[] findAll();

    T findById(int id);
}
