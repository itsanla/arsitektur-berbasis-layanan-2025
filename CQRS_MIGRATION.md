# 📋 CQRS Migration - Perpustakaan Services

## 🎯 Overview
Migrasi 3 service perpustakaan (Anggota, Pengembalian, Peminjaman) dari database lokal H2 ke CQRS service yang terpisah di VPS menggunakan IP private EC2.

## ✅ Services yang Telah Dimigrasi

### 1. **Anggota Service** (Port 8085)
- ✅ Model: Converted from JPA Entity to POJO
- ✅ CqrsClientService: Created for CQRS communication
- ✅ AnggotaService: Refactored to use CQRS pattern
- ✅ AnggotaController: Simplified to match Buku pattern
- ✅ Configuration: Updated application.properties

### 2. **Pengembalian Service** (Port 8086)
- ✅ Model: Converted from JPA Entity to POJO
- ✅ CqrsClientService: Created for CQRS communication
- ✅ PengembalianService: Refactored to use CQRS pattern
- ✅ PengembalianController: Simplified to match Buku pattern
- ✅ Configuration: Updated application.properties

### 3. **Peminjaman Service** (Port 8087)
- ✅ Model: Converted from JPA Entity to POJO
- ✅ CqrsClientService: Created for CQRS communication
- ✅ PeminjamanService: Refactored to use CQRS pattern with RabbitMQ support
- ✅ PeminjamanController: Simplified to match Buku pattern
- ✅ Configuration: Updated application.properties (kept RabbitMQ config)

## 🏗️ Arsitektur Pattern (Berdasarkan Buku Service)

### Model Layer
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Entity {
    @JsonProperty("id")
    private Long id;
    // Other fields with @JsonProperty for snake_case mapping
}
```

### CqrsClientService
```java
@Service
@RequiredArgsConstructor
public class CqrsClientService {
    @Value("${cqrs.service.url}")
    private String cqrsUrl;
    
    private final RestTemplate restTemplate = new RestTemplate();
    
    // CRUD operations via REST API to CQRS service
    public void save(Object data, String entityId) { }
    public void update(Object data, String entityId) { }
    public void delete(String entityId) { }
    public Object findById(String entityId) { }
    public List<Object> findAll() { }
}
```

### Service Layer
```java
@Service
@RequiredArgsConstructor
public class EntityService {
    private final CqrsClientService cqrsClient;
    private final AtomicLong idCounter = new AtomicLong(1);
    
    // Business logic using CQRS client
}
```

### Controller Layer
```java
@RestController
@RequestMapping("/api/entity")
@RequiredArgsConstructor
public class EntityController {
    private final EntityService entityService;
    
    // Standard REST endpoints
}
```

## 🔧 Configuration Changes

### Before (H2 Database)
```properties
spring.datasource.url=jdbc:h2:file:./h2db/db/produk
spring.datasource.driverClassName=org.h2.Driver
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
```

### After (CQRS Service)
```properties
# CQRS Database Configuration
cqrs.service.url=http://127.0.0.1:8088
cqrs.service.name=<service-name>
cqrs.auth.enabled=true

# Enhanced Eureka Configuration
eureka.client.lease-renewal-interval-in-seconds=1
eureka.client.lease-expiration-duration-in-seconds=2

# Graceful shutdown
server.shutdown=graceful
spring.lifecycle.timeout-per-shutdown-phase=3s
```

## 📡 CQRS API Endpoints

### Command (Write Operations)
```bash
POST http://127.0.0.1:8088/api/cqrs/{service}/command
Body: {
  "id": "1",
  "eventType": "CREATE|UPDATE|DELETE",
  "data": { ... }
}
```

### Query (Read Operations)
```bash
# Get by ID
GET http://127.0.0.1:8088/api/cqrs/{service}/query/{id}

# Get all
GET http://127.0.0.1:8088/api/cqrs/{service}/query
```

## 🔑 Key Changes

### 1. **No More JPA Dependencies**
- Removed `@Entity`, `@Id`, `@GeneratedValue`
- Removed JpaRepository
- No more H2 database configuration

### 2. **POJO Models with Jackson**
- Using `@JsonProperty` for field mapping
- Support for snake_case to camelCase conversion

### 3. **RestTemplate Communication**
- All CRUD operations via HTTP REST calls
- Centralized in CqrsClientService

### 4. **AtomicLong for ID Generation**
- Client-side ID generation using AtomicLong
- No more database auto-increment

### 5. **Simplified Controllers**
- Removed ResponseEntity wrapping (except for special cases)
- Direct return of service results

## 🚀 Deployment Configuration

### Local Development
```properties
cqrs.service.url=http://127.0.0.1:8088
```

### Production (VPS with Private IP)
```properties
cqrs.service.url=http://<EC2_PRIVATE_IP>:8088
```

## 📊 Service Dependencies

```
┌─────────────────────────────────────────────────────────┐
│              CQRS Service (VPS/EC2)                     │
│              MongoDB + Event Store                       │
│              Port: 8088                                  │
└─────────────────────────────────────────────────────────┘
                         ▲
                         │ HTTP REST API
                         │
        ┌────────────────┼────────────────┐
        │                │                │
┌───────▼──────┐  ┌──────▼──────┐  ┌─────▼────────┐
│   Anggota    │  │ Pengembalian │  │  Peminjaman  │
│   Service    │  │   Service    │  │   Service    │
│   (8085)     │  │   (8086)     │  │   (8087)     │
└──────────────┘  └──────────────┘  └──────────────┘
```

## 🎯 Benefits

1. **Centralized Data Management**: Semua data dikelola oleh CQRS service
2. **Scalability**: Services dapat di-scale tanpa database overhead
3. **Event Sourcing**: CQRS service menyimpan event history
4. **Separation of Concerns**: Read/Write operations terpisah
5. **Flexibility**: Mudah switch database backend di CQRS service

## 📝 Git Commits

```bash
# Commit 1: Anggota Service
refactor(anggota): migrate to CQRS pattern without local database

# Commit 2: Pengembalian Service
refactor(pengembalian): migrate to CQRS pattern without local database

# Commit 3: Peminjaman Service
refactor(peminjaman): migrate to CQRS pattern without local database

# Commit 4: Configuration
config: update application.properties to use CQRS service instead of local H2 database
```

## 🔍 Testing

### Test CQRS Connection
```bash
# Check CQRS service health
curl http://127.0.0.1:8088/actuator/health

# Test create operation
curl -X POST http://localhost:8085/api/anggota \
  -H "Content-Type: application/json" \
  -d '{"nim":"123","nama":"Test","alamat":"Test","jenis_kelamin":"L","email":"test@test.com"}'

# Test read operation
curl http://localhost:8085/api/anggota/1
```

## ⚠️ Important Notes

1. **CQRS Service Must Be Running**: Pastikan CQRS service berjalan sebelum start services
2. **Network Connectivity**: Pastikan services dapat akses CQRS service via IP
3. **ID Generation**: ID di-generate client-side, pastikan tidak ada konflik
4. **Error Handling**: CqrsClientService mengembalikan null jika error

## 🔄 Rollback Plan

Jika perlu rollback ke H2:
1. Restore model dengan JPA annotations
2. Restore Repository classes
3. Restore application.properties dengan H2 config
4. Remove CqrsClientService

## 📚 References

- Buku Service: `/perpustakaan/buku/` (Reference implementation)
- CQRS Service: `/cqrs/` (Backend service)
- Docker Compose: `/cqrs/docker-compose.yml`

---

**Status**: ✅ Migration Complete  
**Date**: 2025  
**Services Migrated**: 3/3 (Anggota, Pengembalian, Peminjaman)  
**Pattern**: CQRS with Event Sourcing
