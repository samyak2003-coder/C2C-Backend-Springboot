<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>C2C Web App</title>
    <style>

    body {
    background-color: #f4f7fc;
    color: white;
    font-family: Arial, sans-serif;
    margin: 0;
    padding: 0;
    text:white;
    display: flex;
    flex-direction: column; 
    justify-content: center;
    align-items: center;
    height: 100vh;
}

#navbar {
    width: 100%;
    position: fixed; 
    top: 0;
    left: 0;
    background-color: #333;
    text-align: center;
}

h1 {
    text-align: center;
    color: white;
}


        .form-container {
            background-color: #000000;
            position: fixed;
            padding: 50px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 400px;
            align-items: center;
            justify-content: center;
        }

        .form-container div {
            margin-bottom: 15px;
        }

        .form-container label {
            font-size: 14px;
            color: #555;
            display: block;
            margin-bottom: 5px;
        }

        .form-container input {
            width: 100%;
            padding: 10px;
            font-size: 14px;
            border: 1px solid #ccc;
            border-radius: 4px;
            outline: none;
        }

        .form-container label {
    font-size: 14px;
    color: white; /* Changed from #555 to white */
    display: block;
    margin-bottom: 5px;
}

        .form-container input:focus {
            border-color: white;
        }

        .form-container button {
            width: 100%;
            padding: 10px;
            background-color: #4CAF50;
            color: white;
            font-size: 16px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }

        .form-container button:hover {
            background-color: #45a049;
        }

        .form-container button:focus {
            outline: none;
        }

        .form-container p {
            text-align: center;
            color: white;
            margin-top: 15px;
            font-size: 14px;
        }

        .form-container p a {
            color: #4CAF50;
            text-decoration: none;
        }

        .form-container p a:hover {
            text-decoration: underline;
        }

        .alert {
            padding: 10px;
            border-radius: 5px;
            margin-top: 10px;
            text-align: center;
            font-size: 14px;
        }

        .alert-success {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }

        .alert-danger {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
    </style>
</head>
<body>
<div id="navbar"><jsp:include page="navbar.jsp"></jsp:include></div>

<div class="form-container">
    <h1>Admin Sign In</h1>

    <form:form method="POST" action="/signin" modelAttribute="signInDetails">
        <div class="form-group">
            <label for="email">E-Mail</label>
            <form:input path="email" cssClass="form-control"/>
            <form:errors path="email" cssStyle="color: red"/>
        </div>

        <div class="form-group">
            <label for="password">Password</label>
            <form:password path="password" cssClass="form-control"/>
            <form:errors path="password" cssStyle="color: red"/>
        </div>

        <form:button cssClass="btn btn-primary">Log in</form:button>
    </form:form>

    <!--- Check login status and display message -->
    <% Object status = session.getAttribute("AuthStatus"); %>
    <% if ("FAILED".equals(status)) { %>
        <div class="alert alert-danger">
            Login failed. Please try again!!!
        </div>
    <% } %>
</div>

</body>
</html>