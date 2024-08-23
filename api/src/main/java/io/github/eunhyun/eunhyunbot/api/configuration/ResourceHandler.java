package io.github.eunhyun.eunhyunbot.api.configuration;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResourceHandler {

    @SuppressWarnings("all")
    public static void saveResource(@NotNull String resourcePath, boolean replace) {
        File destinationFile = new File(getDataFolder(), resourcePath);
        Path destinationPath = destinationFile.toPath();
        if (!Files.exists(destinationPath) || replace) {
            copyResource(resourcePath, destinationPath);
        }
    }

    private static void copyResource(String resourcePath, Path destination) {
        try (InputStream inputStream = ResourceHandler.class.getResourceAsStream("/" + resourcePath)) {
            if (inputStream == null) {
                log.error("리소스를 찾는 도중에 오류가 발생하였습니다. {}", resourcePath);
                return;
            }

            File parentDir = destination.toFile().getParentFile();
            if (!parentDir.exists()) {
                if (!parentDir.mkdirs()) {
                    log.error("콘피그 폴더를 생성하는 도중에 오류가 발생하였습니다. (관리자에게 문의해 주세요.)");
                }
            }

            Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            log.error("파일 복사에 실패하였습니다. {}", resourcePath, ex);
        }
    }

    public static File getDataFolder() {
        return new File(System.getProperty("user.dir"));
    }
}