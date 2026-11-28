package org.example;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.example.data.persistence.ImplAccountRepository;
import org.example.data.persistence.ImplAvailabilityRepository;
import org.example.data.persistence.ImplFriendshipRepository;
import org.example.data.SteamApiClient;
import org.example.domain.service.AccountInfoService;
import org.example.domain.service.AuthService;
import org.example.domain.service.CalendarService;
import org.example.domain.service.FriendsService;

@WebListener
public class ServletListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String steamApiKey = System.getenv("STEAM_API_KEY");

        ImplAccountRepository implAccountRepository = new ImplAccountRepository();
        ImplFriendshipRepository implFriendshipRepository = new ImplFriendshipRepository();
        ImplAvailabilityRepository implAvailabilityRepository = new ImplAvailabilityRepository();
        SteamApiClient steamApiClient = new SteamApiClient(steamApiKey);

        AuthService authService = new AuthService(implAccountRepository, steamApiClient);
        AccountInfoService accountInfoService = new AccountInfoService(implAccountRepository, steamApiClient);
        CalendarService calendarService = new CalendarService(implAvailabilityRepository);
        FriendsService friendsService = new FriendsService(steamApiClient, implAccountRepository, implFriendshipRepository);

        ServletContext servletContext = sce.getServletContext();
        servletContext.setAttribute("authService", authService);
        servletContext.setAttribute("accountInfoService", accountInfoService);
        servletContext.setAttribute("calendarService", calendarService);
        servletContext.setAttribute("friendsService", friendsService);
    }
}
