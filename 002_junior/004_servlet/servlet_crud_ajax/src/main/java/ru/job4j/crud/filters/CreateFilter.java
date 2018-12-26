package ru.job4j.crud.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.crud.model.Credentials.Role;
import ru.job4j.crud.model.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static ru.job4j.crud.Constants.PARAM_ERROR;

/**
 * Filters if "create user" operation is allowed in this session.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public class CreateFilter implements Filter {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(CreateFilter.class);

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
        this.filterHasAccessToCreate(user, req, resp, chain);
    }

    /**
     * Filters if given user has access to create new user.
     *
     * @param user  User who wants to make "create new user" action.
     * @param req   An object to provide client request information to a servlet.
     * @param resp  An object to assist a servlet in sending a response to the client.
     * @param chain An object provided by the servlet container giving a view into the invocation.
     * @throws IOException      Signals that an I/O exception of some sort has occurred.
     * @throws ServletException A general exception a servlet can throw when it encounters difficulty.
     */
    private void filterHasAccessToCreate(User user, HttpServletRequest req, HttpServletResponse resp,
                                         FilterChain chain) throws IOException, ServletException {
        if (user.getCredentials().getRole() == Role.ADMIN) {
            chain.doFilter(req, resp);
        } else {
            Map<String, String> error = new HashMap<>();
            error.put(PARAM_ERROR.v(), "Forbidden by filter: logged user is not allowed to create users");
            resp.setContentType("application/json");
            resp.getWriter().write(new ObjectMapper().writeValueAsString(error));
        }
    }

    /**
     * Called by the web container to indicate to a filter that it is being taken out of service.
     */
    @Override
    public void destroy() {
    }
}
