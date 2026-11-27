package org.example;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.example.dao.AccountDao;
import org.example.data.SteamApiClient;
import org.example.domain.*;

@WebListener
public class ServletListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {

        String steamApiKey = System.getenv("STEAM_API_KEY");

        AccountDao accountDao = new AccountDao();
        SteamApiClient steamApiClient = new SteamApiClient(steamApiKey);

        AuthService authService = new AuthService(accountDao);
        AccountInfoService accountInfoService = new AccountInfoService(accountDao, steamApiClient);
        CalendarService calendarService = new CalendarService();
        FriendsService friendsService = new FriendsService();

        jakarta.servlet.ServletContext servletContext = sce.getServletContext();
        servletContext.setAttribute("authService", authService);
        servletContext.setAttribute("accountInfoService", accountInfoService);
        servletContext.setAttribute("calendarService", calendarService);
        servletContext.setAttribute("friendsService", friendsService);
    }
}
