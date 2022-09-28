package com.onulstore.web.dto;

import com.onulstore.domain.coupon.Coupon;
import com.onulstore.domain.enums.CouponStatus;
import com.onulstore.domain.enums.DiscountType;
import com.onulstore.domain.member.Member;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CouponDto {

  private String memberId;
  private String couponTitle;
  private LocalDate createdDate;
  private Long duration;
  private Integer discountValue;
  private Integer leastRequiredValue;
  private Integer maxDiscountValue;
  private DiscountType discountType;
  private CouponStatus couponStatus;
  private String categoryCode;

  public Coupon toCoupon(Member member){
    Coupon coupon = Coupon.builder()
        .couponTitle(couponTitle)
        .discountValue(discountValue)
        .leastRequiredValue(leastRequiredValue)
        .maxDiscountValue(maxDiscountValue)
        .discountType(discountType)
        .couponStatus(CouponStatus.DEFAULT)
        .categoryCode(categoryCode)
        .member(member)
        .expirationDate(LocalDateTime.now().plusDays(duration))
        .build();
    return coupon;
  }
}