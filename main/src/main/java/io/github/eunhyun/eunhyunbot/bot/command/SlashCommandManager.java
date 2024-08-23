package io.github.eunhyun.eunhyunbot.bot.command;

import io.github.eunhyun.eunhyunbot.EunhyunBot;
import io.github.eunhyun.eunhyunbot.api.bot.command.slash.ISlashCommand;
import io.github.eunhyun.eunhyunbot.api.bot.command.slash.SlashCommand;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class SlashCommandManager extends ListenerAdapter {

    @Getter
    private final List<CommandData> commands = new ArrayList<>();
    private final Map<String, ISlashCommand> commandActions = new HashMap<>();

    private final String GUILD_ID = EunhyunBot.getInstance().getConfig().getString("GUILD_ID");

    public SlashCommandManager() {
        registerCommands();
    }

    @SuppressWarnings("all")
    private void registerCommands() {
        Set<Class<? extends ISlashCommand>> commandClasses = getCommandClasses();
        commandClasses.forEach(commandClass -> {
            SlashCommand annotation = commandClass.getAnnotation(SlashCommand.class);
            if (annotation != null) {
                try {
                    ISlashCommand commandInstance = commandClass.getDeclaredConstructor().newInstance();

                    SlashCommandData commandData = Commands.slash(annotation.command(), annotation.description());

                    for (SlashCommand.SubCommand subCommand : annotation.subcommands()) {
                        SubcommandData subCommandData = new SubcommandData(subCommand.name(), subCommand.description());
                        if (subCommand.optionName().length > 0 &&
                                subCommand.optionName().length == subCommand.optionType().length &&
                                subCommand.optionName().length == subCommand.optionDescription().length) {
                            for (int i = 0; i < subCommand.optionName().length; i++) {
                                subCommandData.addOption(subCommand.optionType()[i], subCommand.optionName()[i], subCommand.optionDescription()[i], subCommand.optionRequired()[i]);
                            }
                        }
                        commandData.addSubcommands(subCommandData);
                    }

                    if (annotation.optionName().length > 0 &&
                            annotation.optionName().length == annotation.optionType().length &&
                            annotation.optionName().length == annotation.optionDescription().length) {
                        for (int i = 0; i < annotation.optionName().length; i++) {
                            commandData.addOption(annotation.optionType()[i], annotation.optionName()[i], annotation.optionDescription()[i], annotation.optionRequired()[i]);
                        }
                    }

                    commands.add(commandData);
                    commandActions.put(annotation.command(), commandInstance);
                } catch (Exception e) {
                    log.warn(e.getMessage());
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    private Set<Class<? extends ISlashCommand>> getCommandClasses() {
        String packageName = "io.github.eunhyun.eunhyunbot.bot.command.slash";

        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(SlashCommand.class);

        return annotated.stream()
                .filter(ISlashCommand.class::isAssignableFrom)
                .map(clazz -> (Class<? extends ISlashCommand>) clazz)
                .collect(Collectors.toSet());
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getInteraction().getCommandString();

        command = command.split(" ")[0];
        command = command.replace("/", "");

        if (Objects.requireNonNull(event.getGuild()).getId().equals(GUILD_ID)) {
            ISlashCommand discordSlashCommand = commandActions.get(command);

            if (discordSlashCommand != null) {
                discordSlashCommand.execute(event);
            }
        }
    }
}