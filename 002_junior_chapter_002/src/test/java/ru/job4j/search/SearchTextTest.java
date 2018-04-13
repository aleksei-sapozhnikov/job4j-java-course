package ru.job4j.search;

import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class SearchTextTest {

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