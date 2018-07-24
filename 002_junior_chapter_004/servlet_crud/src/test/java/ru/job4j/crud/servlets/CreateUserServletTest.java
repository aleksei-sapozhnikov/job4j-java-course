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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static ru.job4j.crud.model.Role.ADMIN;
import static ru.job4j.crud.model.Role.USER;

public class CreateUserServletTest {

    private Validator<User> validator = DatabaseValidator.getInstance();

    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
    private final HttpSession httpSession = mock(HttpSession.class);
    private final User userRoleAdmin = new User("aName", "aLogin", "aPassword", "aEmail@mail.com", 123, ADMIN, "aCountry", "aCity");
    private final User userRoleUser = new User("uName", "uLogin", "uPassword", "uEmail@mail.com", 456, USER, "uCountry", "uCity");
    private final User userEmailWrongFormat = new User("name", "login", "password", "email", 123, ADMIN, "country", "city");
    private CreateUserServlet servlet = new CreateUserServlet();

    @Before
    public void clearStorageAndSetCommonMocks() {
        this.validator.clear();
        when(this.request.getContextPath()).thenReturn("contextPath");
        when(this.request.getSession()).thenReturn(this.httpSession);
        when(this.request.getSession()).thenReturn(this.httpSession);
        when(this.request.getRequestDispatcher(anyString())).thenReturn(this.requestDispatcher);
    }

    private void setMockForUserFields(User user) {
        when(this.request.getParameter("name")).thenReturn(user.getName());
        when(this.request.getParameter("login")).thenReturn(user.getLogin());
        when(this.request.getParameter("password")).thenReturn(user.getPassword());
        when(this.request.getParameter("email")).thenReturn(user.getEmail());
        when(this.request.getParameter("role")).thenReturn(user.getRole().toString());
        when(this.request.getParameter("country")).thenReturn(user.getCountry());
        when(this.request.getParameter("city")).thenReturn(user.getCity());
    }

    /**
     * Test doPost()
     */
    @Test
    public void whenCreateUserWithValidFieldsThenUserInStorage() throws IOException, ServletException {
        this.setMockForUserFields(this.userRoleAdmin);
        this.servlet.doPost(this.request, this.response);
        User result = this.validator.findByCredentials(
                this.userRoleAdmin.getLogin(), this.userRoleAdmin.getPassword()
        );
        // "created" field different - so compare by fields
        assertThat(result.getName(), is(this.userRoleAdmin.getName()));
        assertThat(result.getLogin(), is(this.userRoleAdmin.getLogin()));
        assertThat(result.getPassword(), is(this.userRoleAdmin.getPassword()));
        assertThat(result.getEmail(), is(this.userRoleAdmin.getEmail()));
        assertThat(result.getRole(), is(this.userRoleAdmin.getRole()));
        assertThat(result.getCountry(), is(this.userRoleAdmin.getCountry()));
        assertThat(result.getCity(), is(this.userRoleAdmin.getCity()));
        verify(this.response).sendRedirect("contextPath");
    }

    @Test
    public void whenCreateUserWithWrongFieldsThenUserNotInStorage() throws IOException, ServletException {
        this.setMockForUserFields(this.userEmailWrongFormat);
        this.servlet.doPost(this.request, this.response);
        verify(this.request).setAttribute(eq("error"), anyString());
        verify(this.requestDispatcher).forward(this.request, this.response);
    }

    /**
     * Test doGet()
     */
    @Test
    public void whenGetMethodThenRedirectToCreatePage() throws IOException, ServletException {
        this.servlet.doGet(this.request, this.response);
        verify(this.request).getRequestDispatcher(String.join("/", servlet.getViewsDir(), "create.jsp"));

    }
}