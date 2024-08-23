package io.github.eunhyun.eunhyunbot.api.bot.permission;

import io.github.eunhyun.eunhyunbot.api.factory.EmbedColorFactory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PermissionUtil {

    private static final String EMBED_TITLE = "<a:cross_mark:1276415059739807744> 권한 없음 | 오류 <a:cross_mark:1276415059739807744>";

    public static boolean checkPermissionsAndSendError(@NotNull MessageReceivedEvent event, @NotNull Member member,
                                                       @NotNull Permission[] requiredPerms, @NotNull String errorMessage) {
        for (Permission perm : requiredPerms) {
            if (!member.hasPermission(perm)) {
                sendErrorMessage(event, errorMessage);
                return false;
            }
        }
        return true;
    }

    public static boolean checkPermissionsAndSendError(@NotNull SlashCommandInteractionEvent event, @NotNull Member member,
                                                       @NotNull Permission[] requiredPerms, @NotNull String errorMessage) {
        for (Permission perm : requiredPerms) {
            if (!member.hasPermission(perm)) {
                sendErrorMessage(event, errorMessage);
                return false;
            }
        }
        return true;
    }

    private static void sendErrorMessage(@NotNull MessageReceivedEvent event, @NotNull String errorMessage) {
        event.getMessage().replyEmbeds(new EmbedBuilder()
                        .setColor(EmbedColorFactory.getEmbedColor(EmbedColorFactory.Type.ERROR))
                        .setTitle(EMBED_TITLE)
                        .setDescription(String.format("> **%s**", errorMessage))
                        .build())
                .queue();
    }

    private static void sendErrorMessage(@NotNull SlashCommandInteractionEvent event, @NotNull String errorMessage) {
        event.replyEmbeds(new EmbedBuilder()
                        .setColor(EmbedColorFactory.getEmbedColor(EmbedColorFactory.Type.ERROR))
                        .setTitle(EMBED_TITLE)
                        .setDescription(String.format("> **%s**", errorMessage))
                        .build())
                .setEphemeral(true)
                .queue();
    }
}