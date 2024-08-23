package io.github.eunhyun.eunhyunbot.bot.command.slash;

import io.github.eunhyun.eunhyunbot.api.bot.command.slash.ISlashCommand;
import io.github.eunhyun.eunhyunbot.api.bot.command.slash.SlashCommand;
import io.github.eunhyun.eunhyunbot.api.bot.permission.PermissionUtil;
import io.github.eunhyun.eunhyunbot.api.factory.EmbedColorFactory;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.awt.*;

@Slf4j
@SlashCommand(
        command = "임베드생성",
        description = "임베드를 생성합니다.",
        optionName = {"색상", "제목", "제목링크", "내용", "썸네일", "이미지", "푸터", "푸터이미지", "작성자", "작성자링크", "작성자이미지"},
        optionType = {
                OptionType.STRING,
                OptionType.STRING,
                OptionType.STRING,
                OptionType.STRING,
                OptionType.STRING,
                OptionType.STRING,
                OptionType.STRING,
                OptionType.STRING,
                OptionType.STRING,
                OptionType.STRING,
                OptionType.STRING
        },
        optionDescription = {
                "임베드의 색상을 입력하세요.",
                "임베드의 제목을 입력하세요.",
                "임베드의 제목-링크를 입력하세요.",
                "임베드의 내용을 입력하세요. (\\n 사용가능)",
                "임베드의 썸네일을 첨부하세요.",
                "임베드의 이미지를 첨부하세요.",
                "임베드의 푸터를 입력하세요.",
                "임베드의 푸터-이미지를 첨부하세요.",
                "임베드의 작성자를 입력하세요.",
                "임베드의 작성자-링크를 입력하세요.",
                "임베드의 작성자-이미지를 첨부하세요."
        },
        optionRequired = {false, false, false, false, false, false, false, false, false, false, false}
)
public class GenerateEmbedCommand implements ISlashCommand {

    private final Color EMBED_COLOR = EmbedColorFactory.getEmbedColor(EmbedColorFactory.Type.NORMAL);
    private final Color EMBED_COLOR_SUCCESS = EmbedColorFactory.getEmbedColor(EmbedColorFactory.Type.SUCCESS);
    private final Color EMBED_COLOR_ERROR = EmbedColorFactory.getEmbedColor(EmbedColorFactory.Type.ERROR);

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Member member = event.getMember();
        if (member == null) {
            return;
        }

        if (!PermissionUtil.checkPermissionsAndSendError(event, member, new Permission[]{Permission.ADMINISTRATOR}, "임베드는 관리자만 생성할 수 있어요.")) {
            return;
        }

        try {
            OptionMapping color = event.getOption("색상");
            OptionMapping title = event.getOption("제목");
            OptionMapping titleUrl = event.getOption("제목링크");
            OptionMapping description = event.getOption("내용");
            OptionMapping thumbnail = event.getOption("썸네일");
            OptionMapping image = event.getOption("이미지");
            OptionMapping footerText = event.getOption("푸터");
            OptionMapping footerIcon = event.getOption("푸터이미지");
            OptionMapping authorText = event.getOption("작성자");
            OptionMapping authorUrl = event.getOption("작성자링크");
            OptionMapping authorIcon = event.getOption("작성자이미지");

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(color == null ? EMBED_COLOR : Color.decode(color.getAsString()));
            embedBuilder.setTitle(getSafeString(title), getSafeString(titleUrl));
            embedBuilder.setDescription(description == null ? null : description.getAsString().replace("\\n", "\n"));
            embedBuilder.setThumbnail(getSafeString(thumbnail));
            embedBuilder.setImage(getSafeString(image));
            embedBuilder.setFooter(getSafeString(footerText), getSafeString(footerIcon));
            embedBuilder.setAuthor(getSafeString(authorText), getSafeString(authorUrl), getSafeString(authorIcon));

            event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();

            MessageEmbed embed = new EmbedBuilder()
                    .setColor(EMBED_COLOR_SUCCESS)
                    .setTitle("<a:check_mark:1276415022498844752> 임베드 생성 | 성공 <a:check_mark:1276415022498844752>")
                    .setDescription("> **성공적으로 임베드를 생성했습니다.**")
                    .build();
            event.replyEmbeds(embed).setEphemeral(true).queue();
        } catch (Exception ex) {
            log.warn(ex.getMessage());

            MessageEmbed embed = new EmbedBuilder()
                    .setColor(EMBED_COLOR_ERROR)
                    .setTitle("<a:cross_mark:1276415059739807744> 임베드 생성 | 오류 <a:cross_mark:1276415059739807744>")
                    .setDescription("> **임베드를 생성하지 못했습니다.**")
                    .build();
            event.replyEmbeds(embed).setEphemeral(true).queue();
        }
    }

    private String getSafeString(OptionMapping optionMapping) {
        return optionMapping == null ? null : optionMapping.getAsString();
    }
}