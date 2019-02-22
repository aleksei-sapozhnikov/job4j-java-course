package ru.job4j.stringreplacer;

import java.util.Map;

/**
 * Replaces key values with concrete values.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public interface IStringReplacer {
    /**
     * Regular expresion used to find keys.
     * Means that key in template must be as: ${keyName}.
     * A key may consist of latin letters, numbers and underscore (_).
     * E.g.: ${sos15}, ${name_33}, ${65_points}, etc.
     */
    String KEY_SEARCH_REGEX = "\\$\\{\\w+?\\}";

    /**
     * Builds phrase from template using given key-value map.
     * <p>
     * For example: given template "Hello, ${name}, I'm ${condition}."
     * and map with key-value pairs: "name"--> "Petr, "condition"-->"fine"
     * the result will be: "Hello, Petr, I'm fine".
     *
     * @param template Phrase template.
     * @param map      Key-value map.
     * @return Result string.
     * @throws NotFoundException If key found in template was not found in map.
     */
    String replaceAll(String template, Map<String, String> map) throws NotFoundException;
}
