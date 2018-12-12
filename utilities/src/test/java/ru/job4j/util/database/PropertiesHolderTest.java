package ru.job4j.util.database;

import org.junit.Test;
import ru.job4j.util.common.Utils;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Optional;
import java.util.Properties;
import java.util.function.Function;

public class PropertiesHolderTest {

    @Test
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public void whenDefaultHolderThenPropertiesLoaded() {
        Properties properties = new Properties();
        properties.put("sql.stringQuery", "select id from user");
        properties.put("sql.fileQuery", "file:ru/job4j/util/database/sample_query_001.sql");
        //
        PropertiesHolder holder = new PropertiesHolder.Builder(properties, "sql.").build();
        String stringExpected = "select id from user";
        String fileExpected = Utils.loadFileAsString(this, "UTF-8", "ru/job4j/util/database/sample_query_001.sql");
        //
        assertThat(holder.get("sql.stringQuery").get(), is(stringExpected));
        assertThat(holder.get("sql.fileQuery").get(), is(fileExpected));
    }

    @Test
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public void whenCustomFileKeyThenPropertiesLoaded() {
        Properties properties = new Properties();
        properties.put("mySqlQuery:::stringQuery", "select id from user");
        properties.put("mySqlQuery:::fileQuery", "myBeautifulFile:;:ru/job4j/util/database/sample_query_001.sql");
        //
        PropertiesHolder holder = new PropertiesHolder.Builder(properties, "mySqlQuery:::")
                .setFileKey("myBeautifulFile:;:").build();
        String stringExpected = "select id from user";
        String fileExpected = Utils.loadFileAsString(this, "UTF-8", "ru/job4j/util/database/sample_query_001.sql");
        //
        assertThat(holder.get("mySqlQuery:::stringQuery").get(), is(stringExpected));
        assertThat(holder.get("mySqlQuery:::fileQuery").get(), is(fileExpected));
    }

    @Test
    public void whenQueryNotFoundThenEmptyOptional() {
        Properties properties = new Properties();
        properties.put("bla", "blabla");
        PropertiesHolder holder = new PropertiesHolder.Builder(properties, ".sql").build();
        assertThat(holder.get("non-present"), is(Optional.empty()));
    }


    @Test
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public void whenHolderAsFunctionThenPropertiesLoaded() {
        Properties properties = new Properties();
        properties.put("sql.stringQuery", "select id from user");
        properties.put("sql.fileQuery", "file:ru/job4j/util/database/sample_query_001.sql");
        //
        Function<String, Optional<String>> function = new PropertiesHolder.Builder(properties, "sql.").build();
        String stringExpected = "select id from user";
        String fileExpected = Utils.loadFileAsString(this, "UTF-8", "ru/job4j/util/database/sample_query_001.sql");
        //
        assertThat(function.apply("sql.stringQuery").get(), is(stringExpected));
        assertThat(function.apply("sql.fileQuery").get(), is(fileExpected));
    }
}