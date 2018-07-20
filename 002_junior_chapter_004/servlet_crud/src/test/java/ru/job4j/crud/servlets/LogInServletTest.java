package ru.job4j.crud.servlets;

import org.junit.Before;
import org.junit.Test;
import ru.job4j.crud.Role;
import ru.job4j.crud.User;
import ru.job4j.crud.logic.DatabaseValidator;
import ru.job4j.crud.logic.Validator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class LogInServletTest {

    private LogInServlet servlet = new LogInServlet();

    private Validator<User> validator = DatabaseValidator.getInstance();

    private HttpServletRequest request = mock(HttpServletRequest.class);
    private HttpServletResponse response = mock(HttpServletResponse.class);
    private RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
    private HttpSession httpSession = mock(HttpSession.class);

    @Before
    public void initValidator() {
        this.validator.clear();
        this.validator.add(new User("name_1", "login_1", "password_1", "email_1_@mail.com", 123, Role.USER));
        this.validator.add(new User("name_2", "login_2", "password_2", "email_2_@mail.com", 3432, Role.ADMIN));
    }

    /**
     * Test doPost()
     */
    @Test
    public void whenStoreContainsGivenCredentialsThenAddsUserToSession() throws IOException, ServletException {
        when(this.request.getContextPath()).thenReturn("root");
        when(this.request.getParameter("login")).thenReturn("login_1");
        when(this.request.getParameter("password")).thenReturn("password_1");
        when(this.request.getSession()).thenReturn(this.httpSession);
        User user = this.validator.findByCredentials("login_1", "password_1");
        this.servlet.doPost(this.request, this.response);
        verify(httpSession).setAttribute("loggedUser", user);
        verify(this.response).sendRedirect("root");
    }

    @Test
    public void whenWrongCredentialsThenReturnsErrorToDoGet() throws IOException, ServletException {
        when(this.request.getContextPath()).thenReturn("root");
        when(this.request.getParameter("login")).thenReturn("wrong_login");
        when(this.request.getParameter("password")).thenReturn("wrong_password");
        when(this.request.getRequestDispatcher(anyString())).thenReturn(this.requestDispatcher);
        this.servlet.doPost(this.request, this.response);
        verify(request).setAttribute("error", "Invalid credentials");
        verify(this.requestDispatcher).forward(this.request, this.response);
    }

    /**
     * Test doGet()
     */
    @Test
    public void whenDoGetThenGoToLoginPage() throws IOException, ServletException {
        when(this.request.getRequestDispatcher(String.join("/", servlet.getViewsDir(), "login.jsp"))).thenReturn(this.requestDispatcher);
        this.servlet.doGet(this.request, this.response);
        verify(this.requestDispatcher).forward(this.request, this.response);
    }


}