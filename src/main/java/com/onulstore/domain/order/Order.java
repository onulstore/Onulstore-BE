package com.onulstore.domain.order;

import com.onulstore.common.BaseTimeEntity;
import com.onulstore.domain.enums.OrderStatus;
import com.onulstore.domain.member.Member;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderProduct> orderProducts = new ArrayList<>();

    private LocalDateTime orderDate;

    @Column
    private String orderStatus;

    public void addOrderProduct(OrderProduct orderProduct) {
        orderProducts.add(orderProduct);
        orderProduct.setOrder(this);
    }

    public static Order createOrder(Member member, List<OrderProduct> orderProductList) {
        Order order = new Order();
        order.setMember(member);
        for (OrderProduct orderProduct : orderProductList) {
            order.addOrderProduct(orderProduct);
        }
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.COMPLETE.getKey());
        return order;
    }

    public int getTotalPrice() {
        int totalPrice = 0;

        for (OrderProduct orderProduct : orderProducts) {
            totalPrice += orderProduct.getTotalPrice();
        }
        return totalPrice;
    }

    public void orderCancel() {

        this.orderStatus = OrderStatus.CANCEL.getKey();
        for (OrderProduct orderProduct : orderProducts) {
            orderProduct.cancel();
        }
    }
}
