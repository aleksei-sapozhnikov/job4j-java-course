package ru.job4j.crud.filters;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {
    private static final Logger LOG = LogManager.getLogger(LoginFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        if (req.getRequestURI().contains("/login")) {
            chain.doFilter(req, resp);
        } else {
            this.onPageFilter(req, resp, chain);
        }
    }

    private void onPageFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpSession session = req.getSession();
        synchronized (session) {
            if (session.getAttribute("user") == null) {
                resp.sendRedirect(String.join("/", req.getContextPath(), "login"));
            } else {
                chain.doFilter(req, resp);
            }
        }
    }


    @Override
    public void destroy() {
    }
}
