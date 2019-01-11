package ru.job4j.chat.input;

public interface Input {
    /**
     * Returns input from user.
     *
     * @param invite Invite message for input.
     * @return User input string.
     */
    String get(String invite);
}
