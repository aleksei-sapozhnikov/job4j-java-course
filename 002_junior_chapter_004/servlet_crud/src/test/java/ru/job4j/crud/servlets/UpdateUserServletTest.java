package ru.job4j.crud.servlets;

import org.junit.Before;
import org.junit.Test;
import ru.job4j.crud.logic.DatabaseValidator;
import ru.job4j.crud.logic.Validator;
import ru.job4j.crud.model.Role;
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
import static ru.job4j.crud.model.Role.ADMIN;
import static ru.job4j.crud.model.Role.USER;

public class UpdateUserServletTest {

    private UpdateUserServlet servlet = new UpdateUserServlet();

    private Validator<User> validator = DatabaseValidator.getInstance();

    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
    private final HttpSession httpSession = mock(HttpSession.class);

    private final User oldUser = new User("old_uName", "old_uLogin", "old_uPassword", "old_uEmail@mail.com", 234, USER, "old_uCountry", "old_uCity");
    private final User newUser = new User("new_uName", "new_uLogin", "new_uPassword", "new_uEmail@mail.com", 567, USER, "new_uCountry", "new_uCity");

    private final User userEmailWrongFormat = new User("name", "login", "password", "email", 123, ADMIN, "country", "city");

    @Before
    public void initValidatorAndSetCommonMocks() {
        this.validator.clear();
        when(this.request.getContextPath()).thenReturn("contextPath");
        when(this.request.getSession()).thenReturn(this.httpSession);
        when(this.request.getSession()).thenReturn(this.httpSession);
        when(this.request.getRequestDispatcher(anyString())).thenReturn(this.requestDispatcher);
    }

    private void setMockForUserFields(int idToUpdate, User user) {
        when(this.request.getParameter("id")).thenReturn(Integer.toString(idToUpdate));
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
    public void whenUserUpdateSuccessfulThenUpdatedAndNoErrors() throws IOException, ServletException {
        int id = this.validator.add(this.oldUser);
        this.setMockForUserFields(id, this.newUser);
        this.servlet.doPost(this.request, this.response);
        User result = this.validator.findById(id);
        assertThat(result.getName(), is(this.newUser.getName()));
        assertThat(result.getLogin(), is(this.newUser.getLogin()));
        assertThat(result.getPassword(), is(this.newUser.getPassword()));
        assertThat(result.getEmail(), is(this.newUser.getEmail()));
        assertThat(result.getRole(), is(this.newUser.getRole()));
        assertThat(result.getCreated(), is(this.oldUser.getCreated()));    // create time shouldn't change
        verify(this.response).sendRedirect("contextPath");
    }

    @Test
    public void whenUserUpdateFailedThenRemainsOldAndErrorMessage() throws IOException, ServletException {
        int id = this.validator.add(this.oldUser);
        this.setMockForUserFields(id, this.userEmailWrongFormat);   // cannot update: wrong field
        this.servlet.doPost(this.request, this.response);
        User result = this.validator.findById(id);
        assertThat(result.getName(), is(this.oldUser.getName()));
        assertThat(result.getLogin(), is(this.oldUser.getLogin()));
        assertThat(result.getPassword(), is(this.oldUser.getPassword()));
        assertThat(result.getEmail(), is(this.oldUser.getEmail()));
        assertThat(result.getRole(), is(this.oldUser.getRole()));
        assertThat(result.getCreated(), is(this.oldUser.getCreated()));
        verify(this.request).setAttribute(eq("error"), anyString());
        verify(this.requestDispatcher).forward(request, response);
    }

    /**
     * Test doGet()
     */
    @Test
    public void whenDoGetThenForwardToUpdateFormWithAttributeObjects() throws IOException, ServletException {
        int id = this.validator.add(this.oldUser);
        when(this.request.getParameter("id")).thenReturn(Integer.toString(id));
        // to make sure the URI in request dispatcher is right (if not - nullPointerException)
        when(this.request.getRequestDispatcher(anyString())).thenReturn(null);
        when(this.request.getRequestDispatcher(
                String.join("/", servlet.getViewsDir(), "update.jsp")
        )).thenReturn(this.requestDispatcher);
        //
        this.servlet.doGet(this.request, this.response);
        verify(this.request).setAttribute("user", this.oldUser);
        verify(this.request).setAttribute("roles", Arrays.asList(Role.values()));
        verify(this.requestDispatcher).forward(this.request, this.response);
    }

}