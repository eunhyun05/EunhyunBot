package io.github.eunhyun.eunhyunbot.object;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import io.github.eunhyun.eunhyunbot.EunhyunBot;
import io.github.eunhyun.eunhyunbot.api.object.Warn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bson.Document;

@AllArgsConstructor
@Getter
public class WarnImpl implements Warn {

    private static final MongoCollection<Document> warnCollection = EunhyunBot.getMongoCollectionHelper().getCollection(EunhyunBot.getInstance().getConfig().getString("mongodb_collections.warn"));

    private final String discordId;
    private int amount;

    public WarnImpl(String discordId) {
        this.discordId = discordId;
        this.amount = 0;
    }

    @Override
    public void add() {
        warnCollection.updateOne(
                Filters.eq("discordId", discordId),
                Updates.inc("amount", +amount),
                new UpdateOptions().upsert(true)
        );
    }

    @Override
    public void subtract() {
        warnCollection.updateOne(
                Filters.eq("discordId", discordId),
                Updates.inc("amount", -amount),
                new UpdateOptions().upsert(true)
        );
    }

    @Override
    public void set() {
        warnCollection.updateOne(
                Filters.eq("discordId", discordId),
                new Document("$set", new Document("amount", amount)),
                new UpdateOptions().upsert(true)
        );
    }

    @Override
    public void reset() {
        warnCollection.updateOne(
                Filters.eq("discordId", discordId),
                new Document("$set", new Document("amount", 0)),
                new UpdateOptions().upsert(true)
        );
    }

    @Override
    public Document toDocument() {
        return new Document("discordId", discordId)
                .append("amount", amount);
    }

    public static WarnImpl fromDocument(Document document) {
        String discordId = document.getString("discordId");
        int amount = document.getInteger("amount");
        return new WarnImpl(discordId, amount);
    }
}