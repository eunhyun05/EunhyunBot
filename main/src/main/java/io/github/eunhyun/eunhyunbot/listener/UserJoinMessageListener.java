package io.github.eunhyun.eunhyunbot.listener;

import io.github.eunhyun.eunhyunbot.EunhyunBot;
import io.github.eunhyun.eunhyunbot.api.bot.BotEventHandler;
import io.github.eunhyun.eunhyunbot.api.factory.EmbedColorFactory;
import io.github.eunhyun.eunhyunbot.api.util.DiscordEmojiUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

@BotEventHandler
public class UserJoinMessageListener extends ListenerAdapter {

    private final long JOIN_MESSAGE_CHANNEL_ID = EunhyunBot.getInstance().getConfig().getLong("join_message_channel.channel_id");
    private final long ROLE_SELECT_CHANNEL_ID = EunhyunBot.getInstance().getConfig().getLong("role_select_channel_settings.channel_id");
    private final long RULE_CHANNEL_ID = EunhyunBot.getInstance().getConfig().getLong("join_message_channel.rule_channel_id");
    private final long MAIN_CHAT_CHANNEL_ID = EunhyunBot.getInstance().getConfig().getLong("join_message_channel.main_chat_channel_id");

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        TextChannel textChannelById = event.getGuild().getTextChannelById(JOIN_MESSAGE_CHANNEL_ID);
        if (textChannelById != null) {
            MessageEmbed embed = new EmbedBuilder()
                    .setColor(EmbedColorFactory.getEmbedColor(EmbedColorFactory.Type.NORMAL))
                    .setTitle("%s 입장 | 환영합니다! %s".formatted(DiscordEmojiUtil.CHICK_STARS, DiscordEmojiUtil.CHICK_STARS))
                    .setDescription("""
                            > `🎉` **서버에 오신 것을 진심으로 환영합니다!** `🎉`
                            
                            > `🤗` **환영합니다, %s님!** 🤗
                            
                            > 👉 **시작하기 전에 몇 가지 안내사항을 확인해 주세요:**
                            > 1. `➕` <#%d>: 기본 역할을 받아주세요!
                            > 1. `📜` <#%d>: 서버의 규칙을 읽어주세요!
                            > 3. `🚀` <#%d>: 인사 메시지를 보내세요!
                            
                            ⭐️ **즐거운 시간 되세요!** ⭐️
                            """.formatted(event.getUser().getAsMention(), ROLE_SELECT_CHANNEL_ID, RULE_CHANNEL_ID, MAIN_CHAT_CHANNEL_ID)
                    )
                    .setThumbnail(event.getUser().getEffectiveAvatarUrl())
                    .build();
            textChannelById.sendMessageEmbeds(embed).queue();
        }
    }
}