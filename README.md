# 📈 Stock Price Service

Stock Price Service is a core microservice of the **Stock Trading Simulation Platform**.  
It simulates real-time stock price fluctuations, stores data in MySQL, caches data in Redis, and publishes price updates using Redis Pub/Sub.

---

## 🚀 Features

- ⏱ Scheduled stock price simulation
- 📊 Dynamic price updates every few seconds
- 🗄 Persistent storage in MySQL
- ⚡ Redis caching (full JSON object)
- 🔁 Cache-aside pattern implementation
- 📡 Redis Pub/Sub for real-time price updates
- 🌐 REST APIs to fetch stock data

---

## 🛠 Tech Stack

- Java 17
- Spring Boot 3.x
- Spring Data JPA
- MySQL (Docker)
- Redis (Docker)
- Jackson (JSON Serialization)
- Lombok

---

## 📂 Project Structure

```
com.stock.stockpriceservice
│
├── controller         # REST APIs
├── service            # Business logic
│   └── impl
├── repository         # JPA Repositories
├── entity             # Stock entity
├── scheduler          # Price simulation logic
├── config             # Redis & app configuration
├── exception          # Global exception handling
└── StockPriceServiceApplication.java
```

---

## ⚙️ Setup Instructions

### 1️⃣ Clone the Repository

```
git clone <your-repo-url>
cd stock-price-service
```

---

### 2️⃣ Start MySQL (Docker)

```
docker run --name stock-mysql \
-e MYSQL_ROOT_PASSWORD=root \
-e MYSQL_DATABASE=stockdb \
-p 3307:3306 \
-d mysql:8
```

---

### 3️⃣ Start Redis (Docker)

```
docker run --name stock-redis -p 6379:6379 -d redis
```

---

### 4️⃣ Configure application.yml

```
server:
  port: 8082

spring:
  datasource:
    url: jdbc:mysql://localhost:3307/stockdb
    username: root
    password: root

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

  data:
    redis:
      host: localhost
      port: 6379
```

---

### 5️⃣ Run the Application

```
mvn spring-boot:run
```

---

## 🔌 API Endpoints

### 📊 Get All Stocks

```
GET /api/stocks
```

---

### 📈 Get Stock by Symbol

```
GET /api/stocks/{symbol}
```

Example:

```
GET /api/stocks/AAPL
```

---

## 🧠 Key Concepts Implemented

- Scheduled tasks using `@Scheduled`
- Cache-aside pattern (Redis → DB fallback)
- Full object caching using JSON serialization
- Redis Pub/Sub messaging system
- Microservice architecture design

---

## 🧪 Testing

### Test API:

1. Call stock API:
```
GET http://localhost:8082/api/stocks/AAPL
```

2. Call again:
- First call → DB hit  
- Second call → Redis (no DB hit)

---

### Test Redis Cache:

```
docker exec -it stock-redis redis-cli
KEYS *
GET stock:AAPL
```

---

### Test Pub/Sub:

```
docker exec -it stock-redis redis-cli
SUBSCRIBE stock-price-updates
```

You will receive updates like:

```
"AAPL:185.32"
```

---

## 🐳 Docker Notes

- MySQL runs on port `3307`
- Redis runs on port `6379`
- Ensure both containers are running before starting service

---

## 📌 Future Improvements

- WebSocket integration for live UI updates
- External market data integration
- Rate limiting & circuit breaker
- Kafka-based event streaming

---

## 👨‍💻 Author

Vipul Singh

