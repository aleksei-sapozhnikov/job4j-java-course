package ru.job4j.search;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@ThreadSafe
class SearchText {
    /**
     * Way to folder where to start search.
     */
    private final String root;
    /**
     * Text to look for.
     */
    private final String text;
    /**
     * List of file extensions, in which perform search.
     */
    private final List<String> extensions;
    /**
     * Queue of files with needed extension found, waiting for search.
     */
    @GuardedBy("this")
    private final Queue<String> files = new LinkedList<>();
    /**
     * List of files (from queue) where needed text was found.
     */
    @GuardedBy("this")
    private final List<String> found = new ArrayList<>();

    /**
     * @param root       way to folder where to start search.
     * @param text       text to look for.
     * @param extensions list of file extensions, in which perform search.
     */
    SearchText(String root, String text, List<String> extensions) {
        this.root = root;
        this.text = text;
        this.extensions = extensions;
    }

    /**
     * Initiates the search threads:
     * <p>
     * <tt>files</tt> finds files with given extensions
     * and adds them to the "files" queue.
     * <p>
     * <tt>read</tt> reads files from queue and, if found text,
     * stores them into the "found" list.
     */
    void init() {
        Thread files = new Thread(() -> {
            //
        });
        Thread read = new Thread(() -> {
            //
        });
    }


    /**
     * Searches given text in files and returns files where the text was found.
     *
     * @return list of file paths where the given text was found.
     */
    List<String> parallelSearch() {
        List<String> result = new LinkedList<>();

        return result;
    }
}
