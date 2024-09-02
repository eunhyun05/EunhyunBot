package io.github.eunhyun.eunhyunbot.bot.command.simple;

import io.github.eunhyun.eunhyunbot.api.bot.command.simple.ISimpleCommand;
import io.github.eunhyun.eunhyunbot.api.bot.command.simple.SimpleCommand;
import io.github.eunhyun.eunhyunbot.api.bot.permission.PermissionUtil;
import io.github.eunhyun.eunhyunbot.api.factory.EmbedColorFactory;
import io.github.eunhyun.eunhyunbot.api.util.DiscordEmojiUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.concurrent.TimeUnit;

@SimpleCommand(
        command = "채팅청소",
        description = "<개수>만큼 메시지를 삭제합니다.",
        usage = "?채팅청소 <개수>"
)
public class ChatCleanerCommand implements ISimpleCommand {

    private final Color EMBED_COLOR_SUCCESS = EmbedColorFactory.getEmbedColor(EmbedColorFactory.Type.SUCCESS);
    private final Color EMBED_COLOR_ERROR = EmbedColorFactory.getEmbedColor(EmbedColorFactory.Type.ERROR);

    @Override
    public void execute(MessageReceivedEvent event) {
        Member member = event.getMember();
        if (member == null) {
            return;
        }

        if (!PermissionUtil.checkPermissionsAndSendError(event, member, new Permission[]{Permission.ADMINISTRATOR}, "채팅 청소 명령어는 관리자만 사용할 수 있어요.")) {
            return;
        }

        String[] args = event.getMessage().getContentRaw().split("\\s", 2);
        if (args.length < 2) {
            MessageEmbed embed = new EmbedBuilder()
                    .setColor(EMBED_COLOR_ERROR)
                    .setTitle("%s 채팅 청소 | 오류 %s".formatted(DiscordEmojiUtil.CROSS_MARK, DiscordEmojiUtil.CROSS_MARK))
                    .setDescription("> **청소할 메시지의 개수를 입력해주세요.**")
                    .build();
            event.getChannel().sendMessageEmbeds(embed).queue();
            return;
        }

        try {
            int count = Integer.parseInt(args[1]);
            if (count < 1 || count > 99) {
                MessageEmbed embed = new EmbedBuilder()
                        .setColor(EMBED_COLOR_ERROR)
                        .setTitle("%s 채팅 청소 | 오류 %s".formatted(DiscordEmojiUtil.CROSS_MARK, DiscordEmojiUtil.CROSS_MARK))
                        .setDescription("**메시지는 1개에서 99개까지만 삭제할 수 있습니다.**")
                        .build();
                event.getChannel().sendMessageEmbeds(embed).queue();
            } else {
                deleteMessages(event.getChannel(), count);
            }
        } catch (NumberFormatException ignored) {
            MessageEmbed embed = new EmbedBuilder()
                    .setColor(EMBED_COLOR_ERROR)
                    .setTitle("%s 채팅 청소 | 오류 %s".formatted(DiscordEmojiUtil.CROSS_MARK, DiscordEmojiUtil.CROSS_MARK))
                    .setDescription("> **유효한 숫자를 입력해주세요.**")
                    .build();
            event.getChannel().sendMessageEmbeds(embed).queue();
        }
    }

    private void deleteMessages(MessageChannel channel, int count) {
        try {
            channel.getIterableHistory()
                    .takeAsync(count + 1)
                    .thenAccept(channel::purgeMessages);
        } catch (Exception ignored) {}

        MessageEmbed embed = new EmbedBuilder()
                .setColor(EMBED_COLOR_SUCCESS)
                .setTitle("%s 채팅 청소 | 성공 %s".formatted(DiscordEmojiUtil.CHECK_MARK, DiscordEmojiUtil.CHECK_MARK))
                .setDescription("""
                        > **%d개의 메시지를 청소하였습니다.**
                        > **이 메시지는 10초 후에 자동으로 삭제됩니다.**
                        """
                        .formatted(count)
                )
                .build();
        channel.sendMessageEmbeds(embed).queue(message -> message.delete().queueAfter(10, TimeUnit.SECONDS));
    }
}