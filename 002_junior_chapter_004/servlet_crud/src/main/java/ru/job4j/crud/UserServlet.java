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

    private static final Logger LOG = LogManager.getLogger(UserServlet.class);
    private final Validator<User> logic = UserValidator.getInstance();
    private final ActionDispatch dispatch = new ActionDispatch().init();

    /**
     * Метод doGet - должен отдавать список всех пользователей в системе.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User[] users = this.logic.findAll();
        try (PrintWriter writer = new PrintWriter(resp.getOutputStream())) {
            writer.append("List of all users:").append(System.lineSeparator());
            for (User user : users) {
                writer.append(user.toString()).append(System.lineSeparator());
            }
            writer.flush();
        }
    }

    /**
     * Метод doPost - должен  уметь делать три вещи, создавать пользователя, обновлять поля пользователя, удалять пользователя.
     * <p>
     * https://github.com/peterarsentev/code_quality_principles#2-dispatch-pattern-instead-of-multiple-if-statements-and-switch-anti-pattern
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String result = this.dispatch.handle(req.getParameter("action"), req, resp);
        try (PrintWriter writer = new PrintWriter(resp.getOutputStream())) {
            writer.append(result);
            writer.flush();
        }
    }


    private class ActionDispatch {
        Map<String, BiFunction<HttpServletRequest, HttpServletResponse, String>> dispatch = new HashMap<>();

        private String handle(final String action, final HttpServletRequest req, final HttpServletResponse resp) {
            return this.dispatch.getOrDefault(action, this.toUnknown()).apply(req, resp);
        }

        private ActionDispatch init() {
            this.load("create", this.toCreate());
            this.load("update", this.toUpdate());
            this.load("delete", this.toDelete());
            return this;
        }

        private void load(String action, BiFunction<HttpServletRequest, HttpServletResponse, String> handler) {
            this.dispatch.put(action, handler);
        }

        private BiFunction<HttpServletRequest, HttpServletResponse, String> toCreate() {
            return (req, resp) -> {
                User adding = this.formUser(req);
                return logic.add(adding) != -1
                        ? String.format("Added user : %s", adding.toString())
                        : String.format("Failed to add user: %s", adding.toString());
            };
        }

        private BiFunction<HttpServletRequest, HttpServletResponse, String> toUpdate() {
            return (req, resp) -> {
                String result;
                int id = this.getId(req);
                if (id != -1) {
                    User updater = this.formUser(req);
                    result = logic.update(id, updater)
                            ? String.format("Successfully updated user with id = %s.", id)
                            : String.format("Failed to update user with id = %s.", id);
                } else {
                    result = "User delete failed: could not parse id.";
                }
                return result;
            };
        }

        private BiFunction<HttpServletRequest, HttpServletResponse, String> toDelete() {
            return (req, resp) -> {
                String result;
                int id = this.getId(req);
                if (id != -1) {
                    result = logic.delete(id) != null
                            ? String.format("Successfully deleted user with id = %s", id)
                            : String.format("Failed to delete user with id = %s.", id);
                } else {
                    result = "User delete failed: could not parse id.";
                }
                return result;
            };
        }

        private BiFunction<HttpServletRequest, HttpServletResponse, String> toUnknown() {
            return (req, resp) ->
                    "Unknown action type. Possible: \"create\', \"update\", \"delete\". Did nothing.";
        }

        private User formUser(HttpServletRequest req) {
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
                    LOG.error(String.format("%s: %s", e.getClass().getName(), e.getMessage()));
                }
            }
            return result;
        }

    }

}
