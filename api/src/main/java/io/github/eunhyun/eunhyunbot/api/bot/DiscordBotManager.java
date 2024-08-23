package io.github.eunhyun.eunhyunbot.api.bot;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.jetbrains.annotations.Nullable;

public interface DiscordBotManager {

    void stop();

    @Nullable
    Guild getGuild();

    @Nullable
    TextChannel getTextChannel(String key);
}