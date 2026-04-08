<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.hoteldb.labs.jpa.entity.RoomEntity" %>
<%@ page import="com.hoteldb.labs.jpa.entity.UserEntity" %>
<%
    String userName = (String) session.getAttribute("userName");
    String userRole = (String) session.getAttribute("userRole");
    if (userName == null) {
        response.sendRedirect("index.jsp");
        return;
    }
    if (userRole == null) {
        userRole = "USER";
        session.setAttribute("userRole", userRole);
    }
    String dataError = (String) request.getAttribute("dataError");
%>
<html>
<head>
    <title>Welcome</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container py-4">
    <div class="d-flex align-items-center justify-content-between mb-3">
        <div>
            <h1 class="h4 mb-0">Welcome, <%= userName %>!</h1>
            <div class="text-muted small">Role: <%= userRole %></div>
        </div>
        <a class="btn btn-outline-secondary btn-sm" href="logout">Log out</a>
    </div>

    <% if (dataError != null) { %>
    <div class="alert alert-warning">Could not load data: <%= dataError %></div>
    <% } %>

    <% if ("ADMIN".equalsIgnoreCase(userRole)) { %>
        <h2 class="h5 mt-4">Rooms (admin view)</h2>
        <%
            List<RoomEntity> rooms = (List<RoomEntity>) request.getAttribute("rooms");
            if (rooms == null) rooms = List.of();
        %>
        <div class="table-responsive">
            <table class="table table-striped table-bordered align-middle">
                <thead class="table-light">
                <tr>
                    <th>ID</th>
                    <th>Number</th>
                    <th>Type</th>
                    <th>Price</th>
                    <th>Available</th>
                </tr>
                </thead>
                <tbody>
                <% for (RoomEntity r : rooms) { %>
                    <tr>
                        <td><%= r.getId() %></td>
                        <td><%= r.getRoomNumber() %></td>
                        <td><%= r.getRoomType() %></td>
                        <td><%= r.getPricePerNight() %></td>
                        <td><%= Boolean.TRUE.equals(r.getIsAvailable()) ? "Yes" : "No" %></td>
                    </tr>
                <% } %>
                </tbody>
            </table>
        </div>
    <% } else { %>
        <h2 class="h5 mt-4">Your account (user view)</h2>
        <%
            UserEntity me = (UserEntity) request.getAttribute("me");
        %>
        <% if (me == null) { %>
            <div class="alert alert-warning">Could not load your user record from DB.</div>
        <% } else { %>
            <div class="table-responsive">
                <table class="table table-striped table-bordered align-middle">
                    <thead class="table-light">
                    <tr>
                        <th>ID</th>
                        <th>Username</th>
                        <th>Role</th>
                        <th>Created</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td><%= me.getId() %></td>
                        <td><%= me.getUsername() %></td>
                        <td><%= me.getRole() %></td>
                        <td><%= me.getCreatedAt() %></td>
                    </tr>
                    </tbody>
                </table>
            </div>
        <% } %>
    <% } %>
</div>
</body>
</html>

