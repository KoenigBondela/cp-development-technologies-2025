package com.hoteldb.labs.web;

import com.hoteldb.labs.jpa.entity.UserEntity;
import com.hoteldb.labs.jpa.service.UserService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

@WebServlet(name = "LoginServlet", urlPatterns = "/Login")
public class LoginServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(LoginServlet.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String username = trimToNull(request.getParameter("username"));
        String password = trimToNull(request.getParameter("password"));

        if (username == null || password == null) {
            response.sendRedirect("index.jsp?error=missing");
            return;
        }

        UserService userService = new UserService();
        try {
            Optional<UserEntity> userOpt = userService.findByUsernameAndPassword(username, password);
            if (userOpt.isEmpty()) {
                response.sendRedirect("index.jsp?error=invalid");
                return;
            }

            UserEntity user = userOpt.get();
            HttpSession session = request.getSession(true);
            session.setAttribute("userName", user.getUsername());
            session.setAttribute("userRole", user.getRole().name());

            response.sendRedirect("welcome");
        } catch (Exception e) {
            logger.error("Login failed: {}", e.getMessage(), e);
            response.sendRedirect("index.jsp?error=server");
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

