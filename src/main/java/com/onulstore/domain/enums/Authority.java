package com.onulstore.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Authority {

    ROLE_USER("ROLE_USER", "일반 사용자"),
    ROLE_SELLER("ROLE_SELLER", "입점사"),
    ROLE_ADMIN("ROLE_ADMIN", "관리자");


    private final String key;
    private final String title;    

}
