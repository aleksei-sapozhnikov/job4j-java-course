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

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static ru.job4j.crud.Constants.*;
import static ru.job4j.crud.model.Credentials.Role.ADMIN;

public class LogInServletTest {
    /**
     * Context path for tests.
     */
    private static final String CONTEXT = "context";
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
    /**
     * Servlet to test.
     */
    private LogInServlet servlet = new LogInServlet();
    /**
     * Validator servlet is working with.
     */
    private Validator<User> validator = DatabaseValidator.getInstance();

    @Before
    public void initValidatorAndSetCommonMocks() {
        this.validator.clear();
        this.validator.add(this.userRoleAdmin);
        when(this.request.getContextPath()).thenReturn(CONTEXT);
        when(this.request.getSession()).thenReturn(this.httpSession);
        when(this.request.getSession()).thenReturn(this.httpSession);
        when(this.request.getRequestDispatcher(anyString())).thenReturn(this.requestDispatcher);
    }

    /**
     * Test doPost()
     */
    @Test
    public void whenStoreContainsGivenCredentialsThenAddsUserToSession() throws IOException, ServletException {
        String login = this.userRoleAdmin.getCredentials().getLogin();
        String password = this.userRoleAdmin.getCredentials().getPassword();
        User stored = this.validator.findByCredentials(login, password);
        when(this.request.getParameter(PARAM_USER_LOGIN.v())).thenReturn(login);
        when(this.request.getParameter(PARAM_USER_PASSWORD.v())).thenReturn(password);
        this.servlet.doPost(this.request, this.response);
        verify(httpSession).setAttribute(PARAM_LOGGED_USER.v(), stored);
        verify(this.response).sendRedirect(CONTEXT);
    }

    @Test
    public void whenWrongCredentialsThenReturnsErrorToDoGet() throws IOException, ServletException {
        when(this.request.getParameter(PARAM_USER_LOGIN.v())).thenReturn("someLoginNotInSystem");
        when(this.request.getParameter(PARAM_USER_PASSWORD.v())).thenReturn("somePasswordNotInSystem");
        this.servlet.doPost(this.request, this.response);
        verify(request).setAttribute(eq(PARAM_ERROR.v()), anyString());
        verify(this.requestDispatcher).forward(this.request, this.response);
    }

    /**
     * Test doGet()
     */
    @Test
    public void whenDoGetThenGoToLoginPage() throws IOException, ServletException {
        // must call the login page - so make NullPointerException if not
        when(this.request.getRequestDispatcher(anyString())).thenReturn(null);
        when(this.request.getRequestDispatcher(JSP_VIEWS_DIR.v().concat(JSP_LOGIN_PAGE.v())))
                .thenReturn(this.requestDispatcher);
        //
        this.servlet.doGet(this.request, this.response);
        verify(this.requestDispatcher).forward(this.request, this.response);
    }


}