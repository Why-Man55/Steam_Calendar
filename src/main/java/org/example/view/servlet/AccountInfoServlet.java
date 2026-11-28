package org.example.view.servlet;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.domain.service.AccountInfoService;
import org.example.domain.service.FriendsService;
import org.example.domain.models.RegisteredFriendDto;
import org.example.domain.models.SteamSearchResult;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class AccountInfoServlet extends HttpServlet {
    private AccountInfoService accountInfoService;
    private FriendsService friendsService;

    @Override
    public void init() {
        ServletContext context = getServletContext();
        this.accountInfoService = (AccountInfoService) context.getAttribute("accountInfoService");
        this.friendsService = (FriendsService) context.getAttribute("friendsService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = (String) req.getSession().getAttribute("userId");

        SteamSearchResult searchResult = accountInfoService.searchProfile(userId);
        if (searchResult.isSuccess()) {
            req.setAttribute("steamProfile", searchResult.getProfile());
        } else {
            req.setAttribute("error", searchResult.getErrorMessage());
        }

        List<RegisteredFriendDto> registeredFriends = friendsService.getAvailableSteamFriends(userId);
        req.setAttribute("registeredFriends", registeredFriends);

        req.getRequestDispatcher("/WEB-INF/views/accountInfo.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = (String) req.getSession().getAttribute("userId");
        String[] selectedIds = req.getParameterValues("friendIds");

        if (selectedIds != null && selectedIds.length > 0) {
            friendsService.saveSelectedFriends(userId, Arrays.asList(selectedIds));
        }

        resp.sendRedirect(req.getContextPath() + "/account");
    }
}
