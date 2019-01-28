package ru.job4j.filemanager;

import ru.job4j.util.function.ConsumerEx;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.WRITE;

/**
 * Socket client.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class Client implements AutoCloseable {
    /**
     * Takes user input.
     */
    private final Scanner userIn;
    /**
     * Reads messages from server.
     */
    private final Scanner in;
    /**
     * Writes data to server.
     */
    private final DataOutputStream out;
    /**
     * Consumes messages from server (shows to user).
     */
    private final Consumer<String> consumer;

    /**
     * Action dispatch map.
     */
    private final Map<String, ConsumerEx<String>> dispatch = new HashMap<>();

    /**
     * Constructs new instance.
     *
     * @param socket   Socket.
     * @param userIn   Stream with user commands.
     * @param consumer Consumer for to-user messages.
     * @throws IOException In case of I/O problems.
     */
    public Client(Socket socket, InputStream userIn, Consumer<String> consumer) throws IOException {
        this.userIn = new Scanner(userIn);
        this.in = new Scanner(socket.getInputStream());
        this.in.useDelimiter(Server.MSG_END);
        this.out = new DataOutputStream(socket.getOutputStream());
        this.consumer = consumer;
        this.initDispatch();
    }

    /**
     * Puts values into action dispatch.
     */
    private void initDispatch() {
        this.dispatch.put("upload", this::sendFile);
        this.dispatch.put("download", this::getFile);
    }

    /**
     * Recursively takes user input, processes it,
     * sends message to server and takes response.
     */
    public void start() {
        this.consumer.accept("Waiting for command");
        String command;
        do {
            command = this.userIn.nextLine();
            this.process(command);
        } while (!("exit".equals(command)));
        consumer.accept("client shutting down...");
    }

    /**
     * Processes user input.
     *
     * @param command User command accepted.
     */
    private void process(String command) {
        try {
            var action = command.split(" ")[0];
            this.dispatch.getOrDefault(action, this::simplySend).accept(command);
            consumer.accept(this.in.next());
        } catch (Exception e) {
            this.consumer.accept(String.format("client-side exception: %s (%s)", e.getClass().getName(), e.getMessage()));
        }
    }

    /**
     * Method to send file to server.
     *
     * @param command User command accepted.
     * @throws IOException In case of problems.
     */
    private void sendFile(String command) throws IOException {
        var tokens = command.split(" ");
        var name = tokens[1];
        var source = Path.of(tokens[2]);
        if (Files.notExists(source)) {
            throw new NoSuchFileException(String.format("File not found: %s", source));
        }
        byte[] contents = Files.readAllBytes(source);
        this.out.writeBytes(String.format("upload %s", name));
        this.out.writeBytes(Server.MSG_END);
        this.out.write(contents);
        this.out.writeBytes(Server.MSG_END);
    }

    /**
     * Method to get file from server.
     *
     * @param command User command accepted.
     * @throws IOException In case of problems.
     */
    private void getFile(String command) throws IOException {
        var tokens = command.split(" ");
        var name = tokens[1];
        var dest = Path.of(tokens[2]);
        this.out.writeBytes(String.format("download %s", name));
        this.out.writeBytes(Server.MSG_END);
        byte[] contents = this.in.next().getBytes();
        Files.createDirectories(dest.getParent());
        try (var out = Files.newOutputStream(dest, CREATE, WRITE)) {
            out.write(contents);
        }
    }

    /**
     * Simply sends user command to server, does no special actions.
     *
     * @param command User command accepted.
     * @throws IOException In case of problems.
     */
    private void simplySend(String command) throws IOException {
        this.out.writeBytes(command);
        this.out.writeBytes(Server.MSG_END);
    }

    /**
     * Closes resources.
     */
    @Override
    public void close() {
        this.userIn.close();
    }
}
