<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>C2C Web App - Sell Product</title>
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
%>
<div id="navbar"><jsp:include page="navbar.jsp"></jsp:include></div>

<div class="main-content">
    <div class="form-page">
        <h1 class="page-title">Sell Product</h1>

        <div class="form-card">
            <form:form method="POST" action="/create-products" modelAttribute="sellProductDetails">
                <div class="form-group">
                    <label for="title">Title</label>
                    <form:input id="title" path="title" cssClass="form-control" placeholder="Enter product title"/>
                </div>

                <div class="form-group">
                    <label for="description">Description</label>
                    <form:textarea id="description" path="description" cssClass="form-control" rows="4" 
                        placeholder="Enter product description"/>
                </div>

                <div class="form-group">
                    <label for="price">Price (₹)</label>
                    <form:input id="price" path="price" cssClass="form-control" type="number" min="0" step="0.01"
                        placeholder="Enter price"/>
                </div>

                <div class="form-group">
                    <label for="category">Category</label>
                    <form:select id="category" path="category" cssClass="form-control">
                        <form:option value="" label="Select a category"/>
                        <form:option value="Electronics" label="Electronics"/>
                        <form:option value="Clothing" label="Clothing"/>
                        <form:option value="Books" label="Books"/>
                        <form:option value="Home & Garden" label="Home & Garden"/>
                        <form:option value="Sports" label="Sports"/>
                        <form:option value="Other" label="Other"/>
                    </form:select>
                </div>

                <div class="form-group">
                    <label for="productCondition">Product Condition</label>
                    <form:select id="productCondition" path="productCondition" cssClass="form-control">
                        <form:option value="" label="Select condition"/>
                        <form:option value="New" label="New"/>
                        <form:option value="Like New" label="Like New"/>
                        <form:option value="Good" label="Good"/>
                        <form:option value="Fair" label="Fair"/>
                        <form:option value="Poor" label="Poor"/>
                    </form:select>
                </div>

                <form:hidden path="token" value="<%= token %>" />

                <div class="form-actions">
                    <form:button class="btn btn-primary btn-block">Sell Product</form:button>
                </div>
            </form:form>
        </div>
    </div>
</div>

</body>
</html>
