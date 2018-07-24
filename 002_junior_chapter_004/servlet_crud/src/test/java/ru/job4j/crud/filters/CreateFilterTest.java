package ru.job4j.crud.filters;

import org.junit.Before;
import org.junit.Test;
import ru.job4j.crud.model.Role;
import ru.job4j.crud.model.User;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.mockito.Mockito.*;

public class CreateFilterTest {

    private final CreateFilter filter = new CreateFilter();

    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final FilterChain chain = mock(FilterChain.class);
    private final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
    private final HttpSession httpSession = mock(HttpSession.class);

    private final User userRoleAdmin = new User("aName", "aLogin", "aPassword", "aEmail@mail.com", 123, Role.ADMIN, "aCountry", "aCity");
    private final User userRoleUser = new User("uName", "uLogin", "uPassword", "uEmail@mail.com", 456, Role.USER, "uCountry", "uCity");

    @Before
    public void setCommonMocks() {
        when(this.request.getContextPath()).thenReturn("contextPath");
        when(this.request.getSession()).thenReturn(this.httpSession);
        when(this.request.getSession()).thenReturn(this.httpSession);
        when(this.request.getRequestDispatcher(anyString())).thenReturn(this.requestDispatcher);
    }

    /**
     * Test doFilter()
     */
    @Test
    public void whenAdminThenPass() throws IOException, ServletException {
        when(this.httpSession.getAttribute("loggedUser")).thenReturn(this.userRoleAdmin);
        this.filter.doFilter(this.request, this.response, this.chain);
        verify(this.chain).doFilter(this.request, this.response);
    }

    @Test
    public void whenAUserThenRedirectToMainPageWithForbiddenError() throws IOException, ServletException {
        when(this.httpSession.getAttribute("loggedUser")).thenReturn(this.userRoleUser);
        this.filter.doFilter(this.request, this.response, this.chain);
        verify(this.request).setAttribute(eq("error"), anyString());
        verify(this.requestDispatcher).forward(this.request, this.response);
    }

}