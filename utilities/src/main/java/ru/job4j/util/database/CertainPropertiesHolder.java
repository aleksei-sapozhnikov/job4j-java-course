package ru.job4j.util.database;

import ru.job4j.util.common.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * Loads properties with given keys.
 * Supports loading property value as string or from file given by string.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class CertainPropertiesHolder implements Function<String, String> {
    /**
     * Map with taken properties values.
     */
    private final Map<String, String> queries = new HashMap<>();
    /**
     * When property key starts with this - a property will be loaded.
     * Default: "sql.".
     * Example:
     * key "sql.seat.add" is loaded,
     * key "seat.add" - is not loaded.
     */
    private final String loadKey;
    /**
     * When property value starts with this - it means this is a path to file where the query is.
     * Default: "file:".
     * Example:
     * value "file:ru/job4j/sql/query.sql" is a file path,
     * value "ru/job4j/sql/query.sql" is just a string.
     */
    private final String fileKey;

    /**
     * Constructs new instance.
     *
     * @param properties Object with parameters.
     * @param loadKey    Key to load property.
     * @param fileKey    Key defining file path.
     */
    public CertainPropertiesHolder(Properties properties, String loadKey, String fileKey) {
        this.loadKey = loadKey;
        this.fileKey = fileKey;
        this.loadParameters(properties);
    }

    /**
     * Constructs new instance.
     *
     * @param properties Object with parameters.
     * @param loadKey    Key to load property.
     */
    public CertainPropertiesHolder(Properties properties, String loadKey) {
        this(properties, loadKey, "file:");
    }

    /**
     * Returns property value.
     *
     * @param key Property key.
     * @return Property value.
     */
    public String get(String key) {
        String result = "null";
        if (this.queries.containsKey(key)) {
            result = this.queries.get(key);
        }
        return result;
    }

    /**
     * Returns property value or default value if property not found.
     *
     * @param key          Property key.
     * @param defaultValue Default value.
     * @return Property value.
     * @
     */
    public String getOrDefault(String key, String defaultValue) {
        String result = defaultValue;
        if (this.queries.containsKey(key)) {
            result = this.queries.get(key);
        }
        return result;
    }

    /**
     * Implementation of 'Function<String, String' interface.
     * Returns property value by key.
     *
     * @param key Property key key.
     * @return Property value.
     */
    @Override
    public String apply(String key) {
        return this.get(key);
    }

    /**
     * Loads queries from Properties object to this holder.
     *
     * @param properties Properties object.
     */
    private void loadParameters(Properties properties) {
        Set<String> keys = properties.stringPropertyNames().stream()
                .filter(s -> s.startsWith(loadKey))
                .collect(Collectors.toSet());
        for (String key : keys) {
            String value = properties.getProperty(key);
            int fileTrim = this.fileKey.length();
            this.queries.put(key, value.startsWith(this.fileKey)
                    ? Utils.loadFileAsString(this, "UTF-8", value.substring(fileTrim))
                    : value
            );
        }
    }
}
