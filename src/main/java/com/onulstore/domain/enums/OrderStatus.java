package com.onulstore.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {

    COMPLETE("COMPLETE", "주문완료"),
    CANCEL("CANCEL", "주문취소"),
    SHIPPING("SHIPPING", "배송중");


    private final String key;
    private final String title;
}
