package com.anla.cqrs.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@RequiredArgsConstructor
public class MultiDatabaseConfig {
    
    private final Map<String, DataSource> h2DataSources = new ConcurrentHashMap<>();
    
    public DataSource getH2DataSource(String serviceName) {
        return h2DataSources.computeIfAbsent(serviceName, name ->
            DataSourceBuilder.create()
                .driverClassName("org.h2.Driver")
                .url("jdbc:h2:file:./h2db/" + name + "_eventstore")
                .username("sa")
                .password("password")
                .build()
        );
    }
    
    public String getMongoDatabase(String serviceName) {
        return "cqrs_" + serviceName;
    }
}