package ru.job4j.socket.oracle.client;

import org.junit.Test;
import ru.job4j.socket.oracle.server.Server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClientTest {

    private static final String ONE_LINE_SEPARATOR = System.lineSeparator();
    private static final String TWO_LINE_SEPARATORS = String.join("", System.lineSeparator(), System.lineSeparator());

    @Test
    public void whenOneLineAnswersThenSendsAndReceives() throws IOException {
        var userInput = Arrays.asList("hello", Server.EXIT);
        var fromServer = Arrays.asList("greeting", "answer1", "goodbye");
        this.userCommandsWereSentToServerAndConsumerHasServerAnswers(userInput, fromServer);
    }

    @Test
    public void whenMultiLineLineAnswersThenSendsAndReceives() throws IOException {
        var userInput = Arrays.asList("hello", Server.EXIT);
        var fromServer = Arrays.asList(
                "greeting",
                String.join(ONE_LINE_SEPARATOR, "line_1", "line_2", "line_3"),
                "goodbye");
        this.userCommandsWereSentToServerAndConsumerHasServerAnswers(userInput, fromServer);
    }


    /**
     * Creates new object and tests input
     * using its input/output streams.
     *
     * @param userCommands Commands to server.
     * @throws IOException In case of I/O problems.
     */
    private void userCommandsWereSentToServerAndConsumerHasServerAnswers(
            List<String> userCommands, List<String> serverAnswers) throws IOException {
        var socket = mock(Socket.class);
        var userInput = String.join(ONE_LINE_SEPARATOR, userCommands);
        var fromServer = String.join(TWO_LINE_SEPARATORS, serverAnswers);
        // we add last line because of println() inside tested class
        var mustSendToServer = String.format("%s%n", userInput);
        try (var in = new ByteArrayInputStream(fromServer.getBytes());
             var out = new ByteArrayOutputStream();
             var user = new ByteArrayInputStream(userInput.getBytes())
        ) {
            when(socket.getInputStream()).thenReturn(in);
            when(socket.getOutputStream()).thenReturn(out);
            var serverAnswersConsumer = new ArrayList<String>();
            new Client(socket, user, serverAnswersConsumer::add).start();
            var wasSentToServer = out.toString();
            assertThat(wasSentToServer, is(mustSendToServer));
            assertThat(serverAnswersConsumer, is(serverAnswers));
        }

    }
}