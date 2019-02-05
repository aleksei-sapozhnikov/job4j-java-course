package ru.job4j.search;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

public class LauncherTest {

    private Path root;
    private Path temp;
    private Path resultFile;

    private Path source;
    private Path file1;
    private Path file2;
    private Path file3;

    private Path inDir1;
    private Path inDir1File1;
    private Path inDir1File2;


    public LauncherTest() throws IOException {
        this.initStructure();
    }

    private void initStructure() throws IOException {
        this.root = Files.createTempDirectory("searcher");
        this.temp = Files.createDirectory(this.root.resolve("temp"));
        this.resultFile = Files.createFile(this.temp.resolve("result"));

        this.source = Files.createDirectory(this.root.resolve("source"));
        this.file1 = Files.createFile(this.source.resolve("file1.txt"));
        this.file2 = Files.createFile(this.source.resolve("file2.txt"));
        this.file3 = Files.createFile(this.source.resolve("file3.png"));

        this.inDir1 = Files.createDirectory(this.source.resolve("inDir1"));
        this.inDir1File1 = Files.createFile(this.inDir1.resolve("file1.txt"));
        this.inDir1File2 = Files.createFile(this.inDir1.resolve("file2.jpg"));
    }

    /**
     * When user prints no arguments then help message.
     */
    @Test
    public void whenNoArgumentsThenMessage() throws IOException {
        String[] args = new String[0];
        checkWasMessageNoExceptionThrown(args);
    }

    /**
     * When user prints help then help message must be written.
     */
    @Test
    public void whenArgumentHelpThenMessage() throws IOException {
        String[] args = "help".split(" ");
        checkWasMessageNoExceptionThrown(args);
    }

    /**
     * Bad arguments make inner method throw exception.
     */
    @Test
    public void whenBadArgumentsThenMessage() throws IOException {
        checkWasMessageNoExceptionThrown("-asdf asdj".split(" "));
    }

    /**
     * Checks if message was printed instead of throwing exception.
     *
     * @param args Application args given by user.
     * @throws IOException In case of problems in ByteArrayOutputStream.
     */
    private void checkWasMessageNoExceptionThrown(String[] args) throws IOException {
        var stdout = System.out;
        try (var out = new ByteArrayOutputStream();
             var printer = new PrintStream(out)) {
            System.setOut(printer);
            var thrownException = new boolean[]{false};
            try {
                Launcher.main(args);
            } catch (Exception e) {
                thrownException[0] = true;
            }
            assertThat(out.size(), greaterThan(0));     // was message
            assertFalse(thrownException[0]);
        }
        System.setOut(stdout);
    }


    /**
     * Integrative test: perform search, get result to file.
     * Single
     *
     * @throws IOException In case of problems reading result file.
     */
    @Test
    public void whenMaskSingleThreadThenResult() throws IOException {
        var args = String.format(
                "-d %s -n %s %s -o %s", this.source, "*.txt", "-m", this.resultFile
        ).split(" ");
        this.testIntegrativeLauncher(args);
    }

    @Test
    public void whenMaskMultiThreadThenResult() throws IOException {
        var args = String.format(
                "-d %s -n %s %s -o %s -pt 5", this.source, "*.txt", "-m", this.resultFile
        ).split(" ");
        this.testIntegrativeLauncher(args);
    }

    private void testIntegrativeLauncher(String[] args) throws IOException {
        Launcher.main(args);
        var resultList = Files.readAllLines(this.resultFile);
        resultList.sort(Comparator.naturalOrder());
        var result = String.join(System.lineSeparator(), resultList);
        var expected = String.join(System.lineSeparator(),
                this.file1.toString(),      // file1.txt
                this.file2.toString(),      // file2.txt
                this.inDir1File1.toString()    //file1.txt
        );
        assertThat(result, is(expected));
    }
}