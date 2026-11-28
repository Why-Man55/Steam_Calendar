package org.example.view;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.domain.AuthService;
import org.example.domain.models.RegisterCodes;
import org.example.view.utils.ViewUtilsForSteam;

import java.io.IOException;

public class RegisterServlet extends HttpServlet {
    private AuthService service;

    @Override
    public void init() throws ServletException {
        ServletContext context = getServletContext();
        this.service = (AuthService) context.getAttribute("authService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("userId");
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (!ViewUtilsForSteam.isCorrectId(id)) {
            req.setAttribute("registerStatus", RegisterCodes.WRONG_ID);
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
            return;
        }

        RegisterCodes status = service.register(id.trim(), username, password);
        if (status == RegisterCodes.SUCCESS) {
            req.getSession().setAttribute("userId", id.trim());
            resp.sendRedirect(req.getContextPath() + "/account");
        } else {
            req.setAttribute("registerStatus", status);
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
        }


        req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
    }
}
