package ru.job4j.menu.formatter;

import ru.job4j.menu.WrongArgumentException;

/**
 * Formatter for hierarchy entries.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public interface IHierarchyFormatter {
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
    String formatEntry(String entry, int level) throws WrongArgumentException;
}
