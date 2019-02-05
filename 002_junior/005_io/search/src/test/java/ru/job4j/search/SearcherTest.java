package ru.job4j.search;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import ru.job4j.util.methods.InputOutputUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class SearcherTest {
    private Path root;

    private Path temp;

    private Path source;
    private Path file1;
    private Path file2;
    private Path file3;

    private Path inDir1;
    private Path inDir1File1;
    private Path inDir1File2;


    public SearcherTest() throws IOException {
        this.initStructure();
    }

    private void initStructure() throws IOException {
        this.root = Files.createTempDirectory("searcher");
        this.temp = Files.createDirectory(this.root.resolve("temp"));

        this.source = Files.createDirectory(this.root.resolve("source"));
        this.file1 = Files.createFile(this.source.resolve("file1.txt"));
        this.file2 = Files.createFile(this.source.resolve("file2.txt"));
        this.file3 = Files.createFile(this.source.resolve("file3.png"));

        this.inDir1 = Files.createDirectory(this.source.resolve("inDir1"));
        this.inDir1File1 = Files.createFile(this.inDir1.resolve("file1.txt"));
        this.inDir1File2 = Files.createFile(this.inDir1.resolve("file2.jpg"));
    }

    @Before
    public void clearTemp() throws IOException {
        InputOutputUtils.deleteIfExistsRecursively(this.temp);
        Files.createDirectory(this.temp);
    }

    /**
     * Common method to test searching.
     * We have to sort result lines first to assure their order.
     * Otherwise - tests on Travis CI fail.
     *
     * @param dir        Directory to search in.
     * @param value      Value to search for.
     * @param expected   Expected result.
     * @param searchType Search type (mask, regex, full name).
     * @throws Exception In case of search problems.
     */
    private void testSearch(Path dir, String value, String expected, SearchType searchType) throws Exception {
        var resultList = new ArrayList<String>();
        new Searcher(value, searchType).search(dir, resultList::add);
        resultList.sort(Comparator.naturalOrder());
        var result = String.join(System.lineSeparator(), resultList);
        assertThat(result, is(expected));
    }

    private void testMultiThreadSearch(Path dir, String value, String expected, SearchType searchType) throws Exception {
        var resultList = new ArrayList<String>();
        new Searcher(value, searchType).search(dir, resultList::add, 5);
        resultList.sort(Comparator.naturalOrder());
        var result = String.join(System.lineSeparator(), resultList);
        assertThat(result, is(expected));
    }

    /**
     * Test MASK search.
     */
    @Test
    public void whenMaskAsteriskThenResult() throws Exception {
        var dir = this.source;
        var value = "*.txt";
        var expected = String.join(System.lineSeparator(),
                this.file1.toString(),  //file1.txt
                this.file2.toString(),   //file2.txt
                this.inDir1File1.toString()     // file1.txt
        );
        this.testSearch(dir, value, expected, SearchType.MASK);
        this.testMultiThreadSearch(dir, value, expected, SearchType.MASK);
    }

    @Test
    public void whenMaskQueueSignThenResult() throws Exception {
        var dir = this.source;
        var value = "file?.txt";
        var expected = String.join(System.lineSeparator(),
                this.file1.toString(),  // file1.txt
                this.file2.toString(),   // file2.txt
                this.inDir1File1.toString()     // file1.txt
        );
        this.testSearch(dir, value, expected, SearchType.MASK);
        this.testMultiThreadSearch(dir, value, expected, SearchType.MASK);
    }

    @Test
    public void whenMaskAsteriskAndQueueSignThenResult() throws Exception {
        var dir = this.source;
        var value = "file?.*";
        var expected = String.join(System.lineSeparator(),
                this.file1.toString(),      // file1.txt
                this.file2.toString(),      // file2.txt
                this.file3.toString(),      // file3.png
                this.inDir1File1.toString(),    // file1.txt
                this.inDir1File2.toString()     // file2.jpg
        );
        this.testSearch(dir, value, expected, SearchType.MASK);
        this.testMultiThreadSearch(dir, value, expected, SearchType.MASK);
    }

    @Test
    public void whenMaskSearchValueNotMaskThenException() {
        var dir = this.source;
        var value = "^file1.txt";
        var expected = "";
        var wasException = new boolean[]{false};
        try {
            this.testSearch(dir, value, expected, SearchType.MASK);
            this.testMultiThreadSearch(dir, value, expected, SearchType.MASK);
        } catch (Exception e) {
            wasException[0] = true;
        }
        assertTrue(wasException[0]);
    }

    /**
     * Test REGEX search.
     */
    @Test
    public void whenRegexThenResult1() throws Exception {
        var dir = this.source;
        var value = "^[a-z]+\\d.txt$";
        var expected = String.join(System.lineSeparator(),
                this.file1.toString(),          // file1.txt
                this.file2.toString(),          // file2.txt
                this.inDir1File1.toString()     // file1.txt
        );
        this.testSearch(dir, value, expected, SearchType.REGEX);
        this.testMultiThreadSearch(dir, value, expected, SearchType.REGEX);
    }

    @Test
    public void whenRegexThenResult2() throws Exception {
        var dir = this.source;
        var value = ".+ng";
        var expected = String.join(System.lineSeparator(),
                this.file3.toString()   // file3.png
        );
        this.testSearch(dir, value, expected, SearchType.REGEX);
        this.testMultiThreadSearch(dir, value, expected, SearchType.REGEX);
    }

    /**
     * Test FULL NAME search.
     */
    @Test
    public void whenFullNameThenResult1() throws Exception {
        var dir = this.source;
        var value = "file1.txt";
        var expected = String.join(System.lineSeparator(),
                this.file1.toString(),   // file1.txt
                this.inDir1File1.toString()     // file1.txt
        );
        this.testSearch(dir, value, expected, SearchType.FULL);
        this.testMultiThreadSearch(dir, value, expected, SearchType.FULL);
    }

    @Test
    public void whenFullNameThenResult2() throws Exception {
        var dir = this.source;
        var value = "file2.jpg";
        var expected = String.join(System.lineSeparator(),
                this.inDir1File2.toString()     // file2.jpg
        );
        this.testSearch(dir, value, expected, SearchType.FULL);
        this.testMultiThreadSearch(dir, value, expected, SearchType.FULL);
    }

    @Test
    public void whenNegativeProcessingThreadsNumberThenException() throws Exception {
        var dir = this.source;
        var value = "file2.jpg";
        var searcher = new Searcher(value, SearchType.MASK);
        var wasException = new boolean[]{false};
        try {
            searcher.search(dir, (s) -> {
            }, -1);
        } catch (Exception e) {
            wasException[0] = true;
        }
        assertTrue(wasException[0]);
    }


    @Test
    @Ignore
    public void notTestSeeMultiThreadingOnMillionFiles() throws Exception {
        var dir = Path.of("C:/111");
        var value = ".+\\.txt";
        var searcher = new Searcher(value, SearchType.REGEX);

        /*
        var nFiles = 1_000_000;
        System.out.println("Creating files... ");
        for (var i = 0; i < nFiles / 2; i++) {
            if (i % 1000 == 0) {
                System.out.println(String.format("i = %s", i));
            }
            Files.createTempFile(dir, "file", ".jpg");
            Files.createTempFile(dir, "file", ".txt");
        }
        System.out.println("Files created");
        */

        // Single-thread search
        System.gc();
        System.out.println();
        System.out.println("Starting single-thread search...");
        var start = System.currentTimeMillis();
        searcher.search(dir, (s) -> {
        });
        var time = System.currentTimeMillis() - start;
        System.out.println(String.format("Single-thread search finished. Time: %s ms", time));

        // Multi-thread search
        var nThreads = 5;
        System.gc();
        System.out.println();
        System.out.println(String.format("Starting multi-thread (%s processing threads) search...", nThreads));
        start = System.currentTimeMillis();
        searcher.search(dir, (s) -> {
        }, nThreads);
        time = System.currentTimeMillis() - start;
        System.out.println(String.format("Multi-thread search finished. Time: %s ms", time));
    }
}