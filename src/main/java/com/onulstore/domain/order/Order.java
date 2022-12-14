package com.onulstore.domain.order;

import com.onulstore.common.BaseTimeEntity;
import com.onulstore.domain.enums.DeliveryMeasure;
import com.onulstore.domain.enums.OrderStatus;
import com.onulstore.domain.member.Member;
import com.onulstore.domain.payment.Payment;
import com.onulstore.web.dto.OrderDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "Orders")
@NoArgsConstructor
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String firstKana;

    @Column
    private String lastKana;

    @Column
    private String postalCode;

    @Column
    private String roadAddress;

    @Column
    private String buildingName;

    @Column
    private String detailAddress;

    @Column
    private String phoneNum;

    @Column
    private String email;

    @Column
    private String deliveryMessage;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Enumerated(EnumType.STRING)
    private DeliveryMeasure deliveryMeasure;

    private LocalDateTime orderDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Payment> payments = new ArrayList<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderProduct> orderProducts = new ArrayList<>();


    public void addOrderProduct(OrderProduct orderProduct) {
        orderProducts.add(orderProduct);
        orderProduct.setOrder(this);
    }

    public static Order createCartOrder(Member member, String deliveryMessage,
        DeliveryMeasure deliveryMeasure, List<OrderProduct> orderProductList) {
        Order order = new Order();
        order.setMember(member);
        order.setEmail(member.getEmail());
        order.setLastKana(member.getLastKana());
        order.setFirstKana(member.getFirstKana());
        order.setLastName(member.getLastName());
        order.setFirstName(member.getFirstName());
        order.setPhoneNum(member.getPhoneNum());
        order.setPostalCode(member.getPostalCode());
        order.setRoadAddress(member.getRoadAddress());
        order.setBuildingName(member.getBuildingName());
        order.setDetailAddress(member.getDetailAddress());
        order.setDeliveryMessage(deliveryMessage);
        for (OrderProduct orderProduct : orderProductList) {
            order.addOrderProduct(orderProduct);
        }
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.COMPLETE);
        order.setDeliveryMeasure(deliveryMeasure);
        return order;
    }

    public static Order createOrder(Member member, String deliveryMessage,
        DeliveryMeasure deliveryMeasure, OrderProduct orderProduct) {
        Order order = new Order();
        order.setMember(member);
        order.setEmail(member.getEmail());
        order.setLastKana(member.getLastKana());
        order.setFirstKana(member.getFirstKana());
        order.setLastName(member.getLastName());
        order.setFirstName(member.getFirstName());
        order.setPhoneNum(member.getPhoneNum());
        order.setPostalCode(member.getPostalCode());
        order.setRoadAddress(member.getRoadAddress());
        order.setBuildingName(member.getBuildingName());
        order.setDetailAddress(member.getDetailAddress());
        order.setDeliveryMessage(deliveryMessage);
        order.addOrderProduct(orderProduct);
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.COMPLETE);
        order.setDeliveryMeasure(deliveryMeasure);
        return order;
    }

    public Order modificationOrder(OrderDto.UpdateOrderRequest updateOrderRequest) {
        this.phoneNum = updateOrderRequest.getPhoneNum();
        this.postalCode = updateOrderRequest.getPostalCode();
        this.roadAddress = updateOrderRequest.getRoadAddress();
        this.buildingName = updateOrderRequest.getBuildingName();
        this.detailAddress = updateOrderRequest.getDetailAddress();
        return this;
    }

    public int getTotalPrice() {
        int totalPrice = 0;

        for (OrderProduct orderProduct : orderProducts) {
            totalPrice += orderProduct.getTotalPrice();
        }
        return totalPrice;
    }

    public void orderCancel() {
        this.orderStatus = OrderStatus.CANCEL;
        for (OrderProduct orderProduct : orderProducts) {
            orderProduct.cancel();
        }
    }

    public void orderRefund() {
        this.orderStatus = OrderStatus.REFUND_COMPLETE;
        for (OrderProduct orderProduct : orderProducts) {
            orderProduct.cancel();
        }
    }

    public void paymentSuccess() {
        this.orderStatus = OrderStatus.PAYMENT_COMPLETE;
    }

    public Order updateStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
        return this;
    }
}
