#!/bin/bash

set -e

# Get version from git commit or use timestamp
VERSION=${1:-$(date +%Y%m%d-%H%M%S)}
echo "🚀 Pushing Docker images to Docker Hub with version: $VERSION..."

# Tag and push with version
echo "Tagging and pushing eureka..."
docker tag itsanla/eureka:latest itsanla/eureka:$VERSION
docker push itsanla/eureka:latest
docker push itsanla/eureka:$VERSION

echo "Tagging and pushing produk..."
docker tag itsanla/produk:latest itsanla/produk:$VERSION
docker push itsanla/produk:latest
docker push itsanla/produk:$VERSION

echo "Tagging and pushing pelanggan..."
docker tag itsanla/pelanggan:latest itsanla/pelanggan:$VERSION
docker push itsanla/pelanggan:latest
docker push itsanla/pelanggan:$VERSION

echo "Tagging and pushing order..."
docker tag itsanla/order:latest itsanla/order:$VERSION
docker push itsanla/order:latest
docker push itsanla/order:$VERSION

echo "Tagging and pushing marketplace-gateway..."
docker tag itsanla/marketplace-gateway:latest itsanla/marketplace-gateway:$VERSION
docker push itsanla/marketplace-gateway:latest
docker push itsanla/marketplace-gateway:$VERSION

echo "Tagging and pushing buku..."
docker tag itsanla/buku:latest itsanla/buku:$VERSION
docker push itsanla/buku:1.0.0
docker push itsanla/buku:latest
docker push itsanla/buku:$VERSION

echo "Tagging and pushing anggota..."
docker tag itsanla/anggota:latest itsanla/anggota:$VERSION
docker push itsanla/anggota:latest
docker push itsanla/anggota:$VERSION

echo "Tagging and pushing peminjaman..."
docker tag itsanla/peminjaman:latest itsanla/peminjaman:$VERSION
docker push itsanla/peminjaman:latest
docker push itsanla/peminjaman:$VERSION

echo "Tagging and pushing pengembalian..."
docker tag itsanla/pengembalian:latest itsanla/pengembalian:$VERSION
docker push itsanla/pengembalian:latest
docker push itsanla/pengembalian:$VERSION

echo "Tagging and pushing perpustakaan-gateway..."
docker tag itsanla/perpustakaan-gateway:latest itsanla/perpustakaan-gateway:$VERSION
docker push itsanla/perpustakaan-gateway:latest
docker push itsanla/perpustakaan-gateway:$VERSION

echo "✅ All images pushed successfully!"
