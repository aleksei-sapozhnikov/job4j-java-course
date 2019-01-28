package ru.job4j.filemanager;

import ru.job4j.util.methods.CommonUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.nio.file.Path;

/**
 * Socket server launcher.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class ServerStart {
    /**
     * Server file storage root folder.
     */
    private final Path storageRoot;
    /**
     * Server socket connection port.
     */
    private final int port;

    /**
     * Constructs new instance.
     * Takes connection parameter from resource .properties file.
     *
     * @param propertiesResourceFile Resource .properties file.
     */
    private ServerStart(String propertiesResourceFile) {
        var properties = CommonUtils.loadProperties(this, propertiesResourceFile);
        this.storageRoot = Path.of(properties.getProperty("storage_root"));
        this.port = Integer.parseInt(properties.getProperty("port"));
    }

    /**
     * Main method to run.
     *
     * @param args Command-line args.
     * @throws IOException In case of I/O problems.
     */
    public static void main(String[] args) throws IOException {
        new ServerStart("ru/job4j/filemanager/app.properties").start();
    }

    /**
     * Starts Server.
     *
     * @throws IOException In case of I/O problems.
     */
    private void start() throws IOException {
        var storage = new FileStorage(this.storageRoot);
        System.out.print("Waiting connection... ");
        try (var socket = new ServerSocket(this.port).accept()) {
            System.out.println("connected");
            new Server(socket, storage).start();
        }
    }
}
