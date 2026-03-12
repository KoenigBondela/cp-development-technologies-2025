<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String error = request.getParameter("error");
%>
<html>
<head>
    <title>Register</title>
</head>
<body>
<h2>Register</h2>

<% if (error != null) { %>
<p style="color: red;">
    <% if ("missing".equals(error)) { %>Please fill all required fields.<% } %>
    <% if ("server".equals(error)) { %>Server error. Possibly duplicate email.<% } %>
</p>
<% } %>

<form action="Register" method="post">
    First name: <input type="text" name="firstName" required><br>
    Last name: <input type="text" name="lastName" required><br>
    Email: <input type="email" name="email" required><br>
    Phone: <input type="text" name="phone"><br>
    <input type="submit" value="register">
</form>

<p><a href="index.jsp">Back to login</a></p>
</body>
</html>

