# 🚀 Setup Jenkins CI/CD untuk Service Buku di VPS EC2

## 📋 Prerequisites
- VPS EC2 Ubuntu (t2.medium minimum, 4GB RAM)
- Security Group: Port 8080, 22 terbuka
- SSH Key untuk akses VPS

---

## 🔧 Step 1: Setup VPS

### 1.1 SSH ke VPS
```bash
ssh -i your-key.pem ubuntu@your-ec2-ip
```

### 1.2 Upload & Run Setup Script
```bash
# Di local machine, upload script
scp -i your-key.pem setup-jenkins-vps.sh ubuntu@your-ec2-ip:~/

# Di VPS, jalankan script
chmod +x setup-jenkins-vps.sh
bash setup-jenkins-vps.sh
```

**Script akan install:**
- Docker
- Java 17
- Jenkins
- Git
- Maven

**Waktu: ~5-10 menit**

---

## 🌐 Step 2: Akses Jenkins

### 2.1 Buka Browser
```
http://your-ec2-ip:8080
```

### 2.2 Unlock Jenkins
Paste password dari output script atau:
```bash
sudo cat /var/lib/jenkins/secrets/initialAdminPassword
```

### 2.3 Install Suggested Plugins
Klik "Install suggested plugins" → Tunggu selesai

### 2.4 Create Admin User
- Username: admin
- Password: [your-password]
- Email: [your-email]

---

## 🔑 Step 3: Setup Docker Hub Credentials

### 3.1 Buat Docker Hub Token
1. Login ke https://hub.docker.com
2. Account Settings → Security → New Access Token
3. Copy token

### 3.2 Add Credentials di Jenkins
1. Dashboard → Manage Jenkins → Credentials
2. (global) → Add Credentials
3. **Kind**: Username with password
4. **Username**: `itsanla`
5. **Password**: [paste token]
6. **ID**: `docker-hub-credentials`
7. Save

---

## 📦 Step 4: Create Pipeline Job

### 4.1 New Item
1. Dashboard → New Item
2. Name: `buku-service-pipeline`
3. Type: Pipeline
4. OK

### 4.2 Configure Pipeline

**General:**
- ✅ GitHub project: `https://github.com/your-username/your-repo`

**Build Triggers:**
- ✅ Poll SCM: `H/5 * * * *` (check every 5 minutes)

**Pipeline:**
- **Definition**: Pipeline script from SCM
- **SCM**: Git
- **Repository URL**: `https://github.com/your-username/your-repo.git`
- **Branch**: `*/main`
- **Script Path**: `Jenkinsfile-buku`

Save

---

## 🚀 Step 5: Run Pipeline

### 5.1 Manual Build
1. Dashboard → buku-service-pipeline
2. Build Now
3. Monitor Console Output

### 5.2 Verify Success
```bash
# Check Docker images
docker images | grep buku

# Check Docker Hub
# https://hub.docker.com/r/itsanla/buku/tags
```

---

## 🔍 Troubleshooting

### Jenkins tidak bisa akses Docker
```bash
sudo usermod -aG docker jenkins
sudo systemctl restart jenkins
```

### Build gagal - Maven error
```bash
# Di VPS, test manual build
cd /var/lib/jenkins/workspace/buku-service-pipeline/Perpustakaan/Buku
./mvnw clean package
```

### Port 8080 tidak bisa diakses
Check Security Group EC2:
- Inbound Rules → Add Rule
- Type: Custom TCP
- Port: 8080
- Source: 0.0.0.0/0

---

## 📊 Pipeline Stages

1. **Checkout** - Clone repository
2. **Test** - Run unit tests (`mvnw test`)
3. **Build** - Build JAR (`mvnw package`)
4. **Docker Build** - Build image dengan tag
5. **Deploy** - Push ke Docker Hub

---

## 🎯 Expected Output

```
✅ Checkout - SUCCESS
✅ Test - SUCCESS  
✅ Build - SUCCESS
✅ Docker Build - SUCCESS
✅ Deploy to Docker Hub - SUCCESS

Docker Images:
- itsanla/buku:123 (build number)
- itsanla/buku:latest
```

---

## 📝 Notes

- Build number auto increment setiap build
- Push ke Docker Hub hanya di branch `main`
- Test results tersimpan di Jenkins
- Workspace di: `/var/lib/jenkins/workspace/buku-service-pipeline`

---

## 🔄 Auto Build on Git Push

### Setup Webhook (Optional)
1. GitHub Repo → Settings → Webhooks
2. Add webhook
3. Payload URL: `http://your-ec2-ip:8080/github-webhook/`
4. Content type: application/json
5. Events: Just the push event
6. Save

Sekarang setiap push ke GitHub akan trigger build otomatis!
