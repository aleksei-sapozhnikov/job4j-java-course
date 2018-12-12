package ru.job4j.util.function;

@FunctionalInterface
public interface TriFunctionEx<T, U, V, R> {
    R apply(T t, U u, V v);
}
