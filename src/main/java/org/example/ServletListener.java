package org.example;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import org.example.dao.Dao;
import org.example.domain.AccountInfoService;
import org.example.domain.AuthService;
import org.example.domain.CalendarService;
import org.example.domain.FriendsService;

public class ServletListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {

        String steamApiKey = System.getenv("STEAM_API_KEY");

        Dao dao = new Dao();
        AuthService authService = new AuthService(dao);
        AccountInfoService accountInfoService = new AccountInfoService(dao);
        CalendarService calendarService = new CalendarService(dao);
        FriendsService friendsService = new FriendsService(dao);

        jakarta.servlet.ServletContext servletContext = sce.getServletContext();
        servletContext.setAttribute("authService", authService);
        servletContext.setAttribute("accountInfoService", accountInfoService);
        servletContext.setAttribute("calendarService", calendarService);
        servletContext.setAttribute("friendsService", friendsService);
    }
}
