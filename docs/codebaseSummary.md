# Codebase Summary

## Key Components and Their Interactions

### Authentication System
1. Controllers
   - AuthController: Handles login, signup, token validation
   - ViewController: Manages web views and session state

2. Security Components
   - SecurityConfig: Spring Security configuration
   - JwtAuthFilter: Token validation middleware
   - JwtAuthenticationMiddleware: JWT utilities

3. Frontend Components
   - signin.jsp: Login page with localStorage token handling
   - navbar.jsp: Common navigation with token validation
   - Other JSP pages: Product and offer management

## Data Flow
1. Authentication Flow
   ```
   Client -> AuthController -> JwtAuthFilter -> Protected Resources
   ```
   - Current: Using mixed cookie/localStorage approach (causing issues)
   - Target: Pure localStorage with Authorization header

2. Token Validation Flow
   ```
   Request -> JwtAuthFilter -> JwtAuthMiddleware -> SecurityContext
   ```
   - Validates tokens on each request
   - Sets authentication in security context

## External Dependencies
1. JWT Dependencies
   - io.jsonwebtoken for JWT operations
   - Spring Security for auth framework

2. Database
   - MySQL for data persistence
   - JPA/Hibernate for ORM

## Recent Significant Changes
1. Moving from cookie-based to localStorage token storage
2. Simplifying security configuration
3. Streamlining token validation process

## User Feedback Integration
Current Issue:
- Constant page refreshes after login
- Token storage mechanism mismatch
- Authentication validation loop

Solution in Progress:
- Unified localStorage approach
- Simplified token validation
- Improved error handling

## Active Development
1. Authentication Improvements
   - Removing cookie-based token handling
   - Implementing pure localStorage solution
   - Fixing validation issues

2. Code Organization
   - Separating concerns in auth flow
   - Improving error handling
   - Streamlining token management
