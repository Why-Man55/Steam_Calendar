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
        if (!ViewUtilsForSteam.isCorrectId(id)) {
            req.setAttribute("registerStatus", RegisterCodes.WRONG_ID);

            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
            return;
        }
        if (service.isRegistered(id)) {
            req.setAttribute("registerStatus", RegisterCodes.EXISTS);
        } else {
            if (service.register(id, req.getParameter("username"), req.getParameter("password"))) {
                req.setAttribute("registerStatus", RegisterCodes.SUCCESS);
            } else {
                req.setAttribute("registerStatus", RegisterCodes.SERVER_ERROR);
            }
        }

        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }
}
