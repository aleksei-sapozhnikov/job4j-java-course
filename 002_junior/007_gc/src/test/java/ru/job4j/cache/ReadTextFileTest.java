package ru.job4j.cache;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ReadTextFileTest {

    private final Path file1;

    public ReadTextFileTest() throws IOException {
        this.file1 = this.initFileOne();
    }

    private Path initFileOne() throws IOException {
        var file = Files.createTempFile("cache-", ".txt");
        Files.writeString(file, "Hello, I'm file one");
        return file;
    }

    @Test
    public void whenReadFileThenResult() throws Exception {
        var result = new ReadTextFile().read(this.file1.toString());
        assertThat(result, is("Hello, I'm file one"));
    }
}