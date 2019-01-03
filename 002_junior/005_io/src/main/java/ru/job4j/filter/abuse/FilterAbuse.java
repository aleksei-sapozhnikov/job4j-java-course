package ru.job4j.filter.abuse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Scanner;
import java.util.Set;

/**
 * Filter abuse words in given InputStream.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class FilterAbuse {

    /**
     * Returns given stream with deleted words.
     *
     * @param in    InputStream.
     * @param out   OutputStream.
     * @param abuse Array of abuse words.
     * @throws IOException In case of problems in writing to Output stream.
     */
    public void dropAbuses(InputStream in, OutputStream out, String[] abuse) throws IOException {
        Set<String> abuseSet = Set.of(abuse);
        try (Scanner scanner = new Scanner(in);
             OutputStreamWriter writer = new OutputStreamWriter(out)) {
            boolean skipSpace = true;
            while (scanner.hasNext()) {
                skipSpace = checkNextWord(abuseSet, scanner, writer, skipSpace);
            }
        }
    }

    /**
     * Checks next word read by scanner and returns 'skipSpace' flag.
     *
     * @param abuseSet  Set of words to drop.
     * @param scanner   Scanner object.
     * @param writer    OutputStreamWriter object.
     * @param skipSpace Flag indicating if this is the first word to write into output stream.
     * @return Value of the 'first' flag. After first word was written to the OutputStream, always <tt>false</tt>.
     * @throws IOException In case of problems in writing to Output stream.
     */
    private boolean checkNextWord(Set<String> abuseSet, Scanner scanner, OutputStreamWriter writer, boolean skipSpace)
            throws IOException {
        boolean skipNextSpace = false;
        var word = scanner.next();
        if (!(abuseSet.contains(word))) {
            writer.write(skipSpace ? word : " " + word);
        } else {
            skipNextSpace = skipSpace;
        }
        return skipNextSpace;
    }
}
