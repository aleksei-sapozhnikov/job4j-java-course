package ru.job4j.music;

import com.sun.javafx.collections.UnmodifiableObservableMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.HashMap;
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
     * Map with empty values - for empty user.
     */
    private static final Map<UserFields, String> EMPTY_VALUES = Collections.unmodifiableMap(
            new HashMap<UserFields, String>() {
                {
                    for (UserFields field : UserFields.values()) {
                        put(field, "");
                    }
                }
            });

    /**
     * Empty user - to use instead of "null" object.
     */
    public static final User EMPTY_USER = new User(-1, EMPTY_VALUES);


    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(User.class);
    /**
     * User unique id.
     */
    private final int id;

    private final Map<UserFields, String> values;

    /**
     * Constructs new object.
     *
     * @param values Map with values of user fields.
     */
    public User(int id, Map<UserFields, String> values) {
        if (values.size() < UserFields.values().length) {
            LOG.error("Argument length (%s) is less then needed (%s). Filling fields with empty values.");
            this.id = id;
            this.values = EMPTY_VALUES;
        } else {
            this.id = id;
            this.values = Collections.unmodifiableMap(values);
        }
    }

    @Override
    public String toString() {
        return String.format(
                "[user id='%s', login='%s', password='%s', role='%s', address='%s', musicGenre='%s']",
                this.id,
                this.values.get(LOGIN), this.values.get(PASSWORD), this.values.get(ROLE),
                this.values.get(ADDRESS), this.values.get(MUSIC_GENRE)
        );
    }


    /**
     * Returns login.
     *
     * @return Value of login field.
     */
    public String getLogin() {
        return this.values.get(LOGIN);
    }

    /**
     * Returns password.
     *
     * @return Value of password field.
     */
    public String getPassword() {
        return this.values.get(PASSWORD);
    }

    /**
     * Returns role.
     *
     * @return Value of role field.
     */
    public String getRole() {
        return this.values.get(ROLE);
    }

    /**
     * Returns address.
     *
     * @return Value of address field.
     */
    public String getAddress() {
        return this.values.get(ADDRESS);
    }

    /**
     * Returns musicGenre.
     *
     * @return Value of musicGenre field.
     */
    public String getMusicGenre() {
        return this.values.get(MUSIC_GENRE);
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
