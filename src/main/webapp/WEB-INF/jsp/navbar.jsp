<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.io.*, java.net.HttpURLConnection, java.net.URL" %>
<%@ page import="org.json.JSONObject" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>C2C Web App</title>
    <link rel="stylesheet" href="/css/styles.css">
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
    <div class="logo"><a href="/">C2C Web App</a></div>

    <div class="buttons-container">
        <div class="buttons">
            <%
                if (userName != null && !userName.isEmpty()) {
            %>
                <a href="/my-offers">My Offers</a>
                <a href="/received-offers">Received Offers</a>
                <a href="/sellProducts">Sell</a>
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
