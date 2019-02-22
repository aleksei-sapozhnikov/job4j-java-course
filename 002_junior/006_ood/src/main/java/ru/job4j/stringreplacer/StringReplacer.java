package ru.job4j.stringreplacer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Replaces key values with concrete values.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class StringReplacer implements IStringReplacer {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(StringReplacer.class);
    /**
     * Pattern to search keys.
     */
    private final Pattern pattern = Pattern.compile(KEY_SEARCH_REGEX);

    /**
     * Returns phrase from template with all keys replaced by values.
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
    @Override
    public String replaceAll(String template, Map<String, String> map) throws NotFoundException {
        var matcher = this.pattern.matcher(template);
        var result = new StringBuilder();
        while (matcher.find()) {
            this.appendNextReplacement(map, matcher, result);
        }
        matcher.appendTail(result);
        return result.toString();
    }

    /**
     * Makes next find-and replace
     *
     * @param map     Key-value map.
     * @param matcher Key regexp matcher.
     * @param result  Char sequence to append result to.
     * @throws NotFoundException If key was not found in map.
     */
    private void appendNextReplacement(Map<String, String> map, Matcher matcher, StringBuilder result) throws NotFoundException {
        var found = matcher.group();
        var key = found.substring(2, found.length() - 1);
        this.checkExistence(map, key);
        var value = map.get(key);
        matcher.appendReplacement(result, value);
    }

    /**
     * Checks if map contains key.
     *
     * @param map Map of key-value.
     * @param key Needed key.
     * @throws NotFoundException If key not found.
     */
    private void checkExistence(Map<String, String> map, String key) throws NotFoundException {
        if (!map.containsKey(key)) {
            throw new NotFoundException(String.format("Key %s not found", key));
        }
    }
}
