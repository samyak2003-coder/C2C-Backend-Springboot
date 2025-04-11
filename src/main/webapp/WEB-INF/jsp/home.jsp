<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.io.*, java.net.HttpURLConnection, java.net.URL" %>
<%@ page import="org.json.JSONObject" %>
<%@ page import="org.json.JSONArray" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>C2C Web App - Home</title>
    <link rel="stylesheet" href="/css/styles.css">
</head>
<body>
<div id="navbar"><jsp:include page="navbar.jsp"></jsp:include></div>
<div class="main-content">
    <div class="status-filters">
        <button class="status-button" data-status="all">All</button>
        <button class="status-button active" data-status="unsold">Available</button>
        <button class="status-button" data-status="sold">Sold</button>
    </div>

    <script>
        // Execute immediately to show only available products by default
        document.addEventListener('DOMContentLoaded', function() {
            updateProductVisibility('unsold');
        });
    </script>
    <div class="products-container">
    <%
        try {
            URL url = new URL("http://localhost:8081/get-products");
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

                JSONArray products = new JSONArray(apiResponse.toString());

                for (int i = 0; i < products.length(); i++) {
                    JSONObject product = products.getJSONObject(i);
                    String productId = product.getString("productId"); 
    %>
                    <a href="/productView?productId=<%= productId %>">
                        <div class="product-box" data-status="<%= product.getString("status").toLowerCase() %>">
                            <div class="card-title"><%= product.getString("title") %></div>
                            <div class="card-description"><%= product.getString("description") %></div>
                            <div class="card-price">₹<%= product.getDouble("price") %></div>
                        </div>
                    </a>
    <%
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    %>
    </div>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        const statusButtons = document.querySelectorAll('.status-button');
        const productBoxes = document.querySelectorAll('.product-box');

        function updateProductVisibility(status) {
            productBoxes.forEach(box => {
                if (status === 'all' || box.getAttribute('data-status') === status) {
                    box.parentElement.style.display = 'block';
                } else {
                    box.parentElement.style.display = 'none';
                }
            });
        }

        statusButtons.forEach(button => {
            button.addEventListener('click', () => {
                statusButtons.forEach(btn => btn.classList.remove('active'));
                button.classList.add('active');
                updateProductVisibility(button.getAttribute('data-status'));
            });
        });
    });
</script>

</body>
</html>
