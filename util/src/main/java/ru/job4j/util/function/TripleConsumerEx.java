package ru.job4j.util.function;

/**
 * It takes three args.
 *
 * @param <F> first.
 * @param <S> second.
 * @param <T> third.
 */
@FunctionalInterface
public interface TripleConsumerEx<F, S, T> {
    /**
     * It takes three args.
     *
     * @param first  first.
     * @param second second.
     * @param third  third.
     * @throws Exception possible exception.
     */
    void accept(F first, S second, T third) throws Exception;
}
