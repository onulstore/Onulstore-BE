package com.onulstore.web.dto;

import com.onulstore.domain.enums.DeliveryMeasure;
import com.onulstore.domain.enums.OrderStatus;
import com.onulstore.domain.enums.PaymentMeasure;
import com.onulstore.domain.order.Order;
import io.swagger.annotations.ApiModelProperty;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class OrderDto {

    @Getter
    @Setter
    public static class OrderRequest {

        @NotNull(message = "상품 아이디는 필수 입력 값입니다.")
        private Long productId;
        @Min(value = 1, message = "최소 주문 수량은 1개 입니다.")
        @Max(value = 999, message = "최대 주문 수량은 999개 입니다.")
        private int count;
        private String deliveryMessage;
        @ApiModelProperty(value = "배송 방법", required = true, example = "INTERNATIONAL/DOMESTIC/STORE")
        private DeliveryMeasure deliveryMeasure;

    }

    @Getter
    @Setter
    @Builder
    public static class OrderResponse {

        private Long orderId;
        private Long memberId;
        private List orderProductList;

        public static OrderDto.OrderResponse of(Order order){
            OrderDto.OrderResponse orderResponse = OrderResponse.builder()
                .orderId(order.getId())
                .memberId(order.getMember().getId())
                .orderProductList(order.getOrderProducts())
                .build();
            return orderResponse;
        }

    }

    @Getter
    @Setter
    public static class OrderProduct {

        private String productName;
        private int count;
        private int orderPrice;

        public OrderProduct(com.onulstore.domain.order.OrderProduct orderProduct) {
            this.productName = orderProduct.getProduct().getProductName();
            this.count = orderProduct.getCount();
            this.orderPrice = orderProduct.getOrderPrice();
        }

    }

    @Getter
    @Setter
    public static class Payment {

        private PaymentMeasure paymentMeasure;
        private Integer productPrice;
        private Integer discount;
        private Integer deliveryPrice;
        private Integer acquirePoint;
        private Integer paymentAmount;

        public Payment(com.onulstore.domain.payment.Payment payment) {
            this.paymentMeasure = payment.getPaymentMeasure();
            this.productPrice = payment.getProductPrice();
            this.discount = payment.getDiscount();
            this.deliveryPrice = payment.getDeliveryPrice();
            this.acquirePoint = payment.getAcquirePoint();
            this.paymentAmount = payment.getPaymentAmount();
        }

    }

    @Getter
    @Setter
    public static class OrderHistory {

        private Long orderId;
        private String orderDate;
        private OrderStatus orderStatus;
        private DeliveryMeasure deliveryMeasure;
        private List<OrderProduct> orderProducts = new ArrayList<>();
        private List<Payment> payments = new ArrayList<>();

        public OrderHistory(Order order) {
            this.orderId = order.getId();
            this.orderDate = order.getOrderDate()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            this.orderStatus = order.getOrderStatus();
            this.deliveryMeasure = order.getDeliveryMeasure();
        }

        public void addOrderProduct(OrderDto.OrderProduct orderProduct) {
            orderProducts.add(orderProduct);
        }

        public void addPayment(OrderDto.Payment payment) {
            payments.add(payment);
        }

    }

    @Getter
    @Setter
    public static class StatusRequest {

        private Long orderId;
        @ApiModelProperty(value = "주문 상태", required = true, example = "REFUND_REQUEST/PURCHASE_CONFIRM")
        private OrderStatus orderStatus;

    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public static class StatusResponse {

        private Long orderId;
        private String orderDate;
        private OrderStatus orderStatus;

        public static OrderDto.StatusResponse of(Order order) {
            return StatusResponse.builder()
                .orderId(order.getId())
                .orderDate(
                    order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .orderStatus(order.getOrderStatus())
                .build();
        }

    }

    @Getter
    @Setter
    public static class CartOrderRequest {

        List<Long> cartList = new ArrayList<>();
        private String deliveryMessage;
        @ApiModelProperty(value = "배송 방법", required = true, example = "INTERNATIONAL/DOMESTIC/STORE")
        private DeliveryMeasure deliveryMeasure;

    }

    @Getter
    @Setter
    public static class UpdateOrderRequest {

        private Long orderId;
        private String phoneNum;
        private String postalCode;
        private String roadAddress;
        private String buildingName;
        private String detailAddress;

    }

}
