package com.onulstore.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CurationForm {

    MAGAZINE("MAGAZINE", "매거진"),
    RECOMMEND("RECOMMEND", "추천 제품");

    private final String key;
    private final String title;
}
