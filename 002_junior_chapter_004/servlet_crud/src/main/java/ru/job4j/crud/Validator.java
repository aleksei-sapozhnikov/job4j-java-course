package ru.job4j.crud;

public interface Validator<T> {

    int add(T model);

    boolean update(int id, T newModel);

    T delete(int id);

    T[] findAll();

    T findById(int id);
}
