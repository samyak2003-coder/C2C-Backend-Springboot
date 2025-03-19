<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.io.*, java.net.HttpURLConnection, java.net.URL" %>
<%@ page import="org.json.JSONObject" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page import="org.json.JSONArray" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>C2C Web App</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: Arial, sans-serif;
        }

        body {
            background-color: #212121;
            color: white;
            text-align: center;
            margin:auto;
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


        .section-heading {
            font-size: 2rem;
            font-weight: bold;
            color: #ffbf00;
            margin-top: 20px;
        }


        .container {
            display: flex;
            flex-wrap: wrap;
            gap: 20px;
            justify-content: center;
            margin: 50px;
        }

        .box {
            background-color: #000000;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(255, 255, 255, 0.1);
            overflow: hidden;
            padding: 20px;
            text-align: center;
            transition: transform 0.3s ease, box-shadow 0.3s ease;
            color: white;
        }

        .box .item {
            font-size: 1rem;
            margin: 5px 0;
            padding: 5px;
            color: white;
        }
        .delete-button {
            padding: 10px 20px;
            font-size: 16px;
            font-weight: bold;
            background-color: #f44336;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }

        .delete-button:hover {
            background-color: #d32f2f;
        }
        .box:hover {
            transform: translateY(-10px);
            box-shadow: 0 4px 20px rgba(255, 255, 255, 0.15);
        }

        a {
            text-decoration: none;
            color: inherit;
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

<nav class="navbar">
    <div class="logo">C2C Admin page</div>

    <div class="buttons-container">
        <div class="buttons">

                <a href="/signin">Sign In</a>
                <a href="/signup" >Sign Up</a>
        </div>

        <% if (userName != null && !userName.isEmpty()) { %>
            <span class="user-name">Hello, <%= userName %>!</span>
        <% } %>
    </div>
</nav>
<% if (userName != null && !userName.isEmpty()) { %>
    <h2 class="section-heading">Users</h2>
    <div class="container">
        <%
            try {
                URL url = new URL("http://localhost:8081/getUsers");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                if (conn.getResponseCode() == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    JSONArray users = new JSONArray(reader.readLine());
                    System.out.println(users);
                    reader.close();

                    for (int i = 0; i < users.length(); i++) {
                        JSONObject user = users.getJSONObject(i);
                        String entityId = user.getString("id");
        %> 
                        <div class="box">
                            <div class="item">User ID:<%= user.getString("id") %></div>
                            <div class="item">Name: <%= user.getString("name") %></div>
                            <div class="item">Email: <%= user.getString("email") %></div>
                        <form:form method="POST" action="/delete-user" modelAttribute="deleteEntityDetails">
                            <form:hidden path="entityId" value="<%= entityId %>" />
                            <form:button class="delete-button">Delete</form:button>
                        </form:form>
                        </div>
        <%
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        %>
    </div>

    <h2 class="section-heading">Offers</h2>
    <div class="container">
        <%
            try {
                URL url = new URL("http://localhost:8081/getOffers");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                if (conn.getResponseCode() == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    JSONArray offers = new JSONArray(reader.readLine());
                    reader.close();

                    for (int i = 0; i < offers.length(); i++) {
                        JSONObject offer = offers.getJSONObject(i);
                        String entityId = offer.getString("offerId");
        %>
                        <div class="box">
                            <div class="item">Offer ID: <%= offer.getString("offerId") %></div>
                            <div class="item">Buyer ID: <%= offer.getString("buyerId") %></div>
                            <div class="item">Offer Date: <%= offer.getString("offerDate") %></div>
                            <div class="item">Price: ₹<%= offer.getDouble("offeredPrice") %></div>
                            <div class="item">Product ID: <%= offer.getString("productId") %></div>
                            <div class="item">Status: <%= offer.getString("status") %></div>
                            <div class="item">Seller ID: <%= offer.getString("sellerId") %></div>
                        <form:form method="POST" action="/delete-offer" modelAttribute="deleteEntityDetails">
                            <form:hidden path="entityId" value="<%= entityId %>" />
                            <form:button class="delete-button">Delete</form:button>
                        </form:form>
                        </div>
        <%
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        %>
    </div>

    <h2 class="section-heading">Products</h2>
    <div class="container">
        <%
            try {
                URL url = new URL("http://localhost:8081/getProducts");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                if (conn.getResponseCode() == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    JSONArray products = new JSONArray(reader.readLine());
                    reader.close();

                    for (int i = 0; i < products.length(); i++) {
                        JSONObject product = products.getJSONObject(i);
                        String entityId = product.getString("productId");
        %>
                        <div class="box">
                            <div class="item">Product ID :<%= product.getString("productId") %></div>
                            <div class="item">Title: <%= product.getString("title") %></div>
                            <div class="item">Description: <%= product.getString("description") %></div>
                            <div class="item">Price: ₹<%= product.getDouble("price") %></div>
                            <div class="item">Category: <%= product.getString("category") %></div>
                            <div class="item">Condition: <%= product.getString("productCondition") %></div>
                            <div class="item">Seller Id: <%= product.getString("sellerId") %></div>
                            <div class="item">Status: <%= product.getString("status") %></div>
                        <form:form method="POST" action="/delete-product" modelAttribute="deleteEntityDetails">
                            <form:hidden path="entityId" value="<%= entityId %>" />
                            <form:button class="delete-button">Delete</form:button>
                        </form:form>
                        </div>
        <%
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        %>
    </div>

    <h2 class="section-heading">Orders</h2>
    <div class="container">
        <%
            try {
                URL url = new URL("http://localhost:8081/getOrders");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                if (conn.getResponseCode() == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    JSONArray orders = new JSONArray(reader.readLine());
                    reader.close();

                    for (int i = 0; i < orders.length(); i++) {
                        JSONObject order = orders.getJSONObject(i);
                        String entityId = order.getString("orderId");
        %>
                        <div class="box">
                            <div class="item">Order ID: <%= order.getString("orderId") %></div>
                            <div class="item">Buyer ID: <%= order.getString("buyerId") %></div>
                            <div class="item">Seller ID: <%= order.getString("sellerId") %></div>
                            <div class="item">Order Price: ₹<%= order.getDouble("orderPrice") %></div>
                            <div class="item">Product ID: <%= order.getString("productId") %></div>
                            <div class="item">Order Date: <%= order.getString("orderDate") %></div>
                            <div class="item">Payment Method: <%= order.getString("paymentMethod") %></div>
                        <form:form method="POST" action="/delete-order" modelAttribute="deleteEntityDetails">
                            <form:hidden path="entityId" value="<%= entityId %>" />
                            <form:button class="delete-button">Delete</form:button>
                        </form:form>
                        </div>
        <%
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        %>
    </div>

<% } else { %>
    <p style="color: #ffbf00; font-size: 1.5rem;">Please sign in to view content.</p>
<% } %>


</body>
</html>