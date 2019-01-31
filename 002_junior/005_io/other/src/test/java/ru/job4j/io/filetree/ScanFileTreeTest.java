package ru.job4j.io.filetree;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.NotDirectoryException;
import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class ScanFileTreeTest {

    private File root;

    private File source;
    private File file1;
    private File file2;

    private File inDir1;
    private File inDir1File1;
    private File inDir1File2;

    private File inDir2;
    private File inDir2File1;
    private File inDir2File2;
    private File inDir2File3;
    private File inDir2File4;

    public ScanFileTreeTest() throws IOException {
        this.createStructure();
    }

    private void createStructure() throws IOException {
        this.root = new File(String.format("%s/%s-%s",
                System.getProperty("java.io.tmpdir"),
                "fileScan",
                System.currentTimeMillis()));
        this.mkdirs(this.root);

        this.source = new File(this.root, "source");
        this.mkdirs(this.source);
        this.file1 = new File(this.source, "file1.txt");
        this.file2 = new File(this.source, "file2.jpg");
        this.createNewFiles(this.file1, this.file2);

        this.inDir1 = new File(this.source, "inDir1");
        this.mkdirs(this.inDir1);
        this.inDir1File1 = new File(this.inDir1, "file1.jpg");
        this.inDir1File2 = new File(this.inDir1, "file2.txt");
        this.createNewFiles(this.inDir1File1, this.inDir1File2);

        this.inDir2 = new File(this.source, "inDir2");
        this.mkdirs(this.inDir2);
        this.inDir2File1 = new File(this.inDir2, "file44.png");
        this.inDir2File2 = new File(this.inDir2, ".txt");
        this.inDir2File3 = new File(this.inDir2, "noExtension");
        this.createNewFiles(
                this.inDir2File1, this.inDir2File2, this.inDir2File3);
    }

    private void mkdirs(File... dirs) {
        for (var dir : dirs) {
            dir.mkdirs();
        }
    }

    private void createNewFiles(File... files) throws IOException {
        for (var file : files) {
            file.createNewFile();
        }
    }

    @Test
    public void whenExtensionThenFound() throws FileNotFoundException, NotDirectoryException {
        var root = this.root;
        var exts = Set.of("txt");
        var expected = Arrays.asList(
                this.file1, this.inDir1File2, this.inDir2File2);
        this.testAllFilesFound(root, exts, expected);
    }

    @Test
    public void whenSetOfExtensionThenFound() throws FileNotFoundException, NotDirectoryException {
        var root = this.root;
        var exts = Set.of("png", "jpg");
        var expected = Arrays.asList(
                this.file2, this.inDir1File1, this.inDir2File1);
        this.testAllFilesFound(root, exts, expected);
    }

    @Test
    public void whenNoExtensionThenNothingFound() throws FileNotFoundException, NotDirectoryException {
        var root = this.root;
        var exts = Collections.<String>emptySet();
        var expected = Collections.<File>emptyList();
        this.testAllFilesFound(root, exts, expected);
    }

    @Test
    public void whenRootNotExistingThenFileNotFoundException() throws NotDirectoryException {
        var root = new File(String.valueOf(System.currentTimeMillis())).getAbsoluteFile();
        assertFalse(root.exists());
        var searcher = new ScanFileTree();
        var wasException = new boolean[]{false};
        try {
            searcher.search(root.getAbsolutePath(), Set.of("txt"));
        } catch (FileNotFoundException e) {
            wasException[0] = true;
        }
        assertTrue(wasException[0]);
    }

    @Test
    public void whenRootNotDirectoryThenNotDirectoryException() throws FileNotFoundException {
        var root = this.file1;
        assertTrue(root.exists());
        var searcher = new ScanFileTree();
        var wasException = new boolean[]{false};
        try {
            searcher.search(root.getAbsolutePath(), Set.of("txt"));
        } catch (NotDirectoryException e) {
            wasException[0] = true;
        }
        assertTrue(wasException[0]);
    }

    /**
     * Common text for finding needed files.
     *
     * @param root     Root search directory.
     * @param exts     Extensions to find.
     * @param expected Expected result.
     * @throws FileNotFoundException If root directory was not found.
     */
    private void testAllFilesFound(File root, Set<String> exts, List<File> expected) throws FileNotFoundException, NotDirectoryException {
        var searcher = new ScanFileTree();
        var result = searcher.search(root.getAbsolutePath(), exts);
        result.sort(Comparator.naturalOrder());
        expected.sort(Comparator.naturalOrder());
        assertThat(result, is(expected));
    }
}