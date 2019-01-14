package ru.job4j.io.inputstream;

import java.io.IOException;
import java.io.InputStream;


/**
 * Finds if byte array is even number.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class EvenNumber {

    /**
     * Returns if given Input stream is an even number.
     * First way: using only regexp.
     *
     * @param in InputStream object.
     * @return <tt>true</tt> if even number, <tt>false</tt> if not.
     * @throws IOException In case of problems with reading bytes from InputStream.
     */
    public boolean isEvenNumberRegexp(InputStream in) throws IOException {
        return new String(in.readAllBytes()).matches("^\\d*[02468]$");
    }

    /**
     * Returns if given Input stream is an even number.
     * Second way: using regexp for validate, then parsing to long.
     *
     * @param in InputStream object.
     * @return <tt>true</tt> if even number, <tt>false</tt> if not.
     * @throws IOException In case of problems with reading bytes from InputStream.
     */
    public boolean isEvenNumberLong(InputStream in) throws IOException {
        var s = new String(in.readAllBytes());
        return s.matches("^\\d+")
                && Long.parseLong(s) % 2 == 0;
    }
}
