package ru.job4j.crud.servlets;

import org.junit.Before;
import org.junit.Test;
import ru.job4j.crud.logic.DatabaseValidator;
import ru.job4j.crud.logic.Validator;
import ru.job4j.crud.model.Credentials;
import ru.job4j.crud.model.Info;
import ru.job4j.crud.model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;
import static ru.job4j.crud.Constants.PARAM_USER_ID;
import static ru.job4j.crud.model.Credentials.Role.ADMIN;

public class DeleteUserServletTest {
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
    private DeleteUserServlet servlet = new DeleteUserServlet();
    /**
     * Validator servlet is working with.
     */
    private Validator<User> validator = DatabaseValidator.getInstance();

    @Before
    public void clearStorageAndSetCommonMocks() {
        this.validator.clear();
        when(this.request.getContextPath()).thenReturn(CONTEXT);
        when(this.request.getSession()).thenReturn(this.httpSession);
        when(this.request.getSession()).thenReturn(this.httpSession);
        when(this.request.getRequestDispatcher(anyString())).thenReturn(this.requestDispatcher);
    }

    /**
     * Test doGet()
     */
    @Test
    public void whenDeleteUserThenFindResultNullRedirect() throws IOException {
        int id = this.validator.add(this.userRoleAdmin);
        when(this.request.getParameter(PARAM_USER_ID.v())).thenReturn(Integer.toString(id));
        User before = this.validator.findById(id);
        this.servlet.doGet(this.request, this.response);
        User after = this.validator.findById(id);
        assertNotNull(before);
        assertNull(after);
        verify(this.response).sendRedirect(CONTEXT);
    }

}