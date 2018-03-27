package ru.job4j.count;

public class CountWordsAndSpaces {

    private String input;

    CountWordsAndSpaces(String input) {
        this.input = input;
    }

    void start() {
        try {
            Thread space = new Thread(() -> System.out.println(String.format("=== Words : %s", this.countWords())));
            Thread word = new Thread(() -> System.out.println(String.format("=== Spaces : %s", this.countSpaces())));
            space.start();
            word.start();
            space.join();
            word.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    int countSpaces() {
        char[] chars = this.input.toCharArray();
        int count = 0;
        for (char c : chars) {
            if (c == ' ') {
                count++;
                System.out.println(String.format("spaces count : %s", count));
            }
        }
        return count;
    }

    int countWords() {
        char[] chars = this.input.toCharArray();
        int count = 0;
        for (int i = 1; i < chars.length; i++) {
            if (chars[i] == ' ' && chars[i - 1] != ' ') {
                count++;
                System.out.println(String.format("words count : %s", count));
            }
        }
        // last word
        if (chars[chars.length - 1] != ' ') {
            count++;
        }
        return count;
    }
}
