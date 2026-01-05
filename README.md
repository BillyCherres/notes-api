# Notes API

A **Spring Boot REST API** for creating and managing personal notes, built to explore **backend architecture**, **authorization**, **Docker**, and **microservices** using production-style patterns.

This project intentionally evolves from a **single service** into a **multi-service system** with explicit ownership, authorization boundaries, and containerized infrastructure.

---
## 📈 Roadmap

- [x] Notes CRUD API
- [x] User ownership model
- [x] Authorization enforcement
- [x] Dockerized Postgres
- [ ] Auth Service (users & JWT)
- [ ] API Gateway
- [ ] Kubernetes deployment
- [ ] CI/CD pipeline
- [ ] 


## 🚀 Features

- Create, read, update, and delete notes
- Each note is **owned by a user (UUID)**
- **Authorization enforced**:
  - Only the note owner can update or delete
  - Unauthorized actions return **403 Forbidden**
- Pagination support for listing notes
- Centralized error handling with consistent API responses
- Fully containerized with **Docker Compose**
- Uses **PostgreSQL** for persistent storage

---

## 🧱 Architecture (Current)

Client
->
Notes API (Spring Boot)
->
PostgreSQL


### Ownership & Authorization Model
- Each note has an `owner_user_id`
- Requests include a user identity header:


- The API validates ownership before allowing updates or deletes

This mirrors real-world authorization logic found in production backend systems.

---

## 📦 Tech Stack

- **Java 17**
- **Spring Boot 4**
- **Spring Data JPA**
- **PostgreSQL 16**
- **Docker & Docker Compose**
- **Maven**

---






