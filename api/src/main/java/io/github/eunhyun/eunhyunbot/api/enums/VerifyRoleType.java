package io.github.eunhyun.eunhyunbot.api.enums;

import io.github.eunhyun.eunhyunbot.api.interfaces.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum VerifyRoleType implements RoleType {

    VERIFY("인증됨", 1280534659612282910L);

    private final String name;
    private final long roleId;

    public static boolean isVerifyRole(long roleId) {
        return RoleType.isRoleType(roleId, values());
    }
}