package ru.job4j.search;

import net.jcip.annotations.ThreadSafe;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Searches for given text in all sub-files and sub-directories of given path with given extensions.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 13.04.2018
 */
@ThreadSafe
class SearchText {
    /**
     * Way to folder where to start search from.
     */
    private final Path root;
    /**
     * Text to look for.
     */
    private final String text;
    /**
     * List of file extensions, in which perform search.
     */
    private final List<String> extensions;
    /**
     * File visitor object, checking file extension during walkFileTree.
     * Adds files with right extensions to the queue waiting for analysis (content search).
     */
    private final FileVisitor<Path> extensionSearcher = new ExtensionSearcher();
    /**
     * Queue of files with needed extension found, waiting for search.
     */
    private final BlockingQueue<Path> files = new LinkedBlockingQueue<>();
    /**
     * List of files (from queue) where needed text was found.
     */
    private final BlockingQueue<Path> found = new LinkedBlockingQueue<>();
    /**
     * Declares that thread searching for files with needed extensions finished doing work
     */
    private volatile boolean extensionSearcherWorking;

    /**
     * Constructs a new object searching for text in files.
     *
     * @param root       way to folder where to start search.
     * @param text       text to look for.
     * @param extensions list of file extensions, in which perform search.
     */
    SearchText(Path root, String text, List<String> extensions) {
        this.root = root;
        this.text = text;
        this.extensions = extensions;
    }

    /**
     * Initiates and runs the search threads:
     * <p>
     * <tt>files</tt> finds files with given extensions
     * and adds them to the "files" queue.
     * <p>
     * <tt>read</tt> reads files from queue and, if found text,
     * stores them into the "found" list.
     */
    void performSearch() {
        try {
            Thread extensions = this.getSearchExtensionsThread();
            Thread contents = this.getSearchContentsThread();
            this.extensionSearcherWorking = true;
            extensions.start();
            contents.start();
            extensions.join();
            contents.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns thread searching for files with needed extensions.
     *
     * @return thread searching for files with needed extensions.
     */
    private Thread getSearchExtensionsThread() {
        return new Thread(() -> {
            try {
                System.out.format("=== EXTS: Started ===%n");
                Files.walkFileTree(this.root, this.extensionSearcher);
                this.extensionSearcherWorking = false;
                System.out.format("Searching files FINISHED%n");
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.format("=== EXTS: Finished ===%n");
        });
    }

    /**
     * Returns thread searching for needed text in a file from queue.
     *
     * @return thread searching for needed text in a file from queue.
     */
    private Thread getSearchContentsThread() {
        return new Thread(() -> {
            try {
                System.out.println("  === CONTENT: Started ===");
                while (extensionSearcherWorking || !this.files.isEmpty()) {
                    this.analyzeNextFile();
                }
                System.out.println("  === CONTENT: Finished ===");
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Makes search contents thread to pick next file from waiting queue,
     * check if it contains needed text and, if contains, add it to result list.
     *
     * @throws IOException if thread was interrupted while waiting.
     */
    private void analyzeNextFile() throws IOException, InterruptedException {
        System.out.format("  CONTENT: Looking for a file in queue. Queue size: %s%n", this.files.size());
        Path file = this.files.poll(1000, TimeUnit.MILLISECONDS);
        if (file != null) {
            System.out.format("  CONTENT: Analyzing file %s%n", file.getFileName());
            String content = new String(Files.readAllBytes(file));
            System.out.format("  CONTENT >>: File content: \"%s\"%n", content);
            if (content.contains(this.text)) {
                System.out.format("  CONTENT >>: File contains \"%s\", adding to result%n", this.text);
                this.found.add(file);
            } else {
                System.out.format("  CONTENT >>: Not found \"%s\" in file, skipping%n", this.text);
            }
        }
    }

    /**
     * Searches given text in files and returns files where the text was found.
     *
     * @return list of strings representing file paths where the given text was found.
     */
    List<String> getSearchResult() {
        List<String> result = new ArrayList<>();
        for (Path path : this.found) {
            result.add(path.toString());
        }
        return result;
    }

    /**
     * Class of a FileVisitor. Visits file, checks its extension and
     * adds file to queue if extension is as needed.
     */
    private class ExtensionSearcher extends SimpleFileVisitor<Path> {
        /**
         * Invoked for a file in a directory.
         *
         * @param file  file visited.
         * @param attrs file attributes.
         */
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
            System.out.format("EXTS: Visiting file: %s%n", file.toAbsolutePath().toString());
            if (this.hasNeededExtension(file, extensions)) {
                files.offer(file);
            }
            return FileVisitResult.CONTINUE;
        }

        /**
         * Check if file has one of needed extensions.
         *
         * @param file file to check.
         * @return <tt>true</tt> if extension is one of needed, <tt>false</tt> if not.
         */
        private boolean hasNeededExtension(Path file, List<String> extensions) {
            boolean result = false;
            for (String ext : extensions) {
                if (file.getFileName().toString().endsWith(String.format(".%s", ext))) {
                    System.out.format("EXTS: >> Found extension \"%s\", adding to queue%n", ext);
                    result = true;
                    break;
                }
            }
            return result;
        }
    }
}
