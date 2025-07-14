# Interview Developer Task 

## Supply Chain Tree Management API

A backend service for managing hierarchical supply chain relationships using a tree data structure. This API allows users to build, modify, and query supply chain networks where companies are represented as nodes and their business relationships as edges.
## Technology Stack

- Spring Boot - Framework
- Kotlin - Programming language
- PostgreSQL - Database
- JOOQ - Database interaction/ORM
- JSON - Data format for API requests/responses

# Core Features
## Data Management

- Tree Structure: Manages supply chain relationships as a tree (simplified from graph)
- Edge-based Storage: Stores relationships using from_id → to_id connections
- PostgreSQL Backend: Persistent storage with optimized queries

## API Endpoints

- Create Edge - Add new company relationships
- Delete Edge - Remove existing relationships
- Get Tree - Retrieve complete tree structure starting from any node

## Key Capabilities

- Performance Optimized: Handles large supply chain networks efficiently
- Memory Safe: Prevents overflow when processing extensive tree structures
- Error Handling: Graceful handling of duplicates, invalid inputs, and database errors
- Flexible Querying: Generate tree view from any node as root

# How to Run
...

## 📘 API Documentation

This project includes auto-generated OpenAPI documentation via Swagger UI.

- 📄 Base URL: `http://localhost:8080`
- 🌐 Swagger UI: [`http://localhost:8080/swagger-ui.html`](http://localhost:8080/swagger-ui.html)
- 🧪 You can test all endpoints directly in your browser.

