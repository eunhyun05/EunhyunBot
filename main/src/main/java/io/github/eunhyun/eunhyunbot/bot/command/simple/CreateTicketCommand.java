package io.github.eunhyun.eunhyunbot.bot.command.simple;

import io.github.eunhyun.eunhyunbot.api.bot.command.simple.ISimpleCommand;
import io.github.eunhyun.eunhyunbot.api.bot.command.simple.SimpleCommand;
import io.github.eunhyun.eunhyunbot.api.bot.permission.PermissionUtil;
import io.github.eunhyun.eunhyunbot.api.enums.TicketType;
import io.github.eunhyun.eunhyunbot.api.factory.EmbedColorFactory;
import io.github.eunhyun.eunhyunbot.api.util.DiscordEmojiUtil;
import io.github.eunhyun.eunhyunbot.api.util.EunhyunImageUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

import java.awt.*;

@SimpleCommand(
        command = "티켓생성",
        description = "티켓 메시지를 생성합니다.",
        usage = "?티켓생성"
)
public class CreateTicketCommand implements ISimpleCommand {

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

        StringSelectMenu stringSelectMenu = StringSelectMenu.create("ticket-select-category")
                .setPlaceholder("🖐️ 원하시는 상담 카테고리를 선택해주세요.")
                .addOptions(SelectOption.of("일반 문의", TicketType.GENERAL.toString().toLowerCase())
                        .withDescription("일반적인 문의")
                        .withEmoji(Emoji.fromUnicode("🌿")))

                .addOptions(SelectOption.of("버그 제보", TicketType.BUG_REPORT.toString().toLowerCase())
                        .withDescription("시스템 버그")
                        .withEmoji(Emoji.fromUnicode("🐞")))

                .addOptions(SelectOption.of("서비스 제한 문의", TicketType.PUNISHMENT.toString().toLowerCase())
                        .withDescription("계정 및 서비스의 제한 항소")
                        .withEmoji(Emoji.fromUnicode("🚫")))

                .addOptions(SelectOption.of("신고", TicketType.USER_REPORT.toString().toLowerCase())
                        .withDescription("유저 신고 및 제보")
                        .withEmoji(Emoji.fromUnicode("📢")))
                .build();

        MessageEmbed embed = new EmbedBuilder()
                .setColor(EMBED_COLOR)
                .setTitle("%s 고객센터 문의 | 은현서버 %s".formatted(DiscordEmojiUtil.TICKET, DiscordEmojiUtil.TICKET))
                .setDescription("""
                        `🎉` **은현 서버 고객센터에 오신 것을 환영합니다!**
                        여기는 귀하의 문의사항이나 건의사항을 기다리고 있는 곳입니다.
                        
                        `🔧` **문의사항은 신속하고 친절하게 처리됩니다.**
                        `⚠️` **직원에게 폭언이나 성희롱을 할 경우, 법적 처벌을 받을 수 있습니다.**
                        
                        `💖` **은현 서버에서 즐거운 시간 보내시길 바랍니다!**
                        """
                )
                .setThumbnail(EunhyunImageUtil.THUMBNAIL_IMAGE_URL)
                .setFooter("문의사항이 있으시면 언제든지 편하게 말씀해 주세요!", EunhyunImageUtil.THUMBNAIL_IMAGE_URL)
                .build();

        event.getChannel().sendMessageEmbeds(embed).addActionRow(stringSelectMenu).queue();
    }
}