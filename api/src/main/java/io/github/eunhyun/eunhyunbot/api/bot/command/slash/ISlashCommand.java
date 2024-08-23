package io.github.eunhyun.eunhyunbot.api.bot.command.slash;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface ISlashCommand {

    void execute(SlashCommandInteractionEvent event);
}