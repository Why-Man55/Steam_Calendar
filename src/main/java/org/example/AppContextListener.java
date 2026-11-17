package org.example;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

public class AppContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        MainDao dao = new MainDao();
        MainService service = new MainService(dao);

        ServletContext servletContext = sce.getServletContext();
        servletContext.setAttribute("mainService", service);
    }
}
