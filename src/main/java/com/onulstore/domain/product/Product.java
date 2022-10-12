package com.onulstore.domain.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onulstore.common.BaseTimeEntity;
import com.onulstore.config.exception.CustomException;
import com.onulstore.domain.brand.Brand;
import com.onulstore.domain.cart.Cart;
import com.onulstore.domain.category.Category;
import com.onulstore.domain.curation.CurationProduct;
import com.onulstore.domain.enums.DiscountStatus;
import com.onulstore.domain.enums.DiscountType;
import com.onulstore.domain.enums.CustomErrorResult;
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
    private Integer discountValue;

    @Column
    @Enumerated(EnumType.STRING)
    private DiscountType discountType = DiscountType.NONE;

    @Column
    private Integer quantity;

    @Column
    private Integer purchaseCount;

    @Column
    private float rating = 0;

    @Column
    private boolean bookmark = false;

    @Column
    private boolean discountCheck = false;

    @Column
    @Enumerated(EnumType.STRING)
    private DiscountStatus discountStatus = DiscountStatus.FALSE;

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
        this.originalPrice = price;
        this.price = price;
        this.quantity = quantity;
        this.productStatus = productStatus;
        discountStartValidation();
    }

    public void changeContent(String contentImage) {
        this.content = contentImage;
    }

    public void newPurchaseCount() {
        this.purchaseCount = 0;
    }

    public void removeStock(int quantity, Product product) {

        int restStock = this.quantity - quantity;
        if (restStock < 0) {
            throw new CustomException(CustomErrorResult.OUT_OF_STOCK);
        } else if (restStock == 0) {
            product.setProductStatus(ProductStatus.SOLD_OUT);
        }
        this.quantity = restStock;
    }

    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }

    public void bookmarked() {
        this.bookmark = true;
    }

    public void discountProduct(DiscountType discountType, DiscountStatus discountStatus,
        Integer discountValue, LocalDate startDate, LocalDate endDate) {
        this.discountValue = discountValue;
        this.discountType = discountType;
        this.discountStatus = discountStatus;
        this.discountStartDate = startDate;
        this.discountEndDate = endDate;
        discountStartValidation();
    }

    public void discountStartValidation() {
        if (this.discountValue > 0) {
            if (this.discountStartDate.isEqual(LocalDate.now())) {
                discountCheck = true;
                if (this.discountType.equals(DiscountType.PERCENT)) {
                    this.price = this.originalPrice * (100-discountValue) / 100;
                } else if (this.discountType.equals(DiscountType.TOTAL)) {
                    this.price = this.originalPrice - discountValue;
                }
            }
        }
    }

    public void discountEndValidation() {
        if (this.discountValue > 0) {
            if (this.discountEndDate.isBefore(LocalDate.now())) {
                discountCheck = false;
                this.discountValue = 0;
                this.discountStatus = DiscountStatus.FALSE;
                this.discountType = DiscountType.NONE;
                this.price = this.originalPrice;
            }
        }
    }

    public void addPurchaseCount(Integer purchaseCount) {
        this.purchaseCount += purchaseCount;
    }

}
