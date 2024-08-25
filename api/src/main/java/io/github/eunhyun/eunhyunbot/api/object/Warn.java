package io.github.eunhyun.eunhyunbot.api.object;

import io.github.eunhyun.eunhyunbot.api.mongodb.BsonSerializer;

public interface Warn extends BsonSerializer {

    String getDiscordId();

    int getAmount();

    void add();

    void subtract();

    void set();

    void reset();
}