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
import java.util.List;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class ShowUsersServletTest {

    private ShowUsersServlet servlet = new ShowUsersServlet();

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

    @Test
    public void whenDoGetThenAttachListOfUsersAndToListPage() throws IOException, ServletException {
        List<User> users = this.validator.findAll();
        when(this.request.getContextPath()).thenReturn("root");
        when(this.request.getSession()).thenReturn(this.httpSession);
        when(this.httpSession.getAttribute("user")).thenReturn(users.get(0));
        when(this.request.getRequestDispatcher(anyString())).thenReturn(this.requestDispatcher);
        this.servlet.doGet(this.request, this.response);
        verify(this.request).setAttribute("users", users);
        verify(this.request).setAttribute("user", users.get(0));
        verify(this.request).getRequestDispatcher(String.join("/", servlet.getViewsDir(), "list.jsp"));
    }

}