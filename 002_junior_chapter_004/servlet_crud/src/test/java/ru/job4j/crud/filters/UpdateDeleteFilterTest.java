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
import static ru.job4j.crud.model.Role.ADMIN;
import static ru.job4j.crud.model.Role.USER;

public class UpdateDeleteFilterTest {

    private final UpdateDeleteFilter filter = new UpdateDeleteFilter();

    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final FilterChain chain = mock(FilterChain.class);
    private final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
    private final HttpSession httpSession = mock(HttpSession.class);

    private final User userRoleAdmin = new User(new Credentials("login_1", "password_1", ADMIN), new Info("name_1", "e@mail.com_1", "country_1", "city_1"));
    private final User userRoleUser = new User(new Credentials("login_2", "password_2", USER), new Info("name_2", "e@mail.com_2", "country_2", "city_2"));

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
    public void whenUserWithIdEqualToUpdateIdThenPass() throws IOException, ServletException {
        when(this.httpSession.getAttribute("loggedUser")).thenReturn(this.userRoleUser);
        when(this.request.getParameter("id")).thenReturn(Integer.toString(this.userRoleUser.getId()));
        this.filter.doFilter(this.request, this.response, this.chain);
        verify(this.chain).doFilter(this.request, this.response);
    }

    @Test
    public void whenUserWithIdNotEqualToUpdateIdThenError() throws IOException, ServletException {
        when(this.httpSession.getAttribute("loggedUser")).thenReturn(this.userRoleUser);
        int anotherId = this.userRoleUser.getId() + 1242;
        when(this.request.getParameter("id")).thenReturn(Integer.toString(anotherId));
        this.filter.doFilter(this.request, this.response, this.chain);
        verify(this.request).setAttribute(eq("error"), anyString());
        verify(this.requestDispatcher).forward(request, response);
    }


}