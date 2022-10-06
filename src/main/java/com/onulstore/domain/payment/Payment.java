package com.onulstore.domain.payment;

import com.onulstore.common.BaseTimeEntity;
import com.onulstore.domain.coupon.Coupon;
import com.onulstore.domain.enums.PaymentMeasure;
import com.onulstore.domain.order.Order;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PaymentMeasure paymentMeasure;

    @Column
    private Integer mileage = 0;

    @Column
    private Integer productPrice;

    @Column
    private Integer discount = 0;

    @Column
    private Integer deliveryPrice = 0;

    @Column
    private Integer paymentAmount;

    @Column
    private Integer acquirePoint;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @OneToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    public static Payment paying(Order order, Coupon coupon, Integer mileage, Integer productPrice,
        Integer discount, Integer deliveryPrice, Integer paymentAmount, Integer acquirePoint,
        PaymentMeasure paymentMeasure) {
        return Payment.builder()
            .order(order)
            .coupon(coupon)
            .mileage(mileage)
            .productPrice(productPrice)
            .discount(discount)
            .deliveryPrice(deliveryPrice)
            .paymentAmount(paymentAmount)
            .acquirePoint(acquirePoint)
            .paymentMeasure(paymentMeasure)
            .build();

    }

}
