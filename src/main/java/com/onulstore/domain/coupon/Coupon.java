package com.onulstore.domain.coupon;

import com.onulstore.common.BaseTimeEntity;
import com.onulstore.domain.enums.CouponStatus;
import com.onulstore.domain.enums.DiscountType;
import com.onulstore.domain.member.Member;

import com.onulstore.domain.payment.Payment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Coupon extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String couponTitle;

    @Column
    private LocalDateTime expirationDate;

    @Column
    private Integer discountValue;

    @Column
    private Integer leastRequiredValue;

    @Column
    private Integer maxDiscountValue;

    @Column
    @Enumerated(EnumType.STRING)
    private CouponStatus couponStatus = CouponStatus.DEFAULT;

    @Column
    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    @Override
    public LocalDateTime getCreatedDate() {
        return super.getCreatedDate();
    }

    @Override
    public LocalDateTime getUpdatedDate() {
        return super.getUpdatedDate();
    }

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder.Default
    @OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL)
    private List<Payment> payments = new ArrayList<>();

    public void changeStatus(CouponStatus couponStatus) {
        this.couponStatus = couponStatus;
    }

    public LocalDateTime extendExpirationDate(Long duration) {
        expirationDate = getCreatedDate().plusDays(duration);
        return expirationDate;
    }

    public void checkLeastValue(Integer totalPrice) {
        if (totalPrice < leastRequiredValue) {
            throw new RuntimeException();
        }
    }

    public Integer checkMaxDiscountValue(Integer discountPrice) {
        if (discountPrice > maxDiscountValue) {
            return maxDiscountValue;
        }
        return discountPrice;
    }

    public void checkValidation() {
        if (LocalDateTime.now().isAfter(this.expirationDate)) {
            this.couponStatus = CouponStatus.EXPIRED;
        }
    }

    public void useCoupon() {
        this.couponStatus = CouponStatus.USED;
    }

}
