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

public class CouponDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RequestCoupon {

        private Long memberId;
        private String couponTitle;
        private LocalDateTime createdDate;
        private Long duration;
        private Integer discountValue;
        private Integer leastRequiredValue;
        private Integer maxDiscountValue;
        private DiscountType discountType;
        private CouponStatus couponStatus;
        private LocalDateTime expirationDate;

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

        public static RequestCoupon of(Coupon coupon){
            RequestCoupon requestCoupon = RequestCoupon.builder()
                .memberId(coupon.getMember().getId())
                .couponTitle(coupon.getCouponTitle())
                .discountValue(coupon.getDiscountValue())
                .discountType(coupon.getDiscountType())
                .couponStatus(coupon.getCouponStatus())
                .createdDate(coupon.getCreatedDate())
                .expirationDate(coupon.getExpirationDate())
                .build();
            return requestCoupon;
        }
    }
}