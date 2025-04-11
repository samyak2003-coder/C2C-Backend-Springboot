<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page import="java.io.*, java.net.HttpURLConnection, java.net.URL" %>
<%@ page import="org.json.JSONObject, org.json.JSONArray" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>C2C Web App</title>
    <style>
body {
    font-family: Arial, sans-serif;
    background-color: #f4f7fc; /* Light background */
    margin: 0;
    padding: 0;
}

#navbar {
    background-color: #fff; /* White background for navbar */
    color: #333; /* Dark text color for contrast */
    text-align: center;
    position: fixed;
    top: 0;
    left: 0;
    width: 100%; 
    z-index: 1000;
    border-bottom: 1px solid #ddd; /* Light border for navbar */
}

.orders-container {
    display: flex;
    flex-wrap: wrap;
    gap: 20px;
    justify-content: center;
    margin: 100px 20px; /* Reduced margin for better spacing */
}

.order-box {
    background-color: #ffffff; /* White background for order box */
    color: #333; /* Dark text color for readability */
    border-radius: 10px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    padding: 20px;
    width: 300px;
    text-align: center;
    transition: transform 0.3s, box-shadow 0.3s;
}

.order-box:hover {
    transform: translateY(-10px);
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15); /* Light shadow effect */
}

.order-title {
    font-size: 1.5rem;
    font-weight: bold;
    color: #333; /* Dark text for titles */
    margin-bottom: 10px;
}

.order-description {
    font-size: 1rem;
    color: #666; /* Lighter gray text for descriptions */
    margin-bottom: 15px;
    height: 60px;
    overflow: hidden;
}

.order-price {
    font-size: 1.25rem;
    font-weight: bold;
    color: #ffbf00; /* Bright yellow for price */
}

a {
    text-decoration: none;
    color: inherit;
}

    </style>
</head>
<body>

<%
    String authToken = null;
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("auth_token")) {
                authToken = cookie.getValue();
            }
        }
    }
%>

<div id="navbar"><jsp:include page="navbar.jsp"></jsp:include></div>

<div class="orders-container">
    <%
        if (authToken != null) {
            try {
                URL url = new URL("http://localhost:8081/get-orders");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Cookie", "auth_token=" + authToken);
                conn.setRequestProperty("Accept", "application/json");

                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder apiResponse = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        apiResponse.append(line);
                    }
                    reader.close();

                    // Parse the JSON response
                    JSONArray orders = new JSONArray(apiResponse.toString());

                    for (int i = 0; i < orders.length(); i++) {
                        JSONObject order = orders.getJSONObject(i);
                        String orderId = order.getString("orderId"); 
    %>
                        <div class="order-box">
                            <div class="order-title"><%= order.getString("orderId") %></div>
                            <div class="order-description"><%= order.getString("buyerId") %></div>
                            <div class="seller-id">₹<%= order.getString("sellerId") %></div>
                            <div class="product-id">₹<%= order.getString("productId") %></div>
                            <div class="order-date"><%= order.getString("orderDate") %></div>
                            <div class="payment"><%= order.getString("paymentMethod") %></div>
                            <div class="order-price">₹<%= order.getDouble("orderPrice") %></div>
                        </div>
    <%
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    %>
</div>

</body>
</html>