package ru.job4j.socket.oracle.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Oracle server side.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class Server {
    /**
     * Command to stop working.
     */
    public static final String EXIT = "exit";
    /**
     * Greeting message to client.
     */
    public static final String GREETING = "Hello my friend I'm oracle! Ask your questions";
    /**
     * Goodbye message to client.
     */
    public static final String GOODBYE = "Goodbye my friend";
    /**
     * Logger.
     */
    @SuppressWarnings("unused")
    private static final Logger LOG = LogManager.getLogger(Server.class);
    /**
     * To-client writer.
     */
    private final PrintWriter out;
    /**
     * From-client reader.
     */
    private final BufferedReader in;

    /**
     * Constructs new instance.
     *
     * @param socket Socket with connection to server.
     * @throws IOException In case of I/O problems.
     */
    public Server(Socket socket) throws IOException {
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    /**
     * Starts iterative server work.
     *
     * @throws IOException In case of I/O problems.
     */
    public void start() throws IOException {
        ResponsesHolder dispatch = new ResponsesHolder();
        this.writeResponse(GREETING);
        String request = this.getNextRequest();
        while (!(EXIT.equals(request))) {
            this.writeResponse(dispatch.getResponse(request));
            request = this.getNextRequest();
        }
        this.writeResponse(GOODBYE);
        LOG.info("Server: shutting down...");
    }

    /**
     * Writes response to client.
     * Adds empty line to show the end of the message.
     *
     * @param response Response to write.
     */
    private void writeResponse(String response) {
        this.out.println(response);
        this.out.println();
    }

    /**
     * Returns next request from user.
     *
     * @return Request from user.
     * @throws IOException In case of I/O problems.
     */
    private String getNextRequest() throws IOException {
        String request = this.in.readLine();
        LOG.info(String.format("Got request: '%s'", request));
        return request;

    }
}
