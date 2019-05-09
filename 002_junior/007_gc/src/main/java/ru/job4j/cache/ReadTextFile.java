package ru.job4j.cache;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Command object to read text file and return value.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class ReadTextFile implements IReadObjectValue<String, String> {
    /**
     * Returns contents of file given by key.
     *
     * @param key File key (path to file).
     * @return File content as String object.
     * @throws IOException In case of problems with reading.
     */
    @Override
    public String read(String key) throws IOException {
        Path path = Path.of(key);
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
