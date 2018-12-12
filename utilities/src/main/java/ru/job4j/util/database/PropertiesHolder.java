package ru.job4j.util.database;

import ru.job4j.util.common.Utils;

import java.util.*;
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
public class PropertiesHolder implements Function<String, Optional<String>> {
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
     * @param builder Builder object for that class.
     */
    private PropertiesHolder(Builder builder) {
        this.loadKey = builder.loadKey;
        this.fileKey = builder.fileKey;
        this.loadQueries(builder.properties);
    }

    /**
     * Returns property value.
     *
     * @param key Property key.
     * @return Property value.
     */
    public Optional<String> get(String key) {
        Optional<String> result = Optional.empty();
        if (this.queries.containsKey(key)) {
            result = Optional.of(this.queries.get(key));
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
    public Optional<String> apply(String key) {
        return this.get(key);
    }

    /**
     * Loads queries from Properties object to this holder.
     *
     * @param properties Properties object.
     */
    private void loadQueries(Properties properties) {
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

    /**
     * Builder class for PropertiesHolder.
     */
    public static class Builder {
        /**
         * Required fields.
         */
        private final Properties properties;
        private final String loadKey;
        /**
         * Optional fields.
         */
        private String fileKey = "file:";

        /**
         * Constructs new Builder with required fields and others default.
         *
         * @param properties Object where to take queries from.
         * @param loadKey    Queries starting with that key will be loaded.
         */
        public Builder(Properties properties, String loadKey) {
            this.properties = properties;
            this.loadKey = loadKey;
        }

        /**
         * Sets 'fileKey' field value and returns this builder.
         *
         * @param value Value to set.
         */
        public Builder setFileKey(String value) {
            this.fileKey = value;
            return this;
        }

        /**
         * Builds PropertiesHolder object using this Builder object.
         *
         * @return new PropertiesHolder object.
         */
        public PropertiesHolder build() {
            return new PropertiesHolder(this);
        }
    }
}
