package ru.job4j.search;

import org.junit.Test;

import java.util.Map;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ArgsParserTest {

    private final String[] normalArgs = new String[]{
            "-d", "dir1/dir2/dir3",     // directory
            "-n", "*.txt",              // value to search for
            "-r",                       // type of search
            "-o", "log.txt",            // output file destination
            "-pt", "5"                  // number of processing threads
    };

    @Test
    public void whenGoodArgsThenValues() throws Exception {
        var args = this.normalArgs;
        Map<String, String> result = new ArgsParser(args).parse();
        assertThat(result.get(ArgsParser.DIRECTORY), is("dir1/dir2/dir3"));
        assertThat(result.get(ArgsParser.WHAT_TO_SEARCH), is("*.txt"));
        assertThat(result.get(ArgsParser.SEARCH_TYPE), is(ArgsParser.SEARCH_TYPE_VALUE_REGEX));
        assertThat(result.get(ArgsParser.OUTPUT), is("log.txt"));
        assertThat(result.get(ArgsParser.N_PROCESS_THREADS), is("5"));
    }

    @Test
    public void whenDifferentSearchTypeThenRightValue() throws Exception {
        var args = this.normalArgs;
        var searchTypePosition = 4;
        Map<String, String> result;
        args[searchTypePosition] = "-m"; // mask
        result = new ArgsParser(args).parse();
        assertThat(result.get(ArgsParser.SEARCH_TYPE), is(ArgsParser.SEARCH_TYPE_VALUE_MASK));
        args[searchTypePosition] = "-f"; // full
        result = new ArgsParser(args).parse();
        assertThat(result.get(ArgsParser.SEARCH_TYPE), is(ArgsParser.SEARCH_TYPE_VALUE_FULL));
        args[searchTypePosition] = "-r"; // regexp
        result = new ArgsParser(args).parse();
        assertThat(result.get(ArgsParser.SEARCH_TYPE), is(ArgsParser.SEARCH_TYPE_VALUE_REGEX));
    }

    @Test
    public void whenNoDirectoryValueThenException() {
        var args = new String[]{
                "-d",   // no directory value
                "-n", "*.txt",
                "-r",
                "-o", "log.txt"
        };
        assertTrue(this.wasException(args));
    }

    @Test
    public void whenNoSearchValueValueThenException() {
        var args = new String[]{
                "-d", "dir1/dir2/dir3",
                "-n",   // no value to search
                "-r",
                "-o", "log.txt"
        };
        assertTrue(this.wasException(args));
    }

    @Test
    public void whenNoOutputValueThenException() {
        var args = new String[]{
                "-d", "dir1/dir2/dir3",
                "-n", "*.txt",
                "-r",
                "-o"    // no output value
        };
        assertTrue(this.wasException(args));
    }

    @Test
    public void whenNotEnoughArgsThenException() {
        // no search type defined
        var args = new String[]{
                "-d", "dir1/dir2/dir3",
                "-n", "*.txt",
                "-o", "log.txt"
        };
        assertTrue(this.wasException(args));
    }

    @Test
    public void whenUnknownArgumentThenException() {
        var args = new String[]{
                "-d", "dir1/dir2/dir3",
                "-n", "*.txt",
                "-u",   // unknown argument
                "-o", "log.txt"
        };
        assertTrue(this.wasException(args));
    }

    @Test
    public void whenNotEnoughKeysThenException() {
        // no key: directory
        // twice key search value
        var args = new String[]{
                "-r",
                "-n", "*.txt",
                "-n", "*.jpg",
                "-m",
                "-o", "log.txt"
        };
        assertTrue(this.wasException(args));
    }

    @Test
    public void whenNoNeededValueAtTheEndThenException() {
        var args = new String[]{
                "-d", "dir1/dir2/dir3",
                "-n", "*.txt",
                "-n", "*.jpg",
                "-m",
                "-o" // no value after key
        };
        assertTrue(this.wasException(args));
    }

    @Test
    public void whenValueOfProcessingThreadsNumberIsNotNumberThenException() {
        var args = new String[]{
                "-d", "dir1/dir2/dir3",
                "-n", "*.txt",
                "-r",
                "-o", "log.txt",
                "-pt", "h"      // not a number
        };
        assertTrue(this.wasException(args));
    }

    private boolean wasException(String[] args) {
        var wasException = new boolean[]{false};
        try {
            new ArgsParser(args).parse();
        } catch (Exception e) {
            wasException[0] = true;
        }
        return wasException[0];
    }
}