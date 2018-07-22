package ru.job4j.crud.filters;


import org.junit.Test;
import ru.job4j.crud.Role;
import ru.job4j.crud.User;

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
    public void whenHaveUserInSessionThenPass() throws IOException, ServletException {
        User user = new User("stub", "stub", "stub", "stub@email.com", 123, Role.USER, "stub", "stub");
        when(this.request.getRequestURI()).thenReturn("root/some_address");
        when(this.request.getSession()).thenReturn(this.httpSession);
        when(this.httpSession.getAttribute("loggedUser")).thenReturn(user);
        this.filter.doFilter(this.request, this.response, this.chain);
        verify(this.chain).doFilter(this.request, this.response);
    }

    @Test
    public void whenNoUserInSessionThenRedirectToLogin() throws IOException, ServletException {
        when(this.request.getContextPath()).thenReturn("root");
        when(this.request.getRequestURI()).thenReturn("root/some_address");
        when(this.request.getSession()).thenReturn(this.httpSession);
        when(this.httpSession.getAttribute("user")).thenReturn(null);
        this.filter.doFilter(this.request, this.response, this.chain);
        verify(this.response).sendRedirect("root/login");
    }

}