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
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static ru.job4j.crud.Constants.*;
import static ru.job4j.crud.model.Credentials.Role.ADMIN;
import static ru.job4j.crud.model.Credentials.Role.USER;

public class UpdateUserServletTest {
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
    private final User loggedUser = new User(new Credentials("logged_uLogin", "logged_uPassword", USER), new Info("logged_uName", "logged_uEmail@mail.com", "logged_uCountry", "logged_uCity"));
    private final User oldUser = new User(new Credentials("old_uLogin", "old_uPassword", USER), new Info("old_uName", "old_uEmail@mail.com", "old_uCountry", "old_uCity"));
    private final User newUser = new User(new Credentials("new_uLogin", "new_uPassword", USER), new Info("new_uName", "new_uEmail@mail.com", "new_uCountry", "new_uCity"));
    private final User userEmailWrongFormat = new User(new Credentials("login", "password", ADMIN), new Info("name", "email", "country", "city"));
    /**
     * Servlet to test.
     */
    private UpdateUserServlet servlet = new UpdateUserServlet();
    /**
     * Validator servlet is working with.
     */
    private Validator<User> validator = DatabaseValidator.getInstance();

    @Before
    public void initValidatorAndSetCommonMocks() {
        this.validator.clear();
        when(this.request.getContextPath()).thenReturn(CONTEXT);
        when(this.request.getSession()).thenReturn(this.httpSession);
        when(this.request.getSession()).thenReturn(this.httpSession);
        when(this.request.getRequestDispatcher(anyString())).thenReturn(this.requestDispatcher);
    }

    private void setMockForUserFields(int idLogged, int idToUpdate, User user) {
        when(this.request.getParameter(PARAM_LOGGED_USER_ID.v())).thenReturn(Integer.toString(idLogged));
        when(this.request.getParameter(PARAM_USER_ID.v())).thenReturn(Integer.toString(idToUpdate));
        when(this.request.getParameter(PARAM_USER_LOGIN.v())).thenReturn(user.getCredentials().getLogin());
        when(this.request.getParameter(PARAM_USER_PASSWORD.v())).thenReturn(user.getCredentials().getPassword());
        when(this.request.getParameter(PARAM_USER_ROLE.v())).thenReturn(user.getCredentials().getRole().toString());
        when(this.request.getParameter(PARAM_USER_NAME.v())).thenReturn(user.getInfo().getName());
        when(this.request.getParameter(PARAM_USER_EMAIL.v())).thenReturn(user.getInfo().getEmail());
        when(this.request.getParameter(PARAM_USER_COUNTRY.v())).thenReturn(user.getInfo().getCountry());
        when(this.request.getParameter(PARAM_USER_CITY.v())).thenReturn(user.getInfo().getCity());
    }

    /**
     * Test doPost()
     */
    @Test
    public void whenUserUpdateSuccessfulThenUpdatedAndNoErrors() throws IOException, ServletException {
        int loggedId = this.validator.add(this.loggedUser);
        int id = this.validator.add(this.oldUser);
        this.setMockForUserFields(loggedId, id, this.newUser);
        this.servlet.doPost(this.request, this.response);
        assertThat(this.validator.findById(id), is(this.newUser));
        verify(this.response).sendRedirect(CONTEXT);
    }

    @Test
    public void whenUserUpdateFailedThenRemainsOldAndErrorMessage() throws IOException, ServletException {
        int loggedId = this.validator.add(this.loggedUser);
        int id = this.validator.add(this.oldUser);
        this.setMockForUserFields(loggedId, id, this.userEmailWrongFormat);
        this.servlet.doPost(this.request, this.response);
        assertThat(this.validator.findById(id), is(this.oldUser));
        verify(this.request).setAttribute(eq(PARAM_ERROR.v()), anyString());
        verify(this.requestDispatcher).forward(request, response);
    }

    /**
     * Test doGet()
     */
    @Test
    public void whenDoGetThenForwardToUpdateFormWithAttributeObjects() throws IOException, ServletException {
        int id = this.validator.add(this.oldUser);
        when(this.request.getParameter(PARAM_USER_ID.v())).thenReturn(Integer.toString(id));
        // to make sure the URI in request dispatcher is right (if not - nullPointerException)
        when(this.request.getRequestDispatcher(anyString())).thenReturn(null);
        when(this.request.getRequestDispatcher(JSP_VIEWS_DIR.v().concat(JSP_UPDATE_USER.v())))
                .thenReturn(this.requestDispatcher);
        //
        this.servlet.doGet(this.request, this.response);
        verify(this.request).setAttribute(PARAM_USER.v(), this.oldUser);
        verify(this.request).setAttribute(PARAM_ALL_ROLES.v(), Arrays.asList(Credentials.Role.values()));
        verify(this.requestDispatcher).forward(this.request, this.response);
    }

}