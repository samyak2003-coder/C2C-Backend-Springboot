<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>C2C Web App - Sign Up</title>
    <link rel="stylesheet" href="/css/styles.css">
    <script src="/js/apiCaller.js"></script>

    <style>
    body {
        margin: 0;
        padding: 0;
        min-height: 100vh;
        display: flex;
        align-items: center;
        justify-content: center;
    }

    /* Auth form layout */
    .form-container {
        width: 1000px;
        padding: 40px;
        background-color: var(--surface-dark);
        border-radius: 8px;
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    }

    .form-container h1 {
        text-align: center;
        color: var(--text-light);
        font-size: 2rem;
        margin-bottom: var(--spacing-lg);
        font-weight: bold;
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
        padding: 16px;
        border: 1px solid rgba(255, 255, 255, 0.1);
        border-radius: var(--border-radius-md);
        background-color: var(--background-dark);
        color: var(--text-light);
        font-size: 1rem;
        transition: all var(--transition-speed);
    }

    .form-control:focus {
        outline: none;
        border-color: var(--accent-color);
        box-shadow: 0 0 0 2px rgba(76, 175, 80, 0.2);
    }

    /* Button styles */
    .btn {
        width: 100%;
        padding: var(--spacing-md);
        margin-top: var(--spacing-md);
        font-size: 1.1rem;
        font-weight: bold;
        text-transform: uppercase;
        letter-spacing: 0.5px;
        background: var(--accent-color);
    }

    .btn:hover {
        background: var(--accent-hover);
        transform: translateY(-2px);
    }

    /* Sign in link */
    .signin-link {
        display: block;
        text-align: center;
        margin-top: var(--spacing-lg);
        color: var(--text-muted);
        text-decoration: none;
        font-size: 0.9rem;
        transition: color var(--transition-speed);
    }

    .signin-link:hover {
        color: var(--accent-color);
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
            width: 90%;
            padding: 20px;
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
    <h1>Sign Up</h1>
    <form id="signupForm" onsubmit="handleSignup(event)">
        <div class="form-group">
            <label for="email">E-Mail</label>
            <input type="email" id="email" name="email" class="form-control" placeholder="Enter your email" required/>
        </div>

        <div class="form-group">
            <label for="name">Name</label>
            <input type="text" id="name" name="name" class="form-control" placeholder="Enter your name" required/>
        </div>

        <div class="form-group">
            <label for="password">Password</label>
            <input type="password" id="password" name="password" class="form-control" placeholder="Choose a password" required/>
        </div>

        <div class="form-group">
            <label for="role">Role</label>
            <select id="role" name="role" class="form-control" required>
                <option value="USER">User</option>
                <option value="ADMIN">Admin</option>
            </select>
        </div>

        <button type="submit" class="btn btn-primary">Sign Up</button>
        <div id="error-message" class="alert alert-danger"></div>
    </form>

    <a href="/signin" class="signin-link">Already have an account? Sign in here</a>
</div>

<script>
async function handleSignup(event) {
    event.preventDefault();
    const errorDiv = document.getElementById('error-message');
    errorDiv.style.display = 'none';

    try {
        const response = await apiCaller.post('/api/auth/signup', {
            email: document.getElementById('email').value,
            name: document.getElementById('name').value,
            password: document.getElementById('password').value,
            role: document.getElementById('role').value
        });

        if (response.success) {
            localStorage.setItem('token', response.data.token);
            localStorage.setItem('user', JSON.stringify(response.data.user));
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
