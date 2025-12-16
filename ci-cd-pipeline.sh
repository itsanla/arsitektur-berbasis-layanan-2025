#!/bin/bash

set -e

echo "🚀 Starting CI/CD Pipeline..."

# Step 1: Quality Checks
echo ""
echo "Step 1: Code Quality Checks"
echo "=========================="
./quality-check.sh

# Step 2: Run Tests
echo ""
echo "Step 2: Running Tests"
echo "===================="
./test-all.sh

# Step 3: Build Services
echo ""
echo "Step 3: Building Services"
echo "========================"
./build-all.sh

# Step 4: Build Docker Images
echo ""
echo "Step 4: Building Docker Images"
echo "============================="
./build.sh

# Step 5: Integration Tests
echo ""
echo "Step 5: Integration Tests"
echo "========================"
echo "Starting services..."
docker-compose up -d

echo "Waiting for services to be ready..."
sleep 60

echo "Running integration tests..."
./test-integration.sh

echo "Stopping services..."
docker-compose down

# Step 6: Deploy (if on main branch)
if [ "${BRANCH:-}" = "main" ]; then
    echo ""
    echo "Step 6: Deploying to Registry"
    echo "============================"
    ./push.sh
    echo "✅ Deployment completed!"
else
    echo ""
    echo "Step 6: Skipping deployment (not main branch)"
    echo "============================================="
fi

echo ""
echo "🎉 CI/CD Pipeline completed successfully!"