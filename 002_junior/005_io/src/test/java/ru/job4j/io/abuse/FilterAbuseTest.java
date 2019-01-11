package ru.job4j.io.abuse;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class FilterAbuseTest {
    private final FilterAbuse filter = new FilterAbuse();

    /**
     * Test dropAbuses()
     */
    @Test
    public void deleteFirstWord() throws IOException {
        String sample = "hello my name is john";
        var abuse = new String[]{"hello"};
        String result;
        try (var in = new ByteArrayInputStream(sample.getBytes());
             var out = new ByteArrayOutputStream()) {
            this.filter.dropAbuses(in, out, abuse);
            result = new String(out.toByteArray());
        }
        assertThat(result, is("my name is john"));
    }

    @Test
    public void deleteLastWord() throws IOException {
        String sample = "hello my name is john";
        var abuse = new String[]{"john"};
        String result;
        try (var in = new ByteArrayInputStream(sample.getBytes());
             var out = new ByteArrayOutputStream()) {
            this.filter.dropAbuses(in, out, abuse);
            result = new String(out.toByteArray());
        }
        assertThat(result, is("hello my name is"));
    }

    @Test
    public void deleteCenterWord() throws IOException {
        String sample = "hello my name is john";
        var abuse = new String[]{"my", "is"};
        String result;
        try (var in = new ByteArrayInputStream(sample.getBytes());
             var out = new ByteArrayOutputStream()) {
            this.filter.dropAbuses(in, out, abuse);
            result = new String(out.toByteArray());
        }
        assertThat(result, is("hello name john"));
    }

    @Test
    public void deleteAllWords() throws IOException {
        String sample = "hello my name is john";
        var abuse = new String[]{"my", "hello", "john", "is", "name"};
        String result;
        try (var in = new ByteArrayInputStream(sample.getBytes());
             var out = new ByteArrayOutputStream()) {
            this.filter.dropAbuses(in, out, abuse);
            result = new String(out.toByteArray());
        }
        assertThat(result, is(""));
    }

    @Test
    public void deleteFirstLastAndCenterWords() throws IOException {
        String sample = "hello my name is john";
        var abuse = new String[]{"name", "john", "hello"};
        String result;
        try (var in = new ByteArrayInputStream(sample.getBytes());
             var out = new ByteArrayOutputStream()) {
            this.filter.dropAbuses(in, out, abuse);
            result = new String(out.toByteArray());
        }
        assertThat(result, is("my is"));
    }

    @Test
    public void whenDeleteNothingThenStringNotChanging() throws IOException {
        String sample = "hello my name is john";
        var abuse = new String[]{};
        String result;
        try (var in = new ByteArrayInputStream(sample.getBytes());
             var out = new ByteArrayOutputStream()) {
            this.filter.dropAbuses(in, out, abuse);
            result = new String(out.toByteArray());
        }
        assertThat(result, is("hello my name is john"));
    }

}