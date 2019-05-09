package ru.job4j.menu.formatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.menu.WrongArgumentException;

/**
 * Formatter for hierarchy entries.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class HierarchyFormatter implements IHierarchyFormatter {
    /**
     * Logger.
     */
    @SuppressWarnings("unused")
    private static final Logger LOG = LogManager.getLogger(HierarchyFormatter.class);
    /**
     * Delimiter of one level in hierarchy.
     */
    private final String delimiter;
    /**
     * Symbols to insert between level delimiters and entry itself.
     */
    private final String postDelimiter;

    /**
     * Constructor.
     *
     * @param delimiter     Delimiter of one level in hierarchy.
     * @param postDelimiter Symbols to insert between level delimiters and entry itself.
     */
    public HierarchyFormatter(String delimiter, String postDelimiter) {
        this.delimiter = delimiter;
        this.postDelimiter = postDelimiter;
    }

    /**
     * Returns string with given entry formatted according to given level
     * in hierarchy.
     *
     * @param entry Entry to format.
     * @param level Level in hierarchy. Assertion: level > 0. Level == 0 is root,
     *              level == 1 is subRoot, level == 2 is subSubRoot, etc...
     * @return Level-according formatted entry.
     * @throws WrongArgumentException If given level < 0.
     */
    @Override
    public String formatEntry(String entry, int level) throws WrongArgumentException {
        this.validateLevel(level);
        var result = new StringBuilder();
        if (level > 0) {
            this.appendFullDelimiter(level, result);
        }
        result.append(entry);
        return result.toString();
    }

    /**
     * Validates given level to be > 0.
     *
     * @param level Level.
     * @throws WrongArgumentException If level < 0.
     */
    private void validateLevel(int level) throws WrongArgumentException {
        if (level < 0) {
            throw new WrongArgumentException("Entry level must be >= 0");
        }
    }

    /**
     * Appends all level delimiters and post-delimiter
     * to given StringBuilder.
     *
     * @param level   Level to format for.
     * @param builder StringBuilder forming result.
     */
    private void appendFullDelimiter(int level, StringBuilder builder) {
        for (int i = 0; i < level; i++) {
            builder.append(this.delimiter);
        }
        builder.append(this.postDelimiter);
    }
}
