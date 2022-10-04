package com.onulstore.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Provider {

    local("local", "일반 회원"),
    local_admin("local_admin", "관리자"),
    google("google", "구글 회원");

    private final String key;
    private final String title;

}
