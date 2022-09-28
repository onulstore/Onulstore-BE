package com.onulstore.web.dto;

import com.onulstore.domain.brand.Brand;
import com.onulstore.domain.category.Category;
import com.onulstore.domain.enums.ProductStatus;
import com.onulstore.domain.product.Product;
import com.onulstore.domain.product.ProductImage;
import com.onulstore.web.dto.ProductImageDto.ProductImageMaker;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

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
        
        @ApiModelProperty(value = "상품 상태", example = "SALE / NEW")
        private ProductStatus productStatus;
        private Long categoryId;
        private Long brandId;

        public Product toProduct(Category category, Brand brand) {
            return Product.builder()
                .productName(productName)
                .content(content)
                .price(price)
                .quantity(quantity)
                .productStatus(productStatus)
                .category(category)
                .brand(brand)
                .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ProductResponse {

        private Long id;
        private String productName;
        private String content;
        private Integer price;
        private Integer quantity;
        private Integer purchaseCount;
        private ProductStatus productStatus;
        private Category category;
        private List<ProductImageMaker> productImage;
        private Integer wishListCount;
        private Brand brand;

        public static ProductResponse of(Product product) {
            return ProductResponse.builder()
                .id(product.getId())
                .productName(product.getProductName())
                .content(product.getContent())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .purchaseCount(product.getPurchaseCount())
                .productStatus(product.getProductStatus())
                .category(product.getCategory())
                .productImage(ProductImageMaker.of(product.getProductImages()))
                .wishListCount(product.getWishlists().size())
                .brand(product.getBrand())
                .build();
        }
    }


    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ProductRes {

        private String productName;
        private String content;
        private Integer price;
        private ProductStatus productStatus;
        private Category category;
        private boolean wishlist;

    }

}
