package ru.job4j.count;

public class CountWordsAndSpaces {

    private String input;

    CountWordsAndSpaces(String input) {
        this.input = input;
    }

    void start() {
        Thread space = new Thread(() -> System.out.printf("=== Words : %s%n", this.countWords()));
        Thread word = new Thread(() -> System.out.printf("=== Spaces : %s%n", this.countSpaces()));
            word.start();
        space.start();
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
