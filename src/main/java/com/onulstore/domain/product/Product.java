package com.onulstore.domain.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onulstore.common.BaseTimeEntity;
import com.onulstore.config.exception.Exception;
import com.onulstore.domain.brand.Brand;
import com.onulstore.domain.cart.Cart;
import com.onulstore.domain.category.Category;
import com.onulstore.domain.curation.CurationProduct;
import com.onulstore.domain.enums.DiscountType;
import com.onulstore.domain.enums.ErrorResult;
import com.onulstore.domain.enums.ProductStatus;
import com.onulstore.domain.order.OrderProduct;
import com.onulstore.domain.question.Question;
import com.onulstore.domain.review.Review;
import com.onulstore.domain.wishlist.Wishlist;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    private String content = "";

    @Column
    private Integer price;

    @Column
    private Integer originalPrice;

    @Column
    private Integer discountValue = 0;

    @Column
    private Integer quantity;

    @Column
    private Integer purchaseCount;

    @Column
    private boolean bookmark = false;

    @Column
    private boolean discountCheck = false;

    @Column
    private LocalDate discountStartDate = LocalDate.now();

    @Column
    private LocalDate discountEndDate = LocalDate.now();

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

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ProductImage> productImages = new ArrayList<>();

    public Product(String productName, Integer price,
        Integer quantity, ProductStatus productStatus, Category category,
        Brand brand) {

        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.productStatus = productStatus;
        this.category = category;
        this.brand = brand;
    }

    public void changeProductData(String productName, Integer price,
        Integer quantity, ProductStatus productStatus) {
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.productStatus = productStatus;
    }

    public void changeContent(String contentImage){
        this.content = contentImage;
    }

    public void newPurchaseCount() {
        this.purchaseCount = 0;
    }

    public void removeStock(int quantity) {

        int restStock = this.quantity - quantity;
        if (restStock < 1) {
            throw new Exception(ErrorResult.OUT_OF_STOCK);
        }
        this.quantity = restStock;
    }

    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }

    public void bookmarked() {
        this.bookmark = true;
    }

    public void discountProduct(DiscountType discountType, Integer discountValue,
        LocalDate startDate, LocalDate endDate) {
        if (discountType.equals(DiscountType.PERCENT)) {
            this.discountValue = price * (discountValue) / 100;
        } else {
            this.discountValue = discountValue;
        }
        this.discountStartDate = startDate;
        this.discountEndDate = endDate;
    }

    public void discountStartValidation() {
        if (this.discountStartDate.isEqual(LocalDate.now())) {
            discountCheck = true;
            this.price = this.originalPrice - this.discountValue;
        }
    }

    public void discountEndValidation() {
        if (this.discountEndDate.isBefore(LocalDate.now())) {
            discountCheck = false;
            this.discountValue = 0;
            this.price = this.originalPrice;
        }
    }

}
