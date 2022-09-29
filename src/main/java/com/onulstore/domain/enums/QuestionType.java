package com.onulstore.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum QuestionType {

    PRODUCT("PRODUCT","상품문의"),
    SIZE("SIZE","사이즈문의"),
    RESTOCK("RESTOCK","재입고문의"),
    SHIP("SHIP","배송문의"),
    ETC("ETC","기타");

    private final String key;
    private final String title;
}
