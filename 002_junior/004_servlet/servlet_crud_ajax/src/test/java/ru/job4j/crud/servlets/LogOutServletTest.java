package ru.job4j.crud.servlets;

import org.junit.Before;
import org.junit.Test;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.mockito.Mockito.*;
import static ru.job4j.crud.Constants.URI_LOGIN;

public class LogOutServletTest {
    /**
     * Context path for tests.
     */
    private static final String CONTEXT = "context";
    /**
     * Mocks.
     */
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
    private final HttpSession httpSession = mock(HttpSession.class);
    /**
     * Servlet to test.
     */
    private LogOutServlet servlet = new LogOutServlet();

    @Before
    public void setCommonMocks() {
        when(this.request.getContextPath()).thenReturn(CONTEXT);
        when(this.request.getSession()).thenReturn(this.httpSession);
        when(this.request.getSession()).thenReturn(this.httpSession);
        when(this.request.getRequestDispatcher(anyString())).thenReturn(this.requestDispatcher);
    }

    /**
     * Test doPost()
     */
    @Test
    public void whenDoPostThenInvalidateSession() throws IOException {
        this.servlet.doPost(this.request, this.response);
        verify(this.httpSession).invalidate();
        verify(this.response).sendRedirect(CONTEXT.concat(URI_LOGIN.v()));
    }

}