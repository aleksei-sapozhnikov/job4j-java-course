package ru.job4j.filemanager;

import ru.job4j.util.methods.CommonUtils;

import java.io.IOException;
import java.net.Socket;

/**
 * Socket client launcher.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class ClientStart {
    /**
     * Server ip address.
     */
    private final String address;
    /**
     * Server socket port.
     */
    private final int port;

    /**
     * Constructs new instance of starter.
     * Reads needed values from resource .properties file.
     *
     * @param propertiesResourceFile Path to resource .properties file.
     */
    private ClientStart(String propertiesResourceFile) {
        var properties = CommonUtils.loadProperties(this, propertiesResourceFile);
        this.address = properties.getProperty("address");
        this.port = Integer.parseInt(properties.getProperty("port"));
    }

    /**
     * Main static method to start.
     *
     * @param args Command-line args.
     * @throws IOException In case of I/O problems.
     */
    public static void main(String[] args) throws IOException {
        new ClientStart("ru/job4j/filemanager/app.properties").start();
    }

    /**
     * Launches client.
     *
     * @throws IOException In case of I/O problems.
     */
    private void start() throws IOException {
        try (var socket = new Socket(this.address, this.port);
             var client = new Client(socket, System.in, System.out::println)
        ) {
            client.start();
        }
    }
}
