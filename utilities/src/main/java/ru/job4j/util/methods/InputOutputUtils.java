package ru.job4j.util.methods;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Util methods for common I/O operations.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class InputOutputUtils {
    /**
     * Writes all bytes from input stream to output stream.
     *
     * @param input  InputStream object.
     * @param output OutputStream object.
     * @throws IOException In case of I/O problems.
     */
    public static void writeAllBytes(InputStream input, OutputStream output) throws IOException {
        int length;
        byte[] buff = new byte[8192];
        while ((length = input.read(buff)) != -1) {
            output.write(buff, 0, length);
        }
    }

    /**
     * Writes given number of bytes from input to outpit.
     *
     * @param input  InputStream object.
     * @param output OutputStream object.
     * @param length Number of bytes.
     * @throws IOException In case of I/O problems.
     */
    public static void writeNBytes(InputStream input, OutputStream output, long length) throws IOException {
        int bufferSize = 8192;
        byte[] buffer = new byte[bufferSize];
        long read = 0;
        while (length - read >= bufferSize) {
            input.read(buffer);
            output.write(buffer);
            read += bufferSize;
        }
        var rest = (int) (length - read);
        input.readNBytes(buffer, 0, rest);
        output.write(buffer, 0, rest);
    }

    /**
     * Deletes everything from directory and the directory itself.
     *
     * @param dir Directory to delete.
     * @throws IOException In case of I/O problems.
     */
    public static boolean deleteIfExistsRecursively(Path dir) throws IOException {
        var result = false;
        if (Files.exists(dir)) {
            Files.walkFileTree(dir, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
            result = true;
        }
        return result;
    }

}
