package org.example.view;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import org.example.domain.AccountInfoService;

public class AccountInfoServlet extends HttpServlet {
    AccountInfoService service;

    @Override
    public void init() throws ServletException {
        ServletContext context = getServletContext();

        this.service = (AccountInfoService) context.getAttribute("accountInfoService");
    }
}
