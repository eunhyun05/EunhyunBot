package io.github.eunhyun.eunhyunbot.api.mongodb;

import com.mongodb.client.MongoCollection;
import io.github.eunhyun.eunhyunbot.api.configuration.FileConfiguration;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.jetbrains.annotations.NotNull;

public class MongoCollectionHelper {

    private final MongoConnectionHandler database;

    public MongoCollectionHelper(FileConfiguration config) {
        this.database = new MongoConnectionHandler(config);
    }

    public void createCollection(@NotNull String collectionName) {
        database.getDatabase().createCollection(collectionName);
    }

    public void deleteCollection(@NotNull String collectionName) {
        database.getDatabase().getCollection(collectionName).drop();
    }

    public MongoCollection<Document> getCollection(@NotNull String collectionName) {
        return database.getDatabase().getCollection(collectionName);
    }

    public <TDocument> MongoCollection<TDocument> getCollection(@NotNull String collectionName, @NotNull Class<TDocument> clazz) {
        return database.getDatabase().getCollection(collectionName, clazz);
    }

    public void insertDocument(@NotNull String collectionName, @NotNull Document document) {
        MongoCollection<Document> collection = database.getDatabase().getCollection(collectionName);
        collection.insertOne(document);
    }

    public void updateDocument(@NotNull String collectionName, @NotNull Document document, @NotNull Bson updates) {
        MongoCollection<Document> collection = database.getDatabase().getCollection(collectionName);
        collection.updateOne(document, updates);
    }

    public void deleteDocument(@NotNull String collectionName, @NotNull Document document) {
        MongoCollection<Document> collection = database.getDatabase().getCollection(collectionName);
        collection.deleteOne(document);
    }
}