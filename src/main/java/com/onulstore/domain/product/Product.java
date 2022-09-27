package com.onulstore.domain.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onulstore.common.BaseTimeEntity;
import com.onulstore.domain.brand.Brand;
import com.onulstore.domain.cart.Cart;
import com.onulstore.domain.category.Category;
import com.onulstore.domain.curation.CurationProduct;
import com.onulstore.domain.enums.ProductStatus;
import com.onulstore.domain.enums.UserErrorResult;
import com.onulstore.domain.order.OrderProduct;
import com.onulstore.domain.question.Question;
import com.onulstore.domain.review.Review;
import com.onulstore.domain.wishlist.Wishlist;
import com.onulstore.exception.UserException;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    @Column
    private Integer price;

    @Column
    private Integer quantity;

    @Column
    private Integer purchaseCount;

    @Column
    private String productImg;

    @Column
    private boolean bookmark = false;

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

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<CurationProduct> curationProducts = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "brand_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Category category;

    public Product(String productName, String content, Integer price,
                   Integer quantity, String productImg, ProductStatus productStatus, Category category, Brand brand) {
        this.productName = productName;
        this.content = content;
        this.price = price;
        this.quantity = quantity;
        this.productImg = productImg;
        this.productStatus = productStatus;
        this.category = category;
        this.brand = brand;
    }

    public void changeProductData(String productName, String content, Integer price,
                                  Integer quantity, String productImg, ProductStatus productStatus) {
        this.productName = productName;
        this.content = content;
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
            throw new UserException(UserErrorResult.OUT_OF_STOCK);
        }
        this.quantity = restStock;
    }

    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }

    public void bookmarked() {
        this.bookmark = true;
    }

}
