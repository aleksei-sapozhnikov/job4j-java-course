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
import java.util.List;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static ru.job4j.crud.model.Role.ADMIN;
import static ru.job4j.crud.model.Role.USER;

public class ShowUsersServletTest {

    private ShowUsersServlet servlet = new ShowUsersServlet();

    private Validator<User> validator = DatabaseValidator.getInstance();

    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
    private final HttpSession httpSession = mock(HttpSession.class);

    private final User userRoleAdmin = new User("aName", "aLogin", "aPassword", "aEmail@mail.com", 123, ADMIN, "aCountry", "aCity");
    private final User userRoleUser = new User("uName", "uLogin", "uPassword", "uEmail@mail.com", 456, USER, "uCountry", "uCity");

    @Before
    public void initValidatorAndSetCommonMocks() {
        this.validator.clear();
        this.validator.add(this.userRoleAdmin);
        this.validator.add(this.userRoleUser);
        when(this.request.getContextPath()).thenReturn("contextPath");
        when(this.request.getSession()).thenReturn(this.httpSession);
        when(this.request.getSession()).thenReturn(this.httpSession);
        when(this.request.getRequestDispatcher(anyString())).thenReturn(this.requestDispatcher);
    }

    @Test
    public void whenDoGetThenAttachListOfUsersAndToListPage() throws IOException, ServletException {
        List<User> users = this.validator.findAll();
        // to make sure the URI in request dispatcher is right (if not - nullPointerException)
        when(this.request.getRequestDispatcher(anyString())).thenReturn(null);
        when(this.request.getRequestDispatcher(
                String.join("/", servlet.getViewsDir(), "list.jsp")
        )).thenReturn(this.requestDispatcher);
        //
        this.servlet.doGet(this.request, this.response);
        verify(this.request).setAttribute("users", users);
        verify(this.requestDispatcher).forward(this.request, this.response);
    }

}