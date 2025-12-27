package com.anla.cqrs.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    @Value("${app.services}")
    private String servicesConfig;
    
    @Value("${app.auth.enabled:true}")
    private boolean authEnabled;
    
    private Map<String, String> serviceIpMap;
    
    public boolean isAuthorized(String serviceName, String clientIP) {
        if (!authEnabled) return true;
        if (serviceIpMap == null) initializeServiceMap();
        String expectedIP = serviceIpMap.get(serviceName);
        return expectedIP != null && expectedIP.equals(clientIP);
    }
    
    private void initializeServiceMap() {
        serviceIpMap = new HashMap<>();
        for (String service : servicesConfig.split(",")) {
            String[] parts = service.trim().split(":");
            if (parts.length == 3) {
                serviceIpMap.put(parts[0], parts[1]);
            }
        }
    }
}