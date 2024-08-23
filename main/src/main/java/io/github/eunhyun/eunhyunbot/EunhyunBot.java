package io.github.eunhyun.eunhyunbot;

import io.github.eunhyun.eunhyunbot.api.EunhyunBotAPI;
import io.github.eunhyun.eunhyunbot.bot.DiscordBotManagerImpl;
import lombok.Getter;

@Getter
public class EunhyunBot extends EunhyunBotAPI {

    @Getter
    private static EunhyunBot instance;
    private DiscordBotManagerImpl botManager;

    @Override
    protected void onEnable() {
        instance = this;

        saveDefaultConfig();

        botManager = new DiscordBotManagerImpl(getConfig());

        handleCommands();
    }
}