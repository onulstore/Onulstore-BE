package com.onulstore.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DiscountType {

    NONE("NONE", "할인 없음"),
    PERCENT("PERCENT", "퍼센트 할인"),
    TOTAL("TOTAL", "총액 할인");

    private final String key;
    private final String title;
}
