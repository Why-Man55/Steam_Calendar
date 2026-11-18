package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class MainServlet extends HttpServlet {
    MainService service;

    @Override
    public void init() {
        this.service = (MainService) getServletContext().getAttribute("mainService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/MainView.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        int status = service.checkAuth(username, password);
        switch(status){
            case -1 -> req.setAttribute("authRes", "");
            case -2 -> req.setAttribute("authRes", "");
            case 0 -> req.setAttribute("authRes", "");
        }
        req.getRequestDispatcher(req.getContextPath()).forward(req,resp);
    }
}
