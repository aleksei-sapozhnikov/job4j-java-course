package ru.job4j.sort.bigfile;

import org.junit.Test;
import ru.job4j.util.methods.CommonUtils;

import java.io.File;
import java.io.IOException;
import java.util.StringJoiner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class SortTest {

    private static final String RESOURCES = "ru/job4j/sort/bigfile/";

    private final File source;
    private final File dest;

    public SortTest() {
        ClassLoader loader = this.getClass().getClassLoader();
        this.source = new File(loader.getResource(RESOURCES + "source.txt").getPath());
        this.dest = new File(loader.getResource(RESOURCES + "result.txt").getPath());
    }

    /**
     * Test sort() on files.
     */
    @Test
    public void whenDifferentAvailableMemoryThenSortingStillWorks() throws IOException {
        String expected = new StringJoiner(System.lineSeparator())
                .add("ф")
                .add("фыва")
                .add("Привет")
                .add("Вася красивый")
                .add("Мама мыла раму")
                .add("Три веселых коня")
                .add("Тридцать три коровы")
                .add("Восемнадцать мне уже")
                .add("Малышей не обижать учат в школе")
                .add("Выпей чаю с французскими булками")
                .add("")
                .toString();
        //
        Sort sortMax = new Sort();
        sortMax.sort(this.source, this.dest);
        String resultMax = CommonUtils.loadFileAsString(this, "UTF8", RESOURCES + "result.txt");
        assertThat(resultMax, is(expected));
        //
        Sort sortMiddle = new Sort(100);
        sortMiddle.sort(this.source, this.dest);
        String resultMiddle = CommonUtils.loadFileAsString(this, "UTF8", RESOURCES + "result.txt");
        assertThat(resultMiddle, is(expected));
        //
        Sort sortLow = new Sort(0);
        sortLow.sort(this.source, this.dest);
        String resultLow = CommonUtils.loadFileAsString(this, "UTF8", RESOURCES + "result.txt");
        assertThat(resultLow, is(expected));
    }
}