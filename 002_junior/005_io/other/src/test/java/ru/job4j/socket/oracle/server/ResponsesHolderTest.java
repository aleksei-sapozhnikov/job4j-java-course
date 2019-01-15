package ru.job4j.socket.oracle.server;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ResponsesHolderTest {

    private final ResponsesHolder responsesHolder = new ResponsesHolder();

    @Test
    public void whenKnownThenAnswerFromMap() {
        String request = "hello";
        String expected = "Hello, dear friend";
        assertThat(this.responsesHolder.getResponse(request), is(expected));
    }

    @Test
    public void whenUnknownThenAnswerUnknown() {
        String request = "abracadabra";
        String expected = ResponsesHolder.RESPONSE_TO_UNKNOWN_REQUEST;
        assertThat(this.responsesHolder.getResponse(request), is(expected));
    }
}