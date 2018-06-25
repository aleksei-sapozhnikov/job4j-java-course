package ru.job4j.crud;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServlet;

public class AbstractUserServlet extends HttpServlet {
    private static final Logger LOG = LogManager.getLogger(AbstractUserServlet.class);
    /**
     * Logger.
     */
    protected final Validator<User> logic;
    /**
     * Actions dispatch.
     */
    protected final ActionsDispatch dispatch;

    /**
     * Constructor to initiate needed fields.
     *
     * @param logic Logic layer class object.
     */
    protected AbstractUserServlet(Validator<User> logic) {
        this.logic = logic;
        this.dispatch = new ActionsDispatch(this.logic).init();
    }

    /**
     * Is called when servlet stops working.
     * Closes connection to database.
     */
    @Override
    public void destroy() {
        try {
            this.logic.close();
        } catch (Exception e) {
            LOG.error(e.getStackTrace());
        }
    }
}
