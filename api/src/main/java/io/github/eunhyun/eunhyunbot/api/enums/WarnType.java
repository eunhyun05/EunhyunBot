package io.github.eunhyun.eunhyunbot.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum WarnType {

    ADD("추가"),
    SUBTRACT("제거"),
    SET("설정"),
    RESET("초기화");

    private final String name;
}