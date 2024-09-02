package io.github.eunhyun.eunhyunbot.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum TicketType {

    GENERAL("일반"),
    BUG_REPORT("버그"),
    PUNISHMENT("제한"),
    USER_REPORT("신고");

    private final String name;

    public static String fromName(String name) {
        return Arrays.stream(TicketType.values())
                .filter(ticketType -> ticketType.name().equalsIgnoreCase(name))
                .map(TicketType::getName)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("알 수 없는 티켓 타입: " + name));
    }
}