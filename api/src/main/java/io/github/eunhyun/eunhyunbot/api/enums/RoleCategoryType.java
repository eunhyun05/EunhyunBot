package io.github.eunhyun.eunhyunbot.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RoleCategoryType {

    AGE("나이"),
    GENDER("성별"),
    VALORANT_TIER("발로란트티어"),
    VERIFY("인증");

    private final String name;

    public static RoleCategoryType fromName(String name) {
        for (RoleCategoryType type : RoleCategoryType.values()) {
            if (type.name.equalsIgnoreCase(name)) {
                return type;
            }
        }
        throw new IllegalArgumentException("잘못된 역할 카테고리: " + name);
    }
}