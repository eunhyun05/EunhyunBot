package io.github.eunhyun.eunhyunbot;

import io.github.eunhyun.eunhyunbot.api.EunhyunBotAPI;
import io.github.eunhyun.eunhyunbot.api.mongodb.MongoCollectionHelper;
import io.github.eunhyun.eunhyunbot.bot.DiscordBotManagerImpl;
import io.github.eunhyun.eunhyunbot.repository.WarnRepositoryImpl;
import lombok.Getter;

@Getter
public class EunhyunBot extends EunhyunBotAPI {

    @Getter
    private static EunhyunBot instance;
    @Getter
    private static MongoCollectionHelper mongoCollectionHelper;
    private DiscordBotManagerImpl botManager;
    private WarnRepositoryImpl warnRepository;

    @Override
    protected void onEnable() {
        instance = this;

        saveDefaultConfig();

        mongoCollectionHelper = new MongoCollectionHelper(getConfig());
        this.botManager = new DiscordBotManagerImpl(getConfig());
        this.warnRepository = new WarnRepositoryImpl();

        handleCommands();
    }
}