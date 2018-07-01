package ru.job4j.crud.servlets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.crud.User;
import ru.job4j.crud.logic.Validator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginController extends AbstractServlet {
    private final Logger log = LogManager.getLogger(LoginController.class);


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(String.join("/", this.getViewsDir(), "login.jsp"))
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String store = req.getParameter(this.getUrlParamStore());
        Validator<User> validator = this.getValidators().get(store);
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        if (validator.containsCredentials(login, password)) {
            HttpSession session = req.getSession();
            synchronized (session) {
                session.setAttribute("login", login);
            }
            resp.sendRedirect(String.format("%s?%s",
                    String.join("/", req.getContextPath(), "list"),
                    String.join("=", this.getUrlParamStore(), store)));
        } else {
            req.setAttribute("error", "Invalid credentials");
            this.doGet(req, resp);
        }
    }
}
    