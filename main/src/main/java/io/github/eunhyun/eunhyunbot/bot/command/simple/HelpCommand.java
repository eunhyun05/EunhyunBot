package io.github.eunhyun.eunhyunbot.bot.command.simple;

import io.github.eunhyun.eunhyunbot.api.bot.command.simple.ISimpleCommand;
import io.github.eunhyun.eunhyunbot.api.bot.command.simple.SimpleCommand;
import io.github.eunhyun.eunhyunbot.api.bot.permission.PermissionUtil;
import io.github.eunhyun.eunhyunbot.api.factory.EmbedColorFactory;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.reflections.Reflections;

import java.awt.*;
import java.util.Set;

@SimpleCommand(
        command = "도움말",
        description = "물음표 명령어 사용 방법을 출력합니다.",
        usage = "?도움말"
)
public class HelpCommand implements ISimpleCommand {

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

        EmbedBuilder embed = new EmbedBuilder()
                .setColor(EMBED_COLOR)
                .setTitle("<a:foxhuhquestion:1276380174534967357> 도움말 | 물음표 명령어 <a:foxhuhquestion:1276380174534967357>");

        String packageName = "io.github.eunhyun.eunhyunbot.bot.command.simple";
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(SimpleCommand.class);

        embed.addBlankField(false);

        annotated.forEach(clazz -> {
            SimpleCommand annotation = clazz.getAnnotation(SimpleCommand.class);
            String description = annotation.description();
            String usage = annotation.usage();

            embed.addField(usage, description, true);
        });

        event.getChannel().sendMessageEmbeds(embed.build()).queue();
    }
}