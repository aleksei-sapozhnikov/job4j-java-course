package ru.job4j.search;

import net.jcip.annotations.ThreadSafe;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

@ThreadSafe
class SearchText {
    /**
     * Way to folder where to start search.
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
    private final Queue<Path> files = new ConcurrentLinkedDeque<>();
    /**
     * List of files (from queue) where needed text was found.
     */
    private final Queue<Path> found = new ConcurrentLinkedDeque<>();
    /**
     * Declares that thread searching for files with needed extensions finished doing work
     */
    private boolean extensionSearcherWorking;

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
            Thread searchFiles = this.getSearchFilesThread();
            Thread searchContent = this.getSearchContentsThread();
            this.extensionSearcherWorking = true;
            searchFiles.start();
            searchContent.start();
            searchFiles.join();
            searchContent.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns thread searching for files with needed extensions.
     *
     * @return thread searching for files with needed extensions.
     */
    private Thread getSearchFilesThread() {
        System.out.format("=== EXTS: Started ===%n");
        return new Thread(() -> {
            try {
                Files.walkFileTree(this.root, this.extensionSearcher);
                synchronized (this.files) {
                    this.extensionSearcherWorking = false;
                    System.out.format("Searching files FINISHED%n");
                    this.files.notifyAll();
                }
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
                    while (extensionSearcherWorking && this.files.isEmpty()) {
                        synchronized (this.files) {
                            System.out.println("  CONTENT: WAIT");
                            this.files.wait();
                        }
                    }
                    System.out.format("  CONTENT: Looking for a file in queue. Queue size: %s%n", this.files.size());
                    Path file = this.files.poll();
                    if (file != null) {
                        String content = new String(Files.readAllBytes(file));
                        System.out.format("  CONTENT >>: File content: \"%s\"%n", content);
                        if (content.contains(this.text)) {
                            System.out.format("  CONTENT >>: File contains \"%s\", adding to result%n", this.text);
                            this.found.add(file);
                        } else {
                            System.out.format("  >> CONTENT: Not found \"%s\" in file, skipping%n", this.text);
                        }
                    }
                }
                System.out.println("  === CONTENT: Finished ===");
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
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
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            boolean extensionOk = false;
            for (String ext : extensions) {
                if (file.getFileName().toString().endsWith(String.format(".%s", ext))) {
                    System.out.format("EXTS: >> Found extension \"%s\", adding to queue%n", ext);
                    extensionOk = true;
                    break;
                }
            }
            if (extensionOk) {
                synchronized (files) {
                    files.offer(file);
                    files.notify();
                }
            } else {
                System.out.format("EXTS: >> Not found needed extension, skipping%n");
            }
            return FileVisitResult.CONTINUE;
        }
    }


}
