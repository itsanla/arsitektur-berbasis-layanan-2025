package com.anla.Peminjaman.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CqrsClientService {
    
    @Value("${cqrs.service.url}")
    private String cqrsUrl;
    
    private final RestTemplate restTemplate = new RestTemplate();
    
    public void save(Object data, String entityId) {
        restTemplate.postForObject(cqrsUrl + "/api/cqrs/peminjaman/command", 
            Map.of("id", entityId, "eventType", "CREATE", "data", data), String.class);
    }
    
    public void update(Object data, String entityId) {
        restTemplate.postForObject(cqrsUrl + "/api/cqrs/peminjaman/command", 
            Map.of("id", entityId, "eventType", "UPDATE", "data", data), String.class);
    }
    
    public void delete(String entityId) {
        restTemplate.postForObject(cqrsUrl + "/api/cqrs/peminjaman/command", 
            Map.of("id", entityId, "eventType", "DELETE", "data", Map.of("deleted", true)), String.class);
    }
    
    @SuppressWarnings("unchecked")
    public Object findById(String entityId) {
        try {
            Map<String, Object> response = restTemplate.getForObject(
                cqrsUrl + "/api/cqrs/peminjaman/query/" + entityId, Map.class);
            return response != null ? response.get("data") : null;
        } catch (Exception e) {
            return null;
        }
    }
    
    @SuppressWarnings("unchecked")
    public List<Object> findAll() {
        try {
            List<Object> result = restTemplate.getForObject(cqrsUrl + "/api/cqrs/peminjaman/query", List.class);
            return result != null ? result : List.of();
        } catch (Exception e) {
            return List.of();
        }
    }
}
