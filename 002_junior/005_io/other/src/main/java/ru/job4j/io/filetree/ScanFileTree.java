package ru.job4j.io.filetree;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.NotDirectoryException;
import java.util.*;

/**
 * Scans file tree and returns files found.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class ScanFileTree {
    /**
     * Constant of empty array - to use when no elements needed to add.
     */
    private static final File[] EMPTY_FILE_ARRAY = new File[0];

    /**
     * Walks file tree and searches for files with needed
     * extensions. Returns list with found files.
     *
     * @param root Directory to start search from.
     * @param exts List of needed extensions.
     * @return List with matching files.
     * @throws FileNotFoundException If root file was not found.
     * @throws NotDirectoryException If given root file is not directory.
     */
    public List<File> search(String root, Set<String> exts) throws FileNotFoundException, NotDirectoryException {
        var rootFile = new File(root).getAbsoluteFile();
        this.checkRootIsValid(rootFile);
        var result = new ArrayList<File>();
        Queue<File> travel = new LinkedList<>();
        travel.offer(rootFile);
        while (!(travel.isEmpty())) {
            this.processElement(travel, exts, result);
        }
        return result;
    }

    /**
     * Checks if given root element is valid to use.
     *
     * @param rootFile Given root file path.
     * @throws FileNotFoundException If root was not found on disk.
     * @throws NotDirectoryException If given root is not directory.
     */
    private void checkRootIsValid(File rootFile) throws FileNotFoundException, NotDirectoryException {
        if (!(rootFile.exists())) {
            throw new FileNotFoundException(String.format(
                    "File not found: %s", rootFile
            ));
        }
        if (!(rootFile.isDirectory())) {
            throw new NotDirectoryException(String.format(
                    "Given file is not directory: %s", rootFile
            ));
        }
    }

    /**
     * Processes one given element - file or directory.
     *
     * @param travel Queue of elements to travel through.
     * @param exts   Set of extensions needed.
     * @param result List of files with matching extensions found.
     */
    private void processElement(Queue<File> travel, Set<String> exts, List<File> result) {
        var element = travel.poll();
        var inner = element.listFiles() != null ? element.listFiles() : EMPTY_FILE_ARRAY;
        Collections.addAll(travel, inner);
        if (element.isFile()
                && this.hasExtension(element.getName(), exts)) {
            result.add(element);
        }
    }

    /**
     * Checks if given file name has one of needed extensions.
     *
     * @param fileName   File name.
     * @param extensions Needed extensions.
     * @return <tt>true</tt> if file name has needed extension, <tt>false</tt> otherwise.
     */
    private boolean hasExtension(String fileName, Set<String> extensions) {
        var divide = fileName.lastIndexOf(".");
        var ext = divide > -1 && (divide + 1) < fileName.length()
                ? fileName.substring(divide + 1)
                : "";
        return extensions.contains(ext);
    }
}
