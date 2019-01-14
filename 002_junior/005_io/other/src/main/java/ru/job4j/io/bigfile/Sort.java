package ru.job4j.io.bigfile;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Sort big file. External sorting.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class Sort {
    /**
     * Logger.
     */
    @SuppressWarnings("unused")
    private static final Logger LOG = LogManager.getLogger(Sort.class);

    /**
     * Object overhead size (to estimate String object size faster).
     */
    private static final long OBJ_OVERHEAD;

    static {
        int objectHeader = 16;
        int arrayHeader = 2;
        int objectReference = 8;
        OBJ_OVERHEAD = objectHeader + arrayHeader + objectReference;
    }

    /**
     * Maximum memory token size (in bytes) available for the application.
     */
    private final long msxTokenSize;

    /**
     * Constructs new object.
     */
    public Sort() {
        this.msxTokenSize = this.maxMemoryTokenSize();
    }

    /**
     * Constructs new object.
     *
     * @param msxTokenSize Maximum memory token suze (in bytes).
     */
    Sort(long msxTokenSize) {
        this.msxTokenSize = msxTokenSize;
    }

    /**
     * Performs sorting of given file by string length and writes result to destination file.
     *
     * @param source Source non-sorted file.
     * @param dest   Destination file.
     * @throws IOException In case of problems during reading/writing/creating temp files.
     */
    public void sort(File source, File dest) throws IOException {
        Queue<File> tokens = this.divideIntoSortedTokens(source, this.msxTokenSize);
        LOG.info(String.format("File was divided into %s tokens", tokens.size()));
        while (tokens.size() > 1) {
            tokens = this.mergeListOfSorted(tokens);
        }
        File sortedTemp = tokens.poll();
        writeResultToDest(sortedTemp, dest);
        sortedTemp.delete();
    }

    /**
     * Divides file into tokens that can stay in memory.
     * Sorts the tokens, then writes them into temp files.
     *
     * @param source    Source big file.
     * @param tokenSize Maximum size of token (bytes).
     * @return Queue of sorted temp files, each sorted.
     * @throws IOException In case of read/write problems.
     */
    private Queue<File> divideIntoSortedTokens(File source, long tokenSize) throws IOException {
        Queue<File> tokenFiles = new LinkedList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(source, StandardCharsets.UTF_8))) {
            List<String> token = this.readToken(reader, tokenSize);
            while (!token.isEmpty()) {
                token = this.sortByLength(token);
                tokenFiles.offer(this.writeTempFile(token));
                token = this.readToken(reader, tokenSize);
            }
        }
        return tokenFiles;
    }

    /**
     * Reads one memory token from file as list of lines (Strings).
     *
     * @param reader  File reader.
     * @param maxSize Maximum token size.
     * @return Token as list of String objects.
     * @throws IOException In case of reading problems.
     */
    private List<String> readToken(BufferedReader reader, long maxSize) throws IOException {
        long size = 0;
        List<String> lines = new ArrayList<>();
        String line = reader.readLine();    // at least one line must be read
        while (line != null) {
            size += this.stringSize(line);
            lines.add(line);
            line = size < maxSize ? reader.readLine() : null;
        }
        return lines;
    }

    /**
     * Sorts given list of string by their length.
     *
     * @param list List of string to sort.
     */
    private List<String> sortByLength(List<String> list) {
        List<String> result = new ArrayList<>(list);
        result.sort(Comparator.comparingInt(String::length));
        return result;
    }

    /**
     * Creates new file and writes given content into it.
     *
     * @param content Content to store.
     * @return File object.
     * @throws IOException In case of writing problems.
     */
    private File writeTempFile(List<String> content) throws IOException {
        File temp = File.createTempFile("temp_", "", new File("."));
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(temp, StandardCharsets.UTF_8))) {
            for (String s : content) {
                writer.write(String.format("%s\n", s));
            }
        }
        return temp;
    }

    /**
     * Performs 'merge two sorted files' operation with list of sorted files.
     *
     * @param files List of sorted files.
     * @return List of merge result files (at least one).
     * @throws IOException In case of problems with merging files.
     */
    private Queue<File> mergeListOfSorted(Queue<File> files) throws IOException {
        Queue<File> merged = new LinkedList<>();
        while (files.size() > 1) {
            File one = files.poll();
            File two = files.poll();
            File sorted = this.mergeSorted(one, two);
            merged.offer(sorted);
        }
        if (files.size() > 0) {
            merged.offer(files.poll());
        }
        return merged;
    }

    /**
     * Merges two sorted files into third one and deletes them.
     *
     * @param one First file.
     * @param two Second file.
     * @throws IOException In case of read/write problems.
     */
    private File mergeSorted(File one, File two) throws IOException {
        File result = File.createTempFile("temp_", "", new File("."));
        try (BufferedReader rOne = new BufferedReader(new FileReader(one, StandardCharsets.UTF_8));
             BufferedReader rTwo = new BufferedReader(new FileReader(two, StandardCharsets.UTF_8));
             BufferedWriter writer = new BufferedWriter(new FileWriter(result, StandardCharsets.UTF_8))
        ) {
            mergeLineByLine(rOne, rTwo, writer);
        }
        one.delete();
        two.delete();
        return result;
    }

    /**
     * Reads files line-by-line and merges them into third sorted file.
     *
     * @param rOne   First file reader.
     * @param rTwo   Second file reader.
     * @param writer Result file writer.
     * @throws IOException In case of read/write problems.
     */
    private void mergeLineByLine(BufferedReader rOne, BufferedReader rTwo, BufferedWriter writer) throws IOException {
        String sOne = rOne.readLine();
        String sTwo = rTwo.readLine();
        while (sOne != null && sTwo != null) {
            if (sOne.length() < sTwo.length()) {
                writer.write(String.format("%s%n", sOne));
                sOne = rOne.readLine();
            } else {
                writer.write(String.format("%s%n", sTwo));
                sTwo = rTwo.readLine();
            }
        }
        writeAllLinesLeft(rOne, writer, sOne);
        writeAllLinesLeft(rTwo, writer, sTwo);
    }

    /**
     * Writes all lines left in given BufferedReader to BufferedWriter.
     *
     * @param reader   BufferedReader object.
     * @param writer   BufferedWriter object.
     * @param lastLine Last line value which was read by the reader.
     * @throws IOException In case of read/write problems.
     */
    private void writeAllLinesLeft(BufferedReader reader, BufferedWriter writer, String lastLine) throws IOException {
        String line = lastLine;
        while (line != null) {
            writer.write(String.format("%s\n", line));
            line = reader.readLine();
        }
    }

    /**
     * Writes sorted temp file into final destination file.
     *
     * @param sortedTemp Sorted temp file.
     * @param dest       Destination file.
     * @throws IOException In case of read/write problems.
     */
    private void writeResultToDest(File sortedTemp, File dest) throws IOException {
        try (FileReader fr = new FileReader(sortedTemp);
             BufferedReader reader = new BufferedReader(fr);
             FileWriter fw = new FileWriter(dest);
             BufferedWriter writer = new BufferedWriter(fw)
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(String.format("%s%n", line));
            }
        }
    }

    /**
     * Returns maximum token size that we can store in memory.
     *
     * @return Maximum memory token size as 80% of all available memory.
     */
    private long maxMemoryTokenSize() {
        Runtime r = Runtime.getRuntime();
        long allocatedMemory = r.totalMemory() - r.freeMemory();
        long presumablyFreeMemory = r.maxMemory() - allocatedMemory;
        return presumablyFreeMemory * 4 / 5;
    }

    /**
     * Return approximate string size in bytes.
     *
     * @param string String to estimate size of.
     * @return String size estimation (in bytes).
     */
    private long stringSize(String string) {
        return (string.length() * 2) + OBJ_OVERHEAD;
    }
}
