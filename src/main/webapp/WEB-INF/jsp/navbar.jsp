<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script src="/js/apiCaller.js"></script>

<style>
/* Navbar container */
.navbar {
    background-color: var(--surface);
    padding: var(--spacing-md) var(--spacing-lg);
    display: flex;
    justify-content: space-between;
    align-items: center;
    border-bottom: 1px solid var(--border-color);
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    z-index: 1000;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

/* Logo styles */
.logo a {
    color: var(--text-primary);
    font-size: 1.5rem;
    font-weight: bold;
    text-decoration: none;
    transition: color var(--transition-speed);
}

.logo a:hover {
    color: var(--accent-color);
}

/* Buttons container */
.buttons-container {
    display: flex;
    align-items: center;
    gap: var(--spacing-md);
}

/* Navigation buttons */
.buttons {
    display: flex;
    align-items: center;
    gap: var(--spacing-md);
}

.buttons a {
    color: var(--text-primary);
    text-decoration: none;
    padding: var(--spacing-sm) var(--spacing-md);
    border-radius: var(--border-radius-sm);
    transition: all var(--transition-speed);
    background-color: var(--accent-color);
    font-weight: 500;
}

.buttons a:hover {
    background-color: var(--accent-hover);
    color: var(--text-primary);
    transform: translateY(-2px);
}

/* User section */
.user-name {
    color: var(--text-muted);
    margin-right: var(--spacing-sm);
    font-weight: 500;
}

/* Logout button */
.btn-logout {
    background-color: transparent;
    color: var(--text-primary);
    border: 2px solid var(--accent-color);
    padding: var(--spacing-sm) var(--spacing-md);
    border-radius: var(--border-radius-sm);
    cursor: pointer;
    transition: all var(--transition-speed);
    font-weight: 500;
}

.btn-logout:hover {
    background-color: var(--accent-color);
    color: var(--text-primary);
    transform: translateY(-2px);
}

/* Responsive adjustments */
@media (max-width: 768px) {
    .navbar {
        padding: var(--spacing-sm);
        flex-direction: column;
        gap: var(--spacing-sm);
    }

    .buttons-container {
        flex-direction: column;
        width: 100%;
        gap: var(--spacing-sm);
    }

    .buttons {
        flex-direction: column;
        width: 100%;
        gap: var(--spacing-sm);
    }

    .buttons a {
        width: 100%;
        text-align: center;
    }

    .btn-logout {
        width: 100%;
    }

    .user-name {
        text-align: center;
        margin-bottom: var(--spacing-xs);
    }
}
</style>

<script>
    async function validateToken() {
        try {
            const token = localStorage.getItem('token');
            if (!token) return false;

            const response = await apiCaller.get('/api/auth/validate-token');
            if (response.success) {
                console.log('Token validation response:', response.data);
                localStorage.setItem('user', JSON.stringify(response.data));
                return true;
            }
            return false;
        } catch (error) {
            return false;
        }
    }

    function logout() {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        window.location.href = '/signin';
    }

    function updateNavbar() {
        try {
            const userStr = localStorage.getItem('user');
            const guestNav = document.getElementById('guestNav');
            const userNav = document.getElementById('userNav');

            if (!userStr) {
                guestNav.style.display = 'flex';
                userNav.style.display = 'none';
                return;
            }

            const user = JSON.parse(userStr);
            console.log('User data from localStorage:', user);
            guestNav.style.display = 'none';
            userNav.style.display = 'flex';
            // Handle both nested and direct user data structures
            const userName = user.name || (user.user && user.user.name);
            document.getElementById('userName').textContent = 'Hello, ' + userName + '!';
            
            // Hide specific links for admin users
            const userRole = user.role || (user.user && user.user.role);
            if (userRole === 'ADMIN') {
                document.getElementById('myOffersLink').style.display = 'none';
                document.getElementById('receivedOffersLink').style.display = 'none';
                document.getElementById('sellLink').style.display = 'none';
            }
        } catch (error) {
            localStorage.removeItem('token');
            localStorage.removeItem('user');
            document.getElementById('guestNav').style.display = 'flex';
            document.getElementById('userNav').style.display = 'none';
        }
    }

    window.onload = async function() {
        const isValid = await validateToken();
        updateNavbar();
        applyStoredTheme();
        const isAuthPage = ['/signin', '/signup'].includes(window.location.pathname);
        if (!isValid && !isAuthPage) {
            window.location.href = '/signin';
        }
    };

    function toggleTheme() {
        const body = document.body;
        const isDark = !body.classList.contains('light-theme');
        body.classList.toggle('light-theme');
        localStorage.setItem('theme', isDark ? 'light' : 'dark');
        updateThemeButton();
    }

    function updateThemeButton() {
        const isDark = !document.body.classList.contains('light-theme');
        const themeButton = document.getElementById('themeToggle');
        themeButton.innerHTML = isDark ? '☀️ Light' : '🌙 Dark';
    }

    function applyStoredTheme() {
        const theme = localStorage.getItem('theme') || 'dark';
        if (theme === 'light') {
            document.body.classList.add('light-theme');
        }
        updateThemeButton();
    }
</script>

<nav class="navbar">
    <div class="logo">
        <a href="/">C2C Web App</a>
    </div>

    <div class="buttons-container">
        <!-- Guest Navigation -->
        <div id="guestNav" class="buttons" style="display: none;">
            <a href="/signin">Sign In</a>
            <a href="/signup">Sign Up</a>
        </div>

        <!-- User Navigation -->
        <div id="userNav" class="buttons" style="display: none;">
            <a href="/myOffers" id="myOffersLink">My Offers</a>
            <a href="/receivedOffers" id="receivedOffersLink">Received Offers</a>
            <a href="/sellProducts" id="sellLink">Sell</a>
            <span id="userName" class="user-name"></span>
            <button id="themeToggle" onclick="toggleTheme()" class="theme-toggle">☀️ Light</button>
            <button onclick="logout()" class="btn btn-logout">Logout</button>
        </div>
    </div>
</nav>
