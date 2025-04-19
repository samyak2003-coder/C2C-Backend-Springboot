<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>C2C Web App - Admin Login</title>
    <link rel="stylesheet" href="/css/styles.css">
    <script src="/js/apiCaller.js"></script>

    <style>
    /* Auth form layout */
    .form-container {
        max-width: 400px;
        margin: 80px auto 0;
        padding: var(--spacing-lg);
        background-color: var(--surface-dark);
        border-radius: var(--border-radius-lg);
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        border: 1px solid var(--primary-color);
    }

    .form-container h1 {
        text-align: center;
        color: var(--text-light);
        font-size: 2rem;
        margin-bottom: var(--spacing-lg);
        font-weight: bold;
        border-bottom: 2px solid var(--primary-color);
        padding-bottom: var(--spacing-sm);
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

    /* Form controls */
    .form-control {
        width: 100%;
        padding: var(--spacing-sm);
        border: 1px solid rgba(255, 255, 255, 0.1);
        border-radius: var(--border-radius-md);
        background-color: var(--background-dark);
        color: var(--text-light);
        font-size: 1rem;
        transition: all var(--transition-speed);
    }

    .form-control:focus {
        outline: none;
        border-color: var(--primary-color);
        box-shadow: 0 0 0 2px rgba(45, 135, 240, 0.2);
    }

    /* Admin button styles */
    .btn {
        width: 100%;
        padding: var(--spacing-md);
        margin-top: var(--spacing-md);
        font-size: 1.1rem;
        font-weight: bold;
        text-transform: uppercase;
        letter-spacing: 0.5px;
        background: var(--primary-color);
        color: var(--text-light);
        border: none;
        border-radius: var(--border-radius-md);
        cursor: pointer;
        transition: all var(--transition-speed);
    }

    .btn:hover {
        background: var(--primary-hover);
        transform: translateY(-2px);
    }

    /* Error message */
    .alert-danger {
        background-color: rgba(255, 68, 68, 0.15);
        border: 1px solid rgba(255, 68, 68, 0.2);
        color: var(--error-color);
        padding: var(--spacing-md);
        border-radius: var(--border-radius-md);
        margin-top: var(--spacing-md);
        font-size: 0.9rem;
        display: none;
    }

    /* Responsive adjustments */
    @media (max-width: 768px) {
        .form-container {
            margin: 60px var(--spacing-sm) 0;
            padding: var(--spacing-md);
        }

        .form-container h1 {
            font-size: 1.75rem;
        }

        .btn {
            padding: var(--spacing-sm);
            font-size: 1rem;
        }
    }
    </style>
</head>
<body>
<div id="navbar"><jsp:include page="navbar.jsp"></jsp:include></div>

<div class="form-container">
    <h1>Admin Sign In</h1>
    <form id="loginForm" onsubmit="handleAdminLogin(event)">
        <div class="form-group">
            <label for="email">E-Mail</label>
            <input type="email" id="email" class="form-control" placeholder="Enter admin email" required/>
        </div>

        <div class="form-group">
            <label for="password">Password</label>
            <input type="password" id="password" class="form-control" placeholder="Enter admin password" required/>
        </div>

        <button type="submit" class="btn">Log in as Admin</button>
        <div id="error-message" class="alert alert-danger"></div>
    </form>
</div>

<script>
async function handleAdminLogin(event) {
    event.preventDefault();
    const errorDiv = document.getElementById('error-message');
    errorDiv.style.display = 'none';

    try {
        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;

        const response = await apiCaller.post('/api/auth/signin', { email, password });

        if (!response.success) {
            throw new Error(response.message);
        }
        
        const user = response.data.user;
        if (user.role !== 'ADMIN') {
            throw new Error('Access denied. Admin privileges required.');
        }
        
        localStorage.setItem('token', response.data.token);
        localStorage.setItem('user', JSON.stringify(user));
        window.location.href = '/admin';
    } catch (error) {
        console.error('Error:', error);
        errorDiv.textContent = error.message;
        errorDiv.style.display = 'block';
    }
}

document.addEventListener('DOMContentLoaded', function() {
    try {
        const userStr = localStorage.getItem('user');
        if (userStr) {
            const user = JSON.parse(userStr);
            if (user.role === 'ADMIN') {
                window.location.href = '/admin';
            }
        }
    } catch (error) {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
    }
});
</script>
</body>
</html>
