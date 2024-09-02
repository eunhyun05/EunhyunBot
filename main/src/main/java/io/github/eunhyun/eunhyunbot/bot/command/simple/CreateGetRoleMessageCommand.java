package io.github.eunhyun.eunhyunbot.bot.command.simple;

import io.github.eunhyun.eunhyunbot.EunhyunBot;
import io.github.eunhyun.eunhyunbot.api.bot.DiscordBotManager;
import io.github.eunhyun.eunhyunbot.api.bot.command.simple.ISimpleCommand;
import io.github.eunhyun.eunhyunbot.api.bot.command.simple.SimpleCommand;
import io.github.eunhyun.eunhyunbot.api.bot.permission.PermissionUtil;
import io.github.eunhyun.eunhyunbot.api.enums.AgeRoleType;
import io.github.eunhyun.eunhyunbot.api.enums.GenderRoleType;
import io.github.eunhyun.eunhyunbot.api.enums.RoleCategoryType;
import io.github.eunhyun.eunhyunbot.api.enums.ValorantTierType;
import io.github.eunhyun.eunhyunbot.api.factory.EmbedColorFactory;
import io.github.eunhyun.eunhyunbot.api.util.DiscordEmojiUtil;
import io.github.eunhyun.eunhyunbot.api.util.EunhyunImageUtil;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.EnumSet;

@Slf4j
@SimpleCommand(
        command = "역할받기생성",
        description = "역할 받기 메시지를 생성합니다.",
        usage = "?역할받기생성 <나이|성별|발로란트티어>"
)
public class CreateGetRoleMessageCommand implements ISimpleCommand {

    private final Color EMBED_COLOR = EmbedColorFactory.getEmbedColor(EmbedColorFactory.Type.NORMAL);

    @Override
    public void execute(MessageReceivedEvent event) {
        Member member = event.getMember();
        if (member == null) {
            return;
        }

        if (!PermissionUtil.checkPermissionsAndSendError(event, member, new Permission[]{Permission.ADMINISTRATOR}, "역할 받기 메시지 생성은 관리자만 사용할 수 있어요.")) {
            return;
        }

        String[] args = event.getMessage().getContentRaw().split("\\s", 2);
        if (args.length < 2) {
            event.getChannel().sendMessage("올바른 명령어 사용법: " + "?역할받기생성 <나이|성별|발로란트티어>").queue();
            return;
        }

        RoleCategoryType roleCategory;
        try {
            roleCategory = RoleCategoryType.fromName(args[1]);
        } catch (IllegalArgumentException e) {
            event.getChannel().sendMessage("잘못된 역할 카테고리입니다. 사용 가능한 카테고리: 나이, 성별, 발로란트티어").queue();
            return;
        }

        event.getMessage().delete().queue();

        switch (roleCategory) {
            case AGE -> sendAgeMessageEmbed(event.getChannel());
            case GENDER -> sendGenderMessageEmbed(event.getChannel());
            case VALORANT_TIER -> sendValorantTierMessageEmbed(event.getChannel());
            default -> throw new IllegalStateException("Unexpected value: " + roleCategory);
        }
    }

    private void sendAgeMessageEmbed(MessageChannelUnion channel) {
        DiscordBotManager botManager = EunhyunBot.getInstance().getBotManager();
        channel.sendMessageEmbeds(new EmbedBuilder()
                        .setColor(EMBED_COLOR)
                        .setTitle("%s 나이 선택 | 역할 %s".formatted(DiscordEmojiUtil.CHICK_QUESTION, DiscordEmojiUtil.CHICK_QUESTION))
                        .setDescription("""
                                > **나이에 맞는 역할을 선택하세요.**
                                
                                > %s %s
                                > %s %s
                                > %s %s
                                """
                                .formatted(
                                        DiscordEmojiUtil.CHICK_SPROUT, botManager.getRoleAsMention(AgeRoleType.TEN.getRoleId()),
                                        DiscordEmojiUtil.CHICK_STARS, botManager.getRoleAsMention(AgeRoleType.TWENTY.getRoleId()),
                                        DiscordEmojiUtil.CHICK_STARS, botManager.getRoleAsMention(AgeRoleType.THIRTY.getRoleId())
                                )
                        )
                        .setThumbnail(EunhyunImageUtil.THUMBNAIL_IMAGE_URL)
                        .setFooter("나이 역할은 하나만 선택해 주세요.", EunhyunImageUtil.THUMBNAIL_IMAGE_URL)
                        .build())
                .setAllowedMentions(EnumSet.of(Message.MentionType.ROLE))
                .queue(message -> {
                    message.addReaction(Emoji.fromFormatted(DiscordEmojiUtil.CHICK_SPROUT)).queue();
                    message.addReaction(Emoji.fromFormatted(DiscordEmojiUtil.CHICK_STARS)).queue();
                    message.addReaction(Emoji.fromFormatted(DiscordEmojiUtil.CHICK_THUMBSUP)).queue();
                });
    }

    private void sendGenderMessageEmbed(MessageChannelUnion channel) {
        DiscordBotManager botManager = EunhyunBot.getInstance().getBotManager();
        channel.sendMessageEmbeds(new EmbedBuilder()
                        .setColor(EMBED_COLOR)
                        .setTitle("%s 성별 선택 | 역할 %s".formatted(DiscordEmojiUtil.MALE, DiscordEmojiUtil.FEMALE))
                        .setDescription("""
                                > **나이에 맞는 역할을 선택하세요.**
                                
                                > %s %s
                                > %s %s
                                """
                                .formatted(
                                        DiscordEmojiUtil.MALE, botManager.getRoleAsMention(GenderRoleType.MALE.getRoleId()),
                                        DiscordEmojiUtil.FEMALE, botManager.getRoleAsMention(GenderRoleType.FEMALE.getRoleId())
                                )
                        )
                        .setThumbnail(EunhyunImageUtil.THUMBNAIL_IMAGE_URL)
                        .setFooter("성별 역할은 하나만 선택해 주세요.", EunhyunImageUtil.THUMBNAIL_IMAGE_URL)
                        .build())
                .setAllowedMentions(EnumSet.of(Message.MentionType.ROLE))
                .queue(message -> {
                    message.addReaction(Emoji.fromFormatted(DiscordEmojiUtil.MALE)).queue();
                    message.addReaction(Emoji.fromFormatted(DiscordEmojiUtil.FEMALE)).queue();
                });
    }

    private void sendValorantTierMessageEmbed(MessageChannelUnion channel) {
        DiscordBotManager botManager = EunhyunBot.getInstance().getBotManager();
        channel.sendMessageEmbeds(new EmbedBuilder()
                        .setColor(EMBED_COLOR)
                        .setTitle("%s 발로란트 티어 선택 | 역할 %s".formatted(DiscordEmojiUtil.VALORANT_TIER_RADIANT, DiscordEmojiUtil.VALORANT_TIER_RADIANT))
                        .setDescription("""
                                > **자신의 발로란트 티어에 맞는 역할을 선택하세요.**
                                
                                > %s %s
                                > %s %s
                                > %s %s
                                > %s %s
                                > %s %s
                                > %s %s
                                > %s %s
                                > %s %s
                                > %s %s
                                """
                                .formatted(
                                        DiscordEmojiUtil.VALORANT_TIER_IRON, botManager.getRoleAsMention(ValorantTierType.IRON.getRoleId()),
                                        DiscordEmojiUtil.VALORANT_TIER_BRONZE, botManager.getRoleAsMention(ValorantTierType.BRONZE.getRoleId()),
                                        DiscordEmojiUtil.VALORANT_TIER_SILVER, botManager.getRoleAsMention(ValorantTierType.SILVER.getRoleId()),
                                        DiscordEmojiUtil.VALORANT_TIER_GOLD, botManager.getRoleAsMention(ValorantTierType.GOLD.getRoleId()),
                                        DiscordEmojiUtil.VALORANT_TIER_PLATINUM, botManager.getRoleAsMention(ValorantTierType.PLATINUM.getRoleId()),
                                        DiscordEmojiUtil.VALORANT_TIER_DIAMOND, botManager.getRoleAsMention(ValorantTierType.DIAMOND.getRoleId()),
                                        DiscordEmojiUtil.VALORANT_TIER_ASCENDANT, botManager.getRoleAsMention(ValorantTierType.ASCENDANT.getRoleId()),
                                        DiscordEmojiUtil.VALORANT_TIER_IMMORTAL, botManager.getRoleAsMention(ValorantTierType.IMMORTAL.getRoleId()),
                                        DiscordEmojiUtil.VALORANT_TIER_RADIANT, botManager.getRoleAsMention(ValorantTierType.RADIANT.getRoleId())
                                )
                        )
                        .setThumbnail(EunhyunImageUtil.THUMBNAIL_IMAGE_URL)
                        .setFooter("발로란트 티어 역할은 하나만 선택해 주세요.", EunhyunImageUtil.THUMBNAIL_IMAGE_URL)
                        .build())
                .setAllowedMentions(EnumSet.of(Message.MentionType.ROLE))
                .queue(message -> {
                    message.addReaction(Emoji.fromFormatted(DiscordEmojiUtil.VALORANT_TIER_IRON)).queue();
                    message.addReaction(Emoji.fromFormatted(DiscordEmojiUtil.VALORANT_TIER_BRONZE)).queue();
                    message.addReaction(Emoji.fromFormatted(DiscordEmojiUtil.VALORANT_TIER_SILVER)).queue();
                    message.addReaction(Emoji.fromFormatted(DiscordEmojiUtil.VALORANT_TIER_GOLD)).queue();
                    message.addReaction(Emoji.fromFormatted(DiscordEmojiUtil.VALORANT_TIER_PLATINUM)).queue();
                    message.addReaction(Emoji.fromFormatted(DiscordEmojiUtil.VALORANT_TIER_DIAMOND)).queue();
                    message.addReaction(Emoji.fromFormatted(DiscordEmojiUtil.VALORANT_TIER_ASCENDANT)).queue();
                    message.addReaction(Emoji.fromFormatted(DiscordEmojiUtil.VALORANT_TIER_IMMORTAL)).queue();
                    message.addReaction(Emoji.fromFormatted(DiscordEmojiUtil.VALORANT_TIER_RADIANT)).queue();
                });
    }
}