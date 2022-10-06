package com.onulstore.web.dto;

import com.onulstore.domain.enums.PaymentMeasure;
import com.onulstore.domain.payment.Payment;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class PaymentDto {

    @Getter
    @Setter
    public static class PaymentRequest {

        private Long orderId;
        @ApiModelProperty(value = "쿠폰 아이디", example = "쿠폰 사용을 하지 않으려면 지우고 사용해주세요.")
        private Long couponId;
        private Integer mileage;
        private Integer deliveryPrice;
        @ApiModelProperty(value = "결제 방법", required = true, example = "CREDIT_CARD/ACCOUNT/STORE_PAY")
        private PaymentMeasure paymentMeasure;

    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public static class PaymentResponse {

        private PaymentMeasure paymentMeasure;
        private Integer productPrice;
        private Integer discount;
        private Integer deliveryPrice;
        private Integer acquirePoint;
        private Integer paymentAmount;

        public static PaymentResponse of(Payment payment) {
            return PaymentResponse.builder()
                .paymentMeasure(payment.getPaymentMeasure())
                .productPrice(payment.getProductPrice())
                .discount(payment.getDiscount())
                .deliveryPrice(payment.getDeliveryPrice())
                .acquirePoint(payment.getAcquirePoint())
                .paymentAmount(payment.getPaymentAmount())
                .build();
        }

    }

}
