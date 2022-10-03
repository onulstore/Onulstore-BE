package com.onulstore.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {

    COMPLETE("COMPLETE", "주문완료"),
    CANCEL("CANCEL", "주문취소"),
    PREPARE_ITEM("WAIT_PAYMENT", "상품준비중"),
    REQUEST_EXCHANGE("REQUEST_EXCHANGE", "교환요청"),
    EXCHANGE("EXCHANGE", "교환중"),
    COMPLETE_EXCHANGE("COMPLETE_EXCHANGE", "교환완료"),
    WAIT_PAYMENT("WAIT_PAYMENT", "결제대기"),
    COMPLETE_PAYMENT("COMPLETE_PAYMENT", "결제완료"),
    PREPARE_DELIVERY("PREPARE_DELIVERY", "배송준비중"),
    DELIVERY("SHIPPING", "배송중");


    private final String key;
    private final String title;
}
