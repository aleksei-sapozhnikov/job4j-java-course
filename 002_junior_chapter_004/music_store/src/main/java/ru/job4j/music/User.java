package ru.job4j.music;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

import static ru.job4j.music.User.UserFields.*;

/**
 * Class for user entity.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class User {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(User.class);

    /**
     * User login.
     */
    private final String login;
    /**
     * User password.
     */
    private final String password;
    /**
     * User role.
     */
    private final String role;
    /**
     * User address.
     */
    private final String address;
    /**
     * User music genre.
     */
    private final String musicGenre;

    /**
     * Constructs new object.
     *
     * @param values Map with values of user fields.
     */
    public User(Map<UserFields, String> values) {
        if (values.size() < UserFields.values().length) {
            LOG.error("Argument length (%s) is less then needed (%s). Filling fields with empty values.");
            this.login = "";
            this.password = "";
            this.role = "";
            this.address = "";
            this.musicGenre = "";
        } else {
            this.login = values.get(LOGIN);
            this.password = values.get(PASSWORD);
            this.role = values.get(ROLE);
            this.address = values.get(ADDRESS);
            this.musicGenre = values.get(MUSIC_GENRE);
        }
    }

    /**
     * Returns login.
     *
     * @return Value of login field.
     */
    public String getLogin() {
        return this.login;
    }

    /**
     * Returns password.
     *
     * @return Value of password field.
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Returns role.
     *
     * @return Value of role field.
     */
    public String getRole() {
        return this.role;
    }

    /**
     * Returns address.
     *
     * @return Value of address field.
     */
    public String getAddress() {
        return this.address;
    }

    /**
     * Returns musicGenre.
     *
     * @return Value of musicGenre field.
     */
    public String getMusicGenre() {
        return this.musicGenre;
    }

    /**
     * Enumerates fields that the user can have.
     */
    public enum UserFields {
        LOGIN,
        PASSWORD,
        ROLE,
        ADDRESS,
        MUSIC_GENRE
    }


}
