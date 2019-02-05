package ru.job4j.search;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.WRITE;

/**
 * Launcher class for search.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class Launcher {
    /**
     * Logger.
     */
    @SuppressWarnings("unused")
    private static final Logger LOG = LogManager.getLogger(Launcher.class);

    /**
     * Main method. Launches the application.
     *
     * @param args Command-line args.
     */
    public static void main(String[] args) {
        try {
            new Launcher().parseAndStart(args);
        } catch (Exception e) {
            System.out.println(String.format(
                    "Exception:%n  class: %s%n  message: %s%nWrong arguments? Print help to get syntax help",
                    e.getClass().getName(), e.getMessage()
            ));
        }
    }

    /**
     * Parses given arguments and starts application.
     *
     * @param args Command-line args.
     * @throws Exception If problems in parsing args or other problems.
     */
    private void parseAndStart(String[] args) throws Exception {
        if (args.length == 0 || args[0].equals("help")) {
            System.out.println(this.helpMessage());
        } else {
            var parsed = new ArgsParser(args).parse();
            var searchValue = parsed.get(ArgsParser.WHAT_TO_SEARCH);
            var searchType = SearchType.valueOf(parsed.get(ArgsParser.SEARCH_TYPE));
            var dir = Path.of(parsed.get(ArgsParser.DIRECTORY));
            var output = Path.of(parsed.get(ArgsParser.OUTPUT));
            var nProcessThreads = Integer.parseInt(parsed.getOrDefault(ArgsParser.N_PROCESS_THREADS, "0"));
            try (var writer = Files.newBufferedWriter(output, CREATE, WRITE)) {
                var searcher = new Searcher(searchValue, searchType);
                searcher.search(dir, (p) -> this.writeLine(writer, p), nProcessThreads);
            }
        }
    }

    /**
     * Writes one line and handles exception.
     *
     * @param writer Writer object.
     * @param line   Line to write.
     */
    private void writeLine(BufferedWriter writer, String line) {
        try {
            writer.write(String.format("%s%n", line));
        } catch (IOException e) {
            LOG.error(String.format(
                    "Error writing result file: %s", line
            ));
        }
    }

    /**
     * Returns application help message.
     *
     * @return Help message.
     */
    private String helpMessage() {
        return String.join(System.lineSeparator(),
                "Usage:",
                "java -jar find.jar -d <dir> -n <searchValue> -m(-f,-r) -o <outputFile> -pt <numberOfProcessingThreads>",
                "where keys are:",
                "    -d <dir> : directory where to perform search in",
                "    -n <searchValue> : search value (name, mask, regex)",
                "    -m, -f, -r : search type - search by mask, by full name or by regex",
                "    -o <outputFile> : output file for result",
                "    -pt <numberOfProcessingThreads> (optional) : number of threads processing matching file name",
                "Example:",
                "java -jar find.jar -d home/john -n *.txt -m -o home/result.txt -pt 5"
        );
    }
}
