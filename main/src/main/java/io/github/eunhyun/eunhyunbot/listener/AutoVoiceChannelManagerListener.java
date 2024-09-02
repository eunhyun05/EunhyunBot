package io.github.eunhyun.eunhyunbot.listener;

import io.github.eunhyun.eunhyunbot.EunhyunBot;
import io.github.eunhyun.eunhyunbot.api.util.KoreanChannelNameGenerator;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.List;

@Slf4j
public class AutoVoiceChannelManagerListener extends ListenerAdapter {

    private static final long SOURCE_VOICE_CHANNEL_ID = EunhyunBot.getInstance().getConfig().getLong("auto_voice_channel.source_voice_channel_id");
    private static final String SOURCE_VOICE_CHANNEL_NAME = EunhyunBot.getInstance().getConfig().getString("auto_voice_channel.source_voice_channel_name");
    private static final long TARGET_CATEGORY_ID = EunhyunBot.getInstance().getConfig().getLong("auto_voice_channel.target_category_id");
    private static final List<Long> BLACKLIST_CHANNEL_IDS = EunhyunBot.getInstance().getConfig().getList("auto_voice_channel.blacklist", Long.class);

    @Override
    public void onGuildVoiceUpdate(@NotNull GuildVoiceUpdateEvent event) {
        AudioChannelUnion channelJoined = event.getChannelJoined();
        AudioChannelUnion channelLeft = event.getChannelLeft();

        if (channelJoined != null && channelJoined.getIdLong() == SOURCE_VOICE_CHANNEL_ID) {
            Guild guild = event.getGuild();
            Category targetCategory = guild.getCategoryById(TARGET_CATEGORY_ID);
            Member member = event.getMember();

            if (targetCategory != null) {
                String randomChannelName = KoreanChannelNameGenerator.generateRandomName();

                targetCategory.createVoiceChannel(randomChannelName).queue(newChannel -> {
                    guild.moveVoiceMember(member, newChannel).queue();

                    EnumSet<Permission> allowPermissions = EnumSet.of(
                            Permission.MANAGE_CHANNEL,
                            Permission.MANAGE_PERMISSIONS,
                            Permission.VOICE_MUTE_OTHERS,
                            Permission.VOICE_DEAF_OTHERS
                    );

                    newChannel.upsertPermissionOverride(member)
                            .setAllowed(allowPermissions)
                            .queue();

                    log.info("사용자 {}에게 새로운 음성 채널 {}의 관리자 권한을 부여했습니다.", member.getEffectiveName(), newChannel.getName());
                });
            } else {
                log.warn("타겟 카테고리를 찾을 수 없습니다.");
            }
        }

        if (channelLeft != null && channelLeft.getMembers().isEmpty()) {
            long channelLeftId = channelLeft.getIdLong();
            if (channelLeftId != SOURCE_VOICE_CHANNEL_ID &&
                    !SOURCE_VOICE_CHANNEL_NAME.equals(channelLeft.getName()) &&
                    !BLACKLIST_CHANNEL_IDS.contains(channelLeftId)) {

                channelLeft.delete().queue(
                        _ -> log.info("Deleted empty voice channel: {}", channelLeft.getName()),
                        failure -> log.error("Failed to delete voice channel: {}", channelLeft.getName(), failure)
                );
            }
        }
    }
}