package io.github.eunhyun.eunhyunbot.api.bot.permission;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.PermissionOverride;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberPermissionUtil {

    public static void addRole(@NotNull Member member, @NotNull Role role) {
        try {
            member.getGuild().addRoleToMember(member, role).queue();
        } catch (HierarchyException | InsufficientPermissionException e) {
            log.error(e.getMessage());
        }
    }

    public static void removeRole(@NotNull Member member, @NotNull Role role) {
        try {
            member.getGuild().removeRoleFromMember(member, role).queue();
        } catch (HierarchyException | InsufficientPermissionException e) {
            log.error(e.getMessage());
        }
    }

    public static void addPermissionsToChannel(@NotNull Member member, @NotNull GuildChannel channel, @NotNull Permission... permissions) {
        modifyPermissionsInChannel(member, channel, true, permissions);
    }

    public static void removePermissionsFromChannel(@NotNull Member member, @NotNull GuildChannel channel, @NotNull Permission... permissions) {
        modifyPermissionsInChannel(member, channel, false, permissions);
    }

    public static boolean hasRole(@NotNull Member member, @NotNull Role role) {
        return member.getRoles().contains(role);
    }

    public static boolean hasPermission(@NotNull Member member, @NotNull Permission permission) {
        return member.hasPermission(permission);
    }

    public static boolean hasPermissionInChannel(@NotNull Member member, @NotNull GuildChannel channel, @NotNull Permission... permissions) {
        PermissionOverride override = getPermissionOverride(member, channel);
        if (override != null) {
            return override.getAllowed().containsAll(List.of(permissions));
        }
        return false;
    }

    private static void modifyPermissionsInChannel(@NotNull Member member, @NotNull GuildChannel channel, boolean add, @NotNull Permission... permissions) {
        try {
            PermissionOverride override = getPermissionOverride(member, channel);
            if (override != null) {
                var manager = override.getManager();
                if (add) {
                    manager.grant(List.of(permissions)).queue();
                } else {
                    manager.deny(List.of(permissions)).queue();
                }
            } else {
                log.warn("No permission override found for the channel: {}", channel.getId());
            }
        } catch (Exception e) {
            log.error("Error modifying permissions: {}", e.getMessage());
        }
    }

    @SuppressWarnings("all")
    private static PermissionOverride getPermissionOverride(@NotNull Member member, @NotNull GuildChannel channel) {
        if (channel instanceof GuildMessageChannel) {
            return channel.getPermissionContainer().getPermissionOverride(member);
        } else if (channel instanceof GuildVoiceState) {
            return ((GuildVoiceState) channel).getChannel().getPermissionOverride(member);
        }
        return null;
    }
}
