package io.github.eunhyun.eunhyunbot.bot;

import io.github.eunhyun.eunhyunbot.api.bot.BotEventHandler;
import io.github.eunhyun.eunhyunbot.api.bot.DiscordBotManager;
import io.github.eunhyun.eunhyunbot.bot.command.SimpleCommandManager;
import io.github.eunhyun.eunhyunbot.bot.command.SlashCommandManager;
import io.github.eunhyun.eunhyunbot.api.configuration.FileConfiguration;
import io.github.eunhyun.eunhyunbot.listener.autovoice.AutoVoiceChannelManagerListener;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reflections.Reflections;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Slf4j
public class DiscordBotManagerImpl implements DiscordBotManager {

    private final FileConfiguration config;
    @Getter
    private JDA jda;

    private final SlashCommandManager slashCommandManager = new SlashCommandManager();

    public DiscordBotManagerImpl(FileConfiguration config) {
        this.config = config;
        start();
    }

    private void start() {
        try {
            JDABuilder builder = JDABuilder.createDefault(config.getString("BOT_TOKEN"));
            configure(builder);

            builder.addEventListeners(new SimpleCommandManager());
            builder.addEventListeners(slashCommandManager);
            builder.addEventListeners(new AutoVoiceChannelManagerListener());

            jda = builder.build();

            registerEventListeners();
        } catch (Exception e) {
            log.error("봇을 실행하는 도중에 오류가 발생하였습니다.", e);
        }
    }

    private void configure(JDABuilder builder) {
        String activityName = config.getString("ACTIVITY_NAME");
        String activityType = config.getString("ACTIVITY_TYPE");
        String status = config.getString("STATUS");

        try {
            Activity.ActivityType type = Activity.ActivityType.valueOf(activityType.toUpperCase());
            Activity activity = Activity.of(type, activityName);
            builder.setActivity(activity);
        } catch (IllegalArgumentException e) {
            log.error("봇을 실행하는 도중에 오류가 발생하였습니다.", e);
        }

        enableAllIntents(builder);
        builder.setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableCache(CacheFlag.MEMBER_OVERRIDES)
                .setEnableShutdownHook(false)
                .setStatus(OnlineStatus.valueOf(status));

        builder.addEventListeners(new ListenerAdapter() {
            @Override
            public void onReady(@NotNull ReadyEvent event) {
                slashCommandManager.getCommands().forEach(commandData -> Objects.requireNonNull(jda.getGuildById(Objects.requireNonNull(getGuild()).getId())).upsertCommand(commandData).queue());
            }
        });
    }

    private void registerEventListeners() {
        String packageName = "io.github.eunhyun.eunhyunbot.listener";
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(BotEventHandler.class);

        for (Class<?> clazz : annotated) {
            try {
                Object instance = clazz.getDeclaredConstructor().newInstance();
                if (instance instanceof ListenerAdapter) {
                    jda.addEventListener(instance);
                }
            } catch (Exception e) {
                log.error("리스너 인스턴스를 생성할 수 없습니다: {}", e.getMessage(), e);
            }
        }
    }

    private void enableAllIntents(JDABuilder builder) {
        builder.enableIntents(List.of(GatewayIntent.values()));
    }

    public void stop() {
        log.info("봇을 종료합니다...");
        jda.shutdown();
        log.info("봇이 종료되었습니다.");
    }

    @Override
    @Nullable
    public Guild getGuild() {
        String guild_id = config.getString("GUILD_ID");
        return jda.getGuildById(guild_id);
    }

    @Override
    @Nullable
    public TextChannel getTextChannel(String key) {
        String channel_id = config.getString(key);
        return jda.getTextChannelById(channel_id);
    }

    @Override
    public String getRoleAsMention(long roleId) {
        Role roleById = jda.getRoleById(roleId);
        return roleById != null ? roleById.getAsMention() : null;
    }
}