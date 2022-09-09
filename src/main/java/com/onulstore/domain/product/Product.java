package com.onulstore.domain.product;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onulstore.common.BaseTimeEntity;
import com.onulstore.domain.cart.Cart;
import com.onulstore.domain.curation.Curation;
import com.onulstore.domain.enums.ProductStatus;
import com.onulstore.domain.question.Question;
import com.onulstore.domain.review.Review;
import com.onulstore.domain.wishlist.Wishlist;
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

    @ManyToOne
    @JoinColumn(name = "curation_id")
    @JsonIgnore
    private Curation curation;

}
