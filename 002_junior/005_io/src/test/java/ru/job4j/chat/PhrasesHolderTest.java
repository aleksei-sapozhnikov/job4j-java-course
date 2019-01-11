package ru.job4j.chat;

import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isIn;
import static org.junit.Assert.assertThat;

public class PhrasesHolderTest {
    private final String emptyFile;
    private final String onePhrase;
    private final String threePhrases;
    private final String logger;

    public PhrasesHolderTest() {
        ClassLoader loader = this.getClass().getClassLoader();
        String rootPath = loader.getResource("ru/job4j/chat").getPath();
        this.emptyFile = String.join("/", rootPath, "empty_file.txt");
        this.onePhrase = String.join("/", rootPath, "one_phrase.txt");
        this.threePhrases = String.join("/", rootPath, "three_phrases.txt");
        this.logger = String.join("/", rootPath, "logger.txt");
    }

    @Test
    public void whenEmptyFileThenException() throws IOException {
        boolean wasException = false;
        try (PhrasesHolder holder = new PhrasesHolder(this.emptyFile)) {
            System.out.println("Statement for checkstyle validator");
        } catch (InfoNotFoundException e) {
            wasException = true;
        }
        assertThat(wasException, is(true));
    }

    @Test
    public void whenOnePhraseThenItRepeatsEachTime() throws IOException {
        int repeat = 1000;
        try (PhrasesHolder holder = new PhrasesHolder(this.onePhrase)) {
            String phrase = holder.getRandomPhrase();
            for (int i = 0; i < repeat; i++) {
                assertThat(holder.getRandomPhrase(), is(phrase));
            }
        }
    }

    @Test
    public void whenThreePhrasesThenOneOfThemEachTime() throws IOException {
        int num = 3;
        String[] phrases = new String[num];
        try (Scanner sc = new Scanner(new FileReader(this.threePhrases))) {
            for (int i = 0; i < num; i++) {
                phrases[i] = sc.nextLine();
            }
        }
        int repeat = 1000;
        try (PhrasesHolder holder = new PhrasesHolder(this.threePhrases)) {
            for (int i = 0; i < repeat; i++) {
                assertThat(holder.getRandomPhrase(), isIn(phrases));
            }
        }
    }


    @Test
    public void getRandomPhrase() {
    }
}