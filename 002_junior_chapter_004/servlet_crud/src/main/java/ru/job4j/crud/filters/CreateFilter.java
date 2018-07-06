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

public class CreateFilter implements Filter {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(CreateFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession();
        synchronized (session) {
            User user = (User) session.getAttribute("user");
            if (user.getRole() == Role.ADMIN) {
                chain.doFilter(req, resp);
            } else {
                resp.sendRedirect(String.format("%s?%s",
                        String.join("/", req.getContextPath(), "list"),
                        String.join("=", "errorString", "only ADMIN can create users")
                ));
            }
        }

    }

    @Override
    public void destroy() {

    }
}
