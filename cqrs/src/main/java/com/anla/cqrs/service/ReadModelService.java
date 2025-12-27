package com.anla.cqrs.service;

import com.anla.cqrs.config.MultiDatabaseConfig;
import com.anla.cqrs.model.ReadModel;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReadModelService {
    
    private final MongoClient mongoClient;
    private final MultiDatabaseConfig multiDatabaseConfig;
    
    private MongoCollection<Document> getCollection(String serviceName) {
        return mongoClient.getDatabase(multiDatabaseConfig.getMongoDatabase(serviceName))
            .getCollection("read_models");
    }
    
    public void saveReadModel(ReadModel readModel) {
        getCollection(readModel.getServiceName()).replaceOne(
            new Document("aggregateId", readModel.getAggregateId()),
            new Document("aggregateId", readModel.getAggregateId())
                .append("data", readModel.getData())
                .append("lastUpdated", readModel.getLastUpdated().toString()),
            new com.mongodb.client.model.ReplaceOptions().upsert(true));
    }
    
    public void deleteReadModel(String serviceName, String aggregateId) {
        getCollection(serviceName).deleteOne(new Document("aggregateId", aggregateId));
    }
    
    public ReadModel findByAggregateId(String serviceName, String aggregateId) {
        Document doc = getCollection(serviceName).find(new Document("aggregateId", aggregateId)).first();
        return doc != null ? toReadModel(doc, serviceName) : null;
    }
    
    public List<ReadModel> findByServiceName(String serviceName) {
        List<ReadModel> models = new ArrayList<>();
        for (Document doc : getCollection(serviceName).find()) {
            models.add(toReadModel(doc, serviceName));
        }
        return models;
    }
    
    private ReadModel toReadModel(Document doc, String serviceName) {
        ReadModel model = new ReadModel();
        model.setId(doc.getObjectId("_id").toString());
        model.setServiceName(serviceName);
        model.setAggregateId(doc.getString("aggregateId"));
        model.setData((Map<String, Object>) doc.get("data"));
        model.setLastUpdated(LocalDateTime.parse(doc.getString("lastUpdated")));
        return model;
    }
}