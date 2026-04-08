package com.hoteldb.labs.web;

import com.hoteldb.labs.jpa.entity.UserRole;
import com.hoteldb.labs.jpa.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AppBootstrapListener implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(AppBootstrapListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ensureAdminUserExists();
    }

    private void ensureAdminUserExists() {
        UserService userService = new UserService();
        try {
            boolean adminExists = userService.findByUsername("admin").isPresent();
            if (adminExists) {
                logger.info("Admin user already exists");
                return;
            }

            userService.register("admin", "admin", UserRole.ADMIN);
            logger.warn("Admin user was missing and has been created (admin/admin)");
        } catch (Exception e) {
            logger.error("Failed to ensure admin user exists: {}", e.getMessage(), e);
        } finally {
            userService.close();
        }
    }
}

