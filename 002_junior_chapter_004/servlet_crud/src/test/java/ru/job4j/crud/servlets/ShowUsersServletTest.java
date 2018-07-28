package ru.job4j.crud.servlets;

import org.junit.Before;
import org.junit.Test;
import ru.job4j.crud.logic.DatabaseValidator;
import ru.job4j.crud.logic.Validator;
import ru.job4j.crud.model.Credentials;
import ru.job4j.crud.model.Info;
import ru.job4j.crud.model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static ru.job4j.crud.Constants.*;
import static ru.job4j.crud.model.Credentials.Role.ADMIN;

public class ShowUsersServletTest {
    /**
     * Context path for tests.
     */
    private static String CONTEXT = "context";
    /**
     * Mocks.
     */
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
    private final HttpSession httpSession = mock(HttpSession.class);
    /**
     * Users to use.
     */
    private final User userRoleAdmin = new User(new Credentials("aLogin", "aPassword", ADMIN), new Info("aName", "aEmail@mail.com", "aCountry", "aCity"));
    private final User userRoleUser = new User(new Credentials("uLogin", "uPassword", ADMIN), new Info("uName", "uEmail@mail.com", "uCountry", "uCity"));
    /**
     * Servlet to test.
     */
    private ShowUsersServlet servlet = new ShowUsersServlet();
    /**
     * Validator servlet is working with.
     */
    private Validator<User> validator = DatabaseValidator.getInstance();

    @Before
    public void initValidatorAndSetCommonMocks() {
        this.validator.clear();
        this.validator.add(this.userRoleAdmin);
        this.validator.add(this.userRoleUser);
        when(this.request.getContextPath()).thenReturn(CONTEXT);
        when(this.request.getSession()).thenReturn(this.httpSession);
        when(this.request.getSession()).thenReturn(this.httpSession);
        when(this.request.getRequestDispatcher(anyString())).thenReturn(this.requestDispatcher);
    }

    @Test
    public void whenDoGetThenAttachListOfUsersAndToListPage() throws IOException, ServletException {
        List<User> users = this.validator.findAll();
        // must call list users page - otherwise make NullPointerException
        when(this.request.getRequestDispatcher(anyString())).thenReturn(null);
        when(this.request.getRequestDispatcher(JSP_VIEWS_DIR.v().concat(JSP_LIST_USERS.v())))
                .thenReturn(this.requestDispatcher);
        //
        this.servlet.doGet(this.request, this.response);
        verify(this.request).setAttribute(PARAM_ALL_USERS.v(), users);
        verify(this.requestDispatcher).forward(this.request, this.response);
    }

}