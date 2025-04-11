<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.io.*, java.net.HttpURLConnection, java.net.URL" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
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
    String token = null;
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("auth_token")) {
                token = cookie.getValue();
            }
        }
    }

    // Retrieve offerStatus session attribute
    String offerStatus = (String) session.getAttribute("offerStatus");
    if (offerStatus != null) {
        session.removeAttribute("offerStatus"); // Clear after displaying
    }
%>
<div id="navbar"><jsp:include page="navbar.jsp"></jsp:include></div>
<div class="main-content">
    <div class="product-detail">
        <%
            String productId = request.getParameter("productId");
            if (productId != null) {
                try {
                    URL url = new URL("http://localhost:8081/get-product/" + productId); 
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
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

                        JSONObject product = new JSONObject(apiResponse.toString());
                        String sellerId = product.getString("sellerId");

        %>
                        <div class="product-title"><%= product.getString("title") %></div>
                        <div class="product-description"><%= product.getString("description") %></div>
                        <div class="product-price">₹<%= product.getDouble("price") %></div>
                        
                        <% 
                            String productStatus = product.getString("status");
                            boolean isSold = "Sold".equals(productStatus);
                        %>
                        <div class="detail-section">
                            <p class="product-category"><span>Category:</span> <%= product.getString("category") %></p>
                            <p class="product-condition"><span>Condition:</span> <%= product.getString("productCondition") %></p>
                            <p class="product-seller"><span>Seller ID:</span> <%= product.getString("sellerId") %></p>
                            <p class="product-status"><span>Status:</span> <%= product.getString("status") %></p>
                        </div>

                        <% if (!isSold) { %>
                        <form:form class="bid-section" method="POST" action="/create-offer" modelAttribute="createOfferDetails">
                            <div class="bid-input-group">
                                <form:input id="offeredPrice" 
                                    class="form-control" 
                                    path="offeredPrice" 
                                    placeholder="Enter your offer amount" 
                                    type="number"
                                    step="0.01"
                                    min="0.01"
                                    required="true" />
                                <form:errors path="offeredPrice" cssClass="error-message" />
                                <form:hidden path="sellerId" value="<%= sellerId %>" />
                                <form:hidden path="offerDate" value="<%= java.time.LocalDate.now() %>" />
                                <form:hidden path="token" value="<%= token %>" />
                                <form:hidden path="productId" value="<%= productId %>" />
                                <form:button class="btn btn-primary">Make Offer</form:button>
                            </div>
                        </form:form>
                        <% } else { %>
                        <div class="sold-message">
                            This product has been sold and is no longer available for offers.
                        </div>
                        <% } %>
        <%
                    } else {
                        out.println("<p>Product not found!</p>");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    out.println("<p>Unable to fetch product details!</p>");
                }
            } else {
                out.println("<p>Product ID is missing!</p>");
            }
        %>
    </div>

    <% if (offerStatus != null) { %>
        <div class="<%= offerStatus.equals("OFFER_SUCCESS") ? "success-message" : "error-message" %>">
                <%
                    switch (offerStatus) {
                        case "BINDING_ERROR":
                            out.println("Please check the following:<br>");
                            out.println("• Offer price must be greater than 0<br>");
                            out.println("• All required fields must be filled out");
                            break;
                        case "TOKEN_PARSE_ERROR":
                            out.println("Your session has expired. Please log in again.");
                            break;
                        case "USER_NOT_FOUND":
                            out.println("User account not found. Please log in again.");
                            break;
                        case "INVALID_PRICE":
                            out.println("Please enter a valid positive amount for your offer.");
                            break;
                        case "PRODUCT_SOLD":
                            out.println("This product has already been sold and is no longer available for offers.");
                            break;
                        case "PRODUCT_NOT_FOUND":
                            out.println("The product you're trying to make an offer for could not be found.");
                            break;
                        case "OFFER_FAILED":
                            out.println("Unable to make offer. Please try again later.");
                            break;
                        case "OFFER_SUCCESS":
                            out.println("Your offer has been successfully submitted! The seller will review your offer.");
                            break;
                        default:
                            out.println("An error occurred while processing your offer. Please try again.");
                            break;
                    }
                %>
            </div>
    <% } %>
</div>
</body>
</html>
