package io.github.eunhyun.eunhyunbot.api.enums;

import io.github.eunhyun.eunhyunbot.api.interfaces.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ValorantTierType implements RoleType{

    IRON("아이언", 1280258934254338119L),
    BRONZE("브론즈", 1280258816054792255L),
    SILVER("실버", 1280258813936668682L),
    GOLD("골드", 1280258803014570015L),
    PLATINUM("플래티넘", 1280258817107431535L),
    DIAMOND("다이아몬드", 1280258735897575474L),
    ASCENDANT("초월자", 1280258668084068486L),
    IMMORTAL("불멸", 1280258555815006279L),
    RADIANT("레디언트", 1280255409050882149L);

    private final String name;
    private final long roleId;

    public static boolean isValorantTierRole(long roleId) {
        return RoleType.isRoleType(roleId, values());
    }
}