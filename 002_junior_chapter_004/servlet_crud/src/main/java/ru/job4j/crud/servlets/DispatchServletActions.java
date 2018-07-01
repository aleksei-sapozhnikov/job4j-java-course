package ru.job4j.crud.servlets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.crud.User;
import ru.job4j.crud.logic.Validator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Class dispatching and performing all operations in logic layer.
 * <p>
 * Irs functions return String result which is to be included into
 * html page going to user.
 */
public class DispatchServletActions {
    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger(DispatchServletActions.class);
    /**
     * Logic layer class validating and adding/updating/deleting users.
     */
    private final Validator<User> logic;
    /**
     * Map of possible actions.
     */
    private Map<String, BiFunction<HttpServletRequest, HttpServletResponse, Boolean>> dispatch = new HashMap<>();

    /**
     * Constructs new instance.
     *
     * @param logic Logic layer object to perform operations on.
     */
    public DispatchServletActions(Validator<User> logic) {
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
    public Boolean handle(final String action, final HttpServletRequest req, final HttpServletResponse resp) {
        return this.dispatch.getOrDefault(action, this.toUnknown()).apply(req, resp);
    }

    /**
     * Initiates dispacth and returns its initiated object.
     *
     * @return Initiated dispatch object.
     */
    public DispatchServletActions init() {
        this.load("create", this.toCreate());
        this.load("update", this.toUpdate());
        this.load("delete", this.toDelete());
        return this;
    }

    /**
     * Loads given action to the "action-handler" map.
     *
     * @param action  Given action.
     * @param handler Action handler.
     */
    private void load(String action, BiFunction<HttpServletRequest, HttpServletResponse, Boolean> handler) {
        this.dispatch.put(action, handler);
    }

    /**
     * Returns handler for the "create" action.
     *
     * @return Handler for the "create" action.
     */
    private BiFunction<HttpServletRequest, HttpServletResponse, Boolean> toCreate() {
        return (req, resp) -> {
            User add = this.formUser(req);
            int id = logic.add(add);
            return id != -1;
        };
    }

    /**
     * Returns handler for the "update" action.
     *
     * @return Handler for the "update" action.
     */
    private BiFunction<HttpServletRequest, HttpServletResponse, Boolean> toUpdate() {
        return (req, resp) -> {
            boolean result = false;
            int id = this.getId(req);
            if (id != -1) {
                User updater = this.formUser(req);
                result = logic.update(id, updater);
            }
            return result;
        };
    }

    /**
     * Returns handler for the "delete" action.
     *
     * @return Handler for the "delete" action.
     */
    private BiFunction<HttpServletRequest, HttpServletResponse, Boolean> toDelete() {
        return (req, resp) -> {
            boolean result = false;
            int id = this.getId(req);
            if (id != -1) {
                result = logic.delete(id) != null;
            }
            return result;
        };
    }

    /**
     * Returns handler for the "unknown" action. "Unknown" action is called if
     * no matching handler was found for the given action in the "handle" method.
     *
     * @return Handler for the "unknown" action.
     */
    private BiFunction<HttpServletRequest, HttpServletResponse, Boolean> toUnknown() {
        return (req, resp) -> {
            LOG.error("Unknown action type. Did nothing.");
            return false;
        };
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
