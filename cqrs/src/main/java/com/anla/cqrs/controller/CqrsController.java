package com.anla.cqrs.controller;

import com.anla.cqrs.event.Event;
import com.anla.cqrs.model.ReadModel;
import com.anla.cqrs.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cqrs")
@RequiredArgsConstructor
public class CqrsController {
    
    private final EventStoreService eventStoreService;
    private final ReadModelService readModelService;
    private final ObjectMapper objectMapper;
    private final AuthService authService;
    
    @PostMapping("/{serviceName}/command")
    public ResponseEntity<String> handleCommand(@PathVariable String serviceName,
                                                @RequestBody Map<String, Object> command,
                                                HttpServletRequest request) {
        if (!authService.isAuthorized(serviceName, getClientIP(request))) {
            return ResponseEntity.status(403).body("Unauthorized");
        }
        
        try {
            Event event = new Event(null, serviceName, command.get("id").toString(),
                command.get("eventType").toString(),
                objectMapper.writeValueAsString(command.get("data")),
                LocalDateTime.now(), 1L);
            
            eventStoreService.saveEvent(event);
            updateReadModel(serviceName, event);
            
            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/{serviceName}/query")
    public ResponseEntity<List<ReadModel>> getAllByService(@PathVariable String serviceName,
                                                           HttpServletRequest request) {
        if (!authService.isAuthorized(serviceName, getClientIP(request))) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(readModelService.findByServiceName(serviceName));
    }
    
    @GetMapping("/{serviceName}/query/{id}")
    public ResponseEntity<ReadModel> getById(@PathVariable String serviceName,
                                             @PathVariable String id,
                                             HttpServletRequest request) {
        if (!authService.isAuthorized(serviceName, getClientIP(request))) {
            return ResponseEntity.status(403).build();
        }
        ReadModel model = readModelService.findByAggregateId(serviceName, id);
        return model != null ? ResponseEntity.ok(model) : ResponseEntity.notFound().build();
    }
    
    @GetMapping("/{serviceName}/events")
    public ResponseEntity<List<Map<String, Object>>> getEventsByService(@PathVariable String serviceName) {
        return ResponseEntity.ok(eventStoreService.findEventsByService(serviceName));
    }
    
    @GetMapping("/{serviceName}/events/{aggregateId}")
    public ResponseEntity<List<Map<String, Object>>> getEventsByAggregate(@PathVariable String serviceName,
                                                                           @PathVariable String aggregateId) {
        return ResponseEntity.ok(eventStoreService.findEventsByAggregate(serviceName, aggregateId));
    }
    
    private String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty()) return ip.split(",")[0].trim();
        ip = request.getHeader("X-Service-IP");
        return ip != null && !ip.isEmpty() ? ip : request.getRemoteAddr();
    }
    
    private void updateReadModel(String serviceName, Event event) {
        try {
            if ("DELETE".equals(event.getEventType())) {
                readModelService.deleteReadModel(serviceName, event.getAggregateId());
            } else {
                ReadModel model = new ReadModel(null, serviceName, event.getAggregateId(),
                    objectMapper.readValue(event.getEventData(), Map.class), LocalDateTime.now());
                readModelService.saveReadModel(model);
            }
        } catch (Exception e) {
            System.err.println("Error updating read model: " + e.getMessage());
        }
    }
}