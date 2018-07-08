package ru.job4j.crud.servlets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.crud.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LogInServlet extends AbstractServlet {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(LogInServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(String.join("/", this.getViewsDir(), "login.jsp"))
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        User user = VALIDATOR.findByCredentials(login, password);
        if (user != null) {
            HttpSession session = req.getSession();
            synchronized (session) {
                session.setAttribute("user", user);
            }
            resp.sendRedirect(String.join("/", req.getContextPath(), "list"));
        } else {
            req.setAttribute("error", "Invalid credentials");
            this.doGet(req, resp);
        }
    }
}
    