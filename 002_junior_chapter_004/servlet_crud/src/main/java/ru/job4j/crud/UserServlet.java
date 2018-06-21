package ru.job4j.crud;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class UserServlet extends HttpServlet {

    private static final Logger log = LogManager.getLogger(UserServlet.class);
    private final Validator<User> logic = UserValidator.getInstance();
    private final ActionDispatch dispatch = new ActionDispatch().init();

    /**
     * Метод doGet - должен отдавать список всех пользователей в системе.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User[] users = this.logic.findAll();
        try (PrintWriter writer = new PrintWriter(resp.getOutputStream())) {
            writer.append("<html><body>");
            writer.append("<center><h1>List of all users:</h1></center>");
            for (User user : users) {
                writer.append(user.toString()).append("<br><br>");
            }
            writer.append("</body></html>");
            writer.flush();
        }
    }

    /**
     * Метод doPost - должен  уметь делать три вещи, создавать пользователя, обновлять поля пользователя, удалять пользователя.
     * <p>
     * https://github.com/peterarsentev/code_quality_principles#2-dispatch-pattern-instead-of-multiple-if-statements-and-switch-anti-pattern
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        this.dispatch.handle(req.getParameter("action"), req, resp);
    }


    private class ActionDispatch {
        Map<String, BiFunction<HttpServletRequest, HttpServletResponse, Boolean>> dispatch = new HashMap<>();

        private boolean handle(final String action, final HttpServletRequest req, final HttpServletResponse resp) {
            return this.dispatch.getOrDefault(action, this.toUnknown()).apply(req, resp);
        }

        private ActionDispatch init() {
            this.load("create", this.toCreate());
            this.load("update", this.toUpdate());
            this.load("delete", this.toDelete());
            return this;
        }

        private void load(String action, BiFunction<HttpServletRequest, HttpServletResponse, Boolean> handler) {
            this.dispatch.put(action, handler);
        }

        private BiFunction<HttpServletRequest, HttpServletResponse, Boolean> toCreate() {
            return (req, resp) -> {
                User adding = this.getUser(req);
                logic.add(adding);
                return true;
            };
        }

        private BiFunction<HttpServletRequest, HttpServletResponse, Boolean> toUpdate() {
            return (req, resp) -> {
                boolean result = false;
                int id = this.getId(req);
                if (id != -1) {
                    User adding = this.getUser(req);
                    result = logic.update(id, adding);
                }
                return result;
            };
        }

        private BiFunction<HttpServletRequest, HttpServletResponse, Boolean> toDelete() {
            return (req, resp) -> {
                int id = this.getId(req);
                return id != -1
                        && logic.delete(id) != null;
            };
        }

        private BiFunction<HttpServletRequest, HttpServletResponse, Boolean> toUnknown() {
            return (req, resp) -> false;
        }

        private User getUser(HttpServletRequest req) {
            return new User(
                    req.getParameter("name"),
                    req.getParameter("login"),
                    req.getParameter("email"),
                    System.currentTimeMillis()
            );
        }

        private int getId(HttpServletRequest req) {
            int result = -1;
            String idString = req.getParameter("id");
            if (idString != null) {
                try {
                    result = Integer.valueOf(idString);
                } catch (Exception e) {
                    log.error(String.format("%s: %s", e.getClass().getName(), e.getMessage()));
                }
            }
            return result;
        }

    }

}
