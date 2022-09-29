package com.onulstore.web.dto;

import com.onulstore.domain.coupon.Coupon;
import com.onulstore.domain.enums.CouponStatus;
import com.onulstore.domain.enums.DiscountType;
import com.onulstore.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CouponDto {

  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public class RequestCoupon {

    private Long memberId;
    private String couponTitle;
    private LocalDate createdDate;
    private Long duration;
    private Integer discountValue;
    private Integer leastRequiredValue;
    private Integer maxDiscountValue;
    private DiscountType discountType;
    private CouponStatus couponStatus;

    public Coupon toCoupon(Member member) {
      Coupon coupon = Coupon.builder()
              .couponTitle(couponTitle)
              .discountValue(discountValue)
              .leastRequiredValue(leastRequiredValue)
              .maxDiscountValue(maxDiscountValue)
              .discountType(discountType)
              .couponStatus(CouponStatus.DEFAULT)
              .member(member)
              .expirationDate(LocalDateTime.now().plusDays(duration))
              .build();
      return coupon;
    }
  }
}