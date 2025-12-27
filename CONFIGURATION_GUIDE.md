# 🔧 VPS Configuration Script

## 📋 What This Script Does

The `configuration.sh` script automatically sets up your Ubuntu VPS with:

1. ✅ **Docker Engine** - Latest stable version
2. ✅ **JDK 17** - Required for Spring Boot services
3. ✅ **Docker Group** - Adds user to docker group (no sudo needed)

## 🚀 Usage

### On Fresh Ubuntu VPS:

```bash
# 1. Clone repository
git clone <your-repo-url>
cd arsitektur-berbasis-layanan-2025

# 2. Run configuration script
sudo bash configuration.sh

# 3. Logout and login again
exit
# SSH again to apply docker group changes

# 4. Verify installation
docker ps
java -version
```

## 📦 What Gets Installed

### Docker Components:
- `docker-ce` - Docker Engine
- `docker-ce-cli` - Docker CLI
- `containerd.io` - Container runtime
- `docker-buildx-plugin` - Build plugin
- `docker-compose-plugin` - Compose V2

### Java:
- `openjdk-17-jdk` - Java Development Kit 17
- Removes any existing JDK versions
- Sets JDK 17 as default

### Permissions:
- Adds current user to `docker` group
- Allows running docker without `sudo`

## ✅ Verification

After running the script and re-login:

```bash
# Check Docker
docker --version
docker compose version
docker ps

# Check Java
java -version
javac -version

# Should show:
# openjdk version "17.x.x"
```

## 🔄 Run on Each VPS

This script should be run on:
- ✅ VPS 1 (Eureka + RabbitMQ)
- ✅ VPS 2 (API Gateways)
- ✅ VPS 3 (ELK Stack)
- ✅ VPS 4 (Jenkins)
- ✅ VPS 5 (Marketplace Services)
- ✅ VPS 6 (Perpustakaan Services)

## ⚠️ Important Notes

1. **Requires sudo**: Script must be run with sudo
2. **Re-login required**: Docker group changes need re-login
3. **Ubuntu only**: Designed for Ubuntu 20.04+
4. **JDK 17 only**: Removes other JDK versions

## 🐛 Troubleshooting

### Issue: Docker permission denied

```bash
# Solution: Re-login or run
newgrp docker
```

### Issue: Wrong Java version

```bash
# Check alternatives
sudo update-alternatives --config java

# Select Java 17
```

### Issue: Docker not starting

```bash
# Check status
sudo systemctl status docker

# Start docker
sudo systemctl start docker
sudo systemctl enable docker
```

## 📝 Manual Steps (If Script Fails)

### Install Docker Manually:
```bash
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh
sudo usermod -aG docker $USER
```

### Install JDK 17 Manually:
```bash
sudo apt update
sudo apt install -y openjdk-17-jdk
```

## 🎯 Next Steps After Configuration

1. ✅ Verify installations
2. ✅ Clone repository
3. ✅ Navigate to service directory
4. ✅ Edit `.env` file with actual IPs
5. ✅ Run `docker-compose up -d`

## 📚 Related Documentation

- [DEPLOYMENT_GUIDE.md](./DEPLOYMENT_GUIDE.md) - Full deployment guide
- [ARCHITECTURE_SUMMARY.md](./ARCHITECTURE_SUMMARY.md) - Architecture overview
