package io.github.eunhyun.eunhyunbot.bot.command.simple;

import io.github.eunhyun.eunhyunbot.api.bot.command.simple.ISimpleCommand;
import io.github.eunhyun.eunhyunbot.api.bot.command.simple.SimpleCommand;
import io.github.eunhyun.eunhyunbot.api.bot.permission.PermissionUtil;
import io.github.eunhyun.eunhyunbot.api.factory.EmbedColorFactory;
import io.github.eunhyun.eunhyunbot.api.util.DiscordEmojiUtil;
import io.github.eunhyun.eunhyunbot.api.util.EunhyunImageUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;

@SimpleCommand(
        command = "규칙생성",
        description = "규칙 메시지를 생성합니다.",
        usage = "?규칙생성"
)
public class CreateRuleCommand implements ISimpleCommand {

    private final Color EMBED_COLOR = EmbedColorFactory.getEmbedColor(EmbedColorFactory.Type.NORMAL);

    @Override
    public void execute(MessageReceivedEvent event) {
        Member member = event.getMember();
        if (member == null) {
            return;
        }

        if (!PermissionUtil.checkPermissionsAndSendError(event, member, new Permission[]{Permission.ADMINISTRATOR}, "물음표 명령어는 관리자만 사용할 수 있어요.")) {
            return;
        }

        event.getMessage().delete().queue();

        MessageEmbed embed = new EmbedBuilder()
                .setColor(EMBED_COLOR)
                .setTitle("%s 서버 규칙 | 은현서버 %s".formatted(DiscordEmojiUtil.CHICK_QUESTION, DiscordEmojiUtil.CHICK_QUESTION))
                .setDescription("""
                        `🎉` **은현 서버 오신걸 환영합니다.**
                        
                        `💖` **작성할거임**
                        """
                )
                .setThumbnail(EunhyunImageUtil.THUMBNAIL_IMAGE_URL)
                .setFooter("규칙에 대해 궁금한 점이 있다면 문의하기를 통해 문의하여 주시길 바랍니다.", EunhyunImageUtil.THUMBNAIL_IMAGE_URL)
                .build();

        Button checkMyWarnBtn = Button.danger("check_my_warn", "내 경고 확인");
        event.getChannel().sendMessageEmbeds(embed).addActionRow(checkMyWarnBtn).queue();
    }
}