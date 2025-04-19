# Design Patterns and Principles Analysis

## Design Principles

### 1. SOLID Principles

#### Single Responsibility Principle (SRP)
- **What**: Each class should have only one reason to change
- **Where**: 
  - Services are split into distinct responsibilities (UserService, ProductService, OfferService)
  - DTOs are separated by domain and function (auth/product/offer)
  - Controllers are divided by domain (AuthController, ProductController, OfferController)
- **Why**: Improves maintainability and reduces coupling
- **How**: Each service class handles operations related to a single entity/domain

#### Open/Closed Principle (OCP)
- **What**: Software entities should be open for extension but closed for modification
- **Where**: 
  - Service interfaces (IProductService, IOfferService)
  - Base classes like ApiResponse
- **Why**: Allows adding new functionality without changing existing code
- **How**: Implementation through interfaces and inheritance

#### Interface Segregation Principle (ISP)
- **What**: Clients should not be forced to depend on interfaces they don't use
- **Where**: 
  - Service interfaces (IProductService, IUserService, IOfferService)
  - Each interface contains only methods relevant to its specific domain
- **Why**: Prevents classes from having to implement unnecessary methods
- **How**: Small, focused interfaces instead of large, monolithic ones

#### Dependency Inversion Principle (DIP)
- **What**: High-level modules should not depend on low-level modules
- **Where**: 
  - Service layer depending on interfaces rather than concrete implementations
  - Controllers depending on service interfaces
- **Why**: Reduces coupling, improves testability
- **How**: Using dependency injection and interface-based design

### 2. Other Key Principles

#### Separation of Concerns (SoC)
- **What**: Different aspects of the application should be separated into distinct sections
- **Where**: 
  - MVC architecture separation
  - DTOs separate from domain models
  - Separate packages for controllers, services, repositories
- **Why**: Improves maintainability and reusability
- **How**: Clear separation between different layers and components

#### DRY (Don't Repeat Yourself)
- **What**: Every piece of knowledge should have a single, unambiguous representation
- **Where**: 
  - Common base classes
  - Shared utilities and constants
  - Reusable components
- **Why**: Reduces code duplication and maintenance overhead
- **How**: Through abstraction and common components

## Design Patterns

### 1. Facade Pattern
- **What**: Provides a unified interface to a set of interfaces in a subsystem
- **Where**: ProductOfferFacade (src/main/java/com/C2CApp/C2CBackend/services/ProductOfferFacade.java)
- **Why**: Simplifies complex interactions between Product and Offer subsystems
- **How**: Coordinates interactions between ProductService and OfferService

### 2. Repository Pattern
- **What**: Mediates between the domain and data mapping layers
- **Where**: 
  - ProductRepository
  - UserRepository
  - OfferRepository
- **Why**: Centralizes common data access functionality
- **How**: Provides abstract interface for data persistence operations

### 3. DTO (Data Transfer Object) Pattern
- **What**: Objects that carry data between processes
- **Where**: dto package containing request/response objects
- **Why**: Reduces the number of method calls and decouples layers
- **How**: Separate classes for data transfer that don't contain business logic

### 4. MVC (Model-View-Controller) Pattern
- **What**: Separates application into three interconnected components
- **Where**: 
  - Controllers in controller package
  - Views in webapp/WEB-INF/jsp
  - Models in schema package
- **Why**: Separates concerns and improves maintainability
- **How**: Clear separation between data, presentation, and control logic

### 5. Middleware Pattern
- **What**: Components that act as filters/interceptors in the request pipeline
- **Where**: 
  - JwtAuthenticationMiddleware
  - JwtAuthFilter
- **Why**: Handles cross-cutting concerns like authentication
- **How**: Intercepts requests to perform security checks

### 6. Builder Pattern
- **What**: Separates object construction from its representation
- **Where**: DTOs and Response objects (ApiResponse, AuthResponse)
- **Why**: Provides clear and flexible object creation
- **How**: Through builder methods in response classes

### 7. Factory Pattern
- **What**: Creates objects without explicitly specifying their exact classes
- **Where**: Mapper classes (UserMapper, ProductMapper, OfferMapper)
- **Why**: Centralizes object creation logic
- **How**: Static factory methods to create DTOs from entities and vice versa

### 8. Singleton Pattern
- **What**: Ensures a class has only one instance
- **Where**: Configuration classes (DotenvConfig, SecurityConfig)
- **Why**: Manages shared resources and configuration
- **How**: Spring's dependency injection container ensures singleton instances

### 9. Adapter Pattern
- **What**: Converts interface of a class into another interface clients expect
- **Where**: Mapper classes adapting between DTOs and domain models
- **Why**: Provides clean translation between different data representations
- **How**: Through mapping methods that convert between different object types

### Patterns Not Identified
- Prototype Pattern
- Proxy Pattern (beyond Spring's built-in proxies)
- Flyweight Pattern
