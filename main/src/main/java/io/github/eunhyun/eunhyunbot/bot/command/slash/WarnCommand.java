package io.github.eunhyun.eunhyunbot.bot.command.slash;

import io.github.eunhyun.eunhyunbot.EunhyunBot;
import io.github.eunhyun.eunhyunbot.api.bot.command.slash.ISlashCommand;
import io.github.eunhyun.eunhyunbot.api.bot.command.slash.SlashCommand;
import io.github.eunhyun.eunhyunbot.api.bot.permission.PermissionUtil;
import io.github.eunhyun.eunhyunbot.api.enums.WarnType;
import io.github.eunhyun.eunhyunbot.api.factory.EmbedColorFactory;
import io.github.eunhyun.eunhyunbot.api.object.Warn;
import io.github.eunhyun.eunhyunbot.api.util.DiscordEmojiUtil;
import io.github.eunhyun.eunhyunbot.api.util.EunhyunImageUtil;
import io.github.eunhyun.eunhyunbot.api.util.NumberFormatter;
import io.github.eunhyun.eunhyunbot.object.WarnImpl;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.awt.*;

@SlashCommand(
        command = "경고",
        description = "유저의 경고를 관리합니다.",
        subcommands = {
                @SlashCommand.SubCommand(
                        name = "추가",
                        description = "경고를 추가합니다.",
                        optionName = {"유저", "경고", "사유"},
                        optionType = {OptionType.USER, OptionType.INTEGER, OptionType.STRING},
                        optionDescription = {"유저를 선택하세요.", "추가할 경고 수를 입력하세요.", "사유를 입력해 주세요."},
                        optionRequired = {true, true, true}
                ),
                @SlashCommand.SubCommand(
                        name = "제거",
                        description = "경고를 제거합니다.",
                        optionName = {"유저", "경고", "사유"},
                        optionType = {OptionType.USER, OptionType.INTEGER, OptionType.STRING},
                        optionDescription = {"유저를 선택하세요.", "제거할 경고 수를 입력하세요.", "사유를 입력해 주세요."},
                        optionRequired = {true, true, true}
                ),
                @SlashCommand.SubCommand(
                        name = "설정",
                        description = "경고를 설정합니다.",
                        optionName = {"유저", "경고", "사유"},
                        optionType = {OptionType.USER, OptionType.INTEGER, OptionType.STRING},
                        optionDescription = {"유저를 선택하세요.", "설정할 경고 수를 입력하세요.", "사유를 입력해 주세요."},
                        optionRequired = {true, true, true}
                ),
                @SlashCommand.SubCommand(
                        name = "확인",
                        description = "경고를 확인합니다.",
                        optionName = {"유저"},
                        optionType = {OptionType.USER},
                        optionDescription = {"확인할 유저를 선택하세요. (선택하지 않으면 자신의 경고를 확인합니다.)"},
                        optionRequired = {false}
                ),
                @SlashCommand.SubCommand(
                        name = "초기화",
                        description = "경고를 초기화합니다.",
                        optionName = {"유저", "사유"},
                        optionType = {OptionType.USER, OptionType.STRING},
                        optionDescription = {"유저를 선택하세요.", "사유를 입력하세요."},
                        optionRequired = {true, true}
                )
        }
)
public class WarnCommand implements ISlashCommand {

    private final long DISCRIMINATION_LOG_CHANNEL_ID = EunhyunBot.getInstance().getConfig().getLong("log_channel_settings.discrimination");

    private final Color EMBED_COLOR = EmbedColorFactory.getEmbedColor(EmbedColorFactory.Type.NORMAL);
    private final Color EMBED_COLOR_SUCCESS = EmbedColorFactory.getEmbedColor(EmbedColorFactory.Type.SUCCESS);

    @Override
    @SuppressWarnings("all")
    public void execute(SlashCommandInteractionEvent event) {
        String subCommand = event.getSubcommandName();
        if (subCommand != null) {
            if ("확인".equals(subCommand)) {
                handleCheckCommand(event);
                return;
            }

            Member member = event.getMember();
            if (member == null) {
                return;
            }

            if (!PermissionUtil.checkPermissionsAndSendError(event, member, new Permission[]{Permission.ADMINISTRATOR}, "경고는 관리자만 사용할 수 있어요.")) {
                return;
            }

            switch (subCommand) {
                case "추가" -> {
                    User target = event.getOption("유저").getAsUser();
                    String reason = event.getOption("사유").getAsString();
                    int amount = event.getOption("경고").getAsInt();

                    MessageEmbed embed1 = new EmbedBuilder()
                            .setColor(EMBED_COLOR)
                            .setTitle("%s 경고 알림 %s".formatted(DiscordEmojiUtil.WARNING, DiscordEmojiUtil.WARNING))
                            .setDescription("""
                                    > **%s님에게 경고 %s회가 추가되었습니다.**
                                    > **사유: %s**
                                    """
                                    .formatted(target.getAsMention(), NumberFormatter.commas(amount), reason)
                            )
                            .setThumbnail(target.getAvatarUrl())
                            .build();

                    target.openPrivateChannel().complete()
                            .sendMessageEmbeds(embed1)
                            .queue(null, (ignored) -> {
                            });

                    Warn warn = new WarnImpl(target.getId(), amount);
                    warn.add();

                    MessageEmbed embed2 = new EmbedBuilder()
                            .setColor(EMBED_COLOR_SUCCESS)
                            .setTitle("%s 경고 추가 | 성공 %s".formatted(DiscordEmojiUtil.CHECK_MARK, DiscordEmojiUtil.CHECK_MARK))
                            .setDescription("""
                                    > **%s님에게 %s경고를 추가하였습니다.**
                                    > **사유: %s**
                                    """
                                    .formatted(target.getAsMention(), NumberFormatter.commas(amount), reason)
                            )
                            .setThumbnail(target.getAvatarUrl())
                            .build();
                    event.replyEmbeds(embed2).setEphemeral(true).queue();

                    sendLogMessage(event.getGuild(), WarnType.ADD, target.getAsMention(), amount, reason);
                }

                case "제거" -> {
                    User target = event.getOption("유저").getAsUser();
                    String reason = event.getOption("사유").getAsString();
                    int amount = event.getOption("경고").getAsInt();

                    Warn warn = new WarnImpl(target.getId(), amount * -1);
                    warn.subtract();

                    MessageEmbed embed = new EmbedBuilder()
                            .setColor(EMBED_COLOR_SUCCESS)
                            .setTitle("%s 경고 제거 | 성공 %s".formatted(DiscordEmojiUtil.CHECK_MARK, DiscordEmojiUtil.CHECK_MARK))
                            .setDescription("""
                                    > **%s님의 경고를 %s만큼 제거하였습니다.**
                                    > **사유: %s**
                                    """
                                    .formatted(target.getAsMention(), NumberFormatter.commas(amount), reason)
                            )
                            .setThumbnail(target.getAvatarUrl())
                            .build();
                    event.replyEmbeds(embed).queue();
                    target.openPrivateChannel().complete()
                            .sendMessageEmbeds(embed)
                            .queue(null, (ignored) -> {
                            });

                    sendLogMessage(event.getGuild(), WarnType.SUBTRACT, target.getAsMention(), amount, reason);
                }

                case "설정" -> {
                    User target = event.getOption("유저").getAsUser();
                    String reason = event.getOption("사유").getAsString();
                    int amount = event.getOption("경고").getAsInt();

                    Warn warn = new WarnImpl(target.getId(), amount * -1);
                    warn.set();

                    MessageEmbed embed = new EmbedBuilder()
                            .setColor(EMBED_COLOR_SUCCESS)
                            .setTitle("%s 경고 설정 | 성공 %s".formatted(DiscordEmojiUtil.CHECK_MARK, DiscordEmojiUtil.CHECK_MARK))
                            .setDescription("""
                                    > **%s님의 경고를 %s로 설정하였습니다.**
                                    > **사유: %s**
                                    """
                                    .formatted(target.getAsMention(), NumberFormatter.commas(amount), reason)
                            )
                            .setThumbnail(target.getAvatarUrl())
                            .build();
                    event.replyEmbeds(embed).queue();
                    target.openPrivateChannel().complete()
                            .sendMessageEmbeds(embed)
                            .queue(null, (ignored) -> {
                            });

                    sendLogMessage(event.getGuild(), WarnType.SUBTRACT, target.getAsMention(), amount, reason);
                }

                case "초기화" -> {
                    User target = event.getOption("유저").getAsUser();
                    String reason = event.getOption("사유").getAsString();

                    Warn warn = new WarnImpl(target.getId(), 0);
                    warn.reset();

                    MessageEmbed embed = new EmbedBuilder()
                            .setColor(EMBED_COLOR_SUCCESS)
                            .setTitle("%s 경고 초기화 | 성공 %s".formatted(DiscordEmojiUtil.CHECK_MARK, DiscordEmojiUtil.CHECK_MARK))
                            .setDescription("> **%s님의 경고를 초기화하였습니다.**".formatted(target.getAsMention()))
                            .setThumbnail(target.getAvatarUrl())
                            .build();
                    event.replyEmbeds(embed).queue();
                    target.openPrivateChannel().complete()
                            .sendMessageEmbeds(embed)
                            .queue(null, (ignored) -> {
                            });

                    sendLogMessage(event.getGuild(), WarnType.RESET, target.getAsMention(), warn.getAmount(), reason);
                }
            }
        }
    }

    @SuppressWarnings("all")
    private void handleCheckCommand(SlashCommandInteractionEvent event) {
        String targetId;

        if (event.getOption("유저") != null) {
            Member member = event.getMember();
            if (member == null) {
                return;
            }

            if (!PermissionUtil.checkPermissionsAndSendError(event, member, new Permission[]{Permission.ADMINISTRATOR}, "다른 유저의 경고는 관리자만 확인할 수 있어요.")) {
                return;
            }

            targetId = event.getOption("유저").getAsUser().getId();
        } else {
            targetId = event.getUser().getId();
        }

        String userAvatarCheck = event.getJDA().retrieveUserById(targetId).complete().getAvatarUrl();

        MessageEmbed embed = new EmbedBuilder()
                .setColor(EMBED_COLOR)
                .setTitle("%s 경고 확인 %s".formatted(DiscordEmojiUtil.WARNING, DiscordEmojiUtil.WARNING))
                .setDescription(
                        "> **<@%s>님의 현재 경고: %s**"
                                .formatted(
                                        targetId,
                                        NumberFormatter.commas(EunhyunBot.getInstance().getWarnRepository().get(targetId).getAmount())
                                ))
                .setThumbnail(userAvatarCheck)
                .build();
        event.replyEmbeds(embed).queue();
    }

    private void sendLogMessage(Guild guild, WarnType warnType, String targetMention, int amount, String reason) {
        TextChannel targetChannel = guild.getTextChannelById(DISCRIMINATION_LOG_CHANNEL_ID);
        if (targetChannel != null) {
            targetChannel.sendMessageEmbeds(new EmbedBuilder()
                            .setColor(EMBED_COLOR)
                            .setTitle("%s 경고 알림 | %s 완료 %s".formatted(DiscordEmojiUtil.WARNING, warnType.getName(), DiscordEmojiUtil.WARNING))
                            .setDescription("""
                                    > **%s님의 경고를 %s만큼 %s하였습니다.**
                                    > **사유: %s**
                                    """.formatted(targetMention, NumberFormatter.commas(amount), warnType.getName(), reason)
                            )
                            .setThumbnail(EunhyunImageUtil.THUMBNAIL_IMAGE_URL)
                            .build())
                    .queue();
        }
    }
}