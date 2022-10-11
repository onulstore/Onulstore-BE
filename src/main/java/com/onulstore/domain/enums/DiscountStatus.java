package com.onulstore.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DiscountStatus {

    ONUL("ONUL", "오늘의 할인"),
    TRUE("TRUE", "그 외의 할인"),
    FALSE("FALSE", "할인 없음");

    private final String key;
    private final String title;
}
