package com.onulstore.web.dto;

import com.onulstore.domain.category.Category;
import com.onulstore.domain.enums.ProductStatus;
import com.onulstore.domain.product.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ProductDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ProductRequest {

        private String productName;
        private String content;
        private Integer price;
        private Integer quantity;
        private String productImg;
        private ProductStatus productStatus;
        private Long categoryId;

        public Product toProduct() {
            return Product.builder()
                    .productName(productName)
                    .content(content)
                    .price(price)
                    .quantity(quantity)
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
        private Integer price;
        private Integer quantity;
        private Integer purchaseCount;
        private String productImg;
        private ProductStatus productStatus;
        private Category category;

        public static ProductResponse of(Product product) {
            return ProductResponse.builder()
                    .productName(product.getProductName())
                    .content(product.getContent())
                    .price(product.getPrice())
                    .quantity(product.getQuantity())
                    .purchaseCount(product.getPurchaseCount())
                    .productImg(product.getProductImg())
                    .productStatus(product.getProductStatus())
                    .category(product.getCategory())
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class modifyRequest {

        private String productName;
        private String content;
        private Integer price;
        private Integer quantity;
        private String productImg;
        private ProductStatus productStatus;

        public Product toProduct() {
            return Product.builder()
                    .productName(productName)
                    .content(content)
                    .price(price)
                    .quantity(quantity)
                    .productImg(productImg)
                    .productStatus(productStatus)
                    .build();
        }
    }

}
