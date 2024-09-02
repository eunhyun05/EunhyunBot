package io.github.eunhyun.eunhyunbot.api.enums;

import io.github.eunhyun.eunhyunbot.api.interfaces.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AgeRoleType implements RoleType {

    TEN("10대", 1277140913734291569L),
    TWENTY("20대", 1277141008240480342L),
    THIRTY("30대", 1277141065857761290L);

    private final String name;
    private final long roleId;

    public static boolean isAgeRole(long roleId) {
        return RoleType.isRoleType(roleId, values());
    }
}