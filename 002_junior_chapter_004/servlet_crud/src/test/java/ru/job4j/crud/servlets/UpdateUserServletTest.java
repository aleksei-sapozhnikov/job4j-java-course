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
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class UpdateUserServletTest {

    private UpdateUserServlet servlet = new UpdateUserServlet();

    private Validator<User> validator = DatabaseValidator.getInstance();

    private HttpServletRequest request = mock(HttpServletRequest.class);
    private HttpServletResponse response = mock(HttpServletResponse.class);
    private RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

    @Before
    public void initValidator() {
        this.validator.clear();
    }

    @Test
    public void whenUserUpdateSuccessfulThenUpdatedAndNoErrors() throws IOException {
        User older = new User("oldName", "oldLogin", "oldPassword", "oldE@mail.com", 12, Role.USER);
        User newer = new User("newName", "newLogin", "newPassword", "newE@mail.com", 11232, Role.USER);
        int id = this.validator.add(older);
        when(this.request.getParameter("id")).thenReturn(Integer.toString(id));
        when(this.request.getParameter("name")).thenReturn(newer.getName());
        when(this.request.getParameter("login")).thenReturn(newer.getLogin());
        when(this.request.getParameter("password")).thenReturn(newer.getPassword());
        when(this.request.getParameter("email")).thenReturn(newer.getEmail());
        when(this.request.getParameter("role")).thenReturn(newer.getRole().toString());
        when(this.request.getContextPath()).thenReturn("stub");
        this.servlet.doPost(this.request, this.response);
        User result = this.validator.findById(id);
        assertThat(result.getName(), is(newer.getName()));
        assertThat(result.getLogin(), is(newer.getLogin()));
        assertThat(result.getPassword(), is(newer.getPassword()));
        assertThat(result.getEmail(), is(newer.getEmail()));
        assertThat(result.getRole(), is(newer.getRole()));
        assertThat(result.getCreated(), is(older.getCreated()));    // create time shouldn't change
        verify(this.response).sendRedirect("stub");
    }

    @Test
    public void whenUserUpdateFailedThenRemainsOldAndErrorMessage() throws IOException {
        User older = new User("oldName", "oldLogin", "oldPassword", "oldE@mail.com", 12, Role.USER);
        User newer = new User("newName", "", "newPassword", "newE@mail.com", 11232, Role.USER); // invalid login
        int id = this.validator.add(older);
        when(this.request.getParameter("id")).thenReturn(Integer.toString(id));
        when(this.request.getParameter("name")).thenReturn(newer.getName());
        when(this.request.getParameter("login")).thenReturn(newer.getLogin());
        when(this.request.getParameter("password")).thenReturn(newer.getPassword());
        when(this.request.getParameter("email")).thenReturn(newer.getEmail());
        when(this.request.getParameter("role")).thenReturn(newer.getRole().toString());
        when(this.request.getContextPath()).thenReturn("stub");
        this.servlet.doPost(this.request, this.response);
        User result = this.validator.findById(id);
        assertThat(result.getName(), is(older.getName()));
        assertThat(result.getLogin(), is(older.getLogin()));
        assertThat(result.getPassword(), is(older.getPassword()));
        assertThat(result.getEmail(), is(older.getEmail()));
        assertThat(result.getRole(), is(older.getRole()));
        assertThat(result.getCreated(), is(older.getCreated()));
        verify(this.response).sendRedirect("stub?error=user UPDATE failed");
    }

    @Test
    public void whenDoGetThenForwardToUpdateFormWithAttributeObjects() throws IOException, ServletException {
        User older = new User("oldName", "oldLogin", "oldPassword", "oldE@mail.com", 12, Role.USER);
        int id = this.validator.add(older);
        when(this.request.getParameter("id")).thenReturn(Integer.toString(id));
        when(this.request.getRequestDispatcher(anyString())).thenReturn(this.requestDispatcher);
        this.servlet.doGet(this.request, this.response);
        Role[] roles = Role.values();
        verify(this.request).setAttribute("user", older);
        verify(this.request).setAttribute("roles", Arrays.asList(roles));
        verify(this.request).getRequestDispatcher(String.join("/", servlet.getViewsDir(), "update.jsp"));
    }

}