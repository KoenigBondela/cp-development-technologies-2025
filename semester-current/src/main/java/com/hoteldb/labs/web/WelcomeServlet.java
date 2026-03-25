package com.hoteldb.labs.web;

import com.hoteldb.labs.jpa.service.ClientService;
import com.hoteldb.labs.jpa.service.RoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

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
            ClientService clientService = new ClientService();
            try {
                request.setAttribute("clients", clientService.findAll());
            } catch (Exception e) {
                logger.error("Failed to load clients: {}", e.getMessage(), e);
                request.setAttribute("dataError", "clients");
            } finally {
                clientService.close();
            }
        }

        request.getRequestDispatcher("/welcome.jsp").forward(request, response);
    }
}

