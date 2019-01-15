package ru.job4j.socket.oracle.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.Socket;

/**
 * Launcher for oracle client side.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class ClientStart {
    /**
     * Logger.
     */
    @SuppressWarnings("unused")
    private static final Logger LOG = LogManager.getLogger(ClientStart.class);

    /**
     * Starts client work.
     *
     * @param args Command-line arguments.
     * @throws IOException In case of I/O problems.
     */
    public static void main(String[] args) throws IOException {
        try (Socket socket = new Socket("localhost", 5000);
             Client client = new Client(socket, System.in, System.out::println)) {
            client.start();
        }
    }
}
