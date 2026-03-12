<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String error = request.getParameter("error");
    String registered = request.getParameter("registered");
%>
<html>
<head>
    <title>Login</title>
</head>
<body>
<h2>Login</h2>

<% if ("1".equals(registered)) { %>
<p style="color: green;">Registration completed. You can log in now.</p>
<% } %>

<% if (error != null) { %>
<p style="color: red;">
    <% if ("missing".equals(error)) { %>Please fill all required fields.<% } %>
    <% if ("invalid".equals(error)) { %>Invalid credentials (email + last name).<% } %>
    <% if ("server".equals(error)) { %>Server error. Check logs / DB connection.<% } %>
</p>
<% } %>

<form action="Login" method="post">
    Email: <input type="email" name="email" required><br>
    Last name: <input type="text" name="lastName" required><br>
    <input type="submit" value="login"><br>
    New here? Please, <a href="register.jsp">register</a>
</form>
</body>
</html>

