package io.github.eunhyun.eunhyunbot.bot.command.slash;

import io.github.eunhyun.eunhyunbot.EunhyunBot;
import io.github.eunhyun.eunhyunbot.api.bot.command.slash.ISlashCommand;
import io.github.eunhyun.eunhyunbot.api.bot.command.slash.SlashCommand;
import io.github.eunhyun.eunhyunbot.api.factory.EmbedColorFactory;
import io.github.eunhyun.eunhyunbot.api.util.DiscordEmojiUtil;
import io.github.eunhyun.eunhyunbot.api.util.EunhyunImageUtil;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.awt.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@SlashCommand(
        command = "차단",
        description = "유저를 차단합니다.",
        optionName = {"유저", "사유"},
        optionType = {OptionType.USER, OptionType.STRING},
        optionDescription = {"유저를 선택하세요.", "사유를 입력하세요."},
        optionRequired = {true, true}
)
public class BanCommand implements ISlashCommand {

    private final long DISCRIMINATION_LOG_CHANNEL_ID = EunhyunBot.getInstance().getConfig().getLong("log_channel_settings.discrimination");

    private final Color EMBED_COLOR = EmbedColorFactory.getEmbedColor(EmbedColorFactory.Type.NORMAL);
    private final Color EMBED_COLOR_SUCCESS = EmbedColorFactory.getEmbedColor(EmbedColorFactory.Type.SUCCESS);

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        OptionMapping userOptionMapping = event.getOption("유저");
        OptionMapping reasonOptionMapping = event.getOption("사유");

        if (userOptionMapping != null && reasonOptionMapping != null) {
            User targetUser = userOptionMapping.getAsUser();
            String reason = reasonOptionMapping.getAsString();

            Guild guild = event.getGuild();
            if (guild != null) {
                guild.ban(targetUser, 0, TimeUnit.DAYS).queue(_ ->
                        event.replyEmbeds(new EmbedBuilder()
                                        .setColor(EMBED_COLOR_SUCCESS)
                                        .setTitle("%s 차단 | 성공 %s".formatted(DiscordEmojiUtil.CHECK_MARK, DiscordEmojiUtil.CHECK_MARK))
                                        .setDescription("""
                                                > **%s님을 서버에서 차단하였습니다.**
                                                > **사유: %s**
                                                """
                                                .formatted(targetUser.getAsMention(), reason)
                                        )
                                        .setThumbnail(targetUser.getEffectiveAvatarUrl())
                                        .build())
                                .setEphemeral(true)
                                .queue(_ -> sendLogMessage(guild, targetUser.getAsMention(), reason)));
            }
        }
    }

    private void sendLogMessage(Guild guild, String targetMention, String reason) {
        TextChannel targetChannel = guild.getTextChannelById(DISCRIMINATION_LOG_CHANNEL_ID);
        if (targetChannel != null) {
            targetChannel.sendMessageEmbeds(new EmbedBuilder()
                            .setColor(EMBED_COLOR)
                            .setTitle("%s 차단 알림 | 알림 %s".formatted(DiscordEmojiUtil.BAN , DiscordEmojiUtil.BAN))
                            .setDescription("""
                                    > **%s님이 서버에서 차단되었습니다.**
                                    > **사유: %s**
                                    """.formatted(targetMention, reason)
                            )
                            .setThumbnail(EunhyunImageUtil.THUMBNAIL_IMAGE_URL)
                            .build())
                    .queue();
        }
    }
}