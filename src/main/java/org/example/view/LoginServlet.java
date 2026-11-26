package org.example.view;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.domain.AuthService;
import org.example.view.utils.UtilsForSteam;

import java.io.IOException;

public class LoginServlet extends HttpServlet {
    AuthService service;

    @Override
    public void init() throws ServletException {
        ServletContext context = getServletContext();

        this.service = (AuthService) context.getAttribute("authService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("userId");
        String password = req.getParameter("password");

        if (UtilsForSteam.isCorrectId(id)) {
            if (service.checkLogin(id, password)) {
                HttpSession session = req.getSession();
                session.setAttribute("userId", id);
                resp.sendRedirect(req.getContextPath() + "/account");
            } else {
                req.setAttribute("loginError", "not found");
                req.getRequestDispatcher("WEB-INF/views/login.jsp");
            }
        }
    }
}
