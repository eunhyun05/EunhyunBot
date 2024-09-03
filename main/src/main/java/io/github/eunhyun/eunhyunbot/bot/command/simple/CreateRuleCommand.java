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
                        `🎉` **환영합니다! 은현 서버에 오신 것을 환영합니다.**
                        
                        `📜` **서버 규칙 안내**
                        > **`1️⃣ 경고 1 1️⃣`**
                        > `⚠️` 부적절한 사용자명 또는 상태 메시지
                        > `⚠️` 과도한 욕설 또는 비속어 사용
                        > `⚠️` 민감한 주제(정치, 종교 등)로 인한 논쟁 유발
                        > `⚠️` 타인의 의견을 무시하거나 무례하게 대하는 행위
                        > `⚠️` 다른 사용자의 발언을 반복적으로 끊는 행위
                        > `⚠️` 불필요한 멘션(예: @everyone/@here) 사용
                        > `⚠️` 채팅방 또는 음성 채널에서의 불필요한 소음 유발
                        > `⚠️` 규칙에 대한 불필요한 논쟁 또는 비아냥거림
                        > `⚠️` 기타 사소한 비매너 행위
                        
                        > **`2️⃣ 경고 2 2️⃣`**
                        > `🚫` 스팸성 메시지 반복 전송
                        > `🚫` 무단 광고 또는 홍보
                        > `🚫` 다른 사용자를 괴롭히거나 위협하는 행위
                        > `🚫` 음성 채널에서의 반복적인 트롤링 또는 방해
                        > `🚫` 의도적으로 불쾌한 소리를 내거나 노래를 부르는 행위
                        > `🚫` 반복적인 도배(이모티콘, 짧은 메시지 등)
                        > `🚫` 타인의 콘텐츠(이미지, 메시지 등)를 허락 없이 반복 전송
                        > `🚫` 채널 주제와 상관없는 대화나 콘텐츠 공유
                        > `🚫` 의도적으로 불쾌감을 주기 위한 이미지, 동영상, 링크 전송
                        > `🚫` 사용자 간의 불필요한 언쟁을 조장하는 행위
                        > `🚫` 성적인 암시나 부적절한 농담
                        > `🚫` 무분별한 도전 또는 싸움 걸기(예: '1:1로 붙자' 등)
                        
                        > **`3️⃣ 경고 3 3️⃣`**
                        > `❗` 악의적인 팀킬 또는 협동 방해
                        > `❗` 서버 운영진을 사칭하는 행위
                        > `❗` 불법적인 행위(예: 해킹, 부정 프로그램 사용)
                        > `❗` 타인의 개인 정보 무단 공유
                        > `❗` 성인물 또는 불쾌감을 줄 수 있는 콘텐츠 공유
                        > `❗` 다수의 경고를 무시하고 지속적으로 규칙 위반
                        > `❗` 심각한 폭력적인 언행
                        > `❗` 서버 운영 방해 행위
                        
                        `🛑` **위반 시 경고, 일시 정지, 또는 영구 차단 조치가 취해질 수 있습니다.**
                        
                        `💬` **문의하기**
                        규칙에 대해 궁금한 점이 있다면 <#1276523763340542128>를 통해 문의하여 주시길 바랍니다.
                        """
                )
                .setThumbnail(EunhyunImageUtil.THUMBNAIL_IMAGE_URL)
                .setFooter("경고가 3회 누적될 경우 서버에서 영구 차단됩니다.", EunhyunImageUtil.THUMBNAIL_IMAGE_URL)
                .build();

        Button checkMyWarnBtn = Button.danger("check_my_warn", "내 경고 확인");
        event.getChannel().sendMessageEmbeds(embed).addActionRow(checkMyWarnBtn).queue();
    }
}