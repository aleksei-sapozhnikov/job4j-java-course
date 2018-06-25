package ru.job4j.crud;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class ActionsDispatch {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(ActionsDispatch.class);
    /**
     * Logic layer class validating and adding/updating/deleting users.
     */
    private final Validator<User> logic;
    /**
     * Map of possible actions.
     */
    private Map<String, BiFunction<HttpServletRequest, HttpServletResponse, String>> dispatch = new HashMap<>();

    public ActionsDispatch(Validator<User> logic) {
        this.logic = logic;
    }

    /**
     * Tries to find handler for given action.
     * <p>
     * If no matching for the given action found, the "unknown" action handler if called.
     *
     * @param action Given action.
     * @param resp   Object that contains the response the servlet sends to the client.
     * @return String with action result.
     */
    public String handle(final String action, final HttpServletRequest req, final HttpServletResponse resp) {
        return this.dispatch.getOrDefault(action, this.toUnknown()).apply(req, resp);
    }

    /**
     * Initiates dispacth and returns its initiated object.
     *
     * @return Initiated dispatch object.
     */
    public ActionsDispatch init() {
        this.load("create", this.toCreate());
        this.load("formCreate", this.toFormCreate());
        this.load("update", this.toUpdate());
        this.load("formUpdate", this.toFormUpdate());
        this.load("delete", this.toDelete());
        this.load("showAll", this.toShowAll());
        return this;
    }

    /**
     * Loads given action to the "action-handler" map.
     *
     * @param action  Given action.
     * @param handler Action handler.
     */
    private void load(String action, BiFunction<HttpServletRequest, HttpServletResponse, String> handler) {
        this.dispatch.put(action, handler);
    }

    /**
     * Returns handler for the "create" action.
     *
     * @return Handler for the "create" action.
     */
    private BiFunction<HttpServletRequest, HttpServletResponse, String> toCreate() {
        return (req, resp) -> {
            User adding = this.formUser(req);
            int id = logic.add(adding);
            return id != -1
                    ? String.format("Added user : %s", new User(id, adding.getName(), adding.getLogin(), adding.getEmail(), adding.getCreated()).toString())
                    : String.format("Failed to add user: %s", adding.toString());
        };
    }

    /**
     * Returns handler for the "formCreate" action.
     *
     * @return Handler for the "formCreate" action.
     */
    private BiFunction<HttpServletRequest, HttpServletResponse, String> toFormCreate() {
        return (req, resp) -> {
            String storageContext = this.getStorageContextPath(req);
            return this.htmlFormCreateUser(storageContext);
        };
    }

    private String getStorageContextPath(HttpServletRequest req) {
        String context = req.getContextPath();
        String storage = req.getServletPath().split("/")[1];
        return context.concat("/").concat(storage);
    }

    private String htmlFormCreateUser(String storageContextPath) {
        return new StringBuilder()
                .append("<form action = \"").append(storageContextPath).append("/create").append("\" ")
                .append("method = \"post\">")
                .append("User name: <input type=\"text\" name=\"name\">").append("<br>")
                .append("User login: <input type=\"text\" name=\"login\">").append("<br>")
                .append("User email: <input type=\"text\" name=\"email\">").append("<br>")
                .append("<input type=\"submit\" value=\"create\"> ")
                .append("</form>")
                .toString();
    }

    /**
     * Returns handler for the "formUpdate" action.
     *
     * @return Handler for the "formUpdate" action.
     */
    private BiFunction<HttpServletRequest, HttpServletResponse, String> toFormUpdate() {
        return (req, resp) -> {
            int id = this.getId(req);
            User upd = this.logic.findById(id);
            String storageContext = this.getStorageContextPath(req);
            String result = new StringBuilder()
                    .append("<form action = \"").append(storageContext).append("/update").append("\" ")
                    .append("method = \"post\">")
                    .append("User name: <input type=\"text\" name=\"name\" value = \"").append(upd.getName()).append("\"> ").append("<br>")
                    .append("User login: <input type=\"text\" name=\"login\" value = \"").append(upd.getLogin()).append("\"> ").append("<br>")
                    .append("User email: <input type=\"text\" name=\"email\" value = \"").append(upd.getEmail()).append("\"> ").append("<br>")
                    .append("<input type=\"hidden\" name=\"id\" value = \"").append(upd.getId()).append("\"> ")
                    .append("<input type=\"submit\" value=\"обновить\"> ")
                    .append("</form>")
                    .toString();
            return result;
        };
    }

    /**
     * Returns handler for the "update" action.
     *
     * @return Handler for the "update" action.
     */
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

    /**
     * Returns handler for the "delete" action.
     *
     * @return Handler for the "delete" action.
     */
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

    /**
     * Returns handler for the "showAll" action.
     *
     * @return Handler for the "showAll" action.
     */
    private BiFunction<HttpServletRequest, HttpServletResponse, String> toShowAll() {
        return (req, resp) -> {
            User[] users = this.logic.findAll();
            String storageContext = this.getStorageContextPath(req);
            StringBuilder result = new StringBuilder()
                    .append("List of all users:").append("<br>")
                    .append("<table border = 1>");
            for (User user : users) {
                result.append("<tr>");
                result.append("<td>").append(user.toString()).append("</td>");
                result.append("<td>").append(this.htmlButtonUpdateUser(storageContext, user.getId())).append("</td>");
                result.append("<td>").append(this.htmlButtonDeleteUser(storageContext, user.getId())).append("</td>");
                result.append("</tr>");
            }
            result.append("</table>");
            result.append(this.htmlButtonCreateUser(storageContext));
            return result.toString();
        };
    }

    private String htmlButtonUpdateUser(String contextPath, int userId) {
        return new StringBuilder()
                .append("<form action = \"").append(contextPath).append("/update").append("\" ")
                .append("method = \"get\">")
                .append("<input type=\"hidden\" name=\"id\" value=\"").append(userId).append("\"> ")
                .append("<input type=\"submit\" value=\"изменить\"> ")
                .append("</form>")
                .toString();
    }

    private String htmlButtonDeleteUser(String contextPath, int userId) {
        return new StringBuilder()
                .append("<form action = \"").append(contextPath).append("\" ")
                .append("method = \"post\">")
                .append("<input type=\"hidden\" name=\"id\" value=\"").append(userId).append("\"> ")
                .append("<input type=\"submit\" value=\"удалить\">")
                .append("</form>")
                .toString();
    }

    private String htmlButtonCreateUser(String contextPath) {
        return new StringBuilder()
                .append("<form action = \"").append(contextPath).append("/create").append("\" ")
                .append("method = \"get\">")
                .append("<input type=\"submit\" value=\"создать\">")
                .append("</form>")
                .toString();
    }

    /**
     * Returns handler for the "unknown" action. "Unknown" action is called if
     * no matching handler was found for the given action in the "handle" method.
     *
     * @return Handler for the "unknown" action.
     */
    private BiFunction<HttpServletRequest, HttpServletResponse, String> toUnknown() {
        return (req, resp) ->
                "Unknown action type. Did nothing.";
    }

    /**
     * Forms User object from the request.
     *
     * @param req Object that contains the request the client has made of the servlet.
     * @return User object formed from the request.
     */
    private User formUser(HttpServletRequest req) {
        return new User(
                req.getParameter("name"),
                req.getParameter("login"),
                req.getParameter("email"),
                System.currentTimeMillis()
        );
    }

    /**
     * Tries to get id from the request and returns if got it.
     *
     * @param req Object that contains the request the client has made of the servlet.
     * @return Id parsed from the request or <tt>-1</tt> if couldn't.
     */
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
