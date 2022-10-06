package com.onulstore.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorResult implements SuperErrorResult {

    // 400 BAD_REQUEST
    TOKEN_INFO_NOT_MATCH(HttpStatus.BAD_REQUEST, "토큰의 유저 정보가 일치하지 않습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "Refresh Token 이 유효하지 않습니다."),
    USER_NOT_MATCH(HttpStatus.BAD_REQUEST, "유저 정보가 일치하지 않습니다."),
    LOGOUT_USER(HttpStatus.BAD_REQUEST, "로그아웃 된 사용자입니다."),
    OUT_OF_STOCK(HttpStatus.BAD_REQUEST, "재고가 부족합니다."),
    WISHLIST_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 찜 등록이 되어있습니다."),
    COUPON_EXPIRED(HttpStatus.BAD_REQUEST, "만료된 쿠폰입니다."),
    OUT_OF_POINT(HttpStatus.BAD_REQUEST, "포인트가 부족합니다."),
    NOT_REFUND_REQUEST_ORDER(HttpStatus.BAD_REQUEST, "환불 요청된 주문이 아닙니다."),

    // 401 UNAUTHORIZED
    ACCESS_PRIVILEGE(HttpStatus.UNAUTHORIZED, "접근 권한이 없습니다."),

    // 403 FORBIDDEN
    NOT_EXIST_USER(HttpStatus.FORBIDDEN, "존재하지 않는 유저입니다."),
    UPDATE_PASSWORD(HttpStatus.FORBIDDEN, "입력한 새 비밀번호가 일치하지 않습니다."),
    PASSWORD_MISMATCH(HttpStatus.FORBIDDEN, "입력한 비밀번호가 일치하지 않습니다"),

    // 404 NOT_FOUND
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다."),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "카테고리를 찾을 수 없습니다."),
    CART_NOT_FOUND(HttpStatus.NOT_FOUND, "장바구니를 찾을 수 없습니다."),
    BRAND_NOT_FOUND(HttpStatus.NOT_FOUND, "브랜드를 찾을 수 없습니다."),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다."),
    CURATION_NOT_FOUND(HttpStatus.NOT_FOUND, "큐레이션을 찾을 수 없습니다."),
    NOT_EXIST_QUESTION(HttpStatus.NOT_FOUND, "질문을 찾을 수 없습니다."),
    NOT_EXIST_ANSWER(HttpStatus.NOT_FOUND, "질문에 대한 답글을 찾을 수 없습니다."),
    NOT_FOUND_NOTICE(HttpStatus.NOT_FOUND, "해당 공지를 찾을 수 없습니다."),
    COUPON_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 쿠폰을 찾을 수 없습니다."),
    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 결재 내역을 찾을 수 없습니다."),

    // 409 CONFLICT
    DUPLICATE_USER_ID(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    DUPLICATE_PHONE_NUMBER(HttpStatus.CONFLICT, "이미 존재하는 휴대폰 번호 입니다."),
    ORDER_ALREADY_PAYED(HttpStatus.CONFLICT, "이미 결제가 완료된 주문입니다.");


    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getName() {
        return this.name();
    }
}
