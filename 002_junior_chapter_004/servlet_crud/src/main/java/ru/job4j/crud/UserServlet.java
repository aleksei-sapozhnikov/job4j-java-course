package ru.job4j.crud;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public class UserServlet extends HttpServlet {

    private final Logger log = LogManager.getLogger(UserServlet.class);
    private final Store<User> storage = new UserStore();
    private final ActionDispatch dispatch = new ActionDispatch().init();

    /**
     * Метод doGet - должен отдавать список всех пользователей в системе.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        super.doGet(req, resp);
    }

    /**
     * Метод doPost - должен  уметь делать три вещи, создавать пользователя, обновлять поля пользователя, удалять пользователя.
     * <p>
     * https://github.com/peterarsentev/code_quality_principles#2-dispatch-pattern-instead-of-multiple-if-statements-and-switch-anti-pattern
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        boolean result = this.dispatch.handle(req.getParameter("action"), resp);
    }


    private class ActionDispatch {
        Map<String, Function<HttpServletResponse, Boolean>> dispatch = new HashMap<>();

        private boolean handle(final String action, final HttpServletResponse resp) {
            return this.dispatch.get(action).apply(resp);
        }

        private ActionDispatch init() {
            this.load("create", this.toCreate());
            this.load("update", this.toUpdate());
            this.load("delete", this.toDelete());
            return this;
        }

        private void load(String action, Function<HttpServletResponse, Boolean> handler) {
            this.dispatch.put(action, handler);
        }

        private BiFunction<HttpServletRequest, HttpServletResponse, Boolean> toCreate() {
            boolean[] result = {false};
            return (req, resp) -> {
                try {
                    User adding = new User(
                            req.getParameter("name"),
                            req.getParameter("login"),
                            req.getParameter("email"),
                            Integer.valueOf(req.getParameter("created"))
                    );
                    storage.add(adding);
                    result[0] = true;
                } catch (NumberFormatException e) {
                    log.error(String.format("User create failed: %s", e.getClass().getName()));
                }
                return result[0];
            };
        }

        private Function<HttpServletResponse, Boolean> toUpdate() {
            boolean[] result = {false};
            return resp -> {
                try (PrintWriter writer = new PrintWriter(resp.getOutputStream())) {
                    resp.setContentType("text/html");
                    writer.append("<center>User updated</center><br>");
                    result[0] = true;
                } catch (IOException e) {
                    log.error(String.format("User update failed: %s", e.getClass().getName()));
                }
                return result[0];
            };
        }

        private Function<HttpServletResponse, Boolean> toDelete() {
            boolean[] result = {false};
            return resp -> {
                try (PrintWriter writer = new PrintWriter(resp.getOutputStream())) {
                    resp.setContentType("text/html");
                    writer.append("<center>User deleted</center><br>");
                    result[0] = true;
                } catch (IOException e) {
                    log.error(String.format("User delete failed: %s", e.getClass().getName()));
                }
                return result[0];
            };
        }


    }

}
