package org.example.view;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class MainFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);

        if (session == null) {
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
        }

        String path = req.getServletPath();
        boolean isPublicPath = path.equals("/login")
                || path.equals("/register")
                || path.startsWith("/");

        boolean isLoggedIn = (session != null && session.getAttribute("userId") != null);

        if (isLoggedIn || isPublicPath) {
            chain.doFilter(request, response);
        } else {
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
        }
    }
}
