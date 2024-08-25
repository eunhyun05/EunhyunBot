package io.github.eunhyun.eunhyunbot.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import io.github.eunhyun.eunhyunbot.EunhyunBot;
import io.github.eunhyun.eunhyunbot.api.repository.TicketRepository;
import org.bson.Document;
import org.bson.conversions.Bson;

public class TicketRepositoryImpl implements TicketRepository {

    private final MongoCollection<Document> collection = EunhyunBot.getMongoCollectionHelper().getCollection(EunhyunBot.getInstance().getConfig().getString("mongodb_collections.ticket"));

    @Override
    public synchronized long incrementTicketCount() {
        Bson filter = Filters.exists("count");

        Document ticketCounter = collection.find(filter).first();

        if (ticketCounter == null) {
            ticketCounter = new Document("count", 1L);
            collection.insertOne(ticketCounter);
        } else {
            long currentCount = ticketCounter.getLong("count");
            ticketCounter.put("count", currentCount + 1);
            collection.replaceOne(filter, ticketCounter);
        }

        return ticketCounter.getLong("count");
    }

    public long getTicketCount() {
        Document ticketCounter = collection.find().first();
        return ticketCounter != null ? ticketCounter.getLong("count") : 0L;
    }
}