package org.example;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.example.data.SteamApiClient;
import org.example.data.persistence.ImplAccountRepository;
import org.example.data.persistence.ImplAvailabilityRepository;
import org.example.data.persistence.ImplFriendshipRepository;
import org.example.domain.repository.AccountRepository;
import org.example.domain.repository.AvailabilityRepository;
import org.example.domain.repository.FriendshipRepository;
import org.example.domain.repository.SteamGateway;
import org.example.domain.service.AccountInfoService;
import org.example.domain.service.AuthService;
import org.example.domain.service.CalendarService;
import org.example.domain.service.FriendsService;

@WebListener
public class ServletListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String steamApiKey = System.getenv("STEAM_API_KEY");

        AccountRepository accountRepository = new ImplAccountRepository();
        FriendshipRepository friendshipRepository = new ImplFriendshipRepository();
        AvailabilityRepository availabilityRepository = new ImplAvailabilityRepository();
        SteamGateway steamGateway = new SteamApiClient(steamApiKey);

        AuthService authService = new AuthService(accountRepository, steamGateway);
        AccountInfoService accountInfoService = new AccountInfoService(accountRepository, steamGateway);
        CalendarService calendarService = new CalendarService(availabilityRepository);
        FriendsService friendsService = new FriendsService(steamGateway, accountRepository, friendshipRepository);

        ServletContext servletContext = sce.getServletContext();
        servletContext.setAttribute("authService", authService);
        servletContext.setAttribute("accountInfoService", accountInfoService);
        servletContext.setAttribute("calendarService", calendarService);
        servletContext.setAttribute("friendsService", friendsService);
    }
}
