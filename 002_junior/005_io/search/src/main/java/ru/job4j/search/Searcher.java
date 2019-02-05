package ru.job4j.search;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.util.function.ConsumerEx;
import ru.job4j.util.function.TriConsumerEx;
import ru.job4j.util.methods.CommonUtils;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * File searcher.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class Searcher {
    /**
     * Logger.
     */
    @SuppressWarnings("unused")
    private static final Logger LOG = LogManager.getLogger(Searcher.class);
    /**
     * Value to search for.
     */
    private final String searchValue;
    /**
     * Type of search.
     */
    private final SearchType searchType;

    /**
     * Blocking queue of files found, waiting for matching.
     */
    private final BlockingQueue<Path> matchQueue = new LinkedBlockingQueue<>(10);
    /**
     * Flag showing that thread parsing fileSystem is still working
     * and threads doing file name match cannot be stopped.
     */
    private volatile boolean walkingTree = true;

    /**
     * Dispatches checks to perform on search value (if needed).
     */
    private Map<SearchType, ConsumerEx<String>> checkDispatch = new HashMap<>();

    /**
     * Dispatches methods to use in case of certain search type.
     */
    private Map<SearchType, TriConsumerEx<Path, Consumer<String>, Integer>> searchDispatch = new HashMap<>();

    /**
     * Constructs new instance.
     *
     * @param searchValue Value to search for.
     * @param searchType  Type of search.
     * @throws Exception If checking search value raises an exception.
     */
    public Searcher(String searchValue, SearchType searchType) throws Exception {
        this.init();
        this.checkSearchTypeAndValue(searchType, searchValue);
        this.searchType = searchType;
        this.searchValue = searchValue;
    }

    /**
     * Starts search by this searcher.
     *
     * @param dir    Directory to search in.
     * @param output Result consumer.
     * @throws Exception In case of problems during search.
     */
    public void search(Path dir, Consumer<String> output) throws Exception {
        this.searchDispatch.get(this.searchType).accept(dir, output, 0);
    }

    /**
     * Starts search by this searcher.
     *
     * @param dir    Directory to search in.
     * @param output Result consumer.
     * @throws Exception In case of problems during search.
     */
    public void search(Path dir, Consumer<String> output, int nProcessThreads) throws Exception {
        if (nProcessThreads < 0) {
            throw new Exception("Number of processing threads must be >= 0");
        }
        this.searchDispatch.get(this.searchType).accept(dir, output, nProcessThreads);
    }

    /**
     * Initiates class object.
     */
    private void init() {
        this.initCheckDispatch();
        this.initSearchDispatch();
    }

    /**
     * Fills checkDispatch.
     */
    private void initCheckDispatch() {
        this.checkDispatch.put(SearchType.MASK, this::checkIsMask);
    }

    /**
     * Fills searchDispatch.
     */
    private void initSearchDispatch() {
        this.searchDispatch.put(SearchType.MASK, this::searchByMask);
        this.searchDispatch.put(SearchType.REGEX, this::searchByRegex);
        this.searchDispatch.put(SearchType.FULL, this::searchByFull);
    }

    /**
     * Checks if given search value is applicable to given
     * search type (if needed).
     *
     * @param type  Search type.
     * @param value Search value.
     * @throws Exception In case of bad check (exception thrown).
     */
    private void checkSearchTypeAndValue(SearchType type, String value) throws Exception {
        this.checkDispatch
                .getOrDefault(type, s -> {
                }).accept(value);
    }

    /**
     * Checks if search value defines file mask.
     * Otherwise - throws exception.
     *
     * @param searchValue Value (mask) to search for.
     * @throws Exception If value is not mask.
     */
    private void checkIsMask(String searchValue) throws Exception {
        var isMask = searchValue.matches("^[a-zA-Z0-9*?.]+$");
        if (!isMask) {
            throw new Exception(String.format("Search value is not mask: %s", searchValue));
        }
    }

    /**
     * Performs searching by mask.
     *
     * @param dir    Directory to search in.
     * @param output Result consumer.
     */
    private void searchByMask(Path dir, Consumer<String> output, int nProcessThreads) throws InterruptedException, IOException {
        var matcher = FileSystems.getDefault().getPathMatcher(String.format("glob:%s", this.searchValue));
        this.walkFileTreeMatchAndWrite(dir, output, matcher::matches, nProcessThreads);
    }

    /**
     * Performs searching by regular expression.
     *
     * @param dir    Directory to search in.
     * @param output Result consumer.
     */
    private void searchByRegex(Path dir, Consumer<String> output, int nProcessThreads) throws InterruptedException, IOException {
        var matcher = FileSystems.getDefault().getPathMatcher(String.format("regex:%s", this.searchValue));
        this.walkFileTreeMatchAndWrite(dir, output, matcher::matches, nProcessThreads);
    }

    /**
     * Performs searching by full name.
     *
     * @param dir    Directory to search in.
     * @param output Result consumer.
     */
    private void searchByFull(Path dir, Consumer<String> output, int nProcessThreads) throws InterruptedException, IOException {
        var searchPath = Path.of(this.searchValue);
        Predicate<Path> predicate = (s) -> s.equals(searchPath);
        this.walkFileTreeMatchAndWrite(dir, output, predicate, nProcessThreads);
    }

    /**
     * Walks file tree in given directory with subdirectories and
     * their files.
     * <p>
     * When file name fits given predicate, gives file path into
     * given result consumer.
     *
     * @param dir       Directory to search in.
     * @param output    Result consumer.
     * @param predicate Predicate to test file name.
     * @throws InterruptedException If thread was interrupted while waiting.
     */
    private void walkFileTreeMatchAndWrite(Path dir, Consumer<String> output, Predicate<Path> predicate, int nProcessThreads) throws InterruptedException, IOException {
        if (nProcessThreads == 0) {
            this.searchSingleThread(dir, output, predicate);
        } else {
            this.searchMultiThreads(dir, output, predicate, nProcessThreads);
        }
    }

    /**
     * Performs walking files and matching in a single main thread.
     *
     * @param dir       Directory to search in.
     * @param output    Result consumer.
     * @param predicate Predicate to test file name.
     * @throws IOException In case of problems while walking file tree.
     */
    private void searchSingleThread(Path dir, Consumer<String> output, Predicate<Path> predicate) throws IOException {
        Files.walkFileTree(dir, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                var name = file.getFileName();
                if (predicate.test(name)) {
                    output.accept(file.toString());
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }

    /**
     * Performs walking files in main thread and matching in
     * a number of other threads.
     *
     * @param dir             Directory to search in.
     * @param output          Result consumer.
     * @param predicate       Predicate to test file name.
     * @param nProcessThreads Number of processing threads (threads doing file matching).
     * @throws IOException          In case of problems while walking file tree.
     * @throws InterruptedException If a processing thread was interrupted while waiting for task.
     */
    private void searchMultiThreads(Path dir, Consumer<String> output, Predicate<Path> predicate, int nProcessThreads) throws IOException, InterruptedException {
        var executor = Executors.newFixedThreadPool(nProcessThreads);
        var matcher = new RunnableMatcher(predicate, output);
        for (int i = 0; i < nProcessThreads; i++) {
            executor.execute(matcher);
        }
        this.walkFilesAddToQueue(dir);
        executor.shutdown();
        while (!(executor.isTerminated())) {
            executor.awaitTermination(10, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * Walks file tree and adds file to a queue for matching.
     *
     * @param dir Directory to search in.
     * @throws IOException In case of problems while walking file tree.
     */
    private void walkFilesAddToQueue(Path dir) throws IOException {
        Files.walkFileTree(dir, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                try {
                    Searcher.this.matchQueue.put(file);
                } catch (InterruptedException e) {
                    LOG.error(e.getMessage(), e);
                }
                return FileVisitResult.CONTINUE;
            }
        });
        this.walkingTree = false;
    }

    /**
     * Runnable for a file name matcher thread.
     */
    private class RunnableMatcher implements Runnable {
        /**
         * Predicate to test file name.
         */
        private final Predicate<Path> predicate;
        /**
         * Result consumer.
         */
        private final Consumer<String> output;

        /**
         * Creates new Runnable instance.
         *
         * @param predicate Predicate to test file name.
         * @param output    Result consumer.
         */
        private RunnableMatcher(Predicate<Path> predicate, Consumer<String> output) {
            this.predicate = predicate;
            this.output = output;
        }

        /**
         * Runs while there are still files to match.
         * Takes file from queue, tests it and writes to
         * consumer if file matches.
         */
        @Override
        public void run() {
            try {
                while (Searcher.this.walkingTree
                        || !Searcher.this.matchQueue.isEmpty()
                ) {
                    this.takeAndProcess();
                }
            } catch (InterruptedException e) {
                LOG.error(CommonUtils.describeThrowable(e));
            }
        }

        /**
         * Takes file from queue and processes matching and writing result.
         *
         * @throws InterruptedException
         */
        private void takeAndProcess() throws InterruptedException {
            var file = Searcher.this.matchQueue.poll(1000, TimeUnit.MILLISECONDS);
            if (file != null) {
                var name = file.getFileName();
                if (predicate.test(name)) {
                    synchronized (this.output) {
                        this.output.accept(file.toString());
                    }
                }
            }
        }
    }

}