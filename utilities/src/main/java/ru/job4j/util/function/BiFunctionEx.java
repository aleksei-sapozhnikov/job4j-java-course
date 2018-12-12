package ru.job4j.util.function;

@FunctionalInterface
public interface BiFunctionEx<T, U, R> {
    R apply(T t, U u) throws Exception;
}
