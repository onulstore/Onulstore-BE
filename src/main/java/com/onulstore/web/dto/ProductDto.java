package com.onulstore.web.dto;

import com.onulstore.domain.enums.Authority;
import com.onulstore.domain.enums.ProductStatus;
import com.onulstore.domain.member.Member;
import com.onulstore.domain.product.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

public class ProductDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ProductRequest {

        private String productName;
        private String content;
        private String largeCategoryCode;
        private String smallCategoryCode;
        private Integer price;
        private Integer quantity;
        private Integer purchaseCount;
        private String productImg;
        private ProductStatus productStatus;

        public Product toProduct() {
            return Product.builder()
                    .productName(productName)
                    .content(content)
                    .largeCategoryCode(largeCategoryCode)
                    .smallCategoryCode(smallCategoryCode)
                    .price(price)
                    .quantity(quantity)
                    .purchaseCount(purchaseCount)
                    .productImg(productImg)
                    .productStatus(productStatus)
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ProductResponse {

        private String productName;
        private String content;
        private String largeCategoryCode;
        private String smallCategoryCode;
        private Integer price;
        private Integer quantity;
        private Integer purchaseCount;
        private String productImg;
        private ProductStatus productStatus;

        public static ProductResponse of(Product product) {
            return ProductResponse.builder()
                    .productName(product.getProductName())
                    .content(product.getContent())
                    .largeCategoryCode(product.getLargeCategoryCode())
                    .smallCategoryCode(product.getSmallCategoryCode())
                    .price(product.getPrice())
                    .quantity(product.getQuantity())
                    .purchaseCount(product.getPurchaseCount())
                    .productImg(product.getProductImg())
                    .productStatus(product.getProductStatus())
                    .build();
        }
    }
}
