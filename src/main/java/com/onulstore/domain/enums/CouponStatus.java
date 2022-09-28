package com.onulstore.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CouponStatus {

  DEFAULT("DEFAULT", "미사용"),
  USED("USED", "사용"),
  EXPIRED("EXPIRED", "만료");

  private final String key;
  private final String title;
}
