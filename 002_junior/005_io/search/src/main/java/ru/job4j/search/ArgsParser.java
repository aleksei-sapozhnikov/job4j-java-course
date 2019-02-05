package ru.job4j.search;

import ru.job4j.util.function.ConsumerEx;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Arguments parser.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class ArgsParser {
    /**
     * Minimum number of arguments in array possible.
     * If number of arguments is less than this -
     * it means some vital parameters are not defined.
     */
    public static final int MIN_ARGS_LENGTH = 7;
    /**
     * Minimum number of keys in result map.
     * If less - that means not all vital parameters
     * were defined.
     */
    public static final int MIN_RESULT_KEYS = 4;
    /**
     * Keys of result map.
     */
    public static final String DIRECTORY = "directory";
    public static final String WHAT_TO_SEARCH = "searchValue";
    public static final String OUTPUT = "output";
    public static final String SEARCH_TYPE = "searchType";
    public static final String N_PROCESS_THREADS = "processThreadsNumber";
    /**
     * Predefined result values for search type key.
     */
    public static final String SEARCH_TYPE_VALUE_MASK = SearchType.MASK.toString();
    public static final String SEARCH_TYPE_VALUE_FULL = SearchType.FULL.toString();
    public static final String SEARCH_TYPE_VALUE_REGEX = SearchType.REGEX.toString();
    /**
     * Parsing result map.
     */
    private final Map<String, String> result = new HashMap<>();
    /**
     * Array with arguments.
     */
    private final String[] args;
    /**
     * Length of arguments array - just to avoid
     * calling args.length many times.
     */
    private final int length;
    /**
     * Dispatches key and action to do when finding this key.
     */
    private final Map<String, ConsumerEx<String>> keyDispatch = new HashMap<>();
    /**
     * Dispatches keys defining search type.
     */
    private final Map<String, String> searchTypeDispatch = new HashMap<>();
    /**
     * Current position in args array.
     * Next element to take.
     */
    private int position = 0;

    /**
     * Constructs new parser.
     *
     * @param args Given array of arguments.
     * @throws Exception In case of problems.
     */
    public ArgsParser(String[] args) throws Exception {
        this.checkArgumentsLength(args);
        this.args = args;
        this.length = args.length;
        this.initKeyDispatch();
        this.initSearchTypeDispatch();
    }

    /**
     * Reads arguments array and fills result key-value map.
     *
     * @return Result map.
     * @throws Exception In case of parsing problem or if too
     *                   low number of keys was found.
     */
    public Map<String, String> parse() throws Exception {
        while (this.position < this.length) {
            this.keyDispatch
                    .getOrDefault(this.args[this.position], this::unknownKey)
                    .accept(this.args[this.position]);
            this.position++;
        }
        this.checkEnoughKeysFound();
        return Collections.unmodifiableMap(this.result);
    }

    /**
     * Checks if arguments length is enough.
     *
     * @param args Arguments array.
     * @throws Exception If length is not enough.
     */
    private void checkArgumentsLength(String[] args) throws Exception {
        if (args.length < MIN_ARGS_LENGTH) {
            throw new Exception(String.format(
                    "Not enough arguments. Must be %s", MIN_ARGS_LENGTH));
        }
    }

    /**
     * Puts key - action values into the dispatch.
     */
    private void initKeyDispatch() {
        this.keyDispatch.put("-d", (s) -> this.result.put(DIRECTORY, this.getNextArg(s)));
        this.keyDispatch.put("-n", (s) -> this.result.put(WHAT_TO_SEARCH, this.getNextArg(s)));
        this.keyDispatch.put("-m", (s) -> this.result.put(SEARCH_TYPE, this.dispatchSearchTypeKey(s)));
        this.keyDispatch.put("-f", (s) -> this.result.put(SEARCH_TYPE, this.dispatchSearchTypeKey(s)));
        this.keyDispatch.put("-r", (s) -> this.result.put(SEARCH_TYPE, this.dispatchSearchTypeKey(s)));
        this.keyDispatch.put("-o", (s) -> this.result.put(OUTPUT, this.getNextArg(s)));
        this.keyDispatch.put("-pt", (s) -> this.result.put(N_PROCESS_THREADS, this.getNextArgAsNumber(s)));
    }

    /**
     * Puts key - search type values into the dispatch.
     */
    private void initSearchTypeDispatch() {
        this.searchTypeDispatch.put("-m", SEARCH_TYPE_VALUE_MASK);
        this.searchTypeDispatch.put("-f", SEARCH_TYPE_VALUE_FULL);
        this.searchTypeDispatch.put("-r", SEARCH_TYPE_VALUE_REGEX);
    }

    /**
     * Checks if number of found keys is enough.
     *
     * @throws Exception If not enough keys found.
     */
    private void checkEnoughKeysFound() throws Exception {
        var size = this.result.size();
        if (size < MIN_RESULT_KEYS) {
            throw new Exception(String.format(
                    "Not enough keys found: need %s, found %s", MIN_RESULT_KEYS, size));
        }
    }

    /**
     * Returns next element in args array if it exists and is a number.
     * Otherwise, throws Exception.
     *
     * @param arg Current argument (to write into exception message).
     * @return Next element in args array if it is a number.
     * @throws Exception If no next element was found or If element was not a number.
     */
    private String getNextArgAsNumber(String arg) throws Exception {
        var nextArg = this.getNextArg(arg);
        if (!nextArg.matches("[0-9]+")) {
            throw new Exception(String.format(
                    "Value after argument %s is not number: %s",
                    arg, nextArg));
        }
        return nextArg;
    }

    /**
     * Returns next element in args array.
     * Moves current position one step forward.
     *
     * @param arg Current argument (to write into exception message).
     * @return Next element in args array.
     * @throws Exception If no next element was found.
     */
    private String getNextArg(String arg) throws Exception {
        this.checkHasNextArg(arg);
        return this.args[++this.position];
    }

    /**
     * Checks if there is next argument in the args array.
     *
     * @param arg Current argument (to write into exception message).
     * @throws Exception If there is no next argument.
     */
    private void checkHasNextArg(String arg) throws Exception {
        if (this.position + 1 >= this.length) {
            throw new Exception(String.format(
                    "Not found value after argument: %s",
                    arg));
        }
    }

    /**
     * Returns value defined by given search type key.
     *
     * @param key Search type key.
     * @return Value defined by this key.
     */
    private String dispatchSearchTypeKey(String key) {
        return this.searchTypeDispatch.get(key);
    }

    /**
     * Throws exception because the unknown key
     * was found.
     *
     * @param key Unknown key.
     * @throws Exception Every time when called.
     */
    private void unknownKey(String key) throws Exception {
        throw new Exception(String.format("Unknown key: %s", key));
    }
}
