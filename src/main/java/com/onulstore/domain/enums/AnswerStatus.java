package com.onulstore.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AnswerStatus {

    YES("YES", "답변 완료"),
    NO("NO", "답변 예정");

    private final String key;
    private final String title;
}
