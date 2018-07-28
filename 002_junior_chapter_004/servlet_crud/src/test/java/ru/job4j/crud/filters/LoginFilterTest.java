package ru.job4j.crud.filters;


import org.junit.Before;
import org.junit.Test;
import ru.job4j.crud.model.Credentials;
import ru.job4j.crud.model.Info;
import ru.job4j.crud.model.User;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.mockito.Mockito.*;
import static ru.job4j.crud.Constants.*;
import static ru.job4j.crud.model.Credentials.Role.ADMIN;
import static ru.job4j.crud.model.Credentials.Role.USER;


public class LoginFilterTest {
    /**
     * Context path for tests.
     */
    private static String CONTEXT = "context";
    /**
     * Mocks.
     */
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final FilterChain chain = mock(FilterChain.class);
    private final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
    private final HttpSession httpSession = mock(HttpSession.class);
    /**
     * Users to use.
     */
    private final User userRoleAdmin = new User(new Credentials("login_1", "password_1", ADMIN), new Info("name_1", "e@mail.com_1", "country_1", "city_1"));
    private final User userRoleUser = new User(new Credentials("login_2", "password_2", USER), new Info("name_2", "e@mail.com_2", "country_2", "city_2"));
    /**
     * Filter to test.
     */
    private LoginFilter filter = new LoginFilter();

    @Before
    public void setCommonMocks() {
        when(this.request.getContextPath()).thenReturn(CONTEXT);
        when(this.request.getSession()).thenReturn(this.httpSession);
        when(this.request.getSession()).thenReturn(this.httpSession);
        when(this.request.getRequestDispatcher(anyString())).thenReturn(this.requestDispatcher);
    }

    /**
     * Test doFilter()
     */
    @Test
    public void whenOnLoginPageThenPass() throws IOException, ServletException {
        when(this.request.getRequestURI()).thenReturn(CONTEXT.concat(URI_LOGIN.v()));
        this.filter.doFilter(this.request, this.response, this.chain);
        verify(this.chain).doFilter(this.request, this.response);
    }

    @Test
    public void whenHaveUserWithRoleUserInSessionThenPass() throws IOException, ServletException {
        when(this.request.getRequestURI()).thenReturn(CONTEXT.concat("someNotLoginAddress"));
        when(this.request.getSession()).thenReturn(this.httpSession);
        when(this.httpSession.getAttribute(PARAM_LOGGED_USER.v())).thenReturn(this.userRoleUser);
        this.filter.doFilter(this.request, this.response, this.chain);
        verify(this.chain).doFilter(this.request, this.response);
    }

    @Test
    public void whenHaveUserWithRoleAdminInSessionThenPass() throws IOException, ServletException {
        when(this.request.getRequestURI()).thenReturn(CONTEXT.concat("someNotLoginAddress"));
        when(this.request.getSession()).thenReturn(this.httpSession);
        when(this.httpSession.getAttribute(PARAM_LOGGED_USER.v())).thenReturn(this.userRoleAdmin);
        this.filter.doFilter(this.request, this.response, this.chain);
        verify(this.chain).doFilter(this.request, this.response);
    }

    @Test
    public void whenNullUserInSessionThenRedirectToLogin() throws IOException, ServletException {
        when(this.request.getRequestURI()).thenReturn(CONTEXT.concat("someNotLoginAddress"));
        when(this.httpSession.getAttribute(PARAM_USER.v())).thenReturn(null);
        this.filter.doFilter(this.request, this.response, this.chain);
        verify(this.response).sendRedirect(CONTEXT.concat(URI_LOGIN.v()));
    }

}