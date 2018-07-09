package ru.job4j.crud.servlets;

import org.junit.Before;
import org.junit.Test;
import ru.job4j.crud.Role;
import ru.job4j.crud.User;
import ru.job4j.crud.logic.DatabaseValidator;
import ru.job4j.crud.logic.Validator;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.mockito.Mockito.*;

public class LogOutServletTest {

    private LogOutServlet servlet = new LogOutServlet();

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

    /**
     * Test doPost()
     */
    @Test
    public void whenDoPostThenInvalidateSession() throws IOException {
        when(this.request.getContextPath()).thenReturn("root");
        when(this.request.getSession()).thenReturn(this.httpSession);
        this.servlet.doPost(this.request, this.response);
        verify(this.httpSession).invalidate();
        verify(this.response).sendRedirect("root/login");
    }

}