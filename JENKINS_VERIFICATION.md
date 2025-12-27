# ✅ Jenkins CI/CD Pipeline - Verification Guide

## 📋 Changes Made

### 1. Jenkins Docker Compose Fixed
**File:** `jenkins/docker-compose.yml`

**Changes:**
- ✅ Removed Docker-in-Docker (DinD) container
- ✅ Use Docker socket directly: `/var/run/docker.sock`
- ✅ Simplified configuration
- ✅ Removed unnecessary environment variables

**Why:** Direct socket access is simpler and more reliable for building Docker images.

### 2. Semantic Versioning Added to All Jenkinsfiles

**Services Updated:**
- ✅ marketplace/produk/Jenkinsfile
- ✅ marketplace/pelanggan/Jenkinsfile
- ✅ marketplace/order/Jenkinsfile
- ✅ perpustakaan/buku/Jenkinsfile
- ✅ perpustakaan/anggota/Jenkinsfile
- ✅ perpustakaan/pengembalian/Jenkinsfile
- ✅ perpustakaan/peminjaman/Jenkinsfile

**New Stage Added:** `Get Version`
```groovy
stage('Get Version') {
    steps {
        script {
            // Fetch latest version from Docker Hub
            def tags = sh(
                script: """curl -s 'https://registry.hub.docker.com/v2/repositories/${DOCKER_IMAGE}/tags?page_size=100' | \
                           grep -o '"name":"[0-9]\\+\\.[0-9]\\+\\.[0-9]\\+"' | \
                           grep -o '[0-9]\\+\\.[0-9]\\+\\.[0-9]\\+' | \
                           sort -V | tail -1""",
                returnStdout: true
            ).trim()
            
            // Calculate new version
            if (tags == '') {
                env.NEW_VERSION = '1.0.0'  // First version
            } else {
                def version = tags.tokenize('.')
                def major = version[0].toInteger()
                def minor = version[1].toInteger()
                def patch = version[2].toInteger() + 1
                env.NEW_VERSION = "${major}.${minor}.${patch}"
            }
            
            echo "Current version: ${tags ?: 'none'}"
            echo "New version: ${env.NEW_VERSION}"
        }
    }
}
```

**Version Logic:**
```
Current Tag: 1.0.9
New Tag: 1.0.10

Current Tag: none
New Tag: 1.0.0

Current Tag: 2.5.99
New Tag: 2.5.100
```

**Docker Tags Pushed:**
- `itsanla/<service>:1.0.10` (semantic version)
- `itsanla/<service>:latest` (always latest)

## 🚀 Testing Jenkins Pipeline

### Step 1: Start Jenkins

```bash
cd jenkins/
docker-compose up -d

# Wait for Jenkins to start
docker logs -f jenkins
```

### Step 2: Get Initial Admin Password

```bash
docker exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword
```

### Step 3: Access Jenkins

```
http://localhost:8080
```

### Step 4: Install Required Plugins

1. Install suggested plugins
2. Additional plugins needed:
   - Docker Pipeline
   - Git
   - Credentials Binding

### Step 5: Add Docker Hub Credentials

1. Go to: **Manage Jenkins** → **Credentials** → **System** → **Global credentials**
2. Click **Add Credentials**
3. Fill:
   - Kind: `Username with password`
   - Username: `itsanla`
   - Password: `<your_docker_hub_token>`
   - ID: `docker-hub-credentials`
4. Save

### Step 6: Create Pipeline Job

1. Click **New Item**
2. Enter name: `produk-service`
3. Select: **Pipeline**
4. Click **OK**

5. In **Pipeline** section:
   - Definition: `Pipeline script from SCM`
   - SCM: `Git`
   - Repository URL: `<your_repo_url>`
   - Branch: `*/main`
   - Script Path: `marketplace/produk/Jenkinsfile`

6. Save

### Step 7: Run Pipeline

1. Click **Build Now**
2. Watch console output

**Expected Output:**
```
[Get Version] Current version: 1.0.9
[Get Version] New version: 1.0.10
[Docker Build] Building image: itsanla/produk:1.0.10
[Docker Push] Pushing itsanla/produk:1.0.10
[Docker Push] Pushing itsanla/produk:latest
✅ produk service v1.0.10 built and pushed successfully!
```

## 🔍 Verify on Docker Hub

```bash
# Check tags on Docker Hub
curl -s 'https://registry.hub.docker.com/v2/repositories/itsanla/produk/tags' | jq '.results[].name'

# Expected output:
"latest"
"1.0.10"
"1.0.9"
"1.0.8"
...
```

## 📊 Pipeline Stages

```
1. Checkout          → Clone repository
2. Get Version       → Fetch latest tag, calculate new version
3. Test              → Run mvnw clean test
4. Build             → Run mvnw clean package
5. Docker Build      → Build image with new version
6. Docker Push       → Push to Docker Hub (version + latest)
7. Cleanup           → Remove local images
```

## 🐛 Troubleshooting

### Issue: Docker command not found

**Solution:**
```bash
# Verify Docker socket is mounted
docker exec jenkins ls -la /var/run/docker.sock

# Should show: srw-rw---- 1 root docker
```

### Issue: Permission denied on Docker socket

**Solution:**
```bash
# Add jenkins user to docker group on host
sudo usermod -aG docker jenkins

# Or change socket permissions (not recommended for production)
sudo chmod 666 /var/run/docker.sock
```

### Issue: Cannot fetch tags from Docker Hub

**Solution:**
```bash
# Test curl command manually
docker exec jenkins curl -s 'https://registry.hub.docker.com/v2/repositories/itsanla/produk/tags?page_size=100'

# Should return JSON with tags
```

### Issue: Version calculation fails

**Solution:**
```bash
# Check if grep and sort are available
docker exec jenkins which grep
docker exec jenkins which sort

# Install if missing (shouldn't be needed)
docker exec -u root jenkins apt-get update && apt-get install -y grep coreutils
```

## 📈 Version Management

### Manual Version Bump

If you want to bump major or minor version:

**Option 1: Delete old tags on Docker Hub**
```bash
# This will reset to 1.0.0
# Delete all tags except latest on Docker Hub UI
```

**Option 2: Modify Jenkinsfile temporarily**
```groovy
// For major version bump (1.x.x → 2.0.0)
env.NEW_VERSION = "2.0.0"

// For minor version bump (1.5.x → 1.6.0)
env.NEW_VERSION = "1.6.0"
```

### Version History

Track versions in Git tags:
```bash
# After successful build
git tag -a v1.0.10 -m "Release version 1.0.10"
git push origin v1.0.10
```

## 🎯 Next Steps

1. ✅ Create pipeline jobs for all 7 services
2. ✅ Test each pipeline
3. ✅ Verify tags on Docker Hub
4. ✅ Setup webhook for auto-trigger on git push
5. ✅ Add notification (Slack/Email) on build success/failure

## 📚 Pipeline Templates

All Jenkinsfiles follow the same pattern:
- Semantic versioning
- Test → Build → Docker Build → Push
- Cleanup after build
- Success/Failure notifications

**Consistency = Easy Maintenance** ✅
