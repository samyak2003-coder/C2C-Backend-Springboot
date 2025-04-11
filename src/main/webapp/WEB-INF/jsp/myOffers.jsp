<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page import="java.io.*, java.net.HttpURLConnection, java.net.URL" %>
<%@ page import="org.json.JSONObject,org.json.JSONArray" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>C2C Web App - My Offers</title>
    <link rel="stylesheet" href="/css/styles.css">
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

<div class="main-content">
    <div class="status-filters">
        <button class="status-button active" data-status="ongoing">Ongoing</button>
        <button class="status-button" data-status="completed">Completed</button>
    </div>

    <div class="offers-section active" data-current-status="ongoing">
        <h2 class="section-header">Offers you've made <span class="offer-count" id="my-offers-count"></span></h2>
        <div class="offers-grid">
        <%
            try {
                URL url = new URL("http://localhost:8081/api/my-offers");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("Authorization", "Bearer " + authToken);

                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder apiResponse = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        apiResponse.append(line);
                    }
                    reader.close();

                    JSONArray offers = new JSONObject(apiResponse.toString()).getJSONArray("offers");
                    int offersCount = 0;

                    for (int i = 0; i < offers.length(); i++) {
                        JSONObject offer = offers.getJSONObject(i);
                        String status = offer.getString("status");
                        String offerId = offer.getString("offerId");
                        String productId = offer.getString("productId");
                        String sellerId = offer.getString("sellerId");
                        Double offeredPrice = offer.getDouble("offeredPrice");
                        offersCount++;
        %>
                        <div class="offer-box">
                            <div class="card-id">Offer ID: <%= offerId %></div>
                            <div class="card-id">Product ID: <%= productId %></div>
                            <div class="card-id">Seller ID: <%= sellerId %></div>
                            <div class="card-price">₹<%= offeredPrice %></div>
                            <div class="card-date">Offered on: <%= offer.getString("offerDate") %></div>
                            
                            <div class="card-status <%= status.equals("Pending") ? "inactive" : "active" %>">
                                <%= status %>
                            </div>
                            <div class="product-info">
                                Product Title: <%= offer.getString("productTitle") %>
                                <div class="product-status">Product Status: <%= offer.getString("productStatus") %></div>
                            </div>
                        </div>
        <%
                    }
        %>
                    <script>
                        document.getElementById('my-offers-count').textContent = '(<%= offersCount %>)';
                    </script>
        <%
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        %>
        </div>
    </div>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        const statusButtons = document.querySelectorAll('.status-button');
        const offerBoxes = document.querySelectorAll('.offer-box');

        function updateOfferVisibility() {
            const currentStatus = document.querySelector('.offers-section').getAttribute('data-current-status');
            offerBoxes.forEach(box => {
                const offerStatus = box.querySelector('.card-status').textContent.trim();
                // Show "Ongoing" if status is "Pending", show "Completed" if not "Pending"
                const isVisible = (currentStatus === 'ongoing' && offerStatus === 'Pending') ||
                                (currentStatus === 'completed' && offerStatus !== 'Pending');
                box.style.display = isVisible ? 'flex' : 'none';
            });
        }

        statusButtons.forEach(button => {
            button.addEventListener('click', () => {
                const status = button.getAttribute('data-status');
                
                statusButtons.forEach(btn => btn.classList.remove('active'));
                button.classList.add('active');
                
                document.querySelector('.offers-section').setAttribute('data-current-status', status);
                updateOfferVisibility();
            });
        });

        // Initial visibility update
        updateOfferVisibility();
    });
</script>

</body>
</html>
