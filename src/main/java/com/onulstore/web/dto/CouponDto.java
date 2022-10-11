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
        private Integer discountValue;
        private DiscountType discountType;
        private CouponStatus couponStatus;
        private LocalDateTime expirationDate;

        public Coupon toCoupon(Member member) {
            Coupon coupon = Coupon.builder()
                .couponTitle(couponTitle)
                .discountValue(discountValue)
                .discountType(discountType)
                .leastRequiredValue(0)
                .maxDiscountValue(1000000)
                .couponStatus(CouponStatus.DEFAULT)
                .member(member)
                .expirationDate(expirationDate)
                .build();
            return coupon;
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ResponseCoupon {

        private String memberEmail;
        private String couponTitle;
        private Integer discountValue;
        private DiscountType discountType;
        private CouponStatus couponStatus;
        private LocalDateTime createdDate;
        private LocalDateTime expirationDate;

        public static ResponseCoupon of(Coupon coupon){
            ResponseCoupon responseCoupon = ResponseCoupon.builder()
                .memberEmail(coupon.getMember().getEmail())
                .couponTitle(coupon.getCouponTitle())
                .discountValue(coupon.getDiscountValue())
                .discountType(coupon.getDiscountType())
                .couponStatus(coupon.getCouponStatus())
                .createdDate(coupon.getCreatedDate())
                .expirationDate(coupon.getExpirationDate())
                .build();
            return responseCoupon;
        }
    }
}