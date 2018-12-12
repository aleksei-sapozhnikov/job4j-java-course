package ru.job4j.util.database;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import ru.job4j.util.function.SupplierEx;

import java.sql.*;
import java.util.Arrays;
import java.util.Map;


import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class DbExecutorTest {

    @Mock
    private Connection connection = Mockito.mock(Connection.class);
    @Mock
    private PreparedStatement pStatement = Mockito.mock(PreparedStatement.class);
    @Mock
    private ResultSet resultSet = Mockito.mock(ResultSet.class);

    private SupplierEx<Connection> connector = () -> this.connection;

    private DbExecutor defaultExecutor = new DbExecutor.Builder(this.connector).build();

    @Test
    public void whenResultSetThenValues() throws Exception {
        Mockito.when(this.connection.prepareStatement(Mockito.anyString(), Mockito.anyInt()))
                .thenReturn(this.pStatement);
        Mockito.when(this.pStatement.executeQuery()).thenReturn(this.resultSet);
        Mockito.when(this.resultSet.getString(1)).thenReturn("string value");
        Mockito.when(this.resultSet.getInt(2)).thenReturn(55);
        Mockito.when(this.resultSet.next()).thenReturn(true, true, false);
        //
        Map<Integer, DbExecutor.ObjValue> result = defaultExecutor.executeQuery(
                "query",
                Arrays.asList(18, "asd"),
                Arrays.asList(String.class, Integer.class)
        ).orElseThrow(() -> new RuntimeException("Empty result obtained."))
                .get(0);
        //
        assertThat(result.get(1).getString(), is("string value"));
        assertThat(result.get(2).getInteger(), is(55));
    }
}