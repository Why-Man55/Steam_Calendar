package org.example.view;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.domain.AccountInfoService;
import org.example.domain.models.SteamSearchResult;
import org.example.view.models.SteamProfileDto;

import java.io.IOException;

public class MainSearchServlet extends HttpServlet {
    AccountInfoService service;

    @Override
    public void init() throws ServletException {
        ServletContext context = getServletContext();

        this.service = (AccountInfoService) context.getAttribute("accountInfoService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/mainSearch.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String steamQuery = req.getParameter("steamId");

        if (steamQuery != null && !steamQuery.isBlank()) {
            SteamSearchResult result = service.searchProfile(steamQuery);
            if (result.isSuccess()) {
                req.setAttribute("steamProfile", result.profile());
            } else {
                req.setAttribute("error", result.errorMessage());
            }
        } else {
            req.setAttribute("error", "Введите ссылку на аккаунт, SteamID или ник");
        }

        req.getRequestDispatcher("/WEB-INF/views/mainSearch.jsp").forward(req, resp);
    }

}
