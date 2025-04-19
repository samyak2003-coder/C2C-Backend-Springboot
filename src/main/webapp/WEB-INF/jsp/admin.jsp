<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>C2C Web App - Admin Panel</title>
    <link rel="stylesheet" href="/css/styles.css">
    <script src="/js/apiCaller.js"></script>

    <style>
    /* Admin panel layout */
    #admin-content {
        max-width: 1200px;
        margin: 0 auto;
        padding: var(--spacing-lg);
    }

    .section-heading {
        color: var(--text-light);
        font-size: 1.8rem;
        margin: var(--spacing-xl) 0 var(--spacing-lg);
        border-bottom: 2px solid var(--primary-color);
        padding-bottom: var(--spacing-sm);
    }

    /* Container styles */
    .container {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
        gap: var(--spacing-md);
    }

    /* Box styles */
    .box {
        background-color: var(--surface-dark);
        border: 1px solid rgba(255, 255, 255, 0.1);
        border-radius: var(--border-radius-lg);
        padding: var(--spacing-lg);
        transition: transform var(--transition-speed), box-shadow var(--transition-speed);
        position: relative;
    }

    .box:hover {
        transform: translateY(-4px);
        box-shadow: 0 6px 12px rgba(0, 0, 0, 0.2);
    }

    /* Item styles */
    .item {
        margin-bottom: var(--spacing-sm);
        font-size: 0.95rem;
        color: var(--text-light);
        line-height: 1.5;
    }

    .item:first-child {
        color: var(--primary-color);
        font-weight: bold;
        font-size: 1.1rem;
        margin-bottom: var(--spacing-md);
    }

    /* Admin navbar */
    .navbar {
        background-color: var(--surface-dark);
        padding: var(--spacing-md) var(--spacing-lg);
        display: flex;
        justify-content: space-between;
        align-items: center;
        border-bottom: 2px solid var(--primary-color);
    }

    .logo {
        color: var(--text-light);
        font-size: 1.5rem;
        font-weight: bold;
    }

    .buttons-container {
        display: flex;
        align-items: center;
        gap: var(--spacing-md);
    }

    .user-name {
        color: var(--text-light);
        margin-right: var(--spacing-md);
    }

    /* Delete button */
    .delete-button {
        position: absolute;
        bottom: var(--spacing-md);
        right: var(--spacing-md);
        padding: var(--spacing-xs) var(--spacing-md);
        background-color: var(--error-color);
        color: var(--text-light);
        border: none;
        border-radius: var(--border-radius-md);
        cursor: pointer;
        font-weight: 500;
        transition: all var(--transition-speed);
    }

    .delete-button:hover {
        background-color: #d32f2f;
        transform: translateY(-2px);
    }

    /* Loading and error states */
    .loading-message,
    .error-message {
        text-align: center;
        padding: var(--spacing-lg);
        border-radius: var(--border-radius-md);
        margin: var(--spacing-md) 0;
    }

    .loading-message {
        color: var(--text-muted);
        background-color: rgba(255, 255, 255, 0.05);
    }

    .error-message {
        color: var(--error-color);
        background-color: rgba(255, 68, 68, 0.1);
    }

    /* Responsive adjustments */
    @media (max-width: 768px) {
        #admin-content {
            padding: var(--spacing-md);
        }

        .container {
            grid-template-columns: 1fr;
        }

        .section-heading {
            font-size: 1.5rem;
            margin: var(--spacing-lg) 0;
        }

        .box {
            padding: var(--spacing-md);
        }

        .navbar {
            flex-direction: column;
            padding: var(--spacing-sm);
            gap: var(--spacing-sm);
            text-align: center;
        }

        .buttons-container {
            flex-direction: column;
        }
    }
    </style>
</head>
<body>
    <nav class="navbar">
        <div class="logo">C2C Admin Panel</div>
        <div class="buttons-container">
            <div class="buttons">
                <a href="#" onclick="handleLogout(event)">Logout</a>
            </div>
            <span class="user-name"></span>
        </div>
    </nav>

    <div id="admin-content" class="main-content">
        <h2 class="section-heading">Users</h2>
        <div id="users-container" class="container"></div>

        <h2 class="section-heading">Offers</h2>
        <div id="offers-container" class="container"></div>

        <h2 class="section-heading">Products</h2>
        <div id="products-container" class="container"></div>

        <h2 class="section-heading">Orders</h2>
        <div id="orders-container" class="container"></div>
    </div>

    <script>
        async function checkAuth() {
            try {
                const token = localStorage.getItem('token');
                const userStr = localStorage.getItem('user');
                
                if (!token || !userStr) {
                    throw new Error('No auth credentials');
                }

                const user = JSON.parse(userStr);
                if (user.role !== 'ADMIN') {
                    throw new Error('Not an admin');
                }

                document.querySelector('.user-name').textContent = 'Hello, ' + user.name + '!';

                const response = await apiCaller.get('/api/auth/validate-token');
                if (!response.success) {
                    throw new Error('Token validation failed');
                }

                await fetchAllData();
            } catch (error) {
                localStorage.removeItem('token');
                localStorage.removeItem('user');
                window.location.href = error.message === 'Not an admin' ? '/' : '/admin-login';
            }
        }

        async function fetchAllData() {
            try {
                await Promise.all([
                    fetchAndRender('users', renderUsers),
                    fetchAndRender('offers/all', renderOffers),
                    fetchAndRender('products', renderProducts),
                    fetchAndRender('orders', renderOrders)
                ]);
            } catch (error) {
                console.error('Error fetching data:', error);
                const containers = document.querySelectorAll('.container');
                containers.forEach(container => {
                    container.innerHTML = '<div class="error-message">Failed to load data</div>';
                });
            }
        }

        async function fetchAndRender(endpoint, renderFunction) {
            const response = await apiCaller.get('/api/' + endpoint);
            if (response.success) {
                renderFunction(response.data);
            }
        }

        async function deleteEntity(type, id) {
            if (!confirm('Are you sure you want to delete this item?')) return;

            try {
                const response = await apiCaller.delete('/api/' + type + '/' + id);
                if (response.success) {
                    await fetchAllData();
                } else {
                    throw new Error(response.message || 'Failed to delete item');
                }
            } catch (error) {
                console.error('Error deleting item:', error);
                alert('Failed to delete item. Please try again.');
            }
        }

        function renderUsers(users) {
            const container = document.getElementById('users-container');
            if (!users || users.length === 0) {
                container.innerHTML = '<div class="loading-message">No users found</div>';
                return;
            }

            container.innerHTML = users.map(user => 
                '<div class="box">' +
                '<div class="item">User ID: ' + user.id + '</div>' +
                '<div class="item">Name: ' + user.name + '</div>' +
                '<div class="item">Email: ' + user.email + '</div>' +
                '<button class="delete-button" onclick="deleteEntity(\'users\', \'' + user.id + '\')">Delete</button>' +
                '</div>'
            ).join('');
        }

        function renderOffers(offers) {
            const container = document.getElementById('offers-container');
            if (!offers || offers.length === 0) {
                container.innerHTML = '<div class="loading-message">No offers found</div>';
                return;
            }

            container.innerHTML = offers.map(offer => 
                '<div class="box">' +
                '<div class="item">Offer ID: ' + offer.offerId + '</div>' +
                '<div class="item">Buyer ID: ' + offer.buyerId + '</div>' +
                '<div class="item">Seller ID: ' + offer.sellerId + '</div>' +
                '<div class="item">Price: ₹' + offer.offeredPrice + '</div>' +
                '<div class="item">Status: ' + offer.status + '</div>' +
                '<button class="delete-button" onclick="deleteEntity(\'offers\', \'' + offer.offerId + '\')">Delete</button>' +
                '</div>'
            ).join('');
        }

        function renderProducts(products) {
            const container = document.getElementById('products-container');
            if (!products || products.length === 0) {
                container.innerHTML = '<div class="loading-message">No products found</div>';
                return;
            }

            container.innerHTML = products.map(product => 
                '<div class="box">' +
                '<div class="item">Product ID: ' + product.id + '</div>' +
                '<div class="item">Title: ' + product.title + '</div>' +
                '<div class="item">Price: ₹' + product.price + '</div>' +
                '<div class="item">Status: ' + product.status + '</div>' +
                '<button class="delete-button" onclick="deleteEntity(\'products\', \'' + product.id + '\')">Delete</button>' +
                '</div>'
            ).join('');
        }

        function renderOrders(orders) {
            const container = document.getElementById('orders-container');
            if (!orders || orders.length === 0) {
                container.innerHTML = '<div class="loading-message">No orders found</div>';
                return;
            }

            container.innerHTML = orders.map(order => 
                '<div class="box">' +
                '<div class="item">Order ID: ' + order.orderId + '</div>' +
                '<div class="item">Buyer ID: ' + order.buyerId + '</div>' +
                '<div class="item">Seller ID: ' + order.sellerId + '</div>' +
                '<div class="item">Price: ₹' + order.orderPrice + '</div>' +
                '<div class="item">Date: ' + order.orderDate + '</div>' +
                '<button class="delete-button" onclick="deleteEntity(\'orders\', \'' + order.orderId + '\')">Delete</button>' +
                '</div>'
            ).join('');
        }

        function handleLogout(event) {
            event.preventDefault();
            localStorage.removeItem('token');
            localStorage.removeItem('user');
            window.location.href = '/admin-login';
        }

        document.addEventListener('DOMContentLoaded', checkAuth);
    </script>
</body>
</html>
