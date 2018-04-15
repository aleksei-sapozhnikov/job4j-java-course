package ru.job4j.search;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class SearchTextTest {

    @Rule
    public TemporaryFolder temp = new TemporaryFolder();

    private Path root;

    private void createFoldersAndFiles() throws IOException {
        // root path
        this.root = this.temp.getRoot().toPath();
        // file paths
        String s = File.separator;
        Path[] files = new Path[]{
                Paths.get(String.format("%s%s_K1_%s_SK1_%s_SSK1_%s_File1.txt", root.toAbsolutePath(), s, s, s, s)),
                Paths.get(String.format("%s%s_K1_%s_SK1_%s_SSK2_%s_File2.ttt", root.toAbsolutePath(), s, s, s, s)),
                Paths.get(String.format("%s%s_K1_%s_SK1_%s_SSK2_%s_File3.xyz", root.toAbsolutePath(), s, s, s, s))
        };
        // create needed folders
        for (Path file : files) {
            Path aa = file.getParent();
            Files.createDirectories(aa);
        }
        // contents to write into files
        String[] contents = new String[]{
                "Extension is right, text 32 contains what needed",
                "Extension is right, but no needed text",
                "Contains wh32at needed but extension not as needed"
        };
        // write contents to files
        for (int i = 0; i < Math.min(files.length, contents.length); i++) {
            try (FileWriter a = new FileWriter(files[i].toFile())) {
                a.write(contents[i]);
            }
        }
    }


    @Test
    public void searchingFilesDirectoryHierarchy() throws IOException {
        this.createFoldersAndFiles();
        String text = "32";
        List<String> extensions = new LinkedList<>(Arrays.asList("txt", "ttt"));
        // work
        SearchText search = new SearchText(root, text, extensions);
        search.performSearch();
        List<String> absolute = search.getSearchResult();
        // result and assert
        String[] result = new String[absolute.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = Paths.get(absolute.get(i)).getFileName().toString();
        }
        String[] expected = {
                "_File1.txt"
        };
        assertThat(result, is(expected));
    }

    @Test
    public void searchingFilesInEmptyFolderIsNotInfinite() {
        this.root = this.temp.getRoot().toPath();
        String text = "32";
        List<String> extensions = new LinkedList<>(Arrays.asList("txt", "ttt"));
        SearchText search = new SearchText(root, text, extensions);
        search.performSearch();
        String[] result = search.getSearchResult().toArray(new String[0]);
        String[] expected = {};
        assertThat(result, is(expected));
    }
}