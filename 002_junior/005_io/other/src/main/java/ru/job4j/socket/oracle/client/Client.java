package ru.job4j.socket.oracle.client;

import ru.job4j.socket.oracle.server.Server;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.StringJoiner;
import java.util.function.Consumer;

/**
 * Oracle client side.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class Client implements AutoCloseable {
    /**
     * To-server writer.
     */
    private final PrintWriter out;
    /**
     * From-server reader.
     */
    private final BufferedReader in;
    /**
     * User input reader.
     */
    private final Scanner console;
    /**
     * Output messages consumer.
     */
    private final Consumer<String> consumer;

    /**
     * Constructs new instance.
     *
     * @param socket   Socket with connection to server.
     * @param console  User input stream.
     * @param consumer Output messages consumer.
     * @throws IOException In case of I/O problems.
     */
    public Client(Socket socket, InputStream console, Consumer<String> consumer) throws IOException {
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.console = new Scanner(console);
        this.consumer = consumer;
    }

    /**
     * Starts client iterative work.
     *
     * @throws IOException In case of I/O problems.
     */
    public void start() throws IOException {
        String request;
        do {
            this.consumer.accept(this.readResponse());
            request = this.sendRequest(this.console);
        } while (!(request.equals(Server.EXIT)));
        this.consumer.accept(this.readResponse());
    }

    /**
     * Sends request from user and sends it to server.
     *
     * @param console User input scanner.
     * @return Request sent.
     */
    private String sendRequest(Scanner console) {
        String request = console.nextLine();
        this.out.println(request);
        return request;
    }

    /**
     * Reads response from server.
     * Asserts that server response ends by empty line.
     *
     * @return Response read.
     * @throws IOException In case of I/O problems.
     */
    private String readResponse() throws IOException {
        StringJoiner result = new StringJoiner(System.lineSeparator());
        String line = this.in.readLine();
        while (!(line == null || line.isEmpty())) {
            result.add(line);
            line = this.in.readLine();
        }
        return result.toString();
    }

    /**
     * Closes resources.
     */
    @Override
    public void close() {
        this.console.close();
    }
}
