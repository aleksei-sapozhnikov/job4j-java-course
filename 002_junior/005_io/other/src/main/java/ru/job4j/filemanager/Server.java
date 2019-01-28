package ru.job4j.filemanager;

import ru.job4j.util.function.FunctionEx;
import ru.job4j.util.methods.InputOutputUtils;

import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.StringJoiner;

/**
 * Socket server.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class Server {
    /**
     * String showing end of message.
     */
    public static final String MSG_END = String.format("%n%s%n", "--- msg end");
    /**
     * Reads input from client.
     */
    private final Scanner in;
    /**
     * Sends output to client.
     */
    private final DataOutputStream out;
    /**
     * File storage of this server.
     */
    private final FileStorage storage;
    /**
     * Actions dispatch map.
     */
    private final Map<String, FunctionEx<String, String>> dispatch = new HashMap<>();

    /**
     * Constructs new instance.
     *
     * @param socket  Socket.
     * @param storage File storage.
     * @throws IOException In case of I/O problems.
     */
    public Server(Socket socket, FileStorage storage) throws IOException {
        this.in = new Scanner(socket.getInputStream());
        this.in.useDelimiter(MSG_END);
        this.out = new DataOutputStream(socket.getOutputStream());
        this.storage = storage;
        this.initDispatch();
    }

    /**
     * Puts values into dispatch map.
     */
    private void initDispatch() {
        this.dispatch.put("help", this::help);
        this.dispatch.put("current", this::current);
        this.dispatch.put("cd", this::cd);
        this.dispatch.put("parent", this::parent);
        this.dispatch.put("list", this::list);
        this.dispatch.put("mkdir", this::mkdir);
        this.dispatch.put("download", this::sendFile);
        this.dispatch.put("upload", this::takeFile);
        this.dispatch.put("exit", this::exit);
    }

    /**
     * Iteratively takes client messages, processes them,
     * sends request.
     *
     * @throws IOException In case of problems with writing messages.
     */
    public void start() throws IOException {
        String request;
        String response;
        do {
            request = in.next();
            response = this.process(request);
            this.out.writeBytes(response);
            this.out.writeBytes(MSG_END);
        } while (!"exit".equals(request));
    }

    /**
     * Processes client message.
     *
     * @param request Client message with request.
     * @return Result which is sent to client.
     */
    private String process(String request) {
        String result;
        try {
            var action = request.split(" ")[0];
            result = this.dispatch.getOrDefault(action, this::unknownAction).apply(request);
        } catch (Exception e) {
            result = String.format("server-side exception: %s (%s)", e.getClass().getName(), e.getMessage());
        }
        return result;
    }

    /**
     * Returns current directory.
     *
     * @param request Client request.
     * @return String with current directory.
     */
    private String current(String request) {
        var joiner = new StringJoiner("/");
        joiner.add("current: <root>");
        for (var elt : this.storage.current()) {
            joiner.add(elt.toString());
        }
        return joiner.toString();
    }

    /**
     * Returns current directory contents.
     *
     * @param request Client request.
     * @return String with current directory contents.
     * @throws IOException In case of problems in getting
     *                     directory contents.
     */
    private String list(String request) throws IOException {
        var contents = this.storage.contents();
        var joiner = new StringJoiner(System.lineSeparator());
        for (Path dir : contents.getDirs()) {
            joiner.add(String.format("%s/", dir));
        }
        for (Path other : contents.getOthers()) {
            joiner.add(other.toString());
        }
        return joiner.length() > 0 ? joiner.toString() : "Directory is empty";
    }

    /**
     * Changes current directory to given.
     *
     * @param request Client request.
     * @return Success message.
     * @throws FileNotFoundException In case if directory doesn't exist.
     */
    private String cd(String request) throws FileNotFoundException {
        var tokens = request.split(" ");
        var dest = Path.of(tokens[1]);
        this.storage.cd(dest);
        return String.format("cd to %s", dest);
    }

    /**
     * Changes current directory to its parent.
     *
     * @param request Client request.
     * @return Success message.
     * @throws IllegalStateException If current directory is root.
     */
    private String parent(String request) {
        this.storage.parent();
        return "go to parent";
    }

    /**
     * Creates new directory in current.
     *
     * @param request Client request.
     * @return Success message.
     * @throws FileAlreadyExistsException If directory exists already.
     * @throws IOException                In case of I/O problems.
     */
    private String mkdir(String request) throws IOException {
        var tokens = request.split(" ");
        var path = Path.of(tokens[1]);
        this.storage.mkdir(path);
        return String.format("created dir: %s", path);
    }

    /**
     * Sends file contents to client.
     *
     * @param request Client request.
     * @return Success message.
     * @throws NoSuchFileException If file does not exist.
     * @throws IOException         In case of I/O problems.
     */
    private String sendFile(String request) throws IOException {
        var tokens = request.split(" ");
        var source = Path.of(tokens[1]);
        try (var in = this.storage.getFileContents(source)) {
            InputOutputUtils.writeAllBytes(in, this.out);
        }
        this.out.writeBytes(MSG_END);
        return "file sent";
    }

    /**
     * Takes file contents from user.
     *
     * @param request Client request.
     * @return Success message.
     * @throws FileAlreadyExistsException If file already exists.
     * @throws IOException                In case of I/O problems.
     */
    private String takeFile(String request) throws IOException {
        var tokens = request.split(" ");
        var dest = Path.of(tokens[1]);
        try (var contents = new ByteArrayInputStream(this.in.next().getBytes())) {
            this.storage.putFileContents(dest, contents);
        }
        return "file taken";
    }

    /**
     * Sends message to client that server is
     * shutting down.
     *
     * @param request Client request.
     * @return Exit message.
     */
    private String exit(String request) {
        return "server shutting down...";
    }

    /**
     * Sends message to client that action
     * in given request is unknown.
     *
     * @param request Client request.
     * @return Exit message.
     */
    private String unknownAction(String request) {
        return String.format("Unknown request: %s%nPrint 'help' to get available commands", request);
    }

    private String help(String request) {
        return this.helpMessage();
    }

    String helpMessage() {
        return new StringJoiner(System.lineSeparator())
                .add("Available commands: ")
                .add(" - current: get directory you're in now")
                .add(" - cd $name$: go to directory $name$")
                .add("      example: 'cd dir1'")
                .add(" - parent: go to parent directory")
                .add(" - list: list directories and files in the current directory")
                .add(" - mkdir $name$: create directory $name$ in the current directory")
                .add("      example: 'mkdir newDir'")
                .add(" - download $name$ $dest$: download file $name$ from current directory to $dest$")
                .add("      example: 'download file1.txt mydisk/mydir/fileToDisk.txt'")
                .add(" - upload $name$ $source$: upload file $source$ as file $name$ to current dir")
                .add("      example: 'upload newFile.txt mydisk/mydir/fileOnDisk.txt'")
                .add(" - exit: stop server and exit application")
                .toString();
    }

}
