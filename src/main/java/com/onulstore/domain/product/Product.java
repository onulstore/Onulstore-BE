package com.onulstore.domain.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onulstore.common.BaseTimeEntity;
import com.onulstore.domain.cart.Cart;
import com.onulstore.domain.curation.Curation;
import com.onulstore.domain.enums.ProductStatus;
import com.onulstore.domain.order.OrderProduct;
import com.onulstore.domain.question.Question;
import com.onulstore.domain.review.Review;
import com.onulstore.domain.wishlist.Wishlist;
import com.onulstore.exception.OutOfStockException;
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
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String productName;

    @Column
    private String content;

    @Column(length = 10)
    private String largeCategoryCode;

    @Column(length = 10)
    private String smallCategoryCode;

    @Column
    private Integer price;

    @Column
    private Integer quantity;

    @Column
    private Integer purchaseCount;

    @Column
    private String productImg;

    @Enumerated(EnumType.STRING)
    private ProductStatus productStatus;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Question> questions = new ArrayList<>();

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Cart> carts = new ArrayList<>();

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Wishlist> wishlists = new ArrayList<>();

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<OrderProduct> orderProducts = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "curation_id")
    @JsonIgnore
    private Curation curation;

    public void changeProductData(String productName, String content, String largeCategoryCode, String smallCategoryCode, Integer price, Integer quantity, String productImg, ProductStatus productStatus) {
        this.productName = productName;
        this.content = content;
        this.largeCategoryCode = largeCategoryCode;
        this.smallCategoryCode = smallCategoryCode;
        this.price = price;
        this.quantity = quantity;
        this.productImg = productImg;
        this.productStatus = productStatus;
    }

    public void insertImage(String image) {
        this.productImg = image;
    }

    public void newPurchaseCount(){
        this.purchaseCount = 0;
    }

    public void removeStock(int quantity) {

        int restStock = this.quantity - quantity;
        if (restStock < 1) {
            throw new OutOfStockException("상품의 재고가 부족합니다. (현재 재고 수량: " + this.quantity + ")");
        }
        this.quantity = restStock;
    }

    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }

}
