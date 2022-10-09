package com.onulstore.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DeliveryMeasure {

    UNDETERMINED("UNDETERMINED", "미정"),
    INTERNATIONAL("INTERNATIONAL", "한일 국제 택배"),
    DOMESTIC("DOMESTIC", "일본 국내 택배"),
    STORE("STORE", "편의점 픽업");

    private final String key;
    private final String title;

}
