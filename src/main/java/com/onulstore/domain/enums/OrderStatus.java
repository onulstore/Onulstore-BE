package com.onulstore.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {

    CANCEL("CANCEL", "주문 취소"),
    COMPLETE("COMPLETE", "주문 완료"),
    REFUND_REQUEST("REFUND_REQUEST", "환불 요청"),
    REFUND_COMPLETE("REFUND_COMPLETE", "환불 완료"),
    PAYMENT_WAIT("PAYMENT_WAIT", "결제 대기"),
    PAYMENT_COMPLETE("PAYMENT_COMPLETE", "결제 완료"),
    PURCHASE_CONFIRM("PURCHASE_CONFIRM", "구매 확정");

    private final String key;
    private final String title;
}
