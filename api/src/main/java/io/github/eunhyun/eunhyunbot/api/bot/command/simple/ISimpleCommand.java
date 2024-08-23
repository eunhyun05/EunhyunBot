package io.github.eunhyun.eunhyunbot.api.bot.command.simple;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface ISimpleCommand {

    void execute(MessageReceivedEvent event);
}