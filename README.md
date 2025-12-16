# 🏛️ Arsitektur Berbasis Layanan - Microservices 2025

<div align="center">

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Docker](https://img.shields.io/badge/Docker-Ready-blue.svg)](https://www.docker.com/)
[![Microservices](https://img.shields.io/badge/Architecture-Microservices-purple.svg)](https://microservices.io/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

**Production-ready microservices architecture dengan Spring Cloud Netflix Eureka**

[Dokumentasi](#-dokumentasi) • [Quick Start](#-quick-start) • [Docker Hub](#-docker-images) • [API](#-api-endpoints)

</div>

## 🎯 Tentang Proyek

Proyek ini mengimplementasikan **arsitektur microservices modern** dengan 10 layanan independen yang terdistribusi dalam 2 domain bisnis: **Marketplace** dan **Perpustakaan**. Setiap service dapat di-deploy secara independen dan tersedia sebagai Docker image di Docker Hub.

### Prinsip Arsitektur:
- ✅ **Independently Deployable** - Setiap service dapat di-deploy tanpa mempengaruhi service lain
- ✅ **Service Discovery** - Automatic service registration dan discovery dengan Netflix Eureka
- ✅ **Load Balancing** - Client-side load balancing dengan Spring Cloud LoadBalancer
- ✅ **API Gateway Pattern** - Centralized entry point untuk setiap domain
- ✅ **Database per Service** - Setiap service memiliki database sendiri (H2)
- ✅ **Containerized** - Semua service tersedia sebagai Docker images

## 🏗️ Arsitektur Sistem

```
┌─────────────────────────────────────────────────────────────────┐
│                     EUREKA SERVER (8761)                        │
│                    Service Discovery Registry                    │
└─────────────────────────────────────────────────────────────────┘
                              ▲
                              │ Register & Discover
                              │
        ┌─────────────────────┴─────────────────────┐
        │                                           │
┌───────▼──────────┐                       ┌────────▼─────────┐
│   MARKETPLACE    │                       │  PERPUSTAKAAN    │
│     DOMAIN       │                       │     DOMAIN       │
├──────────────────┤                       ├──────────────────┤
│ • Produk (8081)  │                       │ • Buku (8084)    │
│ • Pelanggan      │                       │ • Anggota (8085) │
│   (8082)         │                       │ • Pengembalian   │
│ • Order (8083)   │                       │   (8086)         │
│ • API Gateway    │                       │ • Peminjaman     │
│   (Marketplace)  │                       │   (8087)         │
│                  │                       │ • API Gateway    │
│                  │                       │   (Perpustakaan) │
└──────────────────┘                       └──────────────────┘
```

## 🛠️ Tech Stack

| Komponen | Versi/Detail |
|----------|-------------|
| **Language** | Java 17 |
| **Framework** | Spring Boot 3.5.5 |
| **Cloud** | Spring Cloud 2025.0.0 |
| **Service Discovery** | Netflix Eureka |
| **Load Balancer** | Spring Cloud LoadBalancer |
| **Build Tool** | Apache Maven |
| **Database** | H2 In-Memory Database |
| **Containerization** | Docker |
| **Registry** | Docker Hub |

## 🐳 Docker Images

Semua microservices tersedia sebagai Docker images di Docker Hub:

### Infrastructure
| Service | Docker Hub | Port | Status |
|---------|-----------|------|--------|
| **Eureka Server** | [`itsanla/eureka:latest`](https://hub.docker.com/r/itsanla/eureka) | 8761 | ✅ Public |

### Marketplace Domain
| Service | Docker Hub | Port | Role |
|---------|-----------|------|------|
| **Produk** | [`itsanla/produk:latest`](https://hub.docker.com/r/itsanla/produk) | 8081 | Service |
| **Pelanggan** | [`itsanla/pelanggan:latest`](https://hub.docker.com/r/itsanla/pelanggan) | 8082 | Service |
| **Order** | [`itsanla/order:latest`](https://hub.docker.com/r/itsanla/order) | 8083 | Aggregator |
| **API Gateway** | [`itsanla/marketplace-gateway:latest`](https://hub.docker.com/r/itsanla/marketplace-gateway) | - | Gateway |

### Perpustakaan Domain
| Service | Docker Hub | Port | Role |
|---------|-----------|------|------|
| **Buku** | [`itsanla/buku:1.0.0`](https://hub.docker.com/r/itsanla/buku) | 8084 | Service |
| **Anggota** | [`itsanla/anggota:latest`](https://hub.docker.com/r/itsanla/anggota) | 8085 | Service |
| **Pengembalian** | [`itsanla/pengembalian:latest`](https://hub.docker.com/r/itsanla/pengembalian) | 8086 | Service |
| **Peminjaman** | [`itsanla/peminjaman:latest`](https://hub.docker.com/r/itsanla/peminjaman) | 8087 | Aggregator |
| **API Gateway** | [`itsanla/perpustakaan-gateway:latest`](https://hub.docker.com/r/itsanla/perpustakaan-gateway) | - | Gateway |

### 📦 Pull Images

```bash
# Pull semua images
docker pull itsanla/eureka:latest
docker pull itsanla/produk:latest
docker pull itsanla/pelanggan:latest
docker pull itsanla/order:latest
docker pull itsanla/marketplace-gateway:latest
docker pull itsanla/buku:1.0.0
docker pull itsanla/anggota:latest
docker pull itsanla/pengembalian:latest
docker pull itsanla/peminjaman:latest
docker pull itsanla/perpustakaan-gateway:latest
```

## 📊 Service Details

| Domain | Service Name | Port | Type | Main Endpoint |
|--------|-------------|------|------|---------------|
| **Infrastructure** | `EUREKA-SERVER` | `8761` | Registry | `http://localhost:8761` |
| **Marketplace** | `PRODUK-SERVICE` | `8081` | Independent | `http://localhost:8081/api/products/{id}` |
| **Marketplace** | `PELANGGAN-SERVICE` | `8082` | Independent | `http://localhost:8082/api/pelanggan/{id}` |
| **Marketplace** | `ORDER-SERVICE` | `8083` | Aggregator | `http://localhost:8083/api/order/detail/{id}` |
| **Perpustakaan** | `BUKU-SERVICE` | `8084` | Independent | `http://localhost:8084/api/buku/{id}` |
| **Perpustakaan** | `ANGGOTA-SERVICE` | `8085` | Independent | `http://localhost:8085/api/anggota/{id}` |
| **Perpustakaan** | `PENGEMBALIAN-SERVICE` | `8086` | Independent | `http://localhost:8086/api/pengembalian/{id}` |
| **Perpustakaan** | `PEMINJAMAN-SERVICE` | `8087` | Aggregator | `http://localhost:8087/api/peminjaman/detail/{id}` |

## 🚀 Quick Start

### Option 1: Run with Docker (Recommended)

```bash
# 1. Pull dan jalankan dengan docker-compose
docker-compose up -d

# 2. Verifikasi semua container berjalan
docker-compose ps

# 3. Akses Eureka Dashboard
open http://localhost:8761
```

### Option 2: Run Locally

#### Prerequisites
- Java JDK 17+
- Apache Maven 3.8+
- Git

#### Steps

**1. Clone Repository**
```bash
git clone <repository-url>
cd arsitektur-berbasis-layanan-2025
```

**2. Build All Services**
```bash
./build-all.sh
```

**3. Start Services (Urutan Penting!)**

```bash
# Step 1: Start Eureka Server
cd eureka && mvnw spring-boot:run &

# Step 2: Start Independent Services (parallel)
cd Marketplace/Produk && mvnw spring-boot:run &
cd Marketplace/Pelanggan && mvnw spring-boot:run &
cd Perpustakaan/Buku && mvnw spring-boot:run &
cd Perpustakaan/anggota && mvnw spring-boot:run &
cd Perpustakaan/Pengembalian && mvnw spring-boot:run &

# Step 3: Start Aggregator Services
cd Marketplace/Order && mvnw spring-boot:run &
cd Perpustakaan/Peminjaman && mvnw spring-boot:run &
```

**4. Verify Services**
```bash
# Check Eureka Dashboard
curl http://localhost:8761

# All services should show status: UP
```

## 🔧 Development

### Build Docker Images

```bash
# Build semua images
./build.sh

# Push ke Docker Hub
docker login
./push.sh
```

### Build Individual Service

```bash
# Example: Build Buku Service
cd Perpustakaan/Buku
mvnw clean package
docker build -t itsanla/buku:1.0.0 .
```

## 📡 API Endpoints

### Marketplace - Order Detail

Untuk mendapatkan detail pesanan lengkap beserta informasi produk dan pelanggan:

```bash
curl -X GET http://localhost:8083/api/order/detail/1
```

**Response Example:**
```json
{
  "orderId": 1,
  "orderDate": "2024-01-15",
  "status": "COMPLETED",
  "pelanggan": {
    "id": 1,
    "nama": "John Doe",
    "email": "john@example.com"
  },
  "produk": {
    "id": 1,
    "nama": "Laptop Gaming",
    "harga": 15000000
  },
  "totalHarga": 15000000
}
```

### Perpustakaan - Peminjaman Detail

Untuk mendapatkan detail peminjaman lengkap beserta informasi buku, anggota, dan status pengembalian:

```bash
curl -X GET http://localhost:8087/api/peminjaman/detail/1
```

**Response Example:**
```json
{
  "peminjamanId": 1,
  "tanggalPinjam": "2024-01-10",
  "tanggalKembali": "2024-01-24",
  "status": "DIPINJAM",
  "anggota": {
    "id": 1,
    "nama": "Jane Smith",
    "nomorAnggota": "A001"
  },
  "buku": {
    "id": 1,
    "judul": "Pemrograman Java",
    "pengarang": "Budi Santoso"
  },
  "pengembalian": {
    "status": "BELUM_DIKEMBALIKAN",
    "denda": 0
  }
}
```

## 🛡️ Monitoring & Health Check

### Eureka Dashboard
```bash
http://localhost:8761
```

### Service Health Endpoints
```bash
# Check individual service health
curl http://localhost:8081/actuator/health  # Produk
curl http://localhost:8082/actuator/health  # Pelanggan
curl http://localhost:8083/actuator/health  # Order
curl http://localhost:8084/actuator/health  # Buku
curl http://localhost:8085/actuator/health  # Anggota
curl http://localhost:8086/actuator/health  # Pengembalian
curl http://localhost:8087/actuator/health  # Peminjaman
```

### H2 Database Console
Setiap service memiliki H2 console:
```
http://localhost:{port}/h2-console
```

## 🧪 Testing & CI/CD

### Manual Testing
```bash
# Run all tests
./test-all.sh

# Test individual service
cd Perpustakaan/Buku
mvnw test

# Run integration tests
./test-integration.sh

# Run quality checks
./quality-check.sh
```

### CI/CD Pipeline
```bash
# Run complete CI/CD pipeline
./ci-cd-pipeline.sh

# Jenkins pipeline available in Jenkinsfile
# GitHub Actions available in .github/workflows/ci-cd.yml
```

## 📚 Dokumentasi

- [Quick Start Guide](QUICKSTART.md)
- [Docker Setup](DOCKER_SETUP.md)
- [Docker Changes](DOCKER_CHANGES.md)
- [API Documentation](Documentation-18-11.json)

## 🎯 Best Practices Implemented

✅ **Independent Repositories** - Setiap service memiliki Docker repository terpisah  
✅ **Semantic Versioning** - Menggunakan version tags (e.g., `1.0.0`, `latest`)  
✅ **Service Discovery** - Automatic registration dengan Eureka  
✅ **Load Balancing** - Client-side load balancing  
✅ **Health Checks** - Spring Boot Actuator untuk monitoring  
✅ **Database Isolation** - Setiap service memiliki database sendiri  
✅ **API Gateway Pattern** - Centralized entry point per domain  
✅ **Containerization** - Docker-ready untuk deployment  

## 🔑 Key Features

- **Service Discovery**: Automatic service registration dan discovery
- **Load Balancing**: Client-side load balancing dengan RestTemplate
- **API Gateway**: Centralized routing dan aggregation
- **Database per Service**: Isolated data management
- **Health Monitoring**: Built-in health checks dan monitoring
- **Docker Support**: Production-ready containerization
- **Scalability**: Horizontal scaling capability

## 🤝 Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/NewFeature`)
3. Commit changes (`git commit -m 'Add NewFeature'`)
4. Push to branch (`git push origin feature/NewFeature`)
5. Open Pull Request

## 📄 License

This project is licensed under the [MIT License](LICENSE).

## 🙋 Support

- 📧 Open an issue on GitHub
- 📖 Check [Spring Cloud Netflix Documentation](https://spring.io/projects/spring-cloud-netflix)
- 🐳 Visit [Docker Hub Repositories](https://hub.docker.com/u/itsanla)

## 👨‍💻 Author

**itsanla**
- Docker Hub: [@itsanla](https://hub.docker.com/u/itsanla)
- GitHub: [arsitektur-berbasis-layanan-2025](https://github.com/itsanla/arsitektur-berbasis-layanan-2025)

---

<div align="center">

**Built with ❤️ using Spring Cloud Microservices Architecture**

⭐ Star this repo if you find it helpful!

</div>