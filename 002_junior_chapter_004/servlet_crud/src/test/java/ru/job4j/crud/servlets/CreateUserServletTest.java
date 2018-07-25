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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static ru.job4j.crud.model.Info.Fields.*;
import static ru.job4j.crud.model.Role.ADMIN;

public class CreateUserServletTest {

    private Validator<User> validator = DatabaseValidator.getInstance();

    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
    private final HttpSession httpSession = mock(HttpSession.class);

    private final User userRoleAdmin = new User(new Credentials("aLogin", "aPassword", ADMIN), new Info("aName", "aEmail@mail.com", "aCountry", "aCity"));
    private final User userRoleUser = new User(new Credentials("uLogin", "uPassword", ADMIN), new Info("uName", "uEmail@mail.com", "uCountry", "uCity"));
    private final User userEmailWrongFormat = new User(new Credentials("login", "password", ADMIN), new Info("name", "email", "country", "city"));

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
        when(this.request.getParameter("login")).thenReturn(user.getCredentials().getLogin());
        when(this.request.getParameter("password")).thenReturn(user.getCredentials().getPassword());
        when(this.request.getParameter("role")).thenReturn(user.getCredentials().getRole().toString());
        when(this.request.getParameter("name")).thenReturn(user.getInfo().getField(NAME));
        when(this.request.getParameter("email")).thenReturn(user.getInfo().getField(EMAIL));
        when(this.request.getParameter("country")).thenReturn(user.getInfo().getField(COUNTRY));
        when(this.request.getParameter("city")).thenReturn(user.getInfo().getField(CITY));
    }

    /**
     * Test doPost()
     */
    @Test
    public void whenCreateUserWithValidFieldsThenUserInStorage() throws IOException, ServletException {
        this.setMockForUserFields(this.userRoleAdmin);
        this.servlet.doPost(this.request, this.response);
        User result = this.validator.findByCredentials(
                this.userRoleAdmin.getCredentials().getLogin(), this.userRoleAdmin.getCredentials().getPassword()
        );
        assertThat(result, is(this.userRoleAdmin));
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