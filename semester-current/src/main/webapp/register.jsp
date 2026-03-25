<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String error = request.getParameter("error");
%>
<html>
<head>
    <title>Register</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container py-5">
    <div class="row justify-content-center">
        <div class="col-12 col-md-8 col-lg-5">
            <div class="card shadow-sm">
                <div class="card-body p-4">
                    <h2 class="h4 mb-3">Register</h2>

                    <% if (error != null) { %>
                    <div class="alert alert-danger mb-3">
                        <% if ("missing".equals(error)) { %>Please fill all required fields.<% } %>
                        <% if ("server".equals(error)) { %>Server error. Possibly duplicate username.<% } %>
                    </div>
                    <% } %>

                    <form action="Register" method="post" class="vstack gap-3">
                        <div>
                            <label class="form-label">Username</label>
                            <input class="form-control" type="text" name="username" required>
                        </div>
                        <div>
                            <label class="form-label">Password</label>
                            <input class="form-control" type="password" name="password" required>
                        </div>
                        <button class="btn btn-success w-100" type="submit">Create account</button>
                    </form>

                    <div class="mt-3 text-center">
                        <a href="index.jsp">Back to login</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>

