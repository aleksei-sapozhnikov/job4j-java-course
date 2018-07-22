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

public class UpdateDeleteFilterTest {

    private UpdateDeleteFilter filter = new UpdateDeleteFilter();

    private HttpServletRequest request = mock(HttpServletRequest.class);
    private HttpServletResponse response = mock(HttpServletResponse.class);
    private FilterChain chain = mock(FilterChain.class);
    private RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
    private HttpSession httpSession = mock(HttpSession.class);

    private User user = new User(1, "uName", "uLogin", "uPassword", "uEmail@mail.com", 123, Role.USER, "uCountry", "uCity");
    private User admin = new User(2, "aName", "aLogin", "aPassword", "aEmail@mail.com", 123, Role.ADMIN, "aCountry", "aCity");

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
    public void whenUserWithIdEqualToUpdateIdThenPass() throws IOException, ServletException {
        when(this.request.getContextPath()).thenReturn("root");
        when(this.request.getSession()).thenReturn(this.httpSession);
        when(this.httpSession.getAttribute("loggedUser")).thenReturn(this.user);
        when(this.request.getParameter("id")).thenReturn(Integer.toString(this.user.getId()));
        when(this.request.getRequestDispatcher(anyString())).thenReturn(this.requestDispatcher);
        this.filter.doFilter(this.request, this.response, this.chain);
        verify(this.chain).doFilter(this.request, this.response);
    }

    @Test
    public void whenUserWithIdNotEqualToUpdateIdThenError() throws IOException, ServletException {
        when(this.request.getContextPath()).thenReturn("root");
        when(this.request.getSession()).thenReturn(this.httpSession);
        when(this.httpSession.getAttribute("loggedUser")).thenReturn(this.user);
        when(this.request.getParameter("id")).thenReturn(Integer.toString(this.user.getId() + 1242)); // another id
        when(this.request.getRequestDispatcher(anyString())).thenReturn(this.requestDispatcher);
        this.filter.doFilter(this.request, this.response, this.chain);
        verify(this.request).setAttribute(eq("error"), anyString());
        verify(this.requestDispatcher).forward(request, response);
    }


}