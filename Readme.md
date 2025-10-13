# 🛍️ E-Commerce Microservices Platform (Spring Boot + AWS ECS)

A **cloud-native, microservices-based e-commerce platform** built with **Spring Boot**, designed for scalability, modularity, and security.  
This system follows best software design principles, integrates with AWS services for deployment and management, and ensures smooth CI/CD automation via **GitHub Actions**.

---

## 🚀 Architecture Overview

The platform consists of multiple independent **Spring Boot microservices**, each responsible for a specific domain:
- **User Service** – manages registration, login, profiles, and roles
- **Product Service** – handles product catalog, categories, and inventory
- **Cart Service** – manages shopping carts and user sessions
- **Order Service** – processes and tracks orders
- **Payment Service** – integrates with payment gateways and manages transactions

All services communicate through REST APIs and are registered via a **Custom Service Registry (Eureka)**.  
Requests are routed and secured using a **Custom API Gateway** that applies **JWT-based authentication** and **role-based access control**.

---

## 🧱 System Components

| Component | Description |
|------------|-------------|
| **Spring Boot** | Backend framework for microservice development |
| **Spring Security + JWT** | Provides secure authentication and authorization |
| **Spring Cloud Netflix Eureka** | Custom service registry for dynamic service discovery |
| **API Gateway** | Centralized routing and authentication filter for all requests |
| **PostgreSQL (AWS RDS)** | Relational database for each microservice |
| **AWS Secret Manager** | Securely stores database credentials, JWT secrets, and API keys |
| **JavaMailSender** | Sends order and registration confirmation emails |
| **Docker** | Containerizes each microservice for portability |
| **AWS ECR** | Stores and manages container images |
| **AWS ECS (Fargate)** | Hosts and runs containerized microservices |
| **GitHub Actions** | CI/CD workflow for build, test, and deployment automation |

---

## 🧩 Microservice List

1. **User Service**
    - User registration, login, and profile management
    - Role-based authentication (Admin, Customer, etc.)
    - JWT token generation and validation

2. **Product Service**
    - Product listing, categories, and inventory control
    - Accessible via API Gateway with JWT validation

3. **Cart Service**
    - Add, update, and remove items in user cart
    - Maintains cart data linked to users

4. **Order Service**
    - Handles order creation, status tracking, and history
    - Sends order confirmation via mail server

5. **Payment Service**
    - Manages transaction workflows and payment status updates

---

## 🔒 Security & Authentication

- **Spring Security** for authentication and authorization
- **JWT (JSON Web Token)** for secure session management
- **Role-based access control** enforced at API Gateway and microservice level
- **API Gateway Filters** validate JWT tokens before routing requests
- **AWS Secrets Manager** ensures sensitive credentials (DB, JWT secret, Mail server passwords) are never hardcoded

---

## 📧 Email Notifications

The application uses **JavaMailSender** to send:
- Registration confirmation emails
- Password reset links
- Order confirmation and shipping updates

SMTP credentials are securely fetched from **AWS Secrets Manager**.

---

## 🐳 Docker & Deployment

Each service is containerized with its own `Dockerfile`.  
