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

    @Test
    public void whenCreateUserWithValidFieldsThenUserInStorage() throws IOException {
        // mock
        when(this.request.getContextPath()).thenReturn("stub");
        when(this.request.getParameter("name")).thenReturn("testerName");
        when(this.request.getParameter("login")).thenReturn("testerLogin");
        when(this.request.getParameter("password")).thenReturn("testerPassword");
        when(this.request.getParameter("email")).thenReturn("testerEmail@mail.com");
        when(this.request.getParameter("role")).thenReturn("USER");
        // do test
        this.servlet.doPost(this.request, this.response);
        User result = this.validator.findByCredentials("testerLogin", "testerPassword");
        // assert
        assertThat(result.getName(), is("testerName"));
        assertThat(result.getLogin(), is("testerLogin"));
        assertThat(result.getPassword(), is("testerPassword"));
        assertThat(result.getEmail(), is("testerEmail@mail.com"));
        assertThat(result.getRole(), is(Role.USER));
        verify(this.response).sendRedirect("stub");
    }

    @Test
    public void whenCreateUserWithWrongFieldsThenUserNotInStorage() throws IOException, ServletException {
        // mock
        when(this.request.getContextPath()).thenReturn("stub");
        when(this.request.getParameter("name")).thenReturn("testerName");
        when(this.request.getParameter("login")).thenReturn("testerLogin");
        when(this.request.getParameter("password")).thenReturn("testerPassword");
        when(this.request.getParameter("email")).thenReturn("testerEmail");          // invalid: no @ sign
        when(this.request.getParameter("role")).thenReturn("USER");
        // do test
        this.servlet.doPost(this.request, this.response);
        // assert
        verify(this.response).sendRedirect("stub?error=user CREATE failed");
    }

    @Test
    public void whenGetMethodThenRedirectToCreatePage() throws IOException, ServletException {
        when(this.request.getRequestDispatcher(anyString())).thenReturn(this.requestDispatcher);
        this.servlet.doGet(this.request, this.response);
        verify(this.request).getRequestDispatcher(String.join("/", servlet.getViewsDir(), "create.jsp"));

    }
}