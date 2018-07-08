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
import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;


public class DeleteUserServletTest {

    private DeleteUserServlet servlet = new DeleteUserServlet();

    private Validator<User> validator = DatabaseValidator.getInstance();

    private HttpServletRequest request = mock(HttpServletRequest.class);
    private HttpServletResponse response = mock(HttpServletResponse.class);
    private RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

    @Before
    public void initValidator() {
        this.validator.clear();
    }

    /**
     * Test doPost()
     */
    @Test
    public void whenDeleteUserThenFindResultNullRedirect() throws IOException {
        User add = new User("name", "login", "password", "e@mail.com", 12, Role.ADMIN);
        int id = this.validator.add(add);
        when(this.request.getContextPath()).thenReturn("root");
        when(this.request.getParameter("id")).thenReturn(Integer.toString(id));
        User beforeDelete = this.validator.findById(id);
        this.servlet.doPost(this.request, this.response);
        User afterDelete = this.validator.findById(id);
        assertNotNull(beforeDelete);
        assertNull(afterDelete);
        verify(this.response).sendRedirect("root");
    }

}