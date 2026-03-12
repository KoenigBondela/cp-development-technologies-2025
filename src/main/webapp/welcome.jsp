<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String userName = (String) session.getAttribute("userName");
    if (userName == null) {
        response.sendRedirect("index.jsp");
        return;
    }
%>
<html>
<head>
    <title>Welcome</title>
</head>
<body>
<h1>Welcome, <%= userName %>!</h1>
<p>This is the same flow as in SimpleWebApp, but authentication is done via your DB (`clients`).</p>
<p><a href="index.jsp">Log out</a> (clears session if you restart browser; for real logout add a servlet)</p>
</body>
</html>

