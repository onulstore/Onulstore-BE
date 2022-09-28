package com.onulstore.web.dto;

import com.onulstore.domain.coupon.Coupon;
import com.onulstore.domain.enums.CouponStatus;
import com.onulstore.domain.enums.DiscountType;
import java.time.LocalDate;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
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

  public Coupon toCoupon(){
    Coupon coupon = Coupon.builder()
        .couponTitle(couponTitle)
        .discountValue(discountValue)
        .leastRequiredValue(leastRequiredValue)
        .maxDiscountValue(maxDiscountValue)
        .discountType(discountType)
        .couponStatus(CouponStatus.DEFAULT)
        .categoryCode(categoryCode)
        .build();
    coupon.setExpirationDate(coupon.extendExpirationDate(duration));
    return coupon;
  }
}