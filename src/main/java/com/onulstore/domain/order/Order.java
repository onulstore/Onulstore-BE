package com.onulstore.domain.order;

import com.onulstore.common.BaseTimeEntity;
import com.onulstore.domain.cart.Cart;
import com.onulstore.domain.enums.OrderStatus;
import com.onulstore.domain.user.User;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Orders")
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String orderNumber;

    @Column
    private String orderName;

    @Column
    private String deliveryMessage;

    @Column
    private String address;

    @Column
    private Integer amount;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<Cart> carts = new ArrayList<>();

}
