package ru.job4j.io.chat;

import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ChatLoggerTest {

    private final String logPath;

    public ChatLoggerTest() {
        ClassLoader loader = this.getClass().getClassLoader();
        String rootPath = loader.getResource("ru/job4j/chat").getPath();
        this.logPath = String.join("/", rootPath, "logger.txt");
    }

    @Test
    public void whenPrintlnThreeLinesThenLinesInFile() throws IOException {
        try (ChatLogger logger = new ChatLogger(this.logPath)) {
            logger.println("Test string 1");
            logger.println("Test string 2");
            logger.println("Test string 3");
        }
        List<String> linesRead = new ArrayList<>();
        try (Scanner scanner = new Scanner(new FileReader(this.logPath))) {
            while (scanner.hasNext()) {
                linesRead.add(scanner.nextLine());
            }
        }
        assertThat(linesRead, is(Arrays.asList("Test string 1", "Test string 2", "Test string 3")));
    }
}