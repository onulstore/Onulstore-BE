package com.onulstore.domain.cart;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onulstore.common.BaseTimeEntity;
import com.onulstore.domain.enums.UserErrorResult;
import com.onulstore.domain.order.Order;
import com.onulstore.domain.product.Product;
import com.onulstore.domain.member.Member;
import com.onulstore.exception.UserException;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Cart extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Integer productCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Order order;

    public void changeQuantity(Integer quantity){
        this.productCount = productCount + quantity;
    }

    public void plusOne(){
        if(this.productCount >= product.getQuantity()){
            throw new UserException(UserErrorResult.OUT_OF_STOCK);
        }
        this.productCount = productCount + 1;
    }

    public void minusOne(){
        if(this.productCount <= 1){
            throw new UserException(UserErrorResult.OUT_OF_STOCK);
        }
            this.productCount = productCount - 1;
    }

}
