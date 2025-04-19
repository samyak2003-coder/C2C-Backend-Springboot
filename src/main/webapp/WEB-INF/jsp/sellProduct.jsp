<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>C2C Web App - Sell Product</title>
    <link rel="stylesheet" href="/css/styles.css">
    <script src="/js/apiCaller.js"></script>

    <style>
    /* Form page layout */
    .form-page {
        max-width: 600px;
        margin: 0 auto;
        padding: var(--spacing-md);
    }

    .page-title {
        color: var(--text-light);
        text-align: center;
        margin-bottom: var(--spacing-lg);
        font-size: 2rem;
        font-weight: bold;
    }

    /* Form card */
    .form-card {
        background-color: var(--surface);
        padding: var(--spacing-md);
        border-radius: var(--border-radius-lg);
        border: 1px solid var(--border-color);
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.2);
        transition: transform var(--transition-speed), box-shadow var(--transition-speed);
    }

    .form-card:hover {
        transform: translateY(-4px);
        box-shadow: 0 6px 12px rgba(0, 0, 0, 0.2);
    }

    /* Form groups */
    .form-group {
        margin-bottom: var(--spacing-md);
    }

    .form-group label {
        display: block;
        margin-bottom: var(--spacing-xs);
        color: var(--text-light);
        font-weight: 500;
        font-size: 0.95rem;
    }

    /* Form controls base */
    .form-control {
        width: 100%;
        padding: var(--spacing-sm);
        border-radius: var(--border-radius-md);
        font-size: 1rem;
        transition: all var(--transition-speed);
        color: var(--text-light);
    }

    /* Form controls */
    .form-control {
        background-color: #ffffff;
        border: 1px solid #d1d1d1;
        padding: var(--spacing-sm) var(--spacing-md);
        transition: all var(--transition-speed);
        color: #333333 !important;
    }

    .form-control:focus {
        border-color: var(--primary-color);
        box-shadow: 0 0 0 2px var(--primary-color-alpha);
        outline: none;
    }

    textarea.form-control {
        resize: vertical;
        min-height: 100px;
        line-height: 1.5;
    }

    /* Select controls */
    select.form-control {
        appearance: none;
        background-image: url("data:image/svg+xml;charset=UTF-8,%3csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='%23333333'%3e%3cpath d='M7 10l5 5 5-5z'/%3e%3c/svg%3e");
        background-repeat: no-repeat;
        background-position: right 10px center;
        background-size: 20px;
        padding-right: 40px;
        cursor: pointer;
    }

    .form-control::placeholder {
        color: #666666;
    }

    /* Select options */
    select.form-control option {
        background-color: #ffffff;
        color: #333333;
    }

    select.form-control option[value=""] {
        color: #666666;
    }

    /* Form actions */
    .form-actions {
        margin-top: var(--spacing-lg);
    }

    .btn-block {
        padding: var(--spacing-md);
        font-size: 1.1rem;
        font-weight: bold;
        text-transform: uppercase;
        letter-spacing: 0.5px;
    }

    /* Error state */
    .alert-danger {
        background-color: rgba(255, 68, 68, 0.15);
        border: 1px solid rgba(255, 68, 68, 0.2);
        color: var(--error-color);
        padding: var(--spacing-md);
        border-radius: var(--border-radius-md);
        margin-top: var(--spacing-md);
        display: none;
    }

    /* Responsive adjustments */
    @media (max-width: 768px) {
        .form-page {
            padding: var(--spacing-sm);
        }

        .form-card {
            padding: var(--spacing-md);
        }

        .btn-block {
            padding: var(--spacing-sm);
            font-size: 1rem;
        }
    }

    
    </style>
</head>
<body>
<div id="navbar"><jsp:include page="navbar.jsp"></jsp:include></div>

<div class="main-content">
    <div class="form-page">
        <h1 class="page-title">Sell Product</h1>

        <div class="form-card">
            <form id="productForm" onsubmit="handleSubmit(event)">
                <div class="form-group">
                    <label for="title">Title</label>
                    <input type="text" id="title" class="form-control" placeholder="Enter product title" required/>
                </div>

                <div class="form-group">
                    <label for="description">Description</label>
                    <textarea id="description" class="form-control" rows="4" placeholder="Enter product description" required></textarea>
                </div>

                <div class="form-group">
                    <label for="price">Price (₹)</label>
                    <input type="number" id="price" class="form-control" min="0" step="0.01" placeholder="Enter price" required/>
                </div>

                <div class="form-group">
                    <label for="category">Category</label>
                    <select id="category" class="form-control" required>
                        <option value="">Select a category</option>
                        <option value="Electronics">Electronics</option>
                        <option value="Clothing">Clothing</option>
                        <option value="Books">Books</option>
                        <option value="Home & Garden">Home & Garden</option>
                        <option value="Sports">Sports</option>
                        <option value="Other">Other</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="productCondition">Product Condition</label>
                    <select id="productCondition" class="form-control" required>
                        <option value="">Select condition</option>
                        <option value="New">New</option>
                        <option value="Like New">Like New</option>
                        <option value="Good">Good</option>
                        <option value="Fair">Fair</option>
                        <option value="Poor">Poor</option>
                    </select>
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn btn-primary btn-block">Sell Product</button>
                </div>

                <div id="error-message" class="alert alert-danger"></div>
            </form>
        </div>

    </div>
</div>

<script>
document.addEventListener('DOMContentLoaded', async function() {
    try {
        const token = localStorage.getItem('token');
        if (!token) {
            throw new Error('No token found');
        }

        const response = await apiCaller.get('/api/auth/validate-token');
        if (!response.success) {
            throw new Error('Invalid token');
        }
    } catch (error) {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        window.location.href = '/signin';
    }
});

async function handleSubmit(event) {
    event.preventDefault();
    const errorDiv = document.getElementById('error-message');
    errorDiv.style.display = 'none';

    try {
        const productData = {
            title: document.getElementById('title').value,
            description: document.getElementById('description').value,
            price: parseFloat(document.getElementById('price').value),
            category: document.getElementById('category').value,
            productCondition: document.getElementById('productCondition').value
        };

        const response = await apiCaller.post('/api/products', productData);

        if (response.success) {
            window.location.href = '/';
        } else {
            throw new Error(response.message);
        }
    } catch (error) {
        console.error('Error:', error);
        errorDiv.textContent = error.message;
        errorDiv.style.display = 'block';
    }
}
</script>

</body>
</html>
