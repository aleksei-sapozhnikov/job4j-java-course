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

public class UpdateDeleteFilter implements Filter {
    /**
     * Main directory where views are stored.
     */
    protected static final String VIEWS_DIR = "/WEB-INF/views";
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(UpdateDeleteFilter.class);

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession();
        synchronized (session) {
            User user = (User) session.getAttribute("user");
            this.filterCanUpdate(user, req, resp, chain);
        }
    }

    private void filterCanUpdate(User user, HttpServletRequest req, HttpServletResponse resp,
                                 FilterChain chain) throws IOException, ServletException {
        if (user.getRole() == Role.ADMIN) {
            chain.doFilter(req, resp);
        } else {
            this.filterIdTheSame(user, req, resp, chain);
        }
    }

    private void filterIdTheSame(User user, HttpServletRequest req, HttpServletResponse resp,
                                 FilterChain chain) throws IOException, ServletException {
        int updateId = Integer.valueOf(req.getParameter("id"));
        if (user.getId() == updateId) {
            chain.doFilter(req, resp);
        } else {
            String url = String.join("/", req.getContextPath(), "list");
            String params = String.join("&",
                    String.join("=", "errorString", "you can UPDATE / DELETE only yourself")
            );
            resp.sendRedirect(String.join("?", url, params));
        }
    }

    @Override
    public void destroy() {

    }
}
