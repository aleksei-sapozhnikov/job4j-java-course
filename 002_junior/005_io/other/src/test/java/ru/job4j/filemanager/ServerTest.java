package ru.job4j.filemanager;

import org.junit.Test;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class ServerTest {

    private static final String LN = System.lineSeparator();

    private Path root;
    private Path source;
    private Path file1;

    public ServerTest() throws IOException {
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
    public void whenCurrentThenCallCurrentAndMessage() throws IOException {
        // Define client requests
        var requests = String.join(Server.MSG_END,
                "current",
                "exit");
        // Get real messages from server to client
        String resultMessage;
        try (var in = new ByteArrayInputStream(requests.getBytes());
             var out = new ByteArrayOutputStream()
        ) {
            var socket = mock(Socket.class);
            when(socket.getInputStream()).thenReturn(in);
            when(socket.getOutputStream()).thenReturn(out);
            var storage = mock(FileStorage.class);
            // Result from inner called method
            when(storage.current()).thenReturn(Path.of("dir1/dir2"));
            // Start server work
            new Server(socket, storage).start();
            resultMessage = out.toString();
        }
        // Define expected sent from server to client
        String expectedMessage;
        try (var out = new ByteArrayOutputStream()) {
            out.writeBytes("current: <root>/dir1/dir2".getBytes());
            out.writeBytes(Server.MSG_END.getBytes());
            out.writeBytes("server shutting down...".getBytes());
            out.writeBytes(Server.MSG_END.getBytes());
            expectedMessage = out.toString();
        }
        // Asserting
        assertThat(resultMessage, is(expectedMessage));
    }

    @Test
    public void whenListThenCallGetContentsAndMessage() throws IOException {
        // Define client requests
        var requests = String.join(Server.MSG_END,
                "list",
                "exit");
        // Get real messages from server to client
        String resultMessage;
        try (var in = new ByteArrayInputStream(requests.getBytes());
             var out = new ByteArrayOutputStream()
        ) {
            var socket = mock(Socket.class);
            when(socket.getInputStream()).thenReturn(in);
            when(socket.getOutputStream()).thenReturn(out);
            var storage = mock(FileStorage.class);
            // Result from inner called method
            var contents = mock(FileStorage.Contents.class);
            var dirs = List.of(Path.of("dir1"), Path.of("dir2"));
            var others = List.of(Path.of("file1"));
            when(contents.getDirs()).thenReturn(dirs);
            when(contents.getOthers()).thenReturn(others);
            when(storage.contents()).thenReturn(contents);
            // Start server work
            new Server(socket, storage).start();
            resultMessage = out.toString();
        }
        // Define expected sent from server to client
        String expectedMessage;
        try (var out = new ByteArrayOutputStream()) {
            out.writeBytes(String.join(LN, "dir1/", "dir2/", "file1").getBytes());
            out.writeBytes(Server.MSG_END.getBytes());
            out.writeBytes("server shutting down...".getBytes());
            out.writeBytes(Server.MSG_END.getBytes());
            expectedMessage = out.toString();
        }
        // Asserting
        assertThat(resultMessage, is(expectedMessage));
    }

    @Test
    public void whenCdThenCallCdAndMessage() throws IOException {
        // Define client requests
        var requests = String.join(Server.MSG_END,
                "cd dir1",
                "exit");
        // Get real messages from server to client
        String resultMessage;
        boolean calledRight;
        try (var in = new ByteArrayInputStream(requests.getBytes());
             var out = new ByteArrayOutputStream()
        ) {
            var socket = mock(Socket.class);
            when(socket.getInputStream()).thenReturn(in);
            when(socket.getOutputStream()).thenReturn(out);
            var storage = mock(FileStorage.class);
            // Check if needed method was invoked
            var calledTemp = new boolean[]{false};
            doAnswer(i -> {
                calledTemp[0] = true;
                return null;
            }).when(storage).cd(Path.of("dir1"));
            new Server(socket, storage).start();
            calledRight = calledTemp[0];
            resultMessage = out.toString();
        }
        // Define expected sent from server to client
        String expectedMessage;
        try (var out = new ByteArrayOutputStream()) {
            out.writeBytes("cd to dir1".getBytes());
            out.writeBytes(Server.MSG_END.getBytes());
            out.writeBytes("server shutting down...".getBytes());
            out.writeBytes(Server.MSG_END.getBytes());
            expectedMessage = out.toString();
        }
        // Asserting
        assertTrue(calledRight);
        assertThat(resultMessage, is(expectedMessage));
    }

    @Test
    public void whenParentThenCallParentAndMessage() throws IOException {
        // Define client requests
        var requests = String.join(Server.MSG_END,
                "parent",
                "exit");
        // Get real messages from server to client
        String resultMessage;
        boolean calledRight;
        try (var in = new ByteArrayInputStream(requests.getBytes());
             var out = new ByteArrayOutputStream()
        ) {
            var socket = mock(Socket.class);
            when(socket.getInputStream()).thenReturn(in);
            when(socket.getOutputStream()).thenReturn(out);
            var storage = mock(FileStorage.class);
            // Check if needed method was invoked
            var calledTemp = new boolean[]{false};
            doAnswer(i -> {
                calledTemp[0] = true;
                return null;
            }).when(storage).parent();
            new Server(socket, storage).start();
            calledRight = calledTemp[0];
            resultMessage = out.toString();
        }
        // Define expected sent from server to client
        String expectedMessage;
        try (var out = new ByteArrayOutputStream()) {
            out.writeBytes("go to parent".getBytes());
            out.writeBytes(Server.MSG_END.getBytes());
            out.writeBytes("server shutting down...".getBytes());
            out.writeBytes(Server.MSG_END.getBytes());
            expectedMessage = out.toString();
        }
        // Asserting
        assertTrue(calledRight);
        assertThat(resultMessage, is(expectedMessage));
    }

    @Test
    public void whenMkdirThenCallMkdirAndMessage() throws IOException {
        // Define client requests
        var requests = String.join(Server.MSG_END,
                "mkdir dir1",
                "exit");
        // Get real messages from server to client
        String resultMessage;
        boolean calledRight;
        try (var in = new ByteArrayInputStream(requests.getBytes());
             var out = new ByteArrayOutputStream()
        ) {
            var socket = mock(Socket.class);
            when(socket.getInputStream()).thenReturn(in);
            when(socket.getOutputStream()).thenReturn(out);
            var storage = mock(FileStorage.class);
            // Check if needed method was invoked
            var calledTemp = new boolean[]{false};
            doAnswer(i -> {
                calledTemp[0] = true;
                return null;
            }).when(storage).mkdir(Path.of("dir1"));
            new Server(socket, storage).start();
            calledRight = calledTemp[0];
            resultMessage = out.toString();
        }
        // Define expected sent from server to client
        String expectedMessage;
        try (var out = new ByteArrayOutputStream()) {
            out.writeBytes("created dir: dir1".getBytes());
            out.writeBytes(Server.MSG_END.getBytes());
            out.writeBytes("server shutting down...".getBytes());
            out.writeBytes(Server.MSG_END.getBytes());
            expectedMessage = out.toString();
        }
        // Asserting
        assertTrue(calledRight);
        assertThat(resultMessage, is(expectedMessage));
    }

    @Test
    public void whenDownloadThenSendFile() throws Exception {
        var source = this.file1;
        var fileName = source.getFileName();
        // Define client requests
        var requests = String.join(Server.MSG_END,
                String.format("download %s", fileName),
                "exit");
        // Get real messages from server to client
        String resultMessage;
        try (var in = new ByteArrayInputStream(requests.getBytes());
             var out = new ByteArrayOutputStream()
        ) {
            var socket = mock(Socket.class);
            when(socket.getInputStream()).thenReturn(in);
            when(socket.getOutputStream()).thenReturn(out);
            var storage = mock(FileStorage.class);
            when(storage.getFileContents(fileName)).thenReturn(Files.newInputStream(source));
            new Server(socket, storage).start();
            resultMessage = out.toString();
        }
        // Define expected sent from server to client
        String expectedMessage;
        try (var out = new ByteArrayOutputStream()) {
            out.write(Files.readAllBytes(source));
            out.write(Server.MSG_END.getBytes());
            out.writeBytes("file sent".getBytes());
            out.writeBytes(Server.MSG_END.getBytes());
            out.writeBytes("server shutting down...".getBytes());
            out.writeBytes(Server.MSG_END.getBytes());
            expectedMessage = out.toString();
        }
        // Asserting
        assertThat(resultMessage, is(expectedMessage));
    }

    @Test
    public void whenUploadThenFileStorageAddFile() throws Exception {
        var source = this.file1;
        var fileName = source.getFileName();
        // Define client requests
        String requests;
        try (var out = new ByteArrayOutputStream()) {
            out.write(String.format("upload %s", fileName).getBytes());
            out.write(Server.MSG_END.getBytes());
            out.write(Files.readAllBytes(source));
            out.write(Server.MSG_END.getBytes());
            out.write("exit".getBytes());
            out.write(Server.MSG_END.getBytes());
            requests = out.toString();
        }
        // Get real messages from server to client
        boolean calledRight;
        String resultOut;
        try (var in = new ByteArrayInputStream(requests.getBytes());
             var out = new ByteArrayOutputStream()
        ) {
            var socket = mock(Socket.class);
            when(socket.getInputStream()).thenReturn(in);
            when(socket.getOutputStream()).thenReturn(out);
            var storage = mock(FileStorage.class);
            // Check if needed method was invoked
            boolean[] called = new boolean[]{false};
            doAnswer(invocation -> {
                called[0] = true;
                return null;
            }).when(storage).putFileContents(eq(fileName), any(InputStream.class));
            new Server(socket, storage).start();
            calledRight = called[0];
            resultOut = out.toString();
        }
        // Define expected sent from server to client
        String expectedOut;
        try (var out = new ByteArrayOutputStream()) {
            out.writeBytes("file taken".getBytes());
            out.writeBytes(Server.MSG_END.getBytes());
            out.writeBytes("server shutting down...".getBytes());
            out.writeBytes(Server.MSG_END.getBytes());
            expectedOut = out.toString();
        }
        // Asserting
        assertTrue(calledRight);
        assertThat(resultOut, is(expectedOut));
    }

    @Test
    public void whenUnknownActionThenMessage() throws Exception {
        // Define client requests
        String requests;
        try (var out = new ByteArrayOutputStream()) {
            out.write("somethingUnknown".getBytes());
            out.write(Server.MSG_END.getBytes());
            out.write("exit".getBytes());
            out.write(Server.MSG_END.getBytes());
            requests = out.toString();
        }
        // Get real messages from server to client
        String resultOut;
        try (var in = new ByteArrayInputStream(requests.getBytes());
             var out = new ByteArrayOutputStream()
        ) {
            var socket = mock(Socket.class);
            when(socket.getInputStream()).thenReturn(in);
            when(socket.getOutputStream()).thenReturn(out);
            var storage = mock(FileStorage.class);
            new Server(socket, storage).start();
            resultOut = out.toString();
        }
        // Define expected sent from server to client
        String expectedOut;
        try (var out = new ByteArrayOutputStream()) {
            out.writeBytes(String.format("Unknown request: somethingUnknown%nPrint 'help' to get available commands").getBytes());
            out.writeBytes(Server.MSG_END.getBytes());
            out.writeBytes("server shutting down...".getBytes());
            out.writeBytes(Server.MSG_END.getBytes());
            expectedOut = out.toString();
        }
        // Asserting
        assertThat(resultOut, is(expectedOut));
    }

    @Test
    public void whenExceptionThrownThenMessageOfException() throws IOException {
        // Define client requests
        var requests = String.join(Server.MSG_END,
                "cd dir1",
                "exit");
        // Get real messages from server to client
        String resultMessage;
        try (var in = new ByteArrayInputStream(requests.getBytes());
             var out = new ByteArrayOutputStream()
        ) {
            var socket = mock(Socket.class);
            when(socket.getInputStream()).thenReturn(in);
            when(socket.getOutputStream()).thenReturn(out);
            var storage = mock(FileStorage.class);
            doThrow(new FileNotFoundException("no directory found: dir1"))
                    .when(storage).cd(Path.of("dir1"));
            new Server(socket, storage).start();
            resultMessage = out.toString();
        }
        // Define expected sent from server to client
        String expectedMessage;
        try (var out = new ByteArrayOutputStream()) {
            out.writeBytes("server-side exception: java.io.FileNotFoundException (no directory found: dir1)".getBytes());
            out.writeBytes(Server.MSG_END.getBytes());
            out.writeBytes("server shutting down...".getBytes());
            out.writeBytes(Server.MSG_END.getBytes());
            expectedMessage = out.toString();
        }
        // Asserting
        assertThat(resultMessage, is(expectedMessage));
    }

    @Test
    public void whenHelpThenHelpMessage() throws IOException {
        // Define client requests
        var requests = String.join(Server.MSG_END,
                "help",
                "exit");
        // Get real messages from server to client
        String resultMessage;
        try (var in = new ByteArrayInputStream(requests.getBytes());
             var out = new ByteArrayOutputStream()
        ) {
            var socket = mock(Socket.class);
            when(socket.getInputStream()).thenReturn(in);
            when(socket.getOutputStream()).thenReturn(out);
            var storage = mock(FileStorage.class);
            new Server(socket, storage).start();
            resultMessage = out.toString();
        }
        // Define expected sent from server to client
        String expectedMessage;
        try (var out = new ByteArrayOutputStream()) {
            out.writeBytes("server-side exception: java.io.FileNotFoundException (no directory found: dir1)".getBytes());
            out.writeBytes(Server.MSG_END.getBytes());
            out.writeBytes("server shutting down...".getBytes());
            out.writeBytes(Server.MSG_END.getBytes());
            expectedMessage = out.toString();
        }
        // Asserting
        assertTrue(resultMessage.startsWith("Available commands"));
    }

}