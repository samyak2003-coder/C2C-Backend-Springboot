<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.io.*, java.net.HttpURLConnection, java.net.URL" %>
<%@ page import="org.json.JSONObject" %>
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
        }

        #navbar {
            margin-bottom: 20px;
            background-color: #000000;
        }

        .products-container {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
            gap: 20px;
            padding: 20px;
        }

        .product-box {
            background-color: #000000;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(255, 255, 255, 0.1);
            overflow: hidden;
            padding: 20px;
            text-align: center;
            transition: transform 0.3s ease, box-shadow 0.3s ease;
            color: white;
        }

        .product-box:hover {
            transform: translateY(-10px);
            box-shadow: 0 4px 20px rgba(255, 255, 255, 0.15);
        }

        .product-title {
            font-size: 1.5rem;
            font-weight: bold;
            color: white;
            margin-bottom: 10px;
        }

        .product-description {
            font-size: 1rem;
            color: #bbbbbb;
            margin-bottom: 15px;
            height: 60px;
            overflow: hidden;
        }

        .product-price {
            font-size: 1.25rem;
            font-weight: bold;
            color: #ffbf00;
        }

        a {
            text-decoration: none;
            color: inherit;
        }
    </style>
</head>
<body>
<div id="navbar"><jsp:include page="navbar.jsp"></jsp:include></div>
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
                        <div class="product-box">
                            <div class="product-title"><%= product.getString("title") %></div>
                            <div class="product-description"><%= product.getString("description") %></div>
                            <div class="product-price">₹<%= product.getDouble("price") %></div>
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
</body>
</html>
