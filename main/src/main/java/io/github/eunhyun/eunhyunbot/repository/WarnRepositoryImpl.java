package io.github.eunhyun.eunhyunbot.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import io.github.eunhyun.eunhyunbot.EunhyunBot;
import io.github.eunhyun.eunhyunbot.api.object.Warn;
import io.github.eunhyun.eunhyunbot.api.repository.WarnRepository;
import io.github.eunhyun.eunhyunbot.object.WarnImpl;
import org.bson.Document;

public class WarnRepositoryImpl implements WarnRepository {

    private final MongoCollection<Document> warnCollection = EunhyunBot.getMongoCollectionHelper().getCollection(EunhyunBot.getInstance().getConfig().getString("mongodb_collections.warn"));

    @Override
    public Warn get(String discordId) {
        Document document = warnCollection.find(Filters.eq("discordId", discordId)).first();
        if (document != null) {
            return WarnImpl.fromDocument(document);
        }
        return new WarnImpl(discordId);
    }
}