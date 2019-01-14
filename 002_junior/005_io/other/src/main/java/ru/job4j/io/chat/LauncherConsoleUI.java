package ru.job4j.io.chat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.io.chat.input.ConsoleInput;
import ru.job4j.util.methods.CommonUtils;

import java.io.IOException;

/**
 * Console-based user UI Launcher.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class LauncherConsoleUI {
    /**
     * Logger.
     */
    @SuppressWarnings("unused")
    private static final Logger LOG = LogManager.getLogger(LauncherConsoleUI.class);

    /**
     * Console UI launcher.
     *
     * @param args Console-line arguments.
     */
    public static void main(String[] args) {
        LauncherConsoleUI start = new LauncherConsoleUI();
        if (args.length < 2) {
            start.printHelp();
        } else {
            try (Chat chat = new Chat(new ConsoleInput(), args[0], args[1])) {
                chat.printGreeting();
                chat.chat();
            } catch (IOException e) {
                LOG.error(CommonUtils.describeThrowable(e));
            }
        }
    }

    private void printHelp() {
        System.out.println(String.join(System.lineSeparator(),
                "Help: ",
                "| Запуск: 'chat $phrases $log_file', где:",
                "|    $phrases - путь к файлу с фразами бота, например: 'phrases.txt'",
                "|    $log_file - путь к файлу с записью диалога, например: 'chat_log.txt'",
                ""
        ));
    }
}
