package ru.job4j.theater.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import java.io.IOException;

/**
 * Filter to change response encoding to UTF-8 and datatype to json/string.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class JsonUtf8 implements Filter {
    /**
     * Logger.
     */
    @SuppressWarnings("unused")
    private static final Logger LOG = LogManager.getLogger(JsonUtf8.class);

    /**
     * Is called when the Filter object is created.
     *
     * @param filterConfig Object with parameters.
     */
    @Override
    public void init(FilterConfig filterConfig) {
    }

    /**
     * Makes changes in response.
     *
     * @param request  Request object.
     * @param response Response object.
     * @param chain    Filter chain.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        chain.doFilter(request, response);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
    }

    /**
     * Is called before the filter object is destroyed.
     */
    @Override
    public void destroy() {
    }
}
