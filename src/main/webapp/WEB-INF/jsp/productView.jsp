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

         * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: Arial, sans-serif;
        }

        body {
            background-color: #f4f7fc;
        }

        /* Styling for the product detail box */
        .product-detail {
            background-color: #000000; /* Changed to black */
            border-radius: 10px;
            padding: 30px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            margin: 40px auto;
            max-width: 900px;
            text-align: left;
            font-family: Arial, sans-serif;
            color: #ffffff; /* Changed text color to white */
        }

        .product-title {
            font-size: 2.5rem;
            font-weight: bold;
            margin-bottom: 20px;
            color: #ffffff; /* Changed to white */
        }

        .product-description {
            font-size: 1.1rem;
            color: #dcdcdc; /* Light gray for better readability */
            margin-bottom: 20px;
            line-height: 1.6;
        }

        .product-price {
            font-size: 1.75rem;
            font-weight: bold;
            color: #2d87f0;
            margin-bottom: 20px;
        }

        .product-category, .product-condition, .product-seller, .product-status {
            font-size: 1.2rem;
            color: #dcdcdc; /* Light gray for better contrast */
            margin-bottom: 10px;
        }

        .product-category span, .product-condition span, .product-seller span, .product-status span {
            font-weight: bold;
            color: #ffffff; /* Changed labels to white */
        }

        /* Additional styles for better layout */
        .detail-section {
            margin-bottom: 20px;
        }

        .detail-section p {
            margin: 5px 0;
        }

        .back-link {
            display: inline-block;
            margin-top: 20px;
            font-size: 1.2rem;
            color: #2d87f0;
            text-decoration: none;
            border: 2px solid #2d87f0;
            padding: 10px 20px;
            border-radius: 5px;
        }

        .back-link:hover {
            background-color: #2d87f0;
            color: white;
        }
    </style>
</head>
<body>
    <div id="navbar"><jsp:include page="navbar.jsp"></jsp:include></div>

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

        %>
                        <div class="product-title"><%= product.getString("title") %></div>
                        <div class="product-description"><%= product.getString("description") %></div>
                        <div class="product-price">₹<%= product.getDouble("price") %></div>

                        <div class="detail-section">
                            <p class="product-category"><span>Category:</span> <%= product.getString("category") %></p>
                            <p class="product-condition"><span>Condition:</span> <%= product.getString("productCondition") %></p>
                            <p class="product-seller"><span>Seller ID:</span> <%= product.getString("sellerId") %></p>
                            <p class="product-status"><span>Status:</span> <%= product.getString("status") %></p>
                        </div>

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
</body>
</html>
