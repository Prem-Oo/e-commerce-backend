# E-Commerce Backend

## Overview
This is the backend of a highly scalable full-stack e-commerce application built using microservices architecture. The backend includes essential features such as load balancing, a circuit breaker to avoid cascading failures, and optimistic locking to reduce collisions during concurrent transactions.

## Features
- Microservices architecture
- Load balancer to improve performance by 33%
- Circuit breaker to avoid cascading failure
- Optimistic locking to reduce collisions by 99%

## Tech Stack
- Spring Cloud
- Circuit Breaker
- Spring Mail
- Spring Data JPA
- Postman (for API testing)

## Getting Started
To get a local copy up and running, follow these steps:

1. Clone the repository:
   ```sh
   git clone https://github.com/Prem-Oo/e-commerce-backend.git

API Endpoints:
GET /products: Retrieve all products

POST /orders: Create a new order

GET /orders/{id}: Retrieve order details by ID
