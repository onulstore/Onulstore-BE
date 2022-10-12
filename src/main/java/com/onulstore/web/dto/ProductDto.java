package com.onulstore.web.dto;

import com.onulstore.domain.brand.Brand;
import com.onulstore.domain.category.Category;
import com.onulstore.domain.enums.DiscountStatus;
import com.onulstore.domain.enums.DiscountType;
import com.onulstore.domain.enums.ProductStatus;
import com.onulstore.domain.product.Product;
import com.onulstore.web.dto.ProductImageDto.ProductImageMaker;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
        private Integer price;
        private Integer quantity;

        @ApiModelProperty(value = "상품 상태", example = "SALE / NEW")
        private ProductStatus productStatus;
        private DiscountStatus discountStatus = DiscountStatus.FALSE;
        private Long categoryId;
        private Long brandId;

        public Product toProduct(Category category, Brand brand) {
            return Product.builder()
                .productName(productName)
                .originalPrice(price)
                .price(price)
                .quantity(quantity)
                .productStatus(productStatus)
                .discountStatus(discountStatus)
                .category(category)
                .brand(brand)
                .discountValue(0)
                .discountCheck(false)
                .discountType(DiscountType.NONE)
                .productImages(new ArrayList<>())
                .wishlists(new ArrayList<>())
                .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class DiscountProductDto {

        Long productId;
        Integer discountValue;
        DiscountType discountType;
        LocalDate discountStartDate;
        LocalDate discountEndDate;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ProductResponse {

        private Long id;
        private String productName;
        private String content;
        private Integer originalPrice;
        private Integer price;
        private Integer quantity;
        private Integer purchaseCount;
        private float rating;
        private ProductStatus productStatus;
        private Category category;
        private List<ProductImageMaker> productImage;
        private Integer wishListCount;
        private boolean bookmarked;
        private Brand brand;

        public static ProductResponse of(Product product) {
            return ProductResponse.builder()
                .id(product.getId())
                .productName(product.getProductName())
                .content(product.getContent())
                .originalPrice(product.getOriginalPrice())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .purchaseCount(product.getPurchaseCount())
                .productStatus(product.getProductStatus())
                .rating(product.getRating())
                .category(product.getCategory())
                .productImage(ProductImageMaker.of(product.getProductImages()))
                .bookmarked(product.isBookmark())
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
