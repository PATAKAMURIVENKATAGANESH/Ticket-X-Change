# Ticket X Change

A **secure and user-friendly** platform for exchanging and selling e-tickets in various categories such as **movies, travel, hotels, and events**. The platform ensures **secure transactions**, **ticket verification**, and **real-time notifications** to enhance user experience.

## 🚀 Features
- **Spring Boot Backend**: RESTful APIs with **JWT authentication** & **WebSocket** for real-time updates.
- **React.js Frontend**: Responsive UI with **Material-UI** and **Google OAuth authentication**.
- **Automated Ticket Extraction**: Integrated **OpenAPI APIs** using **Spring AI** for ticket details processing.
- **Secure Transactions**: SQL & NoSQL databases for **efficient ticket management**.
- **Email Verification System**: Python scripts to **process e-ticket confirmation emails** for authenticity.

## 🛠️ Tech Stack
### Backend:
- **Spring Boot** (Java)
- **Spring Security** (JWT authentication)
- **Spring WebSocket** (Real-time notifications)
- **Spring AI & OpenAPI APIs** (Ticket details extraction)
- **MySQL/PostgreSQL & MongoDB** (Hybrid database architecture)
- **Apache Kafka** (Event-driven communication)

### Frontend:
- **React.js** (UI development)
- **Material-UI** (Design components)
- **React Router v6** (Navigation)
- **Axios** (API integration)

### Other Integrations:
- **Python Scripts** (Email parsing & verification)
- **AWS S3** (File storage)
- **Docker** (Containerized deployment)
- **GitHub Actions** (CI/CD pipeline)

## 📌 Installation & Setup
### 1️⃣ Clone the Repository
```sh
git clone https://github.com/your-repo/ticket-x-change.git
cd ticket-x-change
```

### 2️⃣ Backend Setup
```sh
cd backend
mvn clean install
mvn spring-boot:run
```
## 📖 API Endpoints
### Authentication
- `POST /api/v1/auth/register` - Register a new user
- `POST /api/v1/auth/login` - Login & get JWT token

### Ticket Operations
- `POST /api/v1/tickets/sell` - List a ticket for sale
- `GET /api/v1/tickets/buy` - Browse available tickets
- `POST /api/v1/tickets/verify` - Verify ticket authenticity

## 📌 Contributing
1. Fork the repository
2. Create a new branch (`feature-branch`)
3. Commit your changes
4. Push to your branch
5. Create a Pull Request

