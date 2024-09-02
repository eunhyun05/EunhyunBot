package io.github.eunhyun.eunhyunbot.api.interfaces;

import java.util.Arrays;

public interface RoleType {

    String getName();

    long getRoleId();

    static boolean isRoleType(long roleId, RoleType[] roleTypes) {
        return Arrays.stream(roleTypes)
                .anyMatch(roleType -> roleType.getRoleId() == roleId);
    }
}