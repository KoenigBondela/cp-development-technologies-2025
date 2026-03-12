package com.hoteldb.labs.web;

import com.hoteldb.labs.jdbc.DatabaseConnection;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

@WebServlet(name = "RegisterServlet", urlPatterns = "/Register")
public class RegisterServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(RegisterServlet.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String firstName = trimToNull(request.getParameter("firstName"));
        String lastName = trimToNull(request.getParameter("lastName"));
        String email = trimToNull(request.getParameter("email"));
        String phone = trimToNull(request.getParameter("phone"));

        if (firstName == null || lastName == null || email == null) {
            response.sendRedirect("register.jsp?error=missing");
            return;
        }

        try (Connection conn = DatabaseConnection.getInstance().openPrimaryConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO clients (first_name, last_name, email, phone, room_id, check_in_date, check_out_date) " +
                             "VALUES (?, ?, ?, ?, NULL, NULL, NULL)"
             )) {
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, email);
            ps.setString(4, phone);

            ps.executeUpdate();
            response.sendRedirect("index.jsp?registered=1");
        } catch (Exception e) {
            logger.error("Registration failed: {}", e.getMessage(), e);
            response.sendRedirect("register.jsp?error=server");
        }
    }

    private static String trimToNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}

