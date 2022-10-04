package com.onulstore.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorResult implements SuperErrorResult{
  DUPLICATE_USER_ID(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
  NOT_EXIST_USER(HttpStatus.FORBIDDEN, "존재하지 않는 유저입니다."),
  OUT_OF_STOCK(HttpStatus.BAD_REQUEST, "재고가 부족합니다."),
  CURATION_NOT_FOUND(HttpStatus.NOT_FOUND, "큐레이션을 찾을 수 없습니다."),
  ACCESS_PRIVILEGE(HttpStatus.UNAUTHORIZED, "접근 권한이 없습니다."),
  CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "카테고리를 찾을 수 없습니다."),
  PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다."),
  CART_NOT_FOUND(HttpStatus.NOT_FOUND, "장바구니를 찾을 수 없습니다."),
  UPDATE_PASSWORD(HttpStatus.FORBIDDEN, "입력한 새 비밀번호가 일치하지 않습니다."),
  TOKEN_INFO_NOT_MATCH(HttpStatus.BAD_REQUEST, "토큰의 유저 정보가 일치하지 않습니다."),
  INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "Refresh Token 이 유효하지 않습니다."),
  LOGOUT_USER(HttpStatus.BAD_REQUEST,"로그아웃 된 사용자입니다."),
  NOT_EXIST_QUESTION(HttpStatus.NOT_FOUND, "질문을 찾을 수 없습니다."),
  NOT_EXIST_ANSWER(HttpStatus.NOT_FOUND, "질문에 대한 답글을 찾을 수 없습니다."),
  WISHLIST_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 찜 등록이 되어있습니다."),
  NOT_FOUND_NOTICE(HttpStatus.NOT_FOUND, "해당 공지를 찾을 수 없습니다."),
  USER_NOT_MATCH(HttpStatus.BAD_REQUEST, "유저 정보가 일치하지 않습니다."),
  BRAND_NOT_FOUND(HttpStatus.NOT_FOUND, "브랜드를 찾을 수 없습니다."),
  ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다.");

  private final HttpStatus httpStatus;
  private final String message;

  @Override
  public String getName() {
    return this.name();
  }
}
