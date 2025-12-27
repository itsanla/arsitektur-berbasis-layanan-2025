#!/bin/bash

set -e

echo "=========================================="
echo "VPS Configuration Script"
echo "=========================================="
echo ""

# Colors
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# ==================== 1. INSTALL DOCKER ====================
echo -e "${YELLOW}[1/3] Installing Docker Engine...${NC}"

# Add Docker's official GPG key
sudo apt update
sudo apt install -y ca-certificates curl
sudo install -m 0755 -d /etc/apt/keyrings
sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc
sudo chmod a+r /etc/apt/keyrings/docker.asc

# Add the repository to Apt sources
sudo tee /etc/apt/sources.list.d/docker.sources > /dev/null <<EOF
Types: deb
URIs: https://download.docker.com/linux/ubuntu
Suites: $(. /etc/os-release && echo "${UBUNTU_CODENAME:-$VERSION_CODENAME}")
Components: stable
Signed-By: /etc/apt/keyrings/docker.asc
EOF

sudo apt update
sudo apt install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

echo -e "${GREEN}✓ Docker installed successfully${NC}"
docker --version

# ==================== 2. INSTALL JDK 17 ====================
echo ""
echo -e "${YELLOW}[2/3] Installing JDK 17...${NC}"

# Check if any JDK is installed
if dpkg -l | grep -q openjdk; then
    echo "Removing existing JDK versions..."
    sudo apt remove -y openjdk-* default-jdk default-jre
    sudo apt autoremove -y
fi

# Install JDK 17
sudo apt install -y openjdk-17-jdk

# Set JDK 17 as default
sudo update-alternatives --set java /usr/lib/jvm/java-17-openjdk-amd64/bin/java
sudo update-alternatives --set javac /usr/lib/jvm/java-17-openjdk-amd64/bin/javac

echo -e "${GREEN}✓ JDK 17 installed successfully${NC}"
java -version

# ==================== 3. ADD USER TO DOCKER GROUP ====================
echo ""
echo -e "${YELLOW}[3/3] Adding user to docker group...${NC}"

# Get current user
CURRENT_USER=${SUDO_USER:-$USER}

# Add user to docker group
sudo usermod -aG docker $CURRENT_USER

echo -e "${GREEN}✓ User '$CURRENT_USER' added to docker group${NC}"

# ==================== VERIFICATION ====================
echo ""
echo "=========================================="
echo "Configuration Complete!"
echo "=========================================="
echo ""
echo "Installed versions:"
echo "  Docker: $(docker --version)"
echo "  Docker Compose: $(docker compose version)"
echo "  Java: $(java -version 2>&1 | head -n 1)"
echo ""
echo -e "${YELLOW}⚠️  IMPORTANT: Please logout and login again for docker group changes to take effect${NC}"
echo ""
echo "To verify docker without sudo, run after re-login:"
echo "  docker ps"
echo ""
echo "=========================================="
