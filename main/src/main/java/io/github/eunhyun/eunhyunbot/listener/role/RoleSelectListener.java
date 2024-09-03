package io.github.eunhyun.eunhyunbot.listener.role;

import io.github.eunhyun.eunhyunbot.EunhyunBot;
import io.github.eunhyun.eunhyunbot.api.bot.BotEventHandler;
import io.github.eunhyun.eunhyunbot.api.enums.AgeRoleType;
import io.github.eunhyun.eunhyunbot.api.enums.GenderRoleType;
import io.github.eunhyun.eunhyunbot.api.enums.ValorantTierType;
import io.github.eunhyun.eunhyunbot.api.enums.VerifyRoleType;
import io.github.eunhyun.eunhyunbot.api.factory.EmbedColorFactory;
import io.github.eunhyun.eunhyunbot.api.interfaces.RoleType;
import io.github.eunhyun.eunhyunbot.api.util.DiscordEmojiUtil;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

@Slf4j
@BotEventHandler
public class RoleSelectListener extends ListenerAdapter {

    private final long ROLE_SELECT_CHANNEL_ID = EunhyunBot.getInstance().getConfig().getLong("role_select_channel_settings.channel_id");
    private final long AGE_MESSAGE_ID = EunhyunBot.getInstance().getConfig().getLong("role_select_channel_settings.age_message_id");
    private final long GENDER_MESSAGE_ID = EunhyunBot.getInstance().getConfig().getLong("role_select_channel_settings.gender_message_id");
    private final long VALORANT_TIER_MESSAGE_ID = EunhyunBot.getInstance().getConfig().getLong("role_select_channel_settings.valorant_tier_message_id");
    private final long VERIFY_MESSAGE_ID = EunhyunBot.getInstance().getConfig().getLong("role_select_channel_settings.verify_message_id");

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        if (isEventValid(event)) return;

        Member member = event.getMember();
        String reactionCode = event.getReaction().getEmoji().getAsReactionCode();
        long messageId = event.getMessageIdLong();

        handleRoleAssignment(member, messageId, reactionCode, false);
    }

    @Override
    public void onMessageReactionRemove(@NotNull MessageReactionRemoveEvent event) {
        if (isEventValid(event)) return;

        Member member = event.getMember();
        String reactionCode = event.getReaction().getEmoji().getAsReactionCode();
        long messageId = event.getMessageIdLong();

        handleRoleAssignment(member, messageId, reactionCode, true);
    }

    private boolean isEventValid(@NotNull GenericMessageReactionEvent event) {
        return event.getChannel().getIdLong() != ROLE_SELECT_CHANNEL_ID || event.getMember() == null;
    }

    private void handleRoleAssignment(Member member, long messageId, String reactionCode, boolean remove) {
        RoleType roleType = getRoleTypeForReaction(messageId, reactionCode);
        if (roleType != null) {
            handleReactionToRole(member, roleType, remove);
        }
    }

    private RoleType getRoleTypeForReaction(long messageId, String reactionCode) {
        if (messageId == AGE_MESSAGE_ID) {
            if (DiscordEmojiUtil.CHICK_SPROUT.contains(reactionCode)) {
                return AgeRoleType.TEN;
            } else if (DiscordEmojiUtil.CHICK_STARS.contains(reactionCode)) {
                return AgeRoleType.TWENTY;
            } else if (DiscordEmojiUtil.CHICK_THUMBSUP.contains(reactionCode)) {
                return AgeRoleType.THIRTY;
            }
        } else if (messageId == GENDER_MESSAGE_ID) {
            if (DiscordEmojiUtil.MALE.contains(reactionCode)) {
                return GenderRoleType.MALE;
            } else if (DiscordEmojiUtil.FEMALE.contains(reactionCode)) {
                return GenderRoleType.FEMALE;
            }
        } else if (messageId == VALORANT_TIER_MESSAGE_ID) {
            if (DiscordEmojiUtil.VALORANT_TIER_IRON.contains(reactionCode)) {
                return ValorantTierType.IRON;
            } else if (DiscordEmojiUtil.VALORANT_TIER_BRONZE.contains(reactionCode)) {
                return ValorantTierType.BRONZE;
            } else if (DiscordEmojiUtil.VALORANT_TIER_SILVER.contains(reactionCode)) {
                return ValorantTierType.SILVER;
            } else if (DiscordEmojiUtil.VALORANT_TIER_GOLD.contains(reactionCode)) {
                return ValorantTierType.GOLD;
            } else if (DiscordEmojiUtil.VALORANT_TIER_PLATINUM.contains(reactionCode)) {
                return ValorantTierType.PLATINUM;
            } else if (DiscordEmojiUtil.VALORANT_TIER_DIAMOND.contains(reactionCode)) {
                return ValorantTierType.DIAMOND;
            } else if (DiscordEmojiUtil.VALORANT_TIER_ASCENDANT.contains(reactionCode)) {
                return ValorantTierType.ASCENDANT;
            } else if (DiscordEmojiUtil.VALORANT_TIER_IMMORTAL.contains(reactionCode)) {
                return ValorantTierType.IMMORTAL;
            } else if (DiscordEmojiUtil.VALORANT_TIER_RADIANT.contains(reactionCode)) {
                return ValorantTierType.RADIANT;
            }
        } else if (messageId == VERIFY_MESSAGE_ID) {
            if (DiscordEmojiUtil.CHECK_MARK.contains(reactionCode)) {
                return VerifyRoleType.VERIFY;
            }
        }
        return null;
    }

    private void handleReactionToRole(Member member, RoleType roleType, boolean remove) {
        long roleId = roleType.getRoleId();
        Guild guild = member.getGuild();
        Role role = guild.getRoleById(roleId);
        if (role == null) {
            log.error("Role with ID: {} does not exist.", roleId);
            return;
        }

        boolean hasRole = member.getRoles().stream()
                .anyMatch(r -> r.getIdLong() == roleId);

        if (remove) {
            if (hasRole) {
                guild.removeRoleFromMember(member, role).queue(
                        _ -> log.info("Removed role {} from member {}.", role.getName(), member.getUser().getAsTag()),
                        error -> log.error("Failed to remove role {} from member {}: {}", role.getName(), member.getUser().getAsTag(), error.getMessage())
                );
            }
        } else {
            boolean hasAnySpecificRole = member.getRoles().stream()
                    .anyMatch(r -> switch (roleType) {
                        case AgeRoleType _ -> AgeRoleType.isAgeRole(r.getIdLong());
                        case GenderRoleType _ -> GenderRoleType.isGenderRole(r.getIdLong());
                        case ValorantTierType _ -> ValorantTierType.isValorantTierRole(r.getIdLong());
                        case VerifyRoleType _ -> VerifyRoleType.isVerifyRole(r.getIdLong());
                        default -> false;
                    });

            if (hasAnySpecificRole) {
                member.getUser().openPrivateChannel().queue(
                        channel -> channel.sendMessageEmbeds(new EmbedBuilder()
                                        .setColor(EmbedColorFactory.getEmbedColor(EmbedColorFactory.Type.ERROR))
                                        .setTitle("%s 역할 받기 | 오류 %s".formatted(DiscordEmojiUtil.CROSS_MARK, DiscordEmojiUtil.CROSS_MARK))
                                        .setDescription("> **이미 선택하신 역할이 있습니다.**")
                                        .setThumbnail(member.getUser().getEffectiveAvatarUrl())
                                        .build())
                                .queue(),
                        error -> log.error("Failed to send error message to user {}: {}", member.getUser().getAsTag(), error.getMessage())
                );
            } else {
                guild.addRoleToMember(member, role).queue(
                        _ -> log.info("Added role {} to member {}.", role.getName(), member.getUser().getAsTag()),
                        error -> log.error("Failed to add role {} to member {}: {}", role.getName(), member.getUser().getAsTag(), error.getMessage())
                );
            }
        }
    }
}