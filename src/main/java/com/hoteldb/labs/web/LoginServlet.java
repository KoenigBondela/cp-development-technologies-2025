package com.hoteldb.labs.web;

import com.hoteldb.labs.jdbc.DatabaseConnection;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet(name = "LoginServlet", urlPatterns = "/Login")
public class LoginServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(LoginServlet.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String email = trimToNull(request.getParameter("email"));
        String lastName = trimToNull(request.getParameter("lastName"));

        if (email == null || lastName == null) {
            response.sendRedirect("index.jsp?error=missing");
            return;
        }

        try (Connection conn = DatabaseConnection.getInstance().openPrimaryConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT id, first_name, last_name FROM clients WHERE email = ? AND last_name = ?"
             )) {
            ps.setString(1, email);
            ps.setString(2, lastName);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    response.sendRedirect("index.jsp?error=invalid");
                    return;
                }

                String firstName = rs.getString("first_name");
                HttpSession session = request.getSession(true);
                session.setAttribute("userEmail", email);
                session.setAttribute("userName", firstName + " " + lastName);
                response.sendRedirect("welcome.jsp");
            }
        } catch (Exception e) {
            logger.error("Login failed: {}", e.getMessage(), e);
            response.sendRedirect("index.jsp?error=server");
        }
    }

    private static String trimToNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}

