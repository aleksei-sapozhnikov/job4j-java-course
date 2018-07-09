package ru.job4j.crud.filters;


import org.junit.Test;
import ru.job4j.crud.User;
import ru.job4j.crud.logic.DatabaseValidator;
import ru.job4j.crud.logic.Validator;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.mockito.Mockito.*;


public class LoginFilterTest {

    private LoginFilter filter = new LoginFilter();

    private Validator<User> validator = DatabaseValidator.getInstance();

    private HttpServletRequest request = mock(HttpServletRequest.class);
    private HttpServletResponse response = mock(HttpServletResponse.class);
    private FilterChain chain = mock(FilterChain.class);
    private RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
    private HttpSession httpSession = mock(HttpSession.class);

    /**
     * Test doFilter()
     */
    @Test
    public void whenOnLoginPageThenPass() throws IOException, ServletException {
        when(this.request.getRequestURI()).thenReturn("root/login");
        this.filter.doFilter(this.request, this.response, this.chain);
        verify(this.chain).doFilter(this.request, this.response);
    }

    @Test
    public void whenHaveUserInSessionThenPass() {
        when(this.request.getRequestURI().contains("/login")).thenReturn(true);

    }

}