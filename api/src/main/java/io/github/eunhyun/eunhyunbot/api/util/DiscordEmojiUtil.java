package io.github.eunhyun.eunhyunbot.api.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DiscordEmojiUtil {

    public static final String CHECK_MARK = "<a:check_mark:1276415022498844752>";

    public static final String CROSS_MARK = "<a:cross_mark:1276415059739807744>";

    public static final String WARNING = "<a:warning:1276495432070987797>";

    public static final String FOX_HUH_QUESTION = "<a:foxhuhquestion:1276380174534967357>";

    public static final String TICKET = "<a:ticket:1276750467585540096>";

    public static final String BAN = "<:ban:1276870432552648867>";

    public static final String CHICK_SPROUT = "<:chick_sprout:1277137623747657810>";

    public static final String CHICK_STARS = "<:chick_stars:1277139388282699856>";

    public static final String CHICK_THUMBSUP = "<:chick_thumbsup:1277139498337042533>";

    public static final String CHICK_QUESTION = "<:chick_question:1277139245856981004>";

    public static final String MALE = "<:male:1277201071072215080>";

    public static final String FEMALE = "<:female:1277201093734170624>";

    public static final String VALORANT_TIER_IRON = "<:valorant_tier_iron:1280258134937702401>";

    public static final String VALORANT_TIER_BRONZE = "<:valorant_tier_bronze:1280258108777693304>";

    public static final String VALORANT_TIER_SILVER = "<:valorant_tier_silver:1280258086526914652>";

    public static final String VALORANT_TIER_GOLD = "<:valorant_tier_gold:1280258062107541555>";

    public static final String VALORANT_TIER_PLATINUM = "<:valorant_tier_platinum:1280258031396851813>";

    public static final String VALORANT_TIER_DIAMOND = "<:valorant_tier_diamond:1280257946135036036>";

    public static final String VALORANT_TIER_ASCENDANT = "<:valorant_tier_ascendant:1280256463415279687>";

    public static final String VALORANT_TIER_IMMORTAL = "<:valorant_tier_immortal:1280256406028554252>";

    public static final String VALORANT_TIER_RADIANT = "<:valorant_tier_radiant:1280255919980154931>";

    public static String extractEmojiId(String id) {
        if (id.startsWith("<:") && id.endsWith(">")) {
            int startIndex = id.lastIndexOf(':') + 1;
            int endIndex = id.length() - 1;
            return id.substring(startIndex, endIndex);
        }

        return null;
    }
}