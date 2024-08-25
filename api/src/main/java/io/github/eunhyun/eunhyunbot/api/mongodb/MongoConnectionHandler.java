package io.github.eunhyun.eunhyunbot.api.mongodb;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import io.github.eunhyun.eunhyunbot.api.configuration.FileConfiguration;
import lombok.Getter;
import org.bson.UuidRepresentation;

@Getter
public class MongoConnectionHandler {

    private final MongoClient client;
    private final MongoDatabase database;

    public MongoConnectionHandler(FileConfiguration config) {
        this(new ConnectionString(config.getString("database_settings.url")), config);
    }

    public MongoConnectionHandler(ConnectionString uri, FileConfiguration config) {
        try {
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(uri)
                    .uuidRepresentation(UuidRepresentation.STANDARD)
                    .build();
            this.client = MongoClients.create(settings);
            this.database = client.getDatabase(config.getString("database_settings.database"));
        } catch (Exception e) {
            throw new IllegalStateException("Failed to establish a connection to the MongoDB database. " +
                    "Please check the supplied database credentials in the config file", e);
        }
    }

    public void closeConnection() {
        if (this.client != null) {
            client.close();
        }
    }
}