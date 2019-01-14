package ru.job4j.io.chat;

import org.junit.Test;
import ru.job4j.io.chat.input.Input;
import ru.job4j.io.chat.input.StubInput;

import java.io.*;
import java.util.Scanner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ChatTest {

    private final String onePhrase;
    private final String logger;

    public ChatTest() {
        ClassLoader loader = this.getClass().getClassLoader();
        String rootPath = loader.getResource("ru/job4j/io/chat").getPath();
        this.onePhrase = String.join("/", rootPath, "one_phrase.txt");
        this.logger = String.join("/", rootPath, "logger.txt");
    }

    @Test
    public void whenUserInputThenRightLog() throws IOException {
        PrintStream oldOut = System.out;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             PrintStream stream = new PrintStream(out)
        ) {
            System.setOut(stream);
            String phrase = this.getOnePhrase();
            Input stubInput = new StubInput(
                    "Привет", "Пока", Chat.KEY_ANSWER_STOP,
                    "Привет", "Пока", Chat.KEY_ANSWER_RESUME,
                    "Привет", "Пока", Chat.KEY_EXIT
            );
            String greeting = this.processInputChatAndGetGreeting(stubInput);
            //
            StringBuilder result = this.readLog();
            StringBuilder expected = this.formExpected(phrase, greeting);
            assertThat(result.toString(), is(expected.toString()));
        }
        System.setOut(oldOut);
    }

    private StringBuilder formExpected(String phrase, String greeting) {
        String sep = System.lineSeparator();
        return new StringBuilder(greeting).append(sep)
                .append(Chat.INVITE_INPUT).append("Привет").append(sep)
                .append(Chat.INVITE_BOT).append(phrase).append(sep)
                .append(Chat.INVITE_INPUT).append("Пока").append(sep)
                .append(Chat.INVITE_BOT).append(phrase).append(sep)
                .append(Chat.INVITE_INPUT).append(Chat.KEY_ANSWER_STOP).append(sep)
                .append(Chat.INVITE_INPUT).append("Привет").append(sep)
                .append(Chat.INVITE_INPUT).append("Пока").append(sep)
                .append(Chat.INVITE_INPUT).append(Chat.KEY_ANSWER_RESUME).append(sep)
                .append(Chat.INVITE_BOT).append(phrase).append(sep)
                .append(Chat.INVITE_INPUT).append("Привет").append(sep)
                .append(Chat.INVITE_BOT).append(phrase).append(sep)
                .append(Chat.INVITE_INPUT).append("Пока").append(sep)
                .append(Chat.INVITE_BOT).append(phrase).append(sep)
                .append(Chat.INVITE_INPUT).append(Chat.KEY_EXIT).append(sep);
    }

    private StringBuilder readLog() throws FileNotFoundException {
        StringBuilder result = new StringBuilder();
        try (Scanner scanner = new Scanner(new FileReader(this.logger))) {
            while (scanner.hasNextLine()) {
                result.append(String.format("%s%n", scanner.nextLine()));
            }
        }
        return result;
    }

    private String processInputChatAndGetGreeting(Input stubInput) throws IOException {
        String greeting;
        try (Chat chat = new Chat(stubInput, this.onePhrase, this.logger)) {
            chat.printGreeting();
            chat.chat();
            greeting = chat.getGreeting();
        }
        return greeting;
    }

    private String getOnePhrase() throws FileNotFoundException {
        String phrase;
        try (Scanner scanner = new Scanner(new FileReader(this.onePhrase))) {
            phrase = scanner.nextLine();
        }
        return phrase;
    }
}