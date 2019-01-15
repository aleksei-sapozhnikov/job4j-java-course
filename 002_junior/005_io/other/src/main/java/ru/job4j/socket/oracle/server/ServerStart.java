package ru.job4j.socket.oracle.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Launcher for oracle server side.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class ServerStart {
    /**
     * Logger.
     */
    @SuppressWarnings("unused")
    private static final Logger LOG = LogManager.getLogger(ServerStart.class);

    /**
     * Starts server work.
     *
     * @param args Command-line arguments.
     * @throws IOException In case of I/O problems.
     */
    public static void main(String[] args) throws IOException {
        try (Socket socket = new ServerSocket(5000).accept()) {
            new Server(socket).start();
        }
    }
}
