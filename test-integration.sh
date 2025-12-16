#!/bin/bash

set -e

echo "🧪 Running Integration Tests..."

# Wait for services to be ready
echo "Waiting for services to start..."
sleep 30

# Test Eureka Server
echo "Testing Eureka Server..."
curl -f http://localhost:8761/actuator/health || exit 1

# Test Perpustakaan Services
echo "Testing Perpustakaan Services..."
curl -f http://localhost:8084/actuator/health || exit 1  # Buku
curl -f http://localhost:8085/actuator/health || exit 1  # Anggota
curl -f http://localhost:8086/actuator/health || exit 1  # Pengembalian
curl -f http://localhost:8087/actuator/health || exit 1  # Peminjaman

# Test API Gateway
echo "Testing API Gateway..."
curl -f http://localhost:9001/actuator/health || exit 1

# Test Service Discovery
echo "Testing Service Discovery..."
REGISTERED_SERVICES=$(curl -s http://localhost:8761/eureka/apps | grep -o '<name>[^<]*</name>' | wc -l)
if [ "$REGISTERED_SERVICES" -lt 4 ]; then
    echo "❌ Not all services registered with Eureka"
    exit 1
fi

# Test API Endpoints
echo "Testing API Endpoints..."
curl -f http://localhost:8084/api/buku || exit 1
curl -f http://localhost:8085/api/anggota || exit 1
curl -f http://localhost:8086/api/pengembalian || exit 1
curl -f http://localhost:8087/api/peminjaman || exit 1

echo "✅ All integration tests passed!"