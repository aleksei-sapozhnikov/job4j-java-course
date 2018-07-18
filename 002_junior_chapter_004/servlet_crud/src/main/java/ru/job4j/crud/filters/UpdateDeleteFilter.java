package ru.job4j.crud.filters;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.crud.Role;
import ru.job4j.crud.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Filters if "update / delete user" operations are allowed in this session.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public class UpdateDeleteFilter implements Filter {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(UpdateDeleteFilter.class);

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
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("loggedUser");
        this.filterCanUpdate(user, req, resp, chain);
    }

    /**
     * Filters if logged user can perform "update" operation.
     *
     * @param user  User who wants to make "update" action.
     * @param req   An object to provide client request information to a servlet.
     * @param resp  An object to assist a servlet in sending a response to the client.
     * @param chain An object provided by the servlet container giving a view into the invocation.
     * @throws IOException      Signals that an I/O exception of some sort has occurred.
     * @throws ServletException A general exception a servlet can throw when it encounters difficulty.
     */
    private void filterCanUpdate(User user, HttpServletRequest req, HttpServletResponse resp,
                                 FilterChain chain) throws IOException, ServletException {
        if (user.getRole() == Role.ADMIN) {
            chain.doFilter(req, resp);
        } else {
            this.filterIdTheSame(user, req, resp, chain);
        }
    }

    /**
     * Filters if logged user has the same id that the user he wants to update.
     *
     * @param user  User who wants to make "update" action.
     * @param req   An object to provide client request information to a servlet.
     * @param resp  An object to assist a servlet in sending a response to the client.
     * @param chain An object provided by the servlet container giving a view into the invocation.
     * @throws IOException      Signals that an I/O exception of some sort has occurred.
     * @throws ServletException A general exception a servlet can throw when it encounters difficulty.
     */
    private void filterIdTheSame(User user, HttpServletRequest req, HttpServletResponse resp,
                                 FilterChain chain) throws IOException, ServletException {
        int updateId = Integer.valueOf(req.getParameter("id"));
        if (user.getId() == updateId) {
            chain.doFilter(req, resp);
        } else {
            String url = req.getContextPath();
            String params = String.join("&",
                    String.join("=", "error", "logged user may only UPDATE / DELETE himself")
            );
            resp.sendRedirect(String.join("?", url, params));
        }
    }

    /**
     * Called by the web container to indicate to a filter that it is being taken out of service.
     */
    @Override
    public void destroy() {
    }
}
