package ru.job4j.crud.servlets;

import org.junit.Before;
import org.junit.Test;
import ru.job4j.crud.logic.DatabaseValidator;
import ru.job4j.crud.logic.Validator;
import ru.job4j.crud.model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static ru.job4j.crud.model.Role.ADMIN;

public class LogInServletTest {

    private LogInServlet servlet = new LogInServlet();

    private Validator<User> validator = DatabaseValidator.getInstance();

    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
    private final HttpSession httpSession = mock(HttpSession.class);

    private final User userRoleAdmin = new User("aName", "aLogin", "aPassword", "aEmail@mail.com", 123, ADMIN, "aCountry", "aCity");

    @Before
    public void initValidatorAndSetCommonMocks() {
        this.validator.clear();
        this.validator.add(this.userRoleAdmin);
        when(this.request.getContextPath()).thenReturn("contextPath");
        when(this.request.getSession()).thenReturn(this.httpSession);
        when(this.request.getSession()).thenReturn(this.httpSession);
        when(this.request.getRequestDispatcher(anyString())).thenReturn(this.requestDispatcher);
    }

    /**
     * Test doPost()
     */
    @Test
    public void whenStoreContainsGivenCredentialsThenAddsUserToSession() throws IOException, ServletException {
        String login = this.userRoleAdmin.getLogin();
        String password = this.userRoleAdmin.getPassword();
        User stored = this.validator.findByCredentials(login, password);
        when(this.request.getParameter("login")).thenReturn(login);
        when(this.request.getParameter("password")).thenReturn(password);
        this.servlet.doPost(this.request, this.response);
        verify(httpSession).setAttribute("loggedUser", stored);
        verify(this.response).sendRedirect("contextPath");
    }

    @Test
    public void whenWrongCredentialsThenReturnsErrorToDoGet() throws IOException, ServletException {
        when(this.request.getParameter("login")).thenReturn("wrong_login");
        when(this.request.getParameter("password")).thenReturn("wrong_password");
        this.servlet.doPost(this.request, this.response);
        verify(request).setAttribute("error", "Invalid credentials");
        verify(this.requestDispatcher).forward(this.request, this.response);
    }

    /**
     * Test doGet()
     */
    @Test
    public void whenDoGetThenGoToLoginPage() throws IOException, ServletException {
        // must call the login page
        when(this.request.getRequestDispatcher(
                String.join("/", servlet.getViewsDir(), "login.jsp"))
        ).thenReturn(
                this.requestDispatcher
        );
        this.servlet.doGet(this.request, this.response);
        verify(this.requestDispatcher).forward(this.request, this.response);
    }


}