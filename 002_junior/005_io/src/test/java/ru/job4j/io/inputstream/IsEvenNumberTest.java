package ru.job4j.io.inputstream;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class IsEvenNumberTest {
    private final EvenNumber test = new EvenNumber();

    /**
     * Test isEvenNumberRegexp()
     */
    @Test
    public void whenEvenNumberThenTrueMethodRegexp() throws IOException {
        String number = "12";
        try (InputStream in = new ByteArrayInputStream(number.getBytes())) {
            assertThat(this.test.isEvenNumberRegexp(in), is(true));
        }
    }

    @Test
    public void whenOddNumberThenFalseMethodRegexp() throws IOException {
        String number = "11";
        try (InputStream in = new ByteArrayInputStream(number.getBytes())) {
            assertThat(this.test.isEvenNumberRegexp(in), is(false));
        }
    }

    @Test
    public void whenNotNumberThenFalseMethodRegexp() throws IOException {
        String number = "four12";
        try (InputStream in = new ByteArrayInputStream(number.getBytes())) {
            assertThat(this.test.isEvenNumberRegexp(in), is(false));
        }
    }

    /**
     * Test isEvenNumberLong()
     */
    @Test
    public void whenEvenNumberThenTrueMethodLong() throws IOException {
        String number = "12";
        try (InputStream in = new ByteArrayInputStream(number.getBytes())) {
            assertThat(this.test.isEvenNumberLong(in), is(true));
        }
    }

    @Test
    public void whenOddNumberThenFalseMethodLong() throws IOException {
        String number = "11";
        try (InputStream in = new ByteArrayInputStream(number.getBytes())) {
            assertThat(this.test.isEvenNumberLong(in), is(false));
        }
    }

    @Test
    public void whenNotNumberThenFalseMethodLong() throws IOException {
        String number = "four12";
        try (InputStream in = new ByteArrayInputStream(number.getBytes())) {
            assertThat(this.test.isEvenNumberLong(in), is(false));
        }
    }
}