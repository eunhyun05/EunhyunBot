package io.github.eunhyun.eunhyunbot.listener.rule;

import io.github.eunhyun.eunhyunbot.EunhyunBot;
import io.github.eunhyun.eunhyunbot.api.bot.BotEventHandler;
import io.github.eunhyun.eunhyunbot.api.factory.EmbedColorFactory;
import io.github.eunhyun.eunhyunbot.api.util.DiscordEmojiUtil;
import io.github.eunhyun.eunhyunbot.api.util.NumberFormatter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

@Slf4j
@BotEventHandler
public class RuleInteractListener extends ListenerAdapter {

    private final Color EMBED_COLOR = EmbedColorFactory.getEmbedColor(EmbedColorFactory.Type.NORMAL);

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        if (!event.getComponentId().equals("check_my_warn")) {
            return;
        }

        MessageEmbed embed = new EmbedBuilder()
                .setColor(EMBED_COLOR)
                .setTitle("%s 경고 확인 %s".formatted(DiscordEmojiUtil.WARNING, DiscordEmojiUtil.WARNING))
                .setDescription(
                        "> **%s님의 현재 경고: %s**"
                                .formatted(
                                        event.getUser().getAsMention(),
                                        NumberFormatter.commas(EunhyunBot.getInstance().getWarnRepository().get(event.getUser().getId()).getAmount())
                                ))
                .setThumbnail(event.getUser().getEffectiveAvatarUrl())
                .build();
        event.replyEmbeds(embed).setEphemeral(true).queue();
    }
}