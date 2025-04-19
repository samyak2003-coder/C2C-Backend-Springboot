
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>C2C Web App - Product Details</title>
<link rel="stylesheet" href="/css/styles.css">
<script src="/js/apiCaller.js"></script>

<style>
/* Product page layout */
.section-container {
    max-width: 1200px;
    margin: 0 auto;
    padding: var(--spacing-lg);
}

.product-offer-wrapper {
    display: flex;
    flex-direction: column;
    gap: var(--spacing-lg);
    max-width: 800px;
    margin: 0 auto;
}

/* Product detail card */
.product-detail {
    width: 100%;
    background-color: var(--surface);
    border-radius: var(--border-radius-lg);
    padding: var(--spacing-lg);
    display: flex;
    flex-direction: column;
    justify-content: space-between;
}

.product-title {
    font-size: 1.8rem;
    margin-bottom: var(--spacing-xs);
    padding-bottom: var(--spacing-xs);
    border-bottom: 2px solid var(--primary-color);
}

.product-description {
    margin: var(--spacing-xs) 0 var(--spacing-sm);
    color: var(--text-muted);
    line-height: 1.5;
}

/* Product info layout */
.product-info-container {
    display: flex;
    gap: var(--spacing-sm);
    margin-top: var(--spacing-sm);
    align-items: stretch;
}

.product-left-section {
    width: 220px;
    display: flex;
    flex-direction: column;
    justify-content: center;
}

/* Price section */
.product-price-section {
    padding: var(--spacing-md);
    border-radius: var(--border-radius-md);
    text-align: center;
    background-color: rgba(255, 255, 255, 0.05);
}

.product-price {
    font-size: 2rem;
    font-weight: bold;
    color: var(--text-light);
}

/* Details section */
.detail-section {
    flex: 1;
    background-color: rgba(255, 255, 255, 0.05);
    padding: var(--spacing-md);
    border-radius: var(--border-radius-md);
    display: flex;
    flex-direction: column;
    justify-content: center;
}

.product-category {
    display: inline-block;
    background-color: var(--primary-color);
    color: var(--text-light);
    padding: var(--spacing-xs) var(--spacing-sm);
    border-radius: var(--border-radius-sm);
    font-size: 0.9rem;
    font-weight: bold;
    text-transform: uppercase;
    align-self: flex-start;
    margin-bottom: var(--spacing-sm);
    display: flex;
    flex-direction: column;
    justify-content: space-between;
}

.bid-header {
    padding-bottom: var(--spacing-sm);
    border-bottom: 1px solid rgba(255, 255, 255, 0.1);
    margin-bottom: var(--spacing-sm);
}

.bid-header h3 {
    font-size: 1.2rem;
    margin: 0;
}

.bid-content {
    flex: 1;
    display: flex;
    flex-direction: column;
    justify-content: center;
}

/* Offer form */
.bid-input-group {
    display: flex;
    flex-direction: column;
    gap: var(--spacing-sm);
}

.bid-input-group input {
    padding: var(--spacing-md);
    font-size: 1rem;
    border-radius: var(--border-radius-md);
    border: 1px solid rgba(255, 255, 255, 0.1);
    background: var(--background-dark);
    color: var(--text-light);
}

.bid-input-group button {
    padding: var(--spacing-md) var(--spacing-lg);
    font-size: 1rem;
    border-radius: var(--border-radius-md);
    background: var(--accent-color);
    color: var(--text-light);
    border: none;
    cursor: pointer;
    transition: background var(--transition-speed);
}

.bid-input-group button:hover {
    background: var(--accent-hover);
}

/* Status messages */
.sold-message {
    text-align: center;
    padding: var(--spacing-sm);
    background-color: rgba(255, 193, 7, 0.15);
    border-radius: var(--border-radius-md);
}

.message {
    padding: var(--spacing-sm);
    margin: var(--spacing-xs) 0;
    border-radius: var(--border-radius-md);
    display: none;
}

.success-message { 
    background-color: rgba(76, 175, 80, 0.15);
    color: var(--success-color);
    border: 1px solid rgba(76, 175, 80, 0.2);
}

.error-message { 
    background-color: rgba(255, 68, 68, 0.15);
    color: var(--error-color);
    border: 1px solid rgba(255, 68, 68, 0.2);
}

/* Admin delete button */
.btn-danger {
    background-color: #dc3545;
    color: #fff;
    padding: var(--spacing-md) var(--spacing-lg);
    border: none;
    border-radius: var(--border-radius-md);
    cursor: pointer;
    transition: background-color var(--transition-speed);
    margin-top: var(--spacing-md);
    font-weight: 500;
}

.btn-danger:hover {
    background-color: #c82333;
}
</style>
</head>

<body>
<div id="navbar"><jsp:include page="navbar.jsp"></jsp:include></div>

<div class="main-content">
  <div class="section-container">
    <div class="product-offer-wrapper">
      <div class="product-detail">
        <div id="product-container">
          <div class="loading">Loading product details...</div>
        </div>
        <div id="message-container" class="message"></div>
      </div>
      <div id="offer-container"></div>
    </div>
  </div>
</div>

<script>
function showMessage(message, isError = false) {
    const container = document.getElementById('message-container');
    container.textContent = message;
    container.className = `message ${isError ? 'error-message' : 'success-message'}`;
    container.style.display = 'block';
    setTimeout(() => container.style.display = 'none', 5000);
}

async function loadProduct() {
    const urlParams = new URLSearchParams(window.location.search);
    const productId = urlParams.get('productId');

    if (!productId) {
        showMessage('Product ID is missing', true);
        return;
    }

    try {
        const response = await apiCaller.get('/api/products/' + productId);
        if (!response.success) throw new Error(response.message || 'Failed to fetch product details');

        const product = response.data;
        const user = JSON.parse(localStorage.getItem('user'));
        const container = document.getElementById('product-container');
        const offerContainer = document.getElementById('offer-container');

        const isAdmin = user.role === 'ADMIN';
        const canMakeOffer = user.id !== product.sellerId && product.status !== 'SOLD';
        const isSold = product.status === 'SOLD';

        container.innerHTML = 
            '<div class="product-title">' + product.title + '</div>' +
            '<div class="product-description">' + product.description + '</div>' +
            '<div class="product-info-container">' +
                '<div class="product-left-section">' +
                    '<div class="product-price-section">' +
                        '<div class="product-price">₹' + product.price + '</div>' +
                    '</div>' +
                '</div>' +
                '<div class="detail-section">' +
                    '<div class="product-category">' + product.category + '</div>' +
                    '<p><strong>Condition:</strong> ' + (product.condition || 'Not specified') + '</p>' +
                    '<p><strong>Seller ID:</strong> ' + product.sellerId + '</p>' +
                    '<p><strong>Status:</strong> ' + product.status + '</p>' +
                '</div>' +
            '</div>' +
            (isAdmin ? '<div class="admin-actions">' +
            '<button class="btn btn-danger" onclick="deleteProduct(\'' + product.id + '\')">Delete Product (Admin)</button>' +
            '</div>' : '');

        offerContainer.innerHTML =
            (canMakeOffer || isSold ?
                '<div class="bid-card">' +
                    '<div class="bid-header">' +
                        '<h3>Make an Offer</h3>' +
                    '</div>' +
                    '<div class="bid-content">' +
                        (!isSold ?
                        '<form id="offerForm" onsubmit="submitOffer(event)">' +
                            '<div class="bid-input-group">' +
                                '<input type="number" id="offeredPrice" class="form-control" ' +
                                'placeholder="Your offer amount" step="0.01" min="0.01" required ' +
                                'oninput="validateOfferAmount()" />' +
                                '<div id="offer-error" class="error-message" style="display:none;"></div>' +
                                '<input type="hidden" id="productId" value="' + product.id + '" />' +
                                '<input type="hidden" id="sellerId" value="' + product.sellerId + '" />' +
                                '<button type="submit" class="btn btn-primary">Submit Offer</button>' +
                            '</div>' +
                        '</form>' :
                        '<div class="sold-message">This product has been sold and is no longer available for offers.</div>') +
                    '</div>' +
                '</div>' : '');

        // Display offers from product response
        if (product.offers && product.offers.length > 0) {
            // Sort offers by price in descending order
            const sortedOffers = product.offers.sort((a, b) => b.price - a.price);
            const highestOffer = sortedOffers[0];
            
            offerContainer.innerHTML +=
                '<div class="offers-section">' +
                    '<div class="offers-header">Product Offers</div>' +
                    '<div id="offers-list">' +
                    sortedOffers.map(function(offer) {
                        const isHighest = offer.price === highestOffer.price;
                        return '<div class="offer-item ' + (isHighest ? 'highest-offer' : '') + '">' +
                            '<div class="offer-info">' +
                                '<span class="offer-buyer">Buyer ID: ' + offer.buyerId + '</span>' +
                                '<span class="offer-amount">₹' + offer.price + '</span>' +
                            '</div>' +
                            (isHighest ? '<div class="highest-badge">Highest Offer</div>' : '') +
                        '</div>';
                    }).join('') +
                    '</div>' +
                '</div>';
            
            // Update input minimum price if making a new offer
            const priceInput = document.getElementById('offeredPrice');
            if (priceInput) {
                priceInput.min = highestOffer.price + 1;
                priceInput.placeholder = `Offer amount (min: ₹${highestOffer.price + 1})`;
            }
        } else {
            offerContainer.innerHTML +=
                '<div class="offers-section">' +
                    '<div class="offers-header">Product Offers</div>' +
                    '<div id="offers-list">' +
                        '<div class="no-offers">No offers made yet</div>' +
                    '</div>' +
                '</div>';
        }
    } catch (error) {
        console.error('Error:', error);
        showMessage('Unable to fetch product details', true);
    }
}

function validateOfferAmount() {
    const priceInput = document.getElementById('offeredPrice');
    const submitButton = document.querySelector('#offerForm button[type="submit"]');
    const errorMsg = document.getElementById('offer-error');
    
    if (!priceInput || !submitButton) return;

    const price = parseFloat(priceInput.value);
    const minPrice = parseFloat(priceInput.min);

    if (price < minPrice) {
        errorMsg.textContent = `Offer must be higher than the current highest offer (₹${minPrice - 1})`;
        errorMsg.style.display = 'block';
        submitButton.disabled = true;
    } else {
        errorMsg.style.display = 'none';
        submitButton.disabled = false;
    }
}

async function submitOffer(event) {
    event.preventDefault();
    const token = localStorage.getItem('token');
    if (!token) {
        window.location.href = '/signin';
        return;
    }

    // Validate offer amount before submitting
    const priceInput = document.getElementById('offeredPrice');
    const price = parseFloat(priceInput.value);
    const minPrice = parseFloat(priceInput.min);
    
    if (price < minPrice) {
        showMessage(`Offer must be higher than the current highest offer (₹${minPrice - 1})`, true);
        return;
    }

    try {
        const price = document.getElementById('offeredPrice').value;
        const productId = document.getElementById('productId').value;
        const sellerId = document.getElementById('sellerId').value;
        const user = JSON.parse(localStorage.getItem('user'));

        const response = await apiCaller.post('/api/offers', {
            price: parseFloat(price),
            productId: productId,
            sellerId: sellerId,
            buyerId: user.id
        });

        if (response.success) {
            showMessage('Offer submitted successfully!');
            // Reload the current page to show the new offer
            setTimeout(() => loadProduct(), 1000);
        } else {
            throw new Error(response.message || 'Failed to submit offer');
        }
    } catch (error) {
        console.error('Error:', error);
        showMessage('An error occurred while submitting your offer', true);
    }
}

async function deleteProduct(productId) {
    if (!confirm('Are you sure you want to delete this product?')) return;

    const token = localStorage.getItem('token');
    if (!token) {
        window.location.href = '/signin';
        return;
    }

    try {
        const response = await apiCaller.delete('/api/products/' + productId);
        if (response.success) {
            showMessage(response.message || 'Product deleted successfully');
            setTimeout(() => window.location.href = '/', 2000);
        } else {
            throw new Error(response.message || 'Failed to delete product');
        }
    } catch (error) {
        console.error('Error:', error);
        showMessage('Error occurred while deleting product', true);
    }
}

document.addEventListener('DOMContentLoaded', async function() {
    const token = localStorage.getItem('token');
    if (!token) {
        window.location.href = '/signin';
        return;
    }

    try {
        const response = await apiCaller.get('/api/auth/validate-token');
        if (response.success) await loadProduct();
    } catch (error) {
        console.error('Error validating token:', error);
        showMessage('Session expired. Please sign in again.', true);
        setTimeout(() => window.location.href = '/signin', 2000);
    }
});
</script>
</body>
</html>
