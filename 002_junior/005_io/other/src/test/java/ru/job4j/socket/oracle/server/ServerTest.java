package ru.job4j.socket.oracle.server;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServerTest {

    private static final String ONE_LINE_SEPARATOR = System.lineSeparator();
    private static final String TWO_LINE_SEPARATORS = String.join("", System.lineSeparator(), System.lineSeparator());

    private final ResponsesHolder responder = new ResponsesHolder();

    @Test
    public void whenOnlyExitThenGoodbye() throws IOException {
        var commands = Arrays.asList(Server.EXIT);
        var expected = Arrays.asList(Server.GREETING, Server.GOODBYE, "");
        this.serverAnswersToCommandsAreExpected(commands, expected);
    }

    @Test
    public void whenOneLineAnswerThenWrittenRight() throws IOException {
        String request = "hello";
        var commands = Arrays.asList(request, Server.EXIT);
        var expected = Arrays.asList(Server.GREETING, this.responder.getResponse(request), Server.GOODBYE, "");
        this.serverAnswersToCommandsAreExpected(commands, expected);
    }

    @Test
    public void whenMultiLineAnswerThenWrittenRight() throws IOException {
        String request = "what can you do";
        var commands = Arrays.asList(request, Server.EXIT);
        var expected = Arrays.asList(Server.GREETING, this.responder.getResponse(request), Server.GOODBYE, "");
        this.serverAnswersToCommandsAreExpected(commands, expected);
    }

    /**
     * Creates new object and tests input
     * using its input/output streams.
     *
     * @param commands      Commands to server.
     * @param expectedLines Expected answers from servers.
     * @throws IOException In case of I/O problems.
     */
    private void serverAnswersToCommandsAreExpected(List<String> commands, List<String> expectedLines) throws IOException {
        var input = String.join(ONE_LINE_SEPARATOR, commands);
        var expected = String.join(TWO_LINE_SEPARATORS, expectedLines);
        var socket = mock(Socket.class);
        String result;
        try (var in = new ByteArrayInputStream(input.getBytes());
             var out = new ByteArrayOutputStream()) {
            when(socket.getInputStream()).thenReturn(in);
            when(socket.getOutputStream()).thenReturn(out);
            new Server(socket).start();
            result = out.toString();
        }
        assertThat(result, is(expected));
    }
}