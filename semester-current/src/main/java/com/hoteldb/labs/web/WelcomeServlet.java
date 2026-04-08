package com.hoteldb.labs.web;

import com.hoteldb.labs.jpa.entity.UserEntity;
import com.hoteldb.labs.jpa.service.RoomService;
import com.hoteldb.labs.jpa.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

@WebServlet(name = "WelcomeServlet", urlPatterns = "/welcome")
public class WelcomeServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(WelcomeServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userName") == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        String username = (String) session.getAttribute("userName");
        String role = (String) session.getAttribute("userRole");
        if (role == null || role.isBlank()) {
            role = "USER";
            session.setAttribute("userRole", role);
        }

        if ("ADMIN".equalsIgnoreCase(role)) {
            RoomService roomService = new RoomService();
            try {
                request.setAttribute("rooms", roomService.findAll());
            } catch (Exception e) {
                logger.error("Failed to load rooms: {}", e.getMessage(), e);
                request.setAttribute("dataError", "rooms");
            } finally {
                roomService.close();
            }
        } else {
            // Lab requirement: user sees a different table. We show only the current user's row from `users`.
            UserService userService = new UserService();
            try {
                Optional<UserEntity> me = userService.findByUsername(username);
                if (me.isPresent()) {
                    request.setAttribute("me", me.get());
                } else {
                    request.setAttribute("dataError", "users");
                }
            } catch (Exception e) {
                logger.error("Failed to load current user: {}", e.getMessage(), e);
                request.setAttribute("dataError", "users");
            } finally {
                userService.close();
            }
        }

        request.getRequestDispatcher("/welcome.jsp").forward(request, response);
    }
}

