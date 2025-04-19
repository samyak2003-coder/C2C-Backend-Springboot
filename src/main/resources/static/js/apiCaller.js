// Centralized API caller utility
console.log('Loading apiCaller.js...');
window.apiCaller = window.apiCaller || {
    getAuthHeader() {
        const token = localStorage.getItem('token');
        console.log('getAuthHeader - token:', token);
        return token ? `Bearer ${token}` : null;
    },

    async call(endpoint, options = {}) {
        console.log('apiCaller.call - endpoint:', endpoint, 'options:', options);
        try {
            const authHeader = this.getAuthHeader();
            const headers = {
                'Content-Type': 'application/json',
                ...options.headers
            };

            if (authHeader && !endpoint.includes('/auth/signin')) {
                headers['Authorization'] = authHeader;
            }

            const response = await fetch(endpoint, {
                ...options,
                headers
            });

            const data = await response.json();

            if (!response.ok) {
                if (response.status === 401) {
                    localStorage.removeItem('token');
                    localStorage.removeItem('user');
                    window.location.href = '/signin';
                }
                throw new Error(data.message || 'API call failed');
            }

            return data;
        } catch (error) {
            console.error('API call error:', error);
            throw error;
        }
    },

    // Common API methods
    async get(endpoint) {
        return this.call(endpoint, { method: 'GET' });
    },

    async post(endpoint, body) {
        return this.call(endpoint, {
            method: 'POST',
            body: JSON.stringify(body)
        });
    },

    async put(endpoint, body) {
        return this.call(endpoint, {
            method: 'PUT',
            body: JSON.stringify(body)
        });
    },

    async delete(endpoint) {
        return this.call(endpoint, { method: 'DELETE' });
    }
};
