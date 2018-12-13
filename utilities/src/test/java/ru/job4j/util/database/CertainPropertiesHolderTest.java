package ru.job4j.util.database;

import org.junit.Test;
import ru.job4j.util.common.Utils;

import java.util.Properties;
import java.util.function.Function;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class CertainPropertiesHolderTest {

    @Test
    public void whenDefaultHolderThenPropertiesLoaded() {
        Properties properties = new Properties();
        properties.put("sql.stringQuery", "select id from user");
        properties.put("sql.fileQuery", "file:ru/job4j/util/database/sample_query_001.sql");
        //
        CertainPropertiesHolder holder = new CertainPropertiesHolder(properties, "sql.", "file:");
        String stringExpected = "select id from user";
        String fileExpected = Utils.loadFileAsString(this, "UTF-8", "ru/job4j/util/database/sample_query_001.sql");
        //
        assertThat(holder.get("sql.stringQuery"), is(stringExpected));
        assertThat(holder.get("sql.fileQuery"), is(fileExpected));
    }

    @Test
    public void whenQueryNotFoundThenStringValueNull() {
        Properties properties = new Properties();
        properties.put("bla", "blabla");
        CertainPropertiesHolder holder = new CertainPropertiesHolder(properties, ".sql", "file:");
        assertThat(holder.get("non-present"), is("null"));
    }


    @Test
    public void whenHolderAsFunctionThenPropertiesLoaded() {
        Properties properties = new Properties();
        properties.put("sql.stringQuery", "select id from user");
        properties.put("sql.fileQuery", "file:ru/job4j/util/database/sample_query_001.sql");
        //
        Function<String, String> function = new CertainPropertiesHolder(properties, "sql.", "file:");
        String stringExpected = "select id from user";
        String fileExpected = Utils.loadFileAsString(this, "UTF-8", "ru/job4j/util/database/sample_query_001.sql");
        //
        assertThat(function.apply("sql.stringQuery"), is(stringExpected));
        assertThat(function.apply("sql.fileQuery"), is(fileExpected));
    }
}