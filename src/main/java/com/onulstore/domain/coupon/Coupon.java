package com.onulstore.domain.coupon;

import com.onulstore.common.BaseTimeEntity;
import com.onulstore.domain.enums.CouponStatus;
import com.onulstore.domain.enums.DiscountType;
import com.onulstore.domain.member.Member;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

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

  public void changeStatus(CouponStatus couponStatus){
    this.couponStatus = couponStatus;
  }

  public LocalDateTime extendExpirationDate(Long duration){
      expirationDate = getCreatedDate().plusDays(duration);
      return expirationDate;
  }

  public void checkLeastValue(Integer totalPrice){
    if(totalPrice < leastRequiredValue){
      throw new RuntimeException();
    }
  }

  public Integer checkMaxDiscountValue(Integer discountPrice){
    if(discountPrice > maxDiscountValue){
      return maxDiscountValue;
    }
    return discountPrice;
  }

  public void settingExpirationDate() {
    this.expirationDate = LocalDateTime.now().plusDays(14);
  }

}
