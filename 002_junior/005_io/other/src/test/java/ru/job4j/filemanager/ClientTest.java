package ru.job4j.filemanager;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.StringJoiner;
import java.util.function.Consumer;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClientTest {

    private static final String LN = System.lineSeparator();

    private Path root;
    private Path source;
    private Path file1;

    public ClientTest() throws IOException {
        this.createStructure();
    }

    private void createStructure() throws IOException {
        this.root = Files.createTempDirectory("filemanager");
        this.source = Files.createDirectory(root.resolve("source"));
        this.file1 = Files.createFile(this.source.resolve("file1.txt"));
        try (var out = new PrintWriter(Files.newBufferedWriter(this.file1))) {
            out.println("line 1");
            out.println("line 2");
            out.print("line 3");
        }
    }

    @Test
    public void whenDownloadThenFileWritten() throws Exception {
        var dest = Files.createTempFile(this.root, "dest", ".txt");
        var source = this.file1;
        var fileName = this.file1.getFileName();
        // Define user commands
        String commands = String.join(LN,
                String.format("download %s %s", fileName, dest),
                "exit");
        // Define server answers
        String serverMessages;
        try (var out = new ByteArrayOutputStream()) {
            out.write(Files.readAllBytes(source));
            out.write(Server.MSG_END.getBytes());
            out.writeBytes("file sent".getBytes());
            out.writeBytes(Server.MSG_END.getBytes());
            out.writeBytes("server shutting down".getBytes());
            out.writeBytes(Server.MSG_END.getBytes());
            serverMessages = out.toString();
        }
        // Get real messages to server
        String resultSentToServer;
        try (var user = new ByteArrayInputStream(commands.getBytes());
             var in = new ByteArrayInputStream(serverMessages.getBytes());
             var out = new ByteArrayOutputStream()
        ) {
            var socket = mock(Socket.class);
            when(socket.getInputStream()).thenReturn(in);
            when(socket.getOutputStream()).thenReturn(out);
            try (var client = new Client(socket, user, s -> {
            })) {
                client.start();
            }
            resultSentToServer = out.toString();
        }
        // Define expected sent to server
        String expectedSentToServer;
        try (var out = new ByteArrayOutputStream()) {
            out.write(String.format("download %s", fileName).getBytes());
            out.writeBytes(Server.MSG_END.getBytes());
            out.writeBytes("exit".getBytes());
            out.writeBytes(Server.MSG_END.getBytes());
            expectedSentToServer = out.toString();
        }
        // Asserting
        assertThat(resultSentToServer, is(expectedSentToServer));
        assertThat(Files.readAllBytes(dest), is(Files.readAllBytes(source)));
    }

    @Test
    public void whenUploadThenFileSent() throws Exception {
        var source = this.file1;
        var fileName = this.file1.getFileName();
        // Define user commands
        var commands = String.join(LN,
                String.format("upload %s %s", fileName, source),
                "exit");
        // Define server answers
        String serverAnswers;
        try (var out = new ByteArrayOutputStream()) {
            out.writeBytes("file taken".getBytes());
            out.writeBytes(Server.MSG_END.getBytes());
            out.writeBytes("server shutting down".getBytes());
            out.writeBytes(Server.MSG_END.getBytes());
            serverAnswers = out.toString();
        }
        // Get real messages to server
        String resultToServer;
        try (var user = new ByteArrayInputStream(commands.getBytes());
             var in = new ByteArrayInputStream(serverAnswers.getBytes());
             var out = new ByteArrayOutputStream()
        ) {
            var socket = mock(Socket.class);
            when(socket.getInputStream()).thenReturn(in);
            when(socket.getOutputStream()).thenReturn(out);
            try (var client = new Client(socket, user, s -> {
            })) {
                client.start();
            }
            resultToServer = out.toString();
        }
        // Define expected sent to server
        String expectedToServer;
        try (var out = new ByteArrayOutputStream()) {
            out.writeBytes(String.format("upload %s", fileName).getBytes());
            out.writeBytes(Server.MSG_END.getBytes());
            out.writeBytes(Files.readAllBytes(source));
            out.writeBytes(Server.MSG_END.getBytes());
            out.writeBytes("exit".getBytes());
            out.writeBytes(Server.MSG_END.getBytes());
            expectedToServer = out.toString();
        }
        // Asserting
        assertThat(resultToServer, is(expectedToServer));
    }

    @Test
    public void whenUploadFileNotFoundThenException() throws Exception {
        // create path and make sure this file doesn't exist
        var source = Path.of("notFoundFile");
        assertTrue(Files.notExists(source));
        var fileName = "file";
        // Define user commands
        var commands = String.join(LN,
                String.format("upload %s %s", fileName, source),
                "exit");
        // Define server answers
        String serverAnswers;
        try (var out = new ByteArrayOutputStream()) {
            out.writeBytes("server shutting down".getBytes());
            out.writeBytes(Server.MSG_END.getBytes());
            serverAnswers = out.toString();
        }
        // Get real messages to server and written to user
        String resultToServer;
        StringJoiner consumerToUser = new StringJoiner("");
        try (var user = new ByteArrayInputStream(commands.getBytes());
             var in = new ByteArrayInputStream(serverAnswers.getBytes());
             var out = new ByteArrayOutputStream()
        ) {
            var socket = mock(Socket.class);
            when(socket.getInputStream()).thenReturn(in);
            when(socket.getOutputStream()).thenReturn(out);
            Consumer<String> consumer = consumerToUser::add;
            try (var client = new Client(socket, user, consumer)) {
                client.start();
            }
            resultToServer = out.toString();
        }
        // Define expected sent to server
        String expectedToServer;
        try (var out = new ByteArrayOutputStream()) {
            out.writeBytes("exit".getBytes());
            out.writeBytes(Server.MSG_END.getBytes());
            expectedToServer = out.toString();
        }
        // Define expected written to user
        String expectedConsumerToUser = new StringBuilder()
                .append("Waiting for command")
                .append("client-side exception: java.nio.file.NoSuchFileException (File not found: notFoundFile)")
                .append("server shutting down")
                .append("client shutting down...")
                .toString();
        // Asserting
        assertThat(resultToServer, is(expectedToServer));
        assertThat(consumerToUser.toString(), is(expectedConsumerToUser));
    }
}