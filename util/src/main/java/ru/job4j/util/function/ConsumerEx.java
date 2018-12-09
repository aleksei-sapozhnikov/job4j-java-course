package ru.job4j.util.function;

/**
 * Function interface. It take a single param.
 *
 * @param <T> type.
 */
@FunctionalInterface
public interface ConsumerEx<T> {
    /**
     * Accept arg.
     *
     * @param param arg
     * @throws Exception possible exception.
     */
    void accept(T param) throws Exception;
}