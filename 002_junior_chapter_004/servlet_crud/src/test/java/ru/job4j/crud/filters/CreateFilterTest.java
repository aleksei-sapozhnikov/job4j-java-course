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

public class CreateFilterTest {

    private CreateFilter filter = new CreateFilter();

    private HttpServletRequest request = mock(HttpServletRequest.class);
    private HttpServletResponse response = mock(HttpServletResponse.class);
    private FilterChain chain = mock(FilterChain.class);
    private RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
    private HttpSession httpSession = mock(HttpSession.class);

    private User user = new User("uName", "uLogin", "uPassword", "uEmail@mail.com", 123, Role.USER);
    private User admin = new User("aName", "aLogin", "aPassword", "aEmail@mail.com", 123, Role.ADMIN);

    /**
     * Test doFilter()
     */
    @Test
    public void whenAdminThenPass() throws IOException, ServletException {
        when(this.request.getContextPath()).thenReturn("root");
        when(this.request.getSession()).thenReturn(this.httpSession);
        when(this.httpSession.getAttribute("loggedUser")).thenReturn(this.admin);
        this.filter.doFilter(this.request, this.response, this.chain);
        verify(this.chain).doFilter(this.request, this.response);
    }

    @Test
    public void whenAUserThenRedirectToMainPageWithForbiddenError() throws IOException, ServletException {
        when(this.request.getContextPath()).thenReturn("root");
        when(this.request.getSession()).thenReturn(this.httpSession);
        when(this.httpSession.getAttribute("loggedUser")).thenReturn(this.user);
        when(this.request.getRequestDispatcher(anyString())).thenReturn(this.requestDispatcher);
        this.filter.doFilter(this.request, this.response, this.chain);
        verify(this.request).setAttribute(eq("error"), anyString());
        verify(this.requestDispatcher).forward(this.request, this.response);
    }

}