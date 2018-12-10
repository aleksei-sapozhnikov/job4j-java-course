package ru.job4j.util.database;

import ru.job4j.util.function.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;

public class DatabaseQueryExecutor {

    /**
     * Dispatch param for prepared statement.
     */
    private final Map<Class<?>,
            TripleConsumerEx<Integer, PreparedStatement, Object>> dispatch = new HashMap<>();

    /**
     * Supplier of Connection object.
     */
    private final SupplierEx<Connection> connector;

    public DatabaseQueryExecutor(SupplierEx<Connection> connector) {
        this.connector = connector;
        this.initDispatch();
    }

    private void initDispatch() {
        this.dispatch.put(Integer.class,
                (index, ps, value) -> ps.setInt(index, (Integer) value));
        this.dispatch.put(String.class,
                (index, ps, value) -> ps.setString(index, (String) value));
    }

    /**
     * For-each statement with index.
     *
     * @param list     list.
     * @param consumer consumer.
     * @param <T>      type.
     * @throws Exception possible exception.
     */
    private <T> void forIndex(List<T> list, BiConsumerEx<Integer, T> consumer) throws Exception {
        for (int index = 0; index != list.size(); index++) {
            consumer.accept(index, list.get(index));
        }
    }

    /**
     * Wrapper with prepared statement.
     *
     * @param query      query
     * @param parameters params
     * @param function   function with prepared statement.
     * @param key        for generating key.
     * @param <R>        type
     * @return value.
     */
    private <R> Optional<R> execute(
            String query,
            List<Object> parameters,
            FunctionEx<PreparedStatement, R> function,
            int key
    ) {
        Optional<R> result = Optional.empty();
        try (Connection connection = connector.get();
             PreparedStatement statement = connection.prepareStatement(query, key)) {
            this.forIndex(parameters,
                    (index, value) -> dispatch.get(value.getClass()).accept(index + 1, statement, value)
            );
            result = Optional.of(function.apply(statement));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Overload without key.
     *
     * @param sql    query.
     * @param params params.
     * @param fun    function with prepared statement.
     * @param <R>    type.
     * @return value.
     */
    private <R> Optional<R> execute(String sql, List<Object> params, FunctionEx<PreparedStatement, R> fun) {
        return this.execute(sql, params, fun, Statement.NO_GENERATED_KEYS);
    }

    /**
     * The same like DB only without return value.
     *
     * @param sql    query.
     * @param params params.
     * @param fun    function with prepared statement.
     * @param key    generated key.
     * @param <R>    type.
     */
    private <R> void execute(
            String sql, List<Object> params,
            ConsumerEx<PreparedStatement> fun,
            int key
    ) {
        this.execute(
                sql, params,
                ps -> {
                    fun.accept(ps);
                    return Optional.empty();
                }, key
        );
    }

    /**
     * The same like DB only without return value.
     *
     * @param sql    query.
     * @param params params.
     * @param fun    funcation with prepared statement.
     * @param <R>    type.
     */
    private <R> void execute(String sql, List<Object> params, ConsumerEx<PreparedStatement> fun) {
        this.execute(sql, params, fun, Statement.NO_GENERATED_KEYS);
    }

    /**
     * The same like DB but without parameters.
     *
     * @param sql query.
     * @param fun funcation with prepared statement.
     * @param <R> type.
     */
    public <R> void execute(String sql, ConsumerEx<PreparedStatement> fun) {
        this.execute(sql, Collections.emptyList(), fun, Statement.NO_GENERATED_KEYS);
    }
}