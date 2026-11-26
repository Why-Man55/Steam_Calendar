package org.example.view;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.domain.AccountInfoService;
import org.example.domain.models.SteamSearchResult;

import java.io.IOException;

public class AccountInfoServlet extends HttpServlet {
    AccountInfoService service;

    @Override
    public void init() throws ServletException {
        ServletContext context = getServletContext();
        this.service = (AccountInfoService) context.getAttribute("accountInfoService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        String id = (session != null) ? (String) session.getAttribute("userId") : null;

        if (id == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        SteamSearchResult result = service.searchProfile(id);
        if (result.isSuccess()) {
            req.setAttribute("steamProfile", result.getProfile());
        } else {
            req.setAttribute("error", result.getErrorMessage());
        }

        req.getRequestDispatcher("/WEB-INF/views/accountInfo.jsp").forward(req, resp);
    }
}
