package io.github.eunhyun.eunhyunbot.api.factory;

import io.github.eunhyun.eunhyunbot.api.EunhyunBotAPI;
import io.github.eunhyun.eunhyunbot.api.configuration.FileConfiguration;
import lombok.Getter;

import java.awt.*;

public class EmbedColorFactory {

    @Getter
    public enum Type {

        NORMAL("EMBED_COLOR", "#FF5733"),
        SUCCESS("EMBED_COLOR_SUCCESS", "#4CAF50"),
        ERROR("EMBED_COLOR_ERROR", "#F44336");

        private final String key;
        private Color color;

        Type(String key, String defaultColorHex) {
            this.key = key;
            this.color = Color.decode(defaultColorHex);
            updateColorFromConfig();
        }

        private void updateColorFromConfig() {
            FileConfiguration config = EunhyunBotAPI.getApi().getConfig();
            String colorCode = config.getString(key);
            if (colorCode != null) {
                this.color = Color.decode(colorCode);
            }
        }

        public String getColorAsHexString() {
            return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
        }
    }

    public static String getEmbedColorAsString(Type colorType) {
        return colorType.getColorAsHexString();
    }

    public static Color getEmbedColor(Type colorType) {
        return colorType.getColor();
    }

    public static void loadColors() {
        for (Type type : Type.values()) {
            type.updateColorFromConfig();
        }
    }
}