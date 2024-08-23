package io.github.eunhyun.eunhyunbot.api.mongodb;

import org.bson.Document;

public interface BsonSerializer {

    Document toDocument();
}