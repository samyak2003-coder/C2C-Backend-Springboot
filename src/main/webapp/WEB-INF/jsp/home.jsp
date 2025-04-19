<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>C2C Web App - Home</title>
    <link rel="stylesheet" href="/css/styles.css">
    <script src="/js/apiCaller.js"></script>

    <style>
    /* Products container */
    .products-container {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
        gap: var(--spacing-lg);
        max-width: 1200px;
        margin: 0 auto;
        padding: var(--spacing-md);
    }

    /* Product card */
    .product-box {
        background-color: var(--surface);
        border: 1px solid var(--border-color);
        border-radius: var(--border-radius-lg);
        padding: var(--spacing-lg);
        cursor: pointer;
        transition: transform var(--transition-speed), box-shadow var(--transition-speed);
    }

    .product-box:hover {
        transform: translateY(-4px);
        box-shadow: 0 6px 12px rgba(0, 0, 0, 0.2);
    }

    /* Product content */
    .product-content {
        display: flex;
        flex-direction: column;
        gap: var(--spacing-sm);
    }

    .product-id {
        color: var(--text-muted);
        font-size: 0.9rem;
        margin-bottom: var(--spacing-xs);
    }

    .product-metadata {
        margin-top: var(--spacing-md);
        padding-top: var(--spacing-sm);
        border-top: 1px solid var(--border-color);
        font-size: 0.9rem;
        display: grid;
        grid-template-columns: repeat(2, 1fr);
        gap: var(--spacing-sm);
    }

    .product-description {
        color: var(--text-muted);
        font-size: 0.9rem;
        line-height: 1.4;
        margin-bottom: var(--spacing-sm);
        display: -webkit-box;
        -webkit-line-clamp: 2;
        -webkit-box-orient: vertical;
        overflow: hidden;
    }

    .product-metadata > div {
        color: var(--text-muted);
    }

    .metadata-label {
        font-weight: 600;
        color: var(--text-primary);
        margin-right: var(--spacing-xs);
        display: block;
        margin-bottom: 2px;
    }

    .metadata-value {
        color: var(--text-muted);
        display: block;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
    }

    .product-title {
        font-size: 1.25rem;
        font-weight: bold;
        color: var(--text-primary);
        margin-bottom: var(--spacing-xs);
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
    }

    .product-category {
        display: inline-block;
        background-color: var(--primary-color);
        color: var(--text-primary);
        padding: var(--spacing-xs) var(--spacing-sm);
        border-radius: var(--border-radius-sm);
        font-size: 0.9rem;
        font-weight: bold;
        text-transform: uppercase;
        align-self: flex-start;
    }

    .product-price {
        font-size: 1.5rem;
        font-weight: bold;
        color: var(--price-color);
        margin: var(--spacing-sm) 0;
    }

    .product-price::before {
        content: '₹';
        margin-right: 2px;
    }

    /* Status filters */
    /* Search bar */
    .search-container {
        padding: var(--spacing-md);
        margin-bottom: var(--spacing-md);
    }

    .search-input {
        width: 100%;
        max-width: 500px;
        padding: var(--spacing-sm) var(--spacing-lg);
        border: 2px solid var(--border-color);
        border-radius: var(--border-radius-lg);
        font-size: 1.1rem;
        background: var(--surface);
        color: var(--text-primary);
        transition: border-color var(--transition-speed);
    }

    .search-input:focus {
        outline: none;
        border-color: var(--primary-color);
    }

    .search-input::placeholder {
        color: var(--text-muted);
    }

    .status-filters {
        display: flex;
        gap: var(--spacing-md);
        padding: var(--spacing-md);
        justify-content: flex-start;
        margin-bottom: var(--spacing-md);
    }

    .status-button {
        padding: var(--spacing-sm) var(--spacing-lg);
        border: 2px solid var(--primary-color);
        border-radius: var(--border-radius-lg);
        background: transparent;
        color: var(--text-light);
        cursor: pointer;
        transition: all var(--transition-speed);
        font-size: 1.1rem;
        font-weight: 500;
        min-width: 140px;
    }

    .status-button:hover {
        background-color: var(--primary-color);
        color: var(--text-light);
    }

    .status-button.active {
        background-color: var(--primary-color);
        color: var(--text-light);
    }

    /* Card status */
    .card-status {
        display: inline-block;
        padding: var(--spacing-xs) var(--spacing-sm);
        border-radius: var(--border-radius-sm);
        font-size: 0.9rem;
        font-weight: bold;
        text-transform: uppercase;
        margin-top: var(--spacing-xs);
        width: fit-content;
    }

    .card-status.active {
        background-color: var(--success-color);
        color: var(--text-light);
    }

    .card-status.inactive {
        background-color: var(--text-muted);
        color: var(--text-light);
    }

    /* Message states */
    .no-products {
        text-align: center;
        color: var(--text-muted);
        padding: var(--spacing-xl);
        font-size: 1.2rem;
    }

    .error-message {
        background-color: rgba(255, 68, 68, 0.15);
        border: 1px solid rgba(255, 68, 68, 0.2);
        color: var(--error-color);
        padding: var(--spacing-md);
        border-radius: var(--border-radius-md);
        text-align: center;
        margin: var(--spacing-lg) auto;
        max-width: 600px;
    }

    /* Responsive adjustments */
    @media (max-width: 768px) {
        .products-container {
            grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
            gap: var(--spacing-md);
            padding: var(--spacing-md);
        }

        .status-filters {
            flex-wrap: wrap;
            gap: var(--spacing-xs);
            justify-content: center;
        }

        .status-button {
            font-size: 1rem;
            padding: 8px 16px;
            min-width: auto;
        }

        .product-box {
            padding: var(--spacing-sm);
        }
    }
    </style>
</head>
<body>
<div id="navbar"><jsp:include page="navbar.jsp"></jsp:include></div>
<div class="main-content">
    <div class="search-container">
        <input type="text" class="search-input" placeholder="Search products..." id="search-input">
    </div>
    <div class="status-filters">
        <button class="status-button" data-status="ALL">All</button>
        <button class="status-button active" data-status="AVAILABLE">Available</button>
        <button class="status-button" data-status="SOLD">Sold</button>
    </div>

    <div class="products-container" id="products-container">
        <!-- Products will be populated here -->
    </div>
</div>

<script>
document.addEventListener('DOMContentLoaded', async function() {
    const token = localStorage.getItem('token');
    if (!token) {
        window.location.href = '/signin';
        return;
    }

    try {
        // Validate token first
        const validationResponse = await apiCaller.get('/api/auth/validate-token');
        if (!validationResponse.success) {
            throw new Error('Invalid token');
        }

        // Fetch products
        const response = await apiCaller.get('/api/products');
        if (!response.success) {
            throw new Error(response.message);
        }

        const products = response.data;
        const container = document.getElementById('products-container');

        if (products.length === 0) {
            container.innerHTML = '<p class="no-products">No products available</p>';
            return;
        }

        products.forEach(product => {
            const productData = {
                status: product.status,
                statusClass: product.status === 'AVAILABLE' ? 'active' : 'inactive',
                title: product.title,
                category: product.category,
                price: product.price,
                id: product.id,
                description: product.description,
                createdDate: product.createdDate
            };
            
            const productBox = document.createElement('div');
            productBox.className = 'product-box';
            productBox.setAttribute('data-product-id', productData.id);
            productBox.setAttribute('data-status', productData.status);
            
            productBox.innerHTML =
                '<div class="product-content">' +
                '<div class="product-title">' + productData.title + '</div>' +
                (productData.description ? '<div class="product-description">' + productData.description.substring(0, 100) + (productData.description.length > 100 ? '...' : '') + '</div>' : '') +
                '<div class="product-category">' + productData.category + '</div>' +
                '<div class="product-price">' + productData.price + '</div>' +
                '<div class="card-status ' + productData.statusClass + '">' + productData.status + '</div>' +
                '<div class="product-metadata">' +
                '<div><span class="metadata-label">Posted on</span><span class="metadata-value">' + new Date(productData.createdDate).toLocaleDateString('en-IN') + '</span></div>' +
                '</div>' +
                '</div>';
            
            productBox.onclick = () => {
                window.location = '/productView?productId=' + product.id;
            };
            
            container.appendChild(productBox);
        });

        // Set up filter functionality
        const statusButtons = document.querySelectorAll('.status-button');
        const searchInput = document.getElementById('search-input');

        function updateProductVisibility(status, searchTerm = '') {
            const products = document.querySelectorAll('.product-box');
            const searchLower = searchTerm.toLowerCase();
            
            products.forEach(product => {
                const productStatus = product.getAttribute('data-status');
                const productTitle = product.querySelector('.product-title').textContent.toLowerCase();
                const productCategory = product.querySelector('.product-category').textContent.toLowerCase();
                const productDescription = product.querySelector('.product-description')?.textContent.toLowerCase() || '';
                
                const matchesSearch = searchTerm === '' || 
                    productTitle.includes(searchLower) || 
                    productCategory.includes(searchLower) || 
                    productDescription.includes(searchLower);
                
                const matchesStatus = status === 'ALL' || productStatus === status;
                
                product.style.display = (matchesSearch && matchesStatus) ? 'block' : 'none';
            });
        }

        // Add search functionality
        let searchTimeout;
        searchInput.addEventListener('input', (e) => {
            clearTimeout(searchTimeout);
            searchTimeout = setTimeout(() => {
                const activeStatus = document.querySelector('.status-button.active').getAttribute('data-status');
                updateProductVisibility(activeStatus, e.target.value);
            }, 300);
        });

        statusButtons.forEach(button => {
            button.addEventListener('click', () => {
                statusButtons.forEach(btn => btn.classList.remove('active'));
                button.classList.add('active');
                updateProductVisibility(button.getAttribute('data-status'));
            });
        });

        // Show only available products by default
        updateProductVisibility('AVAILABLE');

    } catch (error) {
        console.error('Error:', error);
        const container = document.getElementById('products-container');
        container.innerHTML = '<p class="error-message">Error loading products. Please try again.</p>';
        
        if (error.message === 'Invalid token') {
            localStorage.removeItem('token');
            localStorage.removeItem('user');
            window.location.href = '/signin';
        }
    }
});
</script>
</body>
</html>
