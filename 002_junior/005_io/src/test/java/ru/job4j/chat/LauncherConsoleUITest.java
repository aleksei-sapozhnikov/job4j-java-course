package ru.job4j.chat;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;

public class LauncherConsoleUITest {

    @Test
    public void whenRunWithoutArgumentsThenHelpMessage() throws IOException {
        PrintStream oldOut = System.out;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             PrintStream stream = new PrintStream(out)
        ) {
            System.setOut(stream);
            LauncherConsoleUI.main(new String[0]);
            System.setOut(oldOut);
            assertThat(new String(out.toByteArray()), startsWith("Help:"));
        }

    }

}