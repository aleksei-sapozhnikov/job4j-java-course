package ru.job4j.socket.oracle.server;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds and returns responses for different requests.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
class ResponsesHolder {
    /**
     * Returns this string if could not find response
     * matching to request.
     */
    public static final String RESPONSE_TO_UNKNOWN_REQUEST = "Sure, my friend";
    /**
     * Map of request-response strings.
     */
    private final Map<String, String> reqResp = new HashMap<>();

    /**
     * Constructs new instance.
     */
    public ResponsesHolder() {
        this.init();
    }

    /**
     * Fills request-response map.
     */
    private void init() {
        this.reqResp.put("hello", "Hello, dear friend");
        this.reqResp.put("who are you", "I'm an oracle");
        this.reqResp.put("what can you do", String.join(System.lineSeparator(),
                "What do you think?", "I'm an oracle.", "I can answer simple questions"));
    }

    /**
     * Returns response to given request.
     *
     * @param request Request.
     * @return Corresponding response.
     */
    public String getResponse(String request) {
        return this.reqResp.getOrDefault(
                request.toLowerCase(),
                RESPONSE_TO_UNKNOWN_REQUEST
        );
    }
}
