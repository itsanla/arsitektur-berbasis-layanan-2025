package com.anla.cqrs.service;

import com.anla.cqrs.config.MultiDatabaseConfig;
import com.anla.cqrs.event.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class EventStoreService {
    
    private final MultiDatabaseConfig multiDatabaseConfig;
    private final Map<String, JdbcTemplate> jdbcTemplates = new ConcurrentHashMap<>();
    
    private JdbcTemplate getJdbcTemplate(String serviceName) {
        return jdbcTemplates.computeIfAbsent(serviceName, name -> {
            JdbcTemplate template = new JdbcTemplate(multiDatabaseConfig.getH2DataSource(name));
            template.execute("CREATE TABLE IF NOT EXISTS events (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, service_name VARCHAR(255), " +
                "aggregate_id VARCHAR(255), event_type VARCHAR(255), event_data TEXT, " +
                "timestamp TIMESTAMP, version BIGINT)");
            return template;
        });
    }
    
    public void saveEvent(Event event) {
        getJdbcTemplate(event.getServiceName()).update(
            "INSERT INTO events (service_name, aggregate_id, event_type, event_data, timestamp, version) VALUES (?, ?, ?, ?, ?, ?)",
            event.getServiceName(), event.getAggregateId(), event.getEventType(),
            event.getEventData(), Timestamp.valueOf(event.getTimestamp()), event.getVersion());
    }
    
    public List<Map<String, Object>> findEventsByAggregate(String serviceName, String aggregateId) {
        return getJdbcTemplate(serviceName).queryForList(
            "SELECT * FROM events WHERE service_name = ? AND aggregate_id = ? ORDER BY version ASC",
            serviceName, aggregateId);
    }
    
    public List<Map<String, Object>> findEventsByService(String serviceName) {
        return getJdbcTemplate(serviceName).queryForList(
            "SELECT * FROM events WHERE service_name = ? ORDER BY timestamp DESC", serviceName);
    }
}