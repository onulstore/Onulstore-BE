package com.onulstore.web.dto;

import com.onulstore.domain.enums.OrderStatus;
import com.onulstore.domain.enums.PaymentMeasure;
import com.onulstore.domain.order.Order;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
        private PaymentMeasure paymentMeasure;

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
    public static class OrderHistory {

        private Long orderId;
        private String orderDate;
        private OrderStatus orderStatus;
        private List<OrderProduct> orderProducts = new ArrayList<>();

        public OrderHistory(Order order) {
            this.orderId = order.getId();
            this.orderDate = order.getOrderDate()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            this.orderStatus = order.getOrderStatus();
        }

        public void addOrderProduct(OrderDto.OrderProduct orderProduct) {
            orderProducts.add(orderProduct);
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

}
