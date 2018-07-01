package ru.job4j.crud.filters;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Filters if there is logged user in the session.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public class LoginFilter implements Filter {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(LoginFilter.class);

    /**
     * Is called by the servlet container exactly once after instantiating the filter.
     *
     * @param filterConfig A filter configuration object.
     */
    @Override
    public void init(FilterConfig filterConfig) {
    }


    /**
     * The method is called by the container each time a request/response pair is
     * passed through the filter chain due to a client request for a resource at the end of the chain.
     *
     * @param request  An object to provide client request information to a servlet.
     * @param response An object to assist a servlet in sending a response to the client.
     * @param chain    An object provided by the servlet container giving a view into the invocation.
     *                 chain of a filtered request for a resource.
     * @throws IOException      Signals that an I/O exception of some sort has occurred.
     * @throws ServletException A general exception a servlet can throw when it encounters difficulty.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        if (req.getRequestURI().contains("/login")) {
            chain.doFilter(req, resp);
        } else {
            this.filterUserLogged(req, resp, chain);
        }
    }

    /**
     * Filters if there is user logged in the session. If not - sends him to the login page.
     *
     * @param req   An object to provide client request information to a servlet.
     * @param resp  An object to assist a servlet in sending a response to the client.
     * @param chain An object provided by the servlet container giving a view into the invocation.
     * @throws IOException      Signals that an I/O exception of some sort has occurred.
     * @throws ServletException A general exception a servlet can throw when it encounters difficulty.
     */
    private void filterUserLogged(HttpServletRequest req, HttpServletResponse resp,
                                  FilterChain chain) throws IOException, ServletException {
        HttpSession session = req.getSession();
        if (session.getAttribute("user") != null) {
            chain.doFilter(req, resp);
        } else {
            String url = String.join("/", req.getContextPath(), "login");
            resp.sendRedirect(url);
        }
    }

    /**
     * Called by the web container to indicate to a filter that it is being taken out of service.
     */
    @Override
    public void destroy() {
    }
}
