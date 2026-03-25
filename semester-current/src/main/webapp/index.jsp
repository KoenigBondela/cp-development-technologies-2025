<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String error = request.getParameter("error");
    String registered = request.getParameter("registered");
%>
<html>
<head>
    <title>Login</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container py-5">
    <div class="row justify-content-center">
        <div class="col-12 col-md-8 col-lg-5">
            <div class="card shadow-sm">
                <div class="card-body p-4">
                    <h2 class="h4 mb-3">Login</h2>

                    <% if ("1".equals(registered)) { %>
                    <div class="alert alert-success">Registration completed. You can log in now.</div>
                    <% } %>

                    <% if (error != null) { %>
                    <div class="alert alert-danger mb-3">
                        <% if ("missing".equals(error)) { %>Please fill all required fields.<% } %>
                        <% if ("invalid".equals(error)) { %>Invalid credentials (username + password).<% } %>
                        <% if ("server".equals(error)) { %>Server error. Check logs / DB connection.<% } %>
                    </div>
                    <% } %>

                    <form action="Login" method="post" class="vstack gap-3">
                        <div>
                            <label class="form-label">Username</label>
                            <input class="form-control" type="text" name="username" required>
                        </div>
                        <div>
                            <label class="form-label">Password</label>
                            <input class="form-control" type="password" name="password" required>
                        </div>
                        <button class="btn btn-primary w-100" type="submit">Log in</button>
                        <div class="text-center">
                            New here? <a href="register.jsp">Register</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>

