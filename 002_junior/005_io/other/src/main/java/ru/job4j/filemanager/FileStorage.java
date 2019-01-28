package ru.job4j.filemanager;

import ru.job4j.util.methods.InputOutputUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.nio.file.StandardOpenOption.CREATE_NEW;
import static java.nio.file.StandardOpenOption.WRITE;

/**
 * Class to work with disk file storage.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class FileStorage {
    /**
     * Storage root directory.
     */
    private final Path root;
    /**
     * Storage current directory.
     */
    private Path current;

    /**
     * Constructs new instance.
     *
     * @param root Storage root directory.
     */
    public FileStorage(Path root) {
        this.root = root;
        this.current = root;
    }

    /**
     * Returns current storage directory (relative from root).
     *
     * @return Relative current directory path.
     */
    public Path current() {
        return this.root.relativize(this.current);
    }

    /**
     * Returns current directory inner elements
     * as one object.
     *
     * @return Object with directory inner elements.
     * @throws IOException In case of I/O problems.
     */
    public Contents contents() throws IOException {
        try (var stream = Files.newDirectoryStream(this.current)) {
            return new Contents(this.current, stream);
        }
    }

    /**
     * Changes current directory to given (relative from root).
     *
     * @param path Path to change to (relative from root).
     * @throws FileNotFoundException If the directory doesn't exist.
     */
    public void cd(Path path) throws FileNotFoundException {
        Path dest = this.current.resolve(path);
        if (Files.notExists(dest)) {
            throw new FileNotFoundException(String.format("Directory doesn't exist: %s", path));
        }
        this.current = dest;
    }

    /**
     * Changes current directory to its parent directory.
     *
     * @throws IllegalStateException If current directory is root.
     */
    public void parent() throws IllegalStateException {
        if (this.current.equals(this.root)) {
            throw new IllegalStateException("Can't get parent from root");
        }
        this.current = this.current.getParent();
    }

    /**
     * Creates new directory.
     *
     * @param path Directory to create.
     * @throws FileAlreadyExistsException If directory exists already.
     * @throws IOException                In case of I/O problems.
     */
    public void mkdir(Path path) throws IOException {
        Path dest = this.current.resolve(path);
        if (Files.exists(dest)) {
            throw new FileAlreadyExistsException(String.format("Directory already exists: %s", path));
        }
        Files.createDirectories(dest);
    }

    /**
     * Returns file contents an InputStream object.
     *
     * @param file File path (relative).
     * @return InputStream object of file contents.
     * @throws NoSuchFileException If file does not exist.
     * @throws IOException         In case of I/O problems.
     */
    public InputStream getFileContents(Path file) throws IOException {
        var source = this.current.resolve(file);
        if (Files.notExists(source)) {
            throw new NoSuchFileException(String.format("file not found: %s", file));
        }
        return Files.newInputStream(source);
    }

    /**
     * Writes given file contents to disk.
     *
     * @param file          Path to file.
     * @param inputContents InputStream object with contents.
     * @throws FileAlreadyExistsException If file already exists.
     * @throws IOException                In case of I/O problems.
     */
    public void putFileContents(Path file, InputStream inputContents) throws IOException {
        var source = this.current.resolve(file);
        if (Files.exists(source)) {
            throw new FileAlreadyExistsException(String.format("file already exists: %s", file));
        }
        try (var out = Files.newOutputStream(source, CREATE_NEW, WRITE)) {
            InputOutputUtils.writeAllBytes(inputContents, out);
        }
    }

    /**
     * Class to hold directory contents grouped by lists.
     */
    public static class Contents {
        /**
         * List of directories.
         */
        private final List<Path> dirs;
        /**
         * List of other objects.
         */
        private final List<Path> others;

        /**
         * Constructs new instance.
         *
         * @param dir      Directory to get contents of.
         * @param contents DirectoryStream object to iterate.
         */
        private Contents(Path dir, DirectoryStream<Path> contents) {
            this.dirs = new ArrayList<>();
            this.others = new ArrayList<>();
            for (Path elt : contents) {
                if (Files.isDirectory(elt)) {
                    this.dirs.add(dir.relativize(elt));
                } else {
                    this.others.add(dir.relativize(elt));
                }
            }
            this.dirs.sort(Comparator.naturalOrder());
            this.others.sort(Comparator.naturalOrder());
        }

        /**
         * Returns dirs.
         *
         * @return Value of dirs field.
         */
        public List<Path> getDirs() {
            return this.dirs;
        }

        /**
         * Returns others.
         *
         * @return Value of others field.
         */
        public List<Path> getOthers() {
            return this.others;
        }
    }

}
