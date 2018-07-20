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
import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class CreateUserServletTest {

    private CreateUserServlet servlet = new CreateUserServlet();

    private Validator<User> validator = DatabaseValidator.getInstance();

    private HttpServletRequest request = mock(HttpServletRequest.class);
    private HttpServletResponse response = mock(HttpServletResponse.class);
    private RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

    @Before
    public void initValidator() {
        this.validator.clear();
    }

    /**
     * Test doPost()
     */
    @Test
    public void whenCreateUserWithValidFieldsThenUserInStorage() throws IOException, ServletException {
        User create = new User("newName", "newLogin", "newPassword", "newEmail@mail.com", 123, Role.USER);
        when(this.request.getContextPath()).thenReturn("root");
        when(this.request.getParameter("name")).thenReturn(create.getName());
        when(this.request.getParameter("login")).thenReturn(create.getLogin());
        when(this.request.getParameter("password")).thenReturn(create.getPassword());
        when(this.request.getParameter("email")).thenReturn(create.getEmail());
        when(this.request.getParameter("role")).thenReturn(create.getRole().toString());
        this.servlet.doPost(this.request, this.response);
        User result = this.validator.findByCredentials(create.getLogin(), create.getPassword());
        assertThat(result.getName(), is(create.getName()));
        assertThat(result.getLogin(), is(create.getLogin()));
        assertThat(result.getPassword(), is(create.getPassword()));
        assertThat(result.getEmail(), is(create.getEmail()));
        assertThat(result.getRole(), is(create.getRole()));
        verify(this.response).sendRedirect("root");
    }

    @Test
    public void whenCreateUserWithWrongFieldsThenUserNotInStorage() throws IOException, ServletException {
        // user with wrong email: no '@' sign
        User wrong = new User("newName", "newLogin", "newPassword", "newEmail", 123, Role.USER);
        when(this.request.getContextPath()).thenReturn("root");
        when(this.request.getParameter("name")).thenReturn(wrong.getName());
        when(this.request.getParameter("login")).thenReturn(wrong.getLogin());
        when(this.request.getParameter("password")).thenReturn(wrong.getPassword());
        when(this.request.getParameter("email")).thenReturn(wrong.getEmail());
        when(this.request.getParameter("role")).thenReturn(wrong.getRole().toString());
        when(this.request.getRequestDispatcher(anyString())).thenReturn(this.requestDispatcher);
        this.servlet.doPost(this.request, this.response);
        verify(this.request).setAttribute(eq("error"), anyString());
        verify(this.requestDispatcher).forward(this.request, this.response);
    }

    /**
     * Test doGet()
     */
    @Test
    public void whenGetMethodThenRedirectToCreatePage() throws IOException, ServletException {
        when(this.request.getRequestDispatcher(anyString())).thenReturn(this.requestDispatcher);
        this.servlet.doGet(this.request, this.response);
        verify(this.request).getRequestDispatcher(String.join("/", servlet.getViewsDir(), "create.jsp"));

    }
}