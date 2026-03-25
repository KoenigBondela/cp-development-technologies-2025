package com.hoteldb.labs.web;

import com.hoteldb.labs.jpa.entity.UserRole;
import com.hoteldb.labs.jpa.service.UserService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet(name = "RegisterServlet", urlPatterns = "/Register")
public class RegisterServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(RegisterServlet.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String username = trimToNull(request.getParameter("username"));
        String password = trimToNull(request.getParameter("password"));

        if (username == null || password == null) {
            response.sendRedirect("register.jsp?error=missing");
            return;
        }

        UserService userService = new UserService();
        try {
            userService.register(username, password, UserRole.USER);
            response.sendRedirect("index.jsp?registered=1");
        } catch (Exception e) {
            logger.error("Registration failed: {}", e.getMessage(), e);
            response.sendRedirect("register.jsp?error=server");
        } finally {
            userService.close();
        }
    }

    private static String trimToNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}

