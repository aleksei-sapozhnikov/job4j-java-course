package ru.job4j.io.chat;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Parser of file with phrases.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class PhrasesHolder implements AutoCloseable {
    /**
     * File where to take answers from.
     */
    private final RandomAccessFile file;
    /**
     * Number of phrases found in file.
     */
    private final int nPhrases;
    /**
     * List containing positions in file.
     * Each position is the beginning of a new phrase.
     */
    private final List<Long> beginnings;
    /**
     * Random numbers generator.
     */
    private final Random random = new Random();

    public PhrasesHolder(String filePath) throws IOException {
        this.file = new RandomAccessFile(filePath, "r");
        this.beginnings = this.findBeginnings();
        this.nPhrases = this.beginnings.size();
        this.validateNumberOfPhrases();
    }

    /**
     * Gets one random phrase from phrases file.
     *
     * @return One random phrase from file.
     * @throws IOException In case of I/O problems.
     */
    public String getRandomPhrase() throws IOException {
        int index = this.random.nextInt(this.nPhrases);
        long position = this.beginnings.get(index);
        this.file.seek(position);
        String line = this.file.readLine();
        return new String(line.getBytes(ISO_8859_1), UTF_8);
    }

    /**
     * Parses file with phrases, returns list of positions where each
     * phrase begins (each phrase must start from a new line).
     *
     * @return List of phrase start positions.
     * @throws IOException In case of I/O problems.
     */
    private List<Long> findBeginnings() throws IOException {
        var beginnings = new ArrayList<Long>();
        var begin = this.file.getFilePointer();
        while (this.file.readLine() != null) {
            beginnings.add(begin);
            begin = this.file.getFilePointer();
        }
        return beginnings;
    }

    /**
     * Checks if there is enough phrases for bot to speak.
     * If not enough, throws InfoNotFoundException.
     *
     * @throws InfoNotFoundException If number of phrases == 0.
     */
    private void validateNumberOfPhrases() throws InfoNotFoundException {
        if (this.nPhrases == 0) {
            throw new InfoNotFoundException("Phrases for answer not found");
        }
    }

    /**
     * Closes resources.
     *
     * @throws IOException In case of I/O problems.
     */
    @Override
    public void close() throws IOException {
        this.file.close();
    }
}
