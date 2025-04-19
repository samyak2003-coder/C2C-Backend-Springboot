<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>C2C Web App - My Offers</title>
    <link rel="stylesheet" href="/css/styles.css">
    <script src="/js/apiCaller.js"></script>

    <style>
    /* Offers section */
    .offers-section {
        max-width: 1000px;
        margin: 0 auto;
        padding: var(--spacing-md);
    }

    .section-header {
        color: var(--text-light);
        font-size: 1.8rem;
        margin-bottom: var(--spacing-lg);
        border-bottom: 2px solid var(--primary-color);
        padding-bottom: var(--spacing-sm);
    }

    /* Offers grid */
    .offers-grid {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
        gap: var(--spacing-lg);
    }

    /* Status filters */
    .status-filters {
        display: flex;
        gap: var(--spacing-md);
        padding: var(--spacing-md);
        justify-content: flex-start;
        margin-bottom: var(--spacing-lg);
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

    /* Offer box */
    .offer-box {
        background-color: var(--surface);
        border: 1px solid var(--border-color);
        border-radius: var(--border-radius-lg);
        padding: var(--spacing-lg);
        transition: transform var(--transition-speed), box-shadow var(--transition-speed);
    }

    .offer-box:hover {
        transform: translateY(-4px);
        box-shadow: 0 6px 12px rgba(0, 0, 0, 0.2);
    }

    /* Card elements */
    .card-id {
        color: var(--text-muted);
        font-size: 0.9rem;
        margin-bottom: var(--spacing-xs);
    }

    .card-price {
        font-size: 1.5rem;
        font-weight: bold;
        color: var(--price-color);
        margin: var(--spacing-sm) 0;
    }

    .card-price::before {
        content: '₹';
        margin-right: 2px;
    }

    .card-date {
        color: var(--text-muted);
        font-size: 0.9rem;
        margin-bottom: var(--spacing-sm);
    }

    /* Status indicators */
    .card-status {
        display: inline-block;
        padding: var(--spacing-xs) var(--spacing-sm);
        border-radius: var(--border-radius-sm);
        font-size: 0.9rem;
        font-weight: bold;
        text-transform: uppercase;
        margin: var(--spacing-sm) 0;
    }

    .card-status.active {
        background-color: var(--success-color);
        color: var(--text-light);
    }

    .card-status.inactive {
        background-color: var(--text-muted);
        color: var(--text-light);
    }

    .card-status.rejected {
        background-color: var(--error-color);
        color: var(--text-light);
    }

    /* Product info */
    .product-info {
        margin-top: var(--spacing-md);
        padding-top: var(--spacing-sm);
        border-top: 1px solid var(--border-color);
        font-size: 0.95rem;
    }

    .product-info > div {
        margin-bottom: var(--spacing-xs);
    }

    .product-status {
        color: var(--text-muted);
    }

    /* Error message */
    .error-message {
        text-align: center;
        color: var(--error-color);
        background-color: rgba(255, 68, 68, 0.1);
        padding: var(--spacing-lg);
        border-radius: var(--border-radius-md);
        margin: var(--spacing-xl) auto;
        max-width: 600px;
    }

    /* Responsive adjustments */
    @media (max-width: 768px) {
        .offers-grid {
            grid-template-columns: 1fr;
            gap: var(--spacing-md);
        }

        .status-filters {
            flex-wrap: wrap;
            gap: var(--spacing-xs);
        }

        .status-button {
            font-size: 1rem;
            padding: var(--spacing-xs) var(--spacing-md);
            min-width: 120px;
        }

        .section-header {
            font-size: 1.5rem;
            margin-bottom: var(--spacing-md);
        }

        .offer-box {
            padding: var(--spacing-md);
        }
    }
    </style>
</head>
<body>
<div id="navbar"><jsp:include page="navbar.jsp"></jsp:include></div>

<div class="main-content">
    <div class="status-filters">
        <button class="status-button active" data-status="ongoing">Ongoing</button>
        <button class="status-button" data-status="completed">Completed</button>
    </div>

    <div class="offers-section">
        <!-- <h2 class="section-header">Offers you've made</h2> -->
        <div class="offers-grid" id="offers-container">
            <!-- Offers will be populated here -->
        </div>
    </div>
</div>

<script>
document.addEventListener('DOMContentLoaded', async function() {
    try {
        const response = await apiCaller.get('/api/offers/my-offers');
        const offers = response.data;
        const container = document.getElementById('offers-container');
        
        if (!offers || offers.length === 0) {
            container.innerHTML = '<div class="error-message">No offers found</div>';
            return;
        }

        offers.forEach(offer => {
            const offerData = {
                status: offer.offerStatus.toString(),
                statusClass: 'active',
                offerId: offer.offerId,
                productId: offer.productId,
                price: offer.price,
                date: offer.createdDate,
                productTitle: offer.productTitle,
                productStatus: offer.productStatus
            };

            switch(offerData.status) {
                case 'PENDING':
                    offerData.statusClass = 'inactive';
                    break;
                case 'REJECTED':
                    offerData.statusClass = 'rejected';
                    break;
                case 'ACCEPTED':
                    offerData.statusClass = 'active';
                    break;
            }
            
            const offerBox = document.createElement('div');
            offerBox.className = 'offer-box';
            offerBox.setAttribute('data-status', offerData.status.toLowerCase());
            
            offerBox.innerHTML = 
                '<div class="card-id">Offer ID: ' + offerData.offerId + '</div>' +
                '<div class="card-id">Product ID: ' + offerData.productId + '</div>' +
                '<div class="card-price">' + offerData.price + '</div>' +
                '<div class="card-date">Offered on: ' + offerData.date + '</div>' +
                '<div class="card-status ' + offerData.statusClass + '">' +
                offerData.status + '</div>' +
                '<div class="product-info">' +
                '<div>Product Title: ' + offerData.productTitle + '</div>' +
                '<div class="product-status">Product Status: ' + offerData.productStatus + '</div>' +
                '</div>';
            
            container.appendChild(offerBox);
        });
        
        // Set up filter functionality
        const statusButtons = document.querySelectorAll('.status-button');
        function updateOfferVisibility(status) {
            const offers = document.querySelectorAll('.offer-box');
            const container = document.getElementById('offers-container');
            let visibleCount = 0;

            offers.forEach(offer => {
                const offerStatus = offer.getAttribute('data-status').toLowerCase();
                const isOngoing = status === 'ongoing' && offerStatus === 'pending';
                const isCompleted = status === 'completed' && offerStatus !== 'pending';
                const isVisible = isOngoing || isCompleted;
                offer.style.display = isVisible ? 'block' : 'none';
                if (isVisible) visibleCount++;
            });

            // Show message if no offers are visible for current filter
            const existingMessage = container.querySelector('.error-message');
            if (existingMessage) {
                existingMessage.remove();
            }

            if (visibleCount === 0) {
                const message = document.createElement('div');
                message.className = 'error-message';
                message.textContent = status === 'ongoing' ? 'No ongoing offers found' : 'No completed offers found';
                container.appendChild(message);
            }
        }

        statusButtons.forEach(button => {
            button.addEventListener('click', () => {
                statusButtons.forEach(btn => btn.classList.remove('active'));
                button.classList.add('active');
                updateOfferVisibility(button.getAttribute('data-status'));
            });
        });

        // Initial visibility update
        updateOfferVisibility('ongoing');
        
    } catch (error) {
        console.error('Error fetching offers:', error);
        const container = document.getElementById('offers-container');
        container.innerHTML = '<div class="error-message">Failed to load offers. Please try again later.</div>';
    }
});
</script>
</body>
</html>
