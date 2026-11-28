package org.example;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.example.dao.AccountDao;
import org.example.dao.AvailabilityDao;
import org.example.dao.FriendshipDao;
import org.example.data.SteamApiClient;
import org.example.domain.AccountInfoService;
import org.example.domain.AuthService;
import org.example.domain.CalendarService;
import org.example.domain.FriendsService;

@WebListener
public class ServletListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String steamApiKey = System.getenv("STEAM_API_KEY");

        AccountDao accountDao = new AccountDao();
        FriendshipDao friendshipDao = new FriendshipDao();
        AvailabilityDao availabilityDao = new AvailabilityDao();
        SteamApiClient steamApiClient = new SteamApiClient(steamApiKey);

        AuthService authService = new AuthService(accountDao, steamApiClient);
        AccountInfoService accountInfoService = new AccountInfoService(accountDao, steamApiClient);
        CalendarService calendarService = new CalendarService(availabilityDao);
        FriendsService friendsService = new FriendsService(steamApiClient, accountDao, friendshipDao);

        ServletContext servletContext = sce.getServletContext();
        servletContext.setAttribute("authService", authService);
        servletContext.setAttribute("accountInfoService", accountInfoService);
        servletContext.setAttribute("calendarService", calendarService);
        servletContext.setAttribute("friendsService", friendsService);
    }
}
