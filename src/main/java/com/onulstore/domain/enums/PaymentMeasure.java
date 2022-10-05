package com.onulstore.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentMeasure {

    UNDETERMINED("UNDETERMINED", "미정"),
    CREDIT_CARD("CREDIT_CARD", "신용카드"),
    ACCOUNT("ACCOUNT", "계좌 입금"),
    STORE_PAY("STORE_PAY", "편의점 결제");

    private final String key;
    private final String title;

}
