#!/bin/bash

echo "=========================================="
echo "Building All Microservices"
echo "=========================================="

# Color codes
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Track build status
SUCCESS_COUNT=0
FAIL_COUNT=0
FAILED_SERVICES=()

# List of all services to build
services=(
  "eureka"
  "Marketplace/Produk"
  "Marketplace/Pelanggan"
  "Marketplace/Order"
  "Marketplace/api-gateway"
  "GenericService/command-service"
  "GenericService/query-service"
  "Perpustakaan/Buku"
  "Perpustakaan/anggota"
  "Perpustakaan/Peminjaman"
  "Perpustakaan/Pengembalian"
  "Perpustakaan/api-gateway"
)

# Function to build a service
build_service() {
  local service=$1
  echo ""
  echo -e "${YELLOW}Building $service...${NC}"
  
  if cd "$service" 2>/dev/null; then
    # Use mvnw if available, otherwise fall back to mvn
    if [ -f "./mvnw" ]; then
      BUILD_CMD="./mvnw clean package -DskipTests"
    else
      BUILD_CMD="mvn clean package -DskipTests"
    fi
    
    if $BUILD_CMD > /dev/null 2>&1; then
      echo -e "${GREEN}✓ $service built successfully${NC}"
      ((SUCCESS_COUNT++))
      cd - > /dev/null
      return 0
    else
      echo -e "${RED}✗ $service build failed${NC}"
      FAILED_SERVICES+=("$service")
      ((FAIL_COUNT++))
      cd - > /dev/null
      return 1
    fi
  else
    echo -e "${RED}✗ Directory not found: $service${NC}"
    FAILED_SERVICES+=("$service")
    ((FAIL_COUNT++))
    return 1
  fi
}

# Run quality checks first
echo ""
echo -e "${YELLOW}Running quality checks...${NC}"
if ./quality-check.sh; then
  echo -e "${GREEN}✓ Quality checks passed${NC}"
else
  echo -e "${YELLOW}⚠ Quality issues found (continuing build)${NC}"
fi

# Build all services
for service in "${services[@]}"; do
  build_service "$service"
done

# Summary
echo ""
echo "=========================================="
echo "Build Summary"
echo "=========================================="
echo -e "${GREEN}Successful: $SUCCESS_COUNT${NC}"
echo -e "${RED}Failed: $FAIL_COUNT${NC}"

if [ $FAIL_COUNT -gt 0 ]; then
  echo ""
  echo "Failed services:"
  for failed in "${FAILED_SERVICES[@]}"; do
    echo -e "${RED}  - $failed${NC}"
  done
  echo ""
  echo "To see detailed error, run:"
  echo "  cd <service-path> && mvn clean package"
  exit 1
else
  echo ""
  echo -e "${GREEN}All services built successfully!${NC}"
  echo ""
  echo "You can now run:"
  echo "  docker-compose up -d           # Run all services"
  echo "  cd Marketplace && docker-compose up -d    # Run Marketplace only"
  echo "  cd GenericService && docker-compose up -d # Run Generic services only"
  echo "  cd Perpustakaan && docker-compose up -d   # Run Perpustakaan only"
  exit 0
fi
