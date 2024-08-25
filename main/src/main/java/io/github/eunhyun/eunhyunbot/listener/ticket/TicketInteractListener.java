package io.github.eunhyun.eunhyunbot.listener.ticket;

import io.github.eunhyun.eunhyunbot.EunhyunBot;
import io.github.eunhyun.eunhyunbot.api.bot.BotEventHandler;
import io.github.eunhyun.eunhyunbot.api.bot.permission.PermissionUtil;
import io.github.eunhyun.eunhyunbot.api.enums.TicketType;
import io.github.eunhyun.eunhyunbot.api.factory.EmbedColorFactory;
import io.github.eunhyun.eunhyunbot.api.repository.TicketRepository;
import io.github.eunhyun.eunhyunbot.api.util.DiscordEmojiUtil;
import io.github.eunhyun.eunhyunbot.api.util.EunhyunImageUtil;
import io.github.eunhyun.eunhyunbot.repository.TicketRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.EnumSet;

@Slf4j
@BotEventHandler
public class TicketInteractListener extends ListenerAdapter {

    private static final long TARGET_CATEGORY_ID = EunhyunBot.getInstance().getConfig().getLong("ticket_channel.target_category_id");

    private final Color EMBED_COLOR_SUCCESS = EmbedColorFactory.getEmbedColor(EmbedColorFactory.Type.SUCCESS);
    private final TicketRepository ticketRepository = new TicketRepositoryImpl();

    @Override
    public void onStringSelectInteraction(@NotNull StringSelectInteractionEvent event) {
        if (!event.getComponentId().equals("ticket-select-category")) {
            return;
        }

        String selectedValue = event.getValues().get(0);
        Guild guild = event.getGuild();
        if (guild != null) {
            Category targetCategory = guild.getCategoryById(TARGET_CATEGORY_ID);
            if (targetCategory != null) {
                targetCategory.createTextChannel("%s│%s-%s".formatted(ticketRepository.getTicketCount(), TicketType.fromName(selectedValue), event.getUser().getGlobalName())).queue(channel -> {
                    event.replyEmbeds(new EmbedBuilder()
                                    .setColor(EMBED_COLOR_SUCCESS)
                                    .setTitle("%s 고객센터 문의 | 성공 %s".formatted(DiscordEmojiUtil.CHECK_MARK, DiscordEmojiUtil.CHECK_MARK))
                                    .setDescription("""
                                            > `🎉` **성공적으로 티켓을 오픈하였습니다.**
                                            > `🌠` **%s**
                                            """.formatted(channel.getAsMention())
                                    )
                                    .setThumbnail(EunhyunImageUtil.THUMBNAIL_IMAGE_URL)
                                    .setFooter("티켓을 열면 되돌릴 수 없습니다.", EunhyunImageUtil.THUMBNAIL_IMAGE_URL)
                                    .build())
                            .setEphemeral(true)
                            .queue();

                    Member member = event.getMember();
                    if (member == null) {
                        return;
                    }

                    channel.upsertPermissionOverride(member)
                            .setAllowed(EnumSet.of(Permission.MESSAGE_SEND, Permission.VIEW_CHANNEL))
                            .queue();

                    event.editSelectMenu(event.getSelectMenu()).queue();
                    ticketRepository.incrementTicketCount();

                    channel.sendMessageEmbeds(new EmbedBuilder()
                                    .setColor(EMBED_COLOR_SUCCESS)
                                    .setTitle("%s 고객센터 문의 | 알림 %s".formatted(DiscordEmojiUtil.CHECK_MARK, DiscordEmojiUtil.CHECK_MARK))
                                    .setDescription("""
                                            > `🌠` **%s님께서 티켓을 오픈하였습니다.**
                                            > `🚨` **미리 문의 내용을 입력해 주세요.**
                                            """.formatted(channel.getAsMention())
                                    )
                                    .setThumbnail(EunhyunImageUtil.THUMBNAIL_IMAGE_URL)
                                    .setFooter("티켓을 열면 되돌릴 수 없습니다.", EunhyunImageUtil.THUMBNAIL_IMAGE_URL)
                                    .build())
                            .addActionRow(Button.danger("ticket-close", "닫기"))
                            .queue();
                });
            }
        }
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        if (!event.getComponentId().equals("ticket-close")) {
            return;
        }

        Member member = event.getMember();
        if (member == null) {
            return;
        }

        if (!PermissionUtil.checkPermissionsAndSendError(event, event.getMember(), new Permission[]{Permission.ADMINISTRATOR}, "티켓은 관리자만 닫을 수 있어요.")) {
            return;
        }

        event.getChannel().delete().queue();
    }
}