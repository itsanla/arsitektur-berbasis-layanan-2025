# 🔢 Manual Version Control Guide

## 📋 Version Bump Strategy

### Automatic (Default) - Patch Bump
```
1.0.9 → 1.0.10 (patch bump)
1.0.10 → 1.0.11 (patch bump)
```

### Manual Control via Git Commit Message

**Commit message keywords:**
- `[major]` → Bump major version (1.0.10 → 2.0.0)
- `[minor]` → Bump minor version (1.0.10 → 1.1.0)
- `[patch]` or default → Bump patch version (1.0.10 → 1.0.11)

## 🚀 How to Use

### Example 1: Minor Version Bump (1.0.10 → 1.1.0)

```bash
# Make your changes
git add .
git commit -m "[minor] Add new feature to produk service"
git push origin main

# Jenkins will detect [minor] and bump to 1.1.0
```

**Jenkins Output:**
```
[Get Version] Commit message: [minor] Add new feature to produk service
[Get Version] Minor version bump detected
[Get Version] Current version: 1.0.10
[Get Version] New version: 1.1.0
```

### Example 2: Major Version Bump (1.0.10 → 2.0.0)

```bash
# Make breaking changes
git add .
git commit -m "[major] Breaking change: refactor API endpoints"
git push origin main

# Jenkins will detect [major] and bump to 2.0.0
```

**Jenkins Output:**
```
[Get Version] Commit message: [major] Breaking change: refactor API endpoints
[Get Version] Major version bump detected
[Get Version] Current version: 1.0.10
[Get Version] New version: 2.0.0
```

### Example 3: Patch Version Bump (1.0.10 → 1.0.11) - Default

```bash
# Bug fix or small change
git add .
git commit -m "fix: resolve null pointer exception"
git push origin main

# Jenkins will auto-bump patch version to 1.0.11
```

**Jenkins Output:**
```
[Get Version] Commit message: fix: resolve null pointer exception
[Get Version] Patch version bump (default)
[Get Version] Current version: 1.0.10
[Get Version] New version: 1.0.11
```

## 📊 Version Bump Logic

```groovy
if (commitMsg.contains('[major]')) {
    major += 1
    minor = 0
    patch = 0
} else if (commitMsg.contains('[minor]')) {
    minor += 1
    patch = 0
} else {
    patch += 1  // Default
}
```

## 🎯 Real-World Scenarios

### Scenario 1: Feature Development
```bash
# Current: 1.5.3
git commit -m "[minor] Add user authentication feature"
# Result: 1.6.0
```

### Scenario 2: Breaking API Change
```bash
# Current: 1.6.0
git commit -m "[major] Remove deprecated endpoints"
# Result: 2.0.0
```

### Scenario 3: Bug Fixes
```bash
# Current: 2.0.0
git commit -m "fix: memory leak in order processing"
# Result: 2.0.1

git commit -m "fix: validation error"
# Result: 2.0.2
```

### Scenario 4: Multiple Services
```bash
# Only affects the service you're modifying
cd marketplace/produk
# Make changes
git commit -m "[minor] Add product categories"
# Only produk service bumps to 1.1.0
# Other services remain unchanged
```

## 📝 Best Practices

### 1. Use Semantic Versioning
- **Major (X.0.0)**: Breaking changes
- **Minor (1.X.0)**: New features (backward compatible)
- **Patch (1.0.X)**: Bug fixes

### 2. Clear Commit Messages
```bash
# Good
git commit -m "[major] Remove v1 API endpoints"
git commit -m "[minor] Add export to CSV feature"
git commit -m "fix: null pointer in payment processing"

# Bad
git commit -m "update"
git commit -m "changes"
```

### 3. One Version Bump Per Commit
```bash
# Don't do this
git commit -m "[major][minor] confusing message"

# Do this
git commit -m "[major] Breaking change description"
```

## 🔍 Verify Version on Docker Hub

```bash
# Check all tags
curl -s 'https://registry.hub.docker.com/v2/repositories/itsanla/produk/tags' | jq '.results[].name'

# Expected output:
"latest"
"2.0.0"
"1.1.0"
"1.0.11"
"1.0.10"
```

## ⚠️ Important Notes

1. **Keyword is case-sensitive**: Use `[major]` not `[Major]` or `[MAJOR]`
2. **Keyword can be anywhere**: `[minor] Add feature` or `Add feature [minor]` both work
3. **Default is patch**: If no keyword, patch version increments
4. **First version**: If no tags exist, starts at `1.0.0`

## 🛠️ Troubleshooting

### Issue: Version didn't bump as expected

**Check:**
```bash
# View Jenkins console output
# Look for "Get Version" stage
# Verify commit message was detected correctly
```

### Issue: Want to skip version bump

**Solution:**
```bash
# Currently not supported
# Every build creates a new version
# Consider using git tags instead of building
```

## 📈 Version History Example

```
Timeline:
1.0.0  - Initial release
1.0.1  - Bug fix
1.0.2  - Bug fix
1.1.0  - [minor] New feature
1.1.1  - Bug fix
1.2.0  - [minor] Another feature
2.0.0  - [major] Breaking change
2.0.1  - Bug fix
2.1.0  - [minor] New feature
```

## ✅ Summary

| Commit Message | Current | New Version |
|----------------|---------|-------------|
| `[major] ...` | 1.0.10 | 2.0.0 |
| `[minor] ...` | 1.0.10 | 1.1.0 |
| `fix: ...` | 1.0.10 | 1.0.11 |
| `any message` | 1.0.10 | 1.0.11 |

**All Jenkinsfiles updated with this logic!** ✅
