package ru.job4j.crud.servlets;

import org.junit.Before;
import org.junit.Test;
import ru.job4j.crud.Role;
import ru.job4j.crud.User;
import ru.job4j.crud.logic.DatabaseValidator;
import ru.job4j.crud.logic.Validator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CreateUserServletTest {

    private Validator<User> validator = DatabaseValidator.getInstance();

    @Before
    public void initValidator() {
        this.validator.clear();
        this.validator.add(
                new User("admin", "login", "password", "email@com.ru", 434, Role.ADMIN)
        );
    }

    @Test
    public void whenCreateUserThenUserInStorage() throws IOException {
        CreateUserServlet servlet = new CreateUserServlet();
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        when(req.getParameter("name")).thenReturn("testerName");
        when(req.getParameter("login")).thenReturn("testerLogin");
        when(req.getParameter("password")).thenReturn("testerPassword");
        when(req.getParameter("email")).thenReturn("testerEmail@mail.com");
        when(req.getParameter("role")).thenReturn("USER");
        servlet.doPost(req, resp);
        // after adding
        User result = validator.findByCredentials("testerLogin", "testerPassword");
        assertThat(result.getName(), is("testerName"));
        assertThat(result.getLogin(), is("testerLogin"));
        assertThat(result.getPassword(), is("testerPassword"));
        assertThat(result.getEmail(), is("testerEmail@mail.com"));
        assertThat(result.getRole(), is(Role.USER));
    }
}