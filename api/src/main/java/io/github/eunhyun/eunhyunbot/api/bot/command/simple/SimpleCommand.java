package io.github.eunhyun.eunhyunbot.api.bot.command.simple;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SimpleCommand {

    String command();

    String description() default "";

    String usage() default "";
}