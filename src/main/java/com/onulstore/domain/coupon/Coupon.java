package com.onulstore.domain.coupon;

import com.onulstore.common.BaseTimeEntity;
import com.onulstore.domain.category.Category;
import com.onulstore.domain.enums.CouponStatus;
import com.onulstore.domain.enums.DiscountType;
import com.onulstore.domain.member.Member;
import com.onulstore.exception.UserException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
  private CouponStatus couponStatus;

  @Column
  private DiscountType discountType;

  @Column
  private String categoryCode;

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
  @OnDelete(action = OnDeleteAction.CASCADE)
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

}
