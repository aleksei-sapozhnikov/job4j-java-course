package ru.job4j.chat;

import ru.job4j.chat.input.Input;

import java.io.IOException;

/**
 * Simple console chat bot. Replies by random phrase (line) from given file.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class Chat implements AutoCloseable {
    /**
     * Message for user to make input.
     */
    public static final String INVITE_INPUT = " Вы: ";
    /**
     * Message showing before bot's reply phrase.
     */
    public static final String INVITE_BOT = "Бот: ";
    /**
     * When user inputs this, the program exits.
     */
    public static final String KEY_EXIT = "закончить";
    /**
     * When user inputs this, bot stops answering.
     */
    public static final String KEY_ANSWER_STOP = "стоп";
    /**
     * When user inputs this, bot begins answering again.
     */
    public static final String KEY_ANSWER_RESUME = "продолжить";

    /**
     * Holds and gives phrases.
     */
    private final PhrasesHolder phrases;
    /**
     * Object to take input from.
     */
    private final Input input;
    /**
     * Logger of the conversation.
     */
    private final ChatLogger logger;

    /**
     * Constructs new instance.
     *
     * @param input       Where to take input from.
     * @param phrasesFile Path to file with answer phrases.
     * @param loggerFile  Path to file where to write conversation.
     * @throws IOException In case of I/O problems.
     */
    public Chat(Input input, String phrasesFile, String loggerFile) throws IOException {
        this.input = input;
        this.phrases = new PhrasesHolder(phrasesFile);
        this.logger = new ChatLogger(loggerFile);
    }

    /**
     * Returns greeting message (for test).
     */
    String getGreeting() {
        return String.join(System.lineSeparator(),
                "| Добро пожаловать в консольный чат!",
                "| Напишите фразу, и бот вам что-нибудь ответит.",
                String.format("|    введите '%s', чтобы бот перестал отвечать", KEY_ANSWER_STOP),
                String.format("|    введите '%s', чтобы бот продолжил отвечать", KEY_ANSWER_RESUME),
                String.format("|    введите '%s', чтобы выйти из программы", KEY_EXIT),
                ""
        );
    }

    /**
     * Prints greeting message.
     */
    public void printGreeting() {
        String greeting = this.getGreeting();
        System.out.println(greeting);
        logger.println(greeting);
    }

    /**
     * Iteratively asks user for input and answers if needed.
     *
     * @throws IOException In case of I/O problems.
     */
    public void chat() throws IOException {
        boolean doAnswer = true;
        String input = this.input.get(INVITE_INPUT);
        this.logger.println(INVITE_INPUT + input);
        while (!KEY_EXIT.equals(input)) {
            doAnswer = this.analyzeInput(input, doAnswer);
            input = this.input.get(INVITE_INPUT);
            this.logger.println(INVITE_INPUT + input);
        }
    }

    /**
     * Reads input and does actions needed.
     *
     * @param input    User input.
     * @param doAnswer Flag showing if bot is to answer.
     * @return Flag showing if bot is to answer next time.
     * @throws IOException In case of I/O problems.
     */
    private boolean analyzeInput(String input, boolean doAnswer) throws IOException {
        if (KEY_ANSWER_STOP.equals(input)) {
            doAnswer = false;
        }
        if (KEY_ANSWER_RESUME.equals(input)) {
            doAnswer = true;
        }
        this.answerIfNeeded(doAnswer);
        return doAnswer;
    }

    /**
     * Gets random phrase and prints it to user (if needed).
     *
     * @param doAnswer Flag showing if bot is to answer.
     * @throws IOException In case of I/O problems.
     */
    private void answerIfNeeded(boolean doAnswer) throws IOException {
        String invite = INVITE_BOT;
        String text = this.phrases.getRandomPhrase();
        if (doAnswer) {
            System.out.println(invite + text);
            this.logger.println(invite + text);
        }
    }


    /**
     * Closes all resources used.
     *
     * @throws IOException In case of problems closing file.
     */
    @Override
    public void close() throws IOException {
        this.phrases.close();
        this.logger.close();
    }
}
