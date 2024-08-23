package io.github.eunhyun.eunhyunbot.bot.command;

import io.github.eunhyun.eunhyunbot.api.bot.command.simple.ISimpleCommand;
import io.github.eunhyun.eunhyunbot.api.bot.command.simple.SimpleCommand;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class SimpleCommandManager extends ListenerAdapter {

    private final Map<String, ISimpleCommand> commands = new HashMap<>();

    public SimpleCommandManager() {
        registerCommands();
    }

    private void registerCommands() {
        Set<Class<? extends ISimpleCommand>> commandClasses = getCommandClasses();
        commandClasses.forEach(commandClass -> {
            SimpleCommand annotation = commandClass.getAnnotation(SimpleCommand.class);
            if (annotation != null) {
                try {
                    ISimpleCommand commandInstance = commandClass.getDeclaredConstructor().newInstance();
                    commands.put(annotation.command(), commandInstance);
                } catch (Exception e) {
                    log.warn(e.getMessage());
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    private Set<Class<? extends ISimpleCommand>> getCommandClasses() {
        String packageName = "io.github.eunhyun.eunhyunbot.bot.command.simple";

        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(SimpleCommand.class);

        return annotated.stream()
                .filter(ISimpleCommand.class::isAssignableFrom)
                .map(clazz -> (Class<? extends ISimpleCommand>) clazz)
                .collect(Collectors.toSet());
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        String message = event.getMessage().getContentRaw();
        if (!message.startsWith("?")) return;

        String[] args = message.substring(1).split(" ", 2);
        String command = args[0].toLowerCase();
        ISimpleCommand commandExecutor = commands.get(command);

        if (commandExecutor != null) {
            commandExecutor.execute(event);
        }
    }
}