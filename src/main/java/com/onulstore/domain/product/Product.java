package com.onulstore.domain.product;


import com.onulstore.common.BaseTimeEntity;
import com.onulstore.domain.enums.ProductStatus;
import lombok.*;

import javax.persistence.*;

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

}
