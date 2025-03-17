<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.io.*, java.net.HttpURLConnection, java.net.URL" %>
<%@ page import="org.json.JSONObject" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>C2C Web App</title>
<style>
    /* Set body background color */
    body {
        background-color: #212121;
        color: white;
        margin-top: 0;
    }

    /* Navbar styles */
    .navbar {
        position: sticky;
        top: 0;
        z-index: 1000;
        display: flex;
        justify-content: space-between;
        align-items: center;
        background-color: #000000;
        padding: 20px 40px;
        box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
        margin-top: 0;
    }

    /* Set text color inside navbar to white */
    .navbar .logo {
        font-size: 24px;
        font-weight: bold;
    }

    .navbar .search-bar {
        flex: 1;
        display: flex;
        justify-content: center;
        margin: 0 30px;
    }

    .navbar .search-bar input {
        width: 60%;
        padding: 12px 15px;
        font-size: 18px;
        border: 2px solid #ccc;
        border-radius: 6px;
        outline: none;
    }

    /* Buttons container */
    .navbar .buttons-container {
        display: flex;
        align-items: center;
        gap: 15px;
    }

    /* Buttons style */
    .navbar .buttons a {
        padding: 12px 20px;
        font-size: 18px;
        font-weight: bold;
        background-color: #4CAF50;
        color: white;
        text-decoration: none;
        border-radius: 6px;
        transition: background-color 0.3s ease;
    }

    .navbar .buttons a:hover {
        background-color: #45a049;
    }

    /* User name */
    .navbar .user-name {
        font-size: 20px;
        font-weight: bold;
        margin-left: 15px;
        white-space: nowrap;
    }
</style>
</head>
<body>
<%
    Cookie[] cookies = request.getCookies();
    String authToken = null;
    String userName = null;

    if (cookies != null) {
        for (Cookie c : cookies) {
            if ("auth_token".equals(c.getName())) {
                authToken = c.getValue();
                break;
            }
        }
    }

    if (authToken != null) {
        try {
            URL url = new URL("http://localhost:8081/validate-token");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Cookie", "auth_token=" + authToken);
            conn.setRequestProperty("Accept", "text/plain");

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                userName = reader.readLine();
                reader.close();
            }
        } catch (Exception e) {
            userName = null;
        }
    }
%>

<!-- Navigation Bar -->
<nav class="navbar">
    <div class="logo">C2C Web App</div>

    <div class="buttons-container">
        <div class="buttons">
            <%
                if (userName != null && !userName.isEmpty()) {
            %>
                <a href="/offers">Offers</a>
                <a href="/" >Home</a>
                <a href="/orders">Orders</a>
                <a href="/products">Sell</a>
            <%
                } else {
            %>
                <a href="/signin">Sign In</a>
                <a href="/signup" >Sign Up</a>
            <%
                }
            %>
        </div>

        <% if (userName != null && !userName.isEmpty()) { %>
            <span class="user-name">Hello, <%= userName %>!</span>
        <% } %>
    </div>
</nav>

</body>
</html>