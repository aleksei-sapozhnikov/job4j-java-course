package ru.job4j.chat;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Logger of the chat.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class ChatLogger implements AutoCloseable {
    /**
     * Writer to file.
     */
    private final PrintWriter writer;

    /**
     * Constructs new instance.
     *
     * @param logFile Path to log file.
     * @throws IOException On case of I/O problems.
     */
    public ChatLogger(String logFile) throws IOException {
        this.writer = new PrintWriter(new FileWriter(logFile));
    }

    /**
     * Prints line to log file.
     *
     * @param line Line to print.
     */
    public void println(String line) {
        this.writer.println(line);
    }

    /**
     * Closes resources.
     */
    @Override
    public void close() {
        this.writer.close();
    }


}
