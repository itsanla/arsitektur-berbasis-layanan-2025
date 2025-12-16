#!/bin/bash

set -e

echo "🧪 Running All Tests..."

SUCCESS_COUNT=0
FAIL_COUNT=0
FAILED_SERVICES=()

# Perpustakaan Services
PERPUSTAKAAN_SERVICES=("Buku" "anggota" "Peminjaman" "Pengembalian" "api-gateway")

for service in "${PERPUSTAKAAN_SERVICES[@]}"; do
    echo "Testing Perpustakaan/$service..."
    if cd "Perpustakaan/$service" && ./mvnw clean test > /dev/null 2>&1; then
        echo "✅ Perpustakaan/$service tests passed"
        ((SUCCESS_COUNT++))
        cd - > /dev/null
    else
        echo "❌ Perpustakaan/$service tests failed"
        FAILED_SERVICES+=("Perpustakaan/$service")
        ((FAIL_COUNT++))
        cd - > /dev/null
    fi
done

# Marketplace Services
MARKETPLACE_SERVICES=("Produk" "Pelanggan" "Order" "api-gateway")

for service in "${MARKETPLACE_SERVICES[@]}"; do
    echo "Testing Marketplace/$service..."
    if cd "Marketplace/$service" && ./mvnw clean test > /dev/null 2>&1; then
        echo "✅ Marketplace/$service tests passed"
        ((SUCCESS_COUNT++))
        cd - > /dev/null
    else
        echo "❌ Marketplace/$service tests failed"
        FAILED_SERVICES+=("Marketplace/$service")
        ((FAIL_COUNT++))
        cd - > /dev/null
    fi
done

# Eureka Server
echo "Testing eureka..."
if cd "eureka" && ./mvnw clean test > /dev/null 2>&1; then
    echo "✅ eureka tests passed"
    ((SUCCESS_COUNT++))
    cd - > /dev/null
else
    echo "❌ eureka tests failed"
    FAILED_SERVICES+=("eureka")
    ((FAIL_COUNT++))
    cd - > /dev/null
fi

echo ""
echo "=========================================="
echo "Test Summary"
echo "=========================================="
echo "✅ Passed: $SUCCESS_COUNT"
echo "❌ Failed: $FAIL_COUNT"

if [ $FAIL_COUNT -gt 0 ]; then
    echo ""
    echo "Failed services:"
    for failed in "${FAILED_SERVICES[@]}"; do
        echo "  - $failed"
    done
    exit 1
else
    echo ""
    echo "🎉 All tests passed!"
    exit 0
fi