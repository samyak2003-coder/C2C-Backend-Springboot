<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page import="java.io.*, java.net.HttpURLConnection, java.net.URL" %>
<%@ page import="org.json.JSONObject,org.json.JSONArray" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>C2C Web App</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f7fc;
            margin: 0;
            padding: 0;
        }

        #navbar {
            background-color: #333;
            color: #fff;
            text-align: center;
            position: fixed; /* Fixes the navbar to the top */
            top: 0;
            left: 0;
            width: 100%; 
            z-index: 1000;
        }

        .products-container {
            display: flex;
            flex-wrap: wrap;
            gap: 20px;
            justify-content: center;
            margin: 200px;
        }

        .offer-box {
            background-color: #000000; /* Set background to black */
            color: #fff; /* Set text color to white */
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            padding: 20px;
            width: 300px;
            text-align: center;
            transition: transform 0.3s, box-shadow 0.3s;
        }

        .offer-box:hover {
            transform: translateY(-10px);
            box-shadow: 0 10px 20px rgba(0, 0, 0, 0.2);
        }

        .offer-box .product-Id,
        .offer-box .sellerId,
        .offer-box .offeredPrice,
        .offer-box .offerDate,
        .offer-box .stats {
            font-size: 16px;
            margin: 10px 0;
        }

        .offeredPrice {
            color: #FF6F00;
            font-weight: bold;
        }

        .offer-box .offerDate {
            color: #888;
        }

        .offer-box .stats {
            background-color: #e0f7fa;
            padding: 5px;
            border-radius: 5px;
        }

        .offer-box .stats.active {
            background-color: #c8e6c9;
        }

        .offer-box .stats.inactive {
            background-color: #0000FF; /* Set background to red for inactive offers */
            color: #fff; /* Set text color to white */
        }

        .offer-box .product-Id,
        .offer-box .sellerId {
            font-weight: 600;
        }

        .offer-box .label {
            color: #888; /* Grey color for the labels */
            font-weight: 300; /* Thinner font */
        }

        .offer-box .value {
            font-weight: 600; /* Regular font weight for the values */
        }

        .offer-box button {
            background-color: #4CAF50;
            color: white;
            border: none;
            padding: 10px 20px;
            margin-top: 10px;
            cursor: pointer;
            border-radius: 5px;
        }

        .offer-box button.reject {
            background-color: #f44336;
        }

        .offer-box button:hover {
            opacity: 0.8;
        }

        .make-bid-btn {
            padding: 10px 20px;
            font-size: 1.2rem;
            background-color: #2d87f0;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }

        .make-payment-btn {
            background-color: #0000FF; /* Same as 'Not accepted' background */
            color: white;
            border: none;
            padding: 10px 15px;
            cursor: pointer;
            border-radius: 5px;
            margin-top: 5px;
        }
        .make-payment-btn:hover {
            opacity: 0.8;
        }

        .payment-input {
            width: 80%; 
            padding: 5px;
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

    String userName = null;
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

<div id="navbar"><jsp:include page="navbar.jsp"></jsp:include></div>

<div class="products-container">
<%
    try {
        URL url = new URL("http://localhost:8081/get-offers");
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

            JSONObject offersResponse = new JSONObject(apiResponse.toString());
            JSONArray buyOffers = offersResponse.getJSONArray("buy");
            JSONArray sellOffers = offersResponse.getJSONArray("sell");

            // Loop through 'buy' offers
            for (int i = 0; i < buyOffers.length(); i++) {
                JSONObject offer = buyOffers.getJSONObject(i);
                String status = offer.getString("status");
                String offerId = offer.getString("offerId");
                String productId = offer.getString("productId");
                String sellerId = offer.getString("sellerId");
                Double offeredPrice = offer.getDouble("offeredPrice");
%>
                <div class="offer-box">
                    <div class="product-Id">
                        <span class="label">Offer ID: </span><span class="value"><%= offerId %></span>
                    </div>
                    <div class="product-Id">
                        <span class="label">Product ID: </span><span class="value"><%= offer.getString("productId") %></span>
                    </div>
                    <div class="sellerId">
                        <span class="label">Seller ID: </span><span class="value"><%= offer.getString("sellerId") %></span>
                    </div>
                    <div class="offeredPrice">
                        <span class="label">Offered Price: </span><span class="value">₹<%= offer.getDouble("offeredPrice") %></span>
                    </div>
                    <div class="offerDate">
                        <span class="label">Offer Date: </span><span class="value"><%= offer.getString("offerDate") %></span>
                    </div>
                    <% if (status.equals("Accepted")) { %>
                    <div>

                     <form:form method="POST" action="/create-order" modelAttribute="createOrderDetails">
                        <form:input id="paymentMethod" placeholder="Payment Mode" class="payment-input" path="paymentMethod"/>
                        <form:hidden path="token" value="<%= authToken %>" />
                        <form:hidden path="sellerId" value="<%= sellerId %>" />
                        <form:hidden path="productId" value="<%= productId %>" />
                        <form:hidden path="orderPrice" value="<%= offeredPrice %>" />
                        <form:hidden path="orderDate" value="<%= java.time.LocalDate.now() %>" />
                        <form:hidden path="offerId" value="<%= offerId %>" />
                        <form:button class="make-payment-btn">Make Payment</form:button>
                    </form:form>

                    </div>
                <% } else { %>
                    <div class="stats <%= status.equals("Not accepted") ? "inactive" : "active" %>">
                        <span class="value"><%= status %></span>
                    </div>
<% } %>

                </div>
<%
            }

            // Loop through 'sell' offers
            for (int i = 0; i < sellOffers.length(); i++) {
                JSONObject offer = sellOffers.getJSONObject(i);
                String status = offer.getString("status");
                String offerId = offer.getString("offerId");
                if (status.equals("Accepted")) {
                    continue;
                }
%>
                <div class="offer-box">
                    <div class="product-Id">
                        <span class="label">Offer ID: </span><span class="value"><%= offerId %></span>
                    </div>
                    <div class="product-Id">
                        <span class="label">Product ID: </span><span class="value"><%= offer.getString("productId") %></span>
                    </div>
                    <div class="sellerId">
                        <span class="label">Seller ID: </span><span class="value"><%= offer.getString("sellerId") %></span>
                    </div>
                    <div class="offeredPrice">
                        <span class="label">Offered Price: </span><span class="value">₹<%= offer.getDouble("offeredPrice") %></span>
                    </div>
                    <div class="offerDate">
                        <span class="label">Offer Date: </span><span class="value"><%= offer.getString("offerDate") %></span>
                    </div>

                    <form:form method="POST" action="/update-offer" modelAttribute="updateOfferDetails">
                        <input type="hidden" name="offerId" value="<%= offerId %>">
                        <input type="hidden" name="status" value="Accepted">
                        <button type="submit" name="action" value="accept" class="accept">Accept</button>
                    </form:form>

                    <form:form method="POST" action="/delete-offer" modelAttribute="deleteOfferDetails">
                    <input type="hidden" name="offerId" value="<%= offerId %>">
                    <button type="submit" class="reject">Reject</button>
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

</body>
</html>