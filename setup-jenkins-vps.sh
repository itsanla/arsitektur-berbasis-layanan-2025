#!/bin/bash

# Setup Jenkins CI/CD untuk Service Buku di VPS EC2
# Run: bash setup-jenkins-vps.sh

set -e

echo "🚀 Starting Jenkins Setup on EC2..."

# Update system
echo "📦 Updating system..."
sudo apt-get update -y
sudo apt-get upgrade -y

# Install Java 17
echo "☕ Installing Java 17..."
sudo apt-get install -y openjdk-17-jdk

# Install Jenkins
echo "🔧 Installing Jenkins..."
curl -fsSL https://pkg.jenkins.io/debian-stable/jenkins.io-2023.key | sudo tee /usr/share/keyrings/jenkins-keyring.asc > /dev/null
echo deb [signed-by=/usr/share/keyrings/jenkins-keyring.asc] https://pkg.jenkins.io/debian-stable binary/ | sudo tee /etc/apt/sources.list.d/jenkins.list > /dev/null
sudo apt-get update -y
sudo apt-get install -y jenkins

# Start Jenkins
sudo systemctl start jenkins
sudo systemctl enable jenkins

# Install Git
echo "📚 Installing Git..."
sudo apt-get install -y git

# Install Maven
echo "🔨 Installing Maven..."
sudo apt-get install -y maven

# Add Jenkins to Docker group (Docker already installed)
echo "🔗 Adding Jenkins to Docker group..."
sudo usermod -aG docker jenkins

# Get Jenkins initial password
echo ""
echo "✅ Installation Complete!"
echo ""
echo "📋 Jenkins Initial Admin Password:"
sudo cat /var/lib/jenkins/secrets/initialAdminPassword
echo ""
echo "🌐 Access Jenkins at: http://$(curl -s ifconfig.me):8080"
echo ""
echo "⚠️  Make sure Security Group allows:"
echo "   - Port 8080 (Jenkins)"
echo "   - Port 22 (SSH)"
echo ""
echo "🔄 Restarting Jenkins to apply Docker permissions..."
sudo systemctl restart jenkins
echo ""
echo "✅ Setup Complete! Wait 30 seconds then access Jenkins."
