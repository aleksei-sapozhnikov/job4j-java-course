package ru.job4j.search;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class SearchTextTest {

    @Rule
    private TemporaryFolder root = new TemporaryFolder();


    @Before
    public void createFoldersAndFiles() throws IOException {


    }

    private void createFoldersPaths() throws IOException {

    }

    private void createFiles() throws IOException {
        // root folders
        Path k1 = root.newFolder("K1").toPath();


        List<Path> paths = new ArrayList<>(Arrays.asList(
                // root files
                root.newFile("File1.aaa").toPath(),
                root.newFile("File2.bbb").toPath(),
                root.newFile("File3.xxx").toPath(),
                // root folders
                root.newFolder("K1%sSK1").toPath()
        ));
    }

    @Test
    public void testUsingTempFolder() throws IOException {
        // ...
    }


    @Test
    public void performSearch() {
        Path root = Paths.get("C:\\Users\\User-01\\Desktop\\111");
        String text = "32";
        List<String> extensions = new LinkedList<>(Arrays.asList("txt", "ttt"));
        SearchText search = new SearchText(root, text, extensions);
        search.performSearch();
        List<String> result = search.getSearchResult();
        for (String a : result) {
            System.out.println(a);
        }
    }

    @Test
    public void getSearchResult() {
    }
}