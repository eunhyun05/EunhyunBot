package io.github.eunhyun.eunhyunbot.api.enums;

import io.github.eunhyun.eunhyunbot.api.interfaces.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum GenderRoleType implements RoleType {

    MALE("남자", 1277197841017737288L),
    FEMALE("여자", 1277197896026034282L);

    private final String name;
    private final long roleId;

    public static boolean isGenderRole(long roleId) {
        return RoleType.isRoleType(roleId, values());
    }
}