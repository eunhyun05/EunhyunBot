package io.github.eunhyun.eunhyunbot.api;

import io.github.eunhyun.eunhyunbot.api.bot.DiscordBotManager;
import io.github.eunhyun.eunhyunbot.api.configuration.FileConfiguration;
import io.github.eunhyun.eunhyunbot.api.configuration.ResourceHandler;
import io.github.eunhyun.eunhyunbot.api.configuration.YamlConfiguration;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.Scanner;
import java.util.Set;

@Slf4j
public abstract class EunhyunBotAPI {

    @Getter
    private static EunhyunBotAPI api;
    private static File config;

    protected static void setInstance(EunhyunBotAPI api) {
        if (EunhyunBotAPI.api != null) {
            throw new UnsupportedOperationException("Cannot redefine singleton EunhyunBotAPI");
        }

        EunhyunBotAPI.api = api;
    }

    public static void main(String[] args) {
        config = new File(ResourceHandler.getDataFolder(), "config.yml");

        Reflections reflections = new Reflections("io.github.eunhyun.eunhyunbot");
        Set<Class<? extends EunhyunBotAPI>> subTypes = reflections.getSubTypesOf(EunhyunBotAPI.class);

        if (EunhyunBotAPI.api == null) {
            for (Class<? extends EunhyunBotAPI> subType : subTypes) {
                if (!Modifier.isAbstract(subType.getModifiers())) {
                    try {
                        EunhyunBotAPI instance = subType.getDeclaredConstructor().newInstance();
                        if (EunhyunBotAPI.api == null) {
                            setInstance(instance);
                            instance.onEnable();
                        }
                        break;
                    } catch (Exception e) {
                        log.warn("Failed to initialize {}: {}", subType.getName(), e.getMessage(), e);
                    }
                }
            }
        } else {
            log.warn("EunhyunBotAPI instance is already set. Skipping initialization.");
        }
    }

    protected void handleCommands() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            System.out.flush();
            String command = scanner.nextLine();

            if ("stop".equalsIgnoreCase(command)) {
                onDisable();
                getBotManager().stop();
                scanner.close();
                System.exit(0);
                break;
            } else if ("help".equalsIgnoreCase(command)) {
                System.out.println("사용가능한 명령어 목록: stop, help");
            } else if (!command.isEmpty()) {
                System.out.println("알 수 없는 명령어입니다. 도움말을 확인하려면 'help' 명령어를 입력해보세요.");
            }
        }
    }

    protected void saveDefaultConfig() {
        ResourceHandler.saveResource(config.getName(), false);
    }

    public File getDataFolder() {
        return new File(System.getProperty("user.dir"));
    }

    public FileConfiguration getConfig() {
        return YamlConfiguration.loadConfiguration(config);
    }

    protected abstract void onEnable();

    protected void onDisable() {}

    protected abstract DiscordBotManager getBotManager();
}